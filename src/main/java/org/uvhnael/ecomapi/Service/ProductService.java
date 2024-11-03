package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uvhnael.ecomapi.Dto.ProductBody;
import org.uvhnael.ecomapi.Dto.ProductDetailResponse;
import org.uvhnael.ecomapi.Dto.ProductRecommend;
import org.uvhnael.ecomapi.Dto.ProductResponse;
import org.uvhnael.ecomapi.Model.*;
import org.uvhnael.ecomapi.Repository.*;
import org.uvhnael.ecomapi.exception.category.CategoryNotFoundException;
import org.uvhnael.ecomapi.exception.gallery.GalleryNotFoundException;
import org.uvhnael.ecomapi.exception.product.ProductCreationException;
import org.uvhnael.ecomapi.exception.product.ProductNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AttributeService attributeService;
    private final VariantService variantService;
    private final GalleryService galleryService;
    private final CategoryRepository categoryRepository;
    private final ProductRateRepository productRateRepository;
    private final SellRepository sellRepository;
    private final EventRepository eventRepository;

    public List<ProductResponse> getProducts(int page, int size) {
        List<Integer> productIds = productRepository.getProducts(page, size);

        List<ProductResponse> products = new ArrayList<>();
        for (int productId : productIds) {
            ProductResponse product = getProduct(productId);
            products.add(product);
        }
        return products;
    }

    public List<ProductRecommend> getProductsData(int page, int size) {
        List<Integer> productIds = productRepository.getProducts(page, size);

        List<ProductRecommend> products = new ArrayList<>();
        for (int productId : productIds) {
            Product product = productRepository.getById(productId);
            int categoryId = categoryRepository.getIdByProductID(productId);
            double rate = productRateRepository.existsByProductId((long) productId) ? getAvgRate(productId) : 0.0;
            int salesVolume = sellRepository.getSalesVolume(productId);
            int userInterest = eventRepository.countByProductId(productId);

            products.add(new ProductRecommend(productId, product.getRegularPrice(), rate, salesVolume, categoryId, userInterest));

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
        attributeService.createAttributes(product.getAttributes(), product.getAttributeValues(), productId, product.getCreatedBy());
        variantService.createVariants(product.getVariants(), productId);
        galleryService.createGallery(productId, product.getImagePath(), product.getCreatedBy());
        if (!categoryRepository.isCategoryExists(product.getCategoryId())) {
            throw new CategoryNotFoundException("Category ID: " + product.getCategoryId() + " not found");
        }
        categoryRepository.addProductCategory(productId, product.getCategoryId());
        return true;
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
            return (ProductResponse.form(product, gallery, 0.0));
        }

        double rate = getAvgRate(productId);

        return (ProductResponse.form(product, gallery, rate));
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
