package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uvhnael.ecomapi.Dto.CartBody;
import org.uvhnael.ecomapi.Dto.CartResponse;
import org.uvhnael.ecomapi.Dto.ProductDetailResponse;
import org.uvhnael.ecomapi.Model.Cart;
import org.uvhnael.ecomapi.Model.Event;
import org.uvhnael.ecomapi.Model.Variant;
import org.uvhnael.ecomapi.Repository.*;
import org.uvhnael.ecomapi.exception.cart.CartNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final VariantRepository variantRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final ProductService productService;
    private final EventRepository eventRepository;

    public List<CartResponse> getCart(int customerId) {

        List<Cart> carts = cartRepository.getCartByCustomerId(customerId);
        List<CartResponse> cartResponses = new ArrayList<>();
        for (Cart cart : carts) {
            ProductDetailResponse product = (productService.getDetailProduct(cart.getProductId()));
            List<String> attributeValues = attributeValueRepository.getAttributeValuesByVariantID(cart.getVariantId());
            cartResponses.add(CartResponse.form(cart, String.join(", ", attributeValues), product));
        }
        return cartResponses;
    }

    public List<CartResponse> getCartForOrder(List<Integer> cartIds) {
        List<CartResponse> cartResponses = new ArrayList<>();
        for (int cartId : cartIds) {
            Cart cart = cartRepository.getCartById(cartId);
            if (cart == null) {
                throw new CartNotFoundException("Cart ID: " + cartId + " not found");
            }
            ProductDetailResponse product = productService.getDetailProduct(cart.getProductId());
            List<String> attributeValues = attributeValueRepository.getAttributeValuesByVariantID(cart.getVariantId());
            cartResponses.add(CartResponse.form(cart, String.join(", ", attributeValues), product));
        }
        return cartResponses;
    }

    public boolean addToCart(Cart cart) {
        eventRepository.save(new Event(cart.getCustomerId(), cart.getProductId(), "ADD_TO_CART", new Date()));

        Cart isExist = cartRepository.isExist(cart.getCustomerId(), cart.getProductId(), cart.getVariantId());
        if (isExist != null) {
            cart.setQuantity(cart.getQuantity() + isExist.getQuantity());
            cart.setId(isExist.getId());
            return updateCart(cart);
        }
        if (cart.getVariantId() == 0) {
            return cartRepository.addToCart(cart.getCustomerId(), cart.getProductId(), cart.getQuantity());
        }
        return cartRepository.addToCart(cart.getCustomerId(), cart.getProductId(), cart.getQuantity(), cart.getVariantId());
    }

    public boolean removeFromCart(int customerId, int productId, int variantId) {
        return cartRepository.removeFromCart(customerId, productId, variantId);
    }

    public boolean removeFromCart(int id) {
        return cartRepository.removeFromCart(id);
    }

    public boolean updateCart(Cart cartUpdate) {
        Cart cart = cartRepository.getCartById(cartUpdate.getId());
        if (cart == null) {
            throw new RuntimeException("Cart ID: " + cartUpdate.getId() + " not found");
        }
        if (cart.getVariantId() != cartUpdate.getVariantId()) {
            Cart isExist = cartRepository.isExist(cartUpdate.getCustomerId(), cartUpdate.getProductId(), cartUpdate.getVariantId());
            if (isExist != null) {
                Variant variant = variantRepository.getById(cartUpdate.getVariantId());
                cartUpdate.setQuantity(Math.min(isExist.getQuantity() + cartUpdate.getQuantity(), variant.getQuantity()));
                cartRepository.removeFromCart(cartUpdate.getId());
                cartUpdate.setId(isExist.getId());
            }
        }
        return cartRepository.updateCart(cartUpdate);
    }


}
