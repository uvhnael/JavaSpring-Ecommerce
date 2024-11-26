package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uvhnael.ecomapi.Dto.*;
import org.uvhnael.ecomapi.Model.*;
import org.uvhnael.ecomapi.Repository.*;
import org.uvhnael.ecomapi.exception.category.CategoryNotFoundException;
import org.uvhnael.ecomapi.exception.gallery.GalleryNotFoundException;
import org.uvhnael.ecomapi.exception.product.ProductCreationException;
import org.uvhnael.ecomapi.exception.product.ProductNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AttributeService attributeService;
    private final VariantService variantService;
    private final GalleryService galleryService;
    private final CategoryRepository categoryRepository;
    private final ProductRateRepository productRateRepository;
    private final EventService eventService;
    private final RecommendService recommendService;


    public List<ProductResponse> getProducts(int page, int size) {
        List<Integer> productIds = productRepository.getProducts(page, size);
        List<ProductResponse> products = new ArrayList<>();
        for (int productId : productIds) {
            ProductResponse product = getProduct(productId);
            products.add(product);
        }
        return products;
    }

    public List<ProductResponse> getRandomProducts(int size) {
        List<Integer> productIds = productRepository.getRandomProducts(size);

        List<ProductResponse> products = new ArrayList<>();
        for (int productId : productIds) {
            ProductResponse product = getProduct(productId);
            products.add(product);
        }
        return products;
    }

    public List<ProductResponse> getRecommendProduct(int customerId, int page, int size) {
        RecommendId recommendId = recommendService.getCustomerRecommendation(customerId, page, size);
        List<Product> products = productRepository.getProductResponse(recommendId.getProduct_id());
        List<String> thumbnails = galleryService.getThumbnails(recommendId.getProduct_id());

        return getListProduct(products, thumbnails);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        List<Integer> productIds = productRepository.searchProducts(keyword);

        List<ProductResponse> products = new ArrayList<>();
        for (int productId : productIds) {
            ProductResponse product = getProduct(productId);
            products.add(product);
        }
        return products;
    }

    @Transactional
    public boolean createProduct(ProductBody product) {
        checkNullProductBody(product);
        int productId = productRepository.createProduct(product.getProductName(), product.getRegularPrice(), product.getQuantity(), product.getDescription(), product.isPublished(), product.getCreatedBy());
        if (productId == 0) {
            throw new ProductCreationException("Product creation failed");
        }
        recommendService.addNewProduct(productId, product.getCategoryId());
        attributeService.createAttributes(product.getAttributes(), product.getAttributeValues(), productId, product.getCreatedBy());
        variantService.createVariants(product.getVariants(), productId);
        galleryService.createGallery(productId, product.getImagePath(), product.getCreatedBy());
        if (!categoryRepository.isCategoryExists(product.getCategoryId())) {
            throw new CategoryNotFoundException("Category ID: " + product.getCategoryId() + " not found");
        }
        categoryRepository.addProductCategory(productId, product.getCategoryId());
        return true;
    }

    public ProductDetailResponse viewProduct(int productId, int customerId) {
        if (customerId != 0) {
            eventService.addEvent(customerId, productId, "VIEW");
            recommendService.addNewEvent(customerId, productId, 1);
        }
        return getDetailProduct(productId);
    }

    public ProductDetailResponse getDetailProduct(int productId) {
        Product product = productRepository.getById(productId);
        if (product == null) {
            throw new ProductNotFoundException("Product ID: " + productId + " not found");
        }
        List<Attribute> attribute = attributeService.getByProductId(productId);
        List<Variant> variant = variantService.getByProductId(productId);
        List<Gallery> gallery = galleryService.getByProductId(productId);
        if (gallery.isEmpty()) {
            throw new GalleryNotFoundException("Gallery not found for product ID: " + productId);
        }
        Category category = categoryRepository.getByProductId(productId);
        if (category == null) {
            throw new CategoryNotFoundException("Category not found for product ID: " + productId);
        }

        return ProductDetailResponse.form(product, attribute, variant, gallery, category);
    }

    public List<ProductResponse> getByCategory(int categoryId) {
        if (categoryRepository.isCategoryExists(categoryId)) {
            List<Integer> productIds = productRepository.getByCategoryId(categoryId);
            List<ProductResponse> products = new ArrayList<>();
            for (int productId : productIds) {
                ProductResponse product = getProduct(productId);
                products.add(product);
            }
            return products;
        } else {
            throw new CategoryNotFoundException("Category ID: " + categoryId + " not found");
        }
    }

    public List<ProductResponse> getListProduct(List<Product> products, List<String> thumbnails) {
        List<ProductResponse> productResponses = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            String gallery = thumbnails.get(i);
            Optional<Double> rate = productRateRepository.getAverageRateByProductId(Long.valueOf(product.getId()));

            if (rate.isEmpty()) {
                productResponses.add(ProductResponse.form(product.getId(), product.getProductName(), product.getQuantity(), product.getRegularPrice(), gallery, 0.0));
            } else {
                productResponses.add(ProductResponse.form(product.getId(), product.getProductName(), product.getQuantity(), product.getRegularPrice(), gallery, rate.get()));
            }
        }
        return productResponses;
    }

    public ProductResponse getProduct(int productId) {
        Product product = productRepository.getById(productId);
        if (product == null) {
            throw new ProductNotFoundException("Product ID: " + productId + " not found");
        }
        String gallery = galleryService.getThumbnail(productId);
        if (gallery == null) {
            throw new GalleryNotFoundException("Gallery not found for product ID: " + productId);
        }

        if (!productRateRepository.existsByProductId((long) productId)) {
            return (ProductResponse.form(product.getId(), product.getProductName(), product.getQuantity(), product.getRegularPrice(), gallery, 0.0));
        }

        double rate = getAvgRate(productId);

        return (ProductResponse.form(product.getId(), product.getProductName(), product.getQuantity(), product.getRegularPrice(), gallery, rate));
    }

    private double getAvgRate(long id) {
        List<ProductRate> productRates = productRateRepository.findByProductId(id);
        double totalRate = 0;
        for (ProductRate productRate : productRates) {
            totalRate += productRate.getRate();
        }
        return totalRate / productRates.size();
    }

    private void checkNullProductBody(ProductBody product) {
        if (product.getProductName() == null) {
            throw new ProductCreationException("Product name is required");
        }
        if (product.getRegularPrice() == 0) {
            throw new ProductCreationException("Regular price is required");
        }
        if (product.getQuantity() == 0) {
            throw new ProductCreationException("Quantity is required");
        }
        if (product.getDescription() == null) {
            throw new ProductCreationException("Description is required");
        }
        if (product.getCreatedBy() == 0) {
            throw new ProductCreationException("Created by is required");
        }
        if (product.getCategoryId() == 0) {
            throw new ProductCreationException("Category ID is required");
        }
        if (product.getImagePath().isEmpty()) {
            throw new ProductCreationException("Image path is required");
        }
    }
}
