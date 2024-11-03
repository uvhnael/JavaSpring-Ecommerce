package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uvhnael.ecomapi.Dto.OrderItemResponse;
import org.uvhnael.ecomapi.Dto.OrderResponse;
import org.uvhnael.ecomapi.Filter.RequestLoggingFilter;
import org.uvhnael.ecomapi.Model.*;
import org.uvhnael.ecomapi.Repository.*;
import org.uvhnael.ecomapi.exception.coupon.CouponExpiredException;
import org.uvhnael.ecomapi.exception.coupon.CouponNotFoundException;
import org.uvhnael.ecomapi.exception.coupon.CouponUsageLimitExceededException;
import org.uvhnael.ecomapi.exception.order.OrderCreationException;
import org.uvhnael.ecomapi.exception.order.OrderItemException;
import org.uvhnael.ecomapi.exception.product.ProductOutOfStockException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;
    private final CouponRepository couponRepository;
    private final AddressRepository addressRepository;
    private final GalleryRepository galleryRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final MailService mailService;
    private final EventRepository eventRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);


    @Transactional
    public List<OrderResponse> getByCustomerId(int id) {
        logger.info("Getting orders for customer ID: {}", id);

        List<Order> orders = orderRepository.getOrdersByCustomerId(id);

        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepository.getByOrderId(order.getId());
            List<OrderItemResponse> orderItemResponses = new ArrayList<>();
            for (OrderItem orderItem : orderItems) {
                String productName = productRepository.getProductName(orderItem.getProductId());
                String imagePath = galleryRepository.getThumbnail(orderItem.getProductId());
                List<String> attributeValues = attributeValueRepository.getAttributeValuesByVariantID(orderItem.getVariantId());
                orderItemResponses.add(OrderItemResponse.from(orderItem, productName, imagePath, String.join(", ", attributeValues)));
            }
            Address address = addressRepository.getById(order.getCustomerAddressId());
            orderResponses.add(OrderResponse.from(order, address, orderItemResponses));

        }

        logger.info("Orders retrieved successfully for customer ID: {}", id);
        return orderResponses;

    }

    @Transactional
    public OrderResponse getById(int id) {
        logger.info("Getting order by ID: {}", id);

        Order order = orderRepository.getById(id);
        List<OrderItem> orderItems = orderItemRepository.getByOrderId(order.getId());
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            String productName = productRepository.getProductName(orderItem.getProductId());
            String imagePath = galleryRepository.getThumbnail(orderItem.getProductId());
            List<String> attributeValues = attributeValueRepository.getAttributeValuesByVariantID(orderItem.getVariantId());
            orderItemResponses.add(OrderItemResponse.from(orderItem, productName, imagePath, String.join(", ", attributeValues)));
        }
        Address address = addressRepository.getById(order.getCustomerAddressId());

        logger.info("Order retrieved successfully by ID: {}", id);
        return OrderResponse.from(order, address, orderItemResponses);
    }

    @Transactional
    public boolean createOrder(Order order) {
        logger.info("Creating order for customer ID: {}", order.getCustomerId());

        int orderId = orderRepository.createOrder(order.getPrice(), order.getCouponId(), order.getCustomerId(), order.getCustomerAddressId());

        if (orderId == 0) {
            logger.error("Order creation failed for customer ID: {}", order.getCustomerId());
            throw new OrderCreationException("Order creation failed.");
        }

        if (isCoupon(order.getCouponId())) {
            couponRepository.updateTimesUsed(order.getCouponId());
        }


        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem.getProductId() == 0) {
                logger.error("Order item creation failed cause product ID is required.");
                throw new IllegalArgumentException("Product ID is required for order item.");
            }
            if (orderItem.getQuantity() <= 0) {
                logger.error("Order item creation failed cause quantity must be greater than 0.");
                throw new IllegalArgumentException("Quantity must be greater than 0 for product ID: " + orderItem.getProductId());
            }
            if (orderItem.getPrice() < 0) {
                logger.error("Order item creation failed cause price must be greater than 0.");
                throw new IllegalArgumentException("Price must be greater than 0 for product ID: " + orderItem.getProductId());
            }

            eventRepository.save(new Event(order.getCustomerId(), orderItem.getProductId(), "PURCHASE", new Date()));


            int quantity;

            if (orderItem.getVariantId() == 0) {
                Product product = productRepository.getById(orderItem.getProductId());
                quantity = product.getQuantity();
            } else {
                Variant variant = variantRepository.getById(orderItem.getVariantId());
                quantity = variant.getQuantity();
            }

            if (quantity < orderItem.getQuantity()) {
                logger.error("Product ID: {} is out of stock.", orderItem.getProductId());
                throw new ProductOutOfStockException("Product ID: " + orderItem.getProductId() + " is out of stock.");
            }

            if (!orderItemRepository.addOrderItem(orderItem.getProductId(), orderId, orderItem.getVariantId(), orderItem.getPrice(), orderItem.getQuantity())) {
                logger.error("Order item creation failed for product ID: {}", orderItem.getProductId());
                throw new OrderItemException("Order item creation failed for product ID: " + orderItem.getProductId());
            }

            if (orderItem.getVariantId() != 0) {
                variantRepository.updateQuantity(orderItem.getVariantId(), orderItem.getQuantity());
            }

            productRepository.updateQuantity(orderItem.getProductId(), orderItem.getQuantity());

            cartRepository.removeFromCart(order.getCustomerId(), orderItem.getProductId(), orderItem.getVariantId());

            // update coupon usage
            if (order.getCouponId() != 0) {
                couponRepository.updateTimesUsed(orderItem.getCouponId());
            }

            // send email to customer
            try {
                mailService.send(new Mail(customerRepository.getEmail(order.getCustomerId()), "Order Created", mailService.orderSuccess()));
            } catch (Exception e) {
                logger.error("Failed to send email to customer ID: {}", order.getCustomerId());
            }
            // send notification to admin


        }

        logger.info("Order created successfully for customer ID: {}", order.getCustomerId());
        return true;
    }

    private boolean isCoupon(int couponId) {
        if (couponId == 0) {
            return false;
        }
        Coupon coupon = couponRepository.isExist(couponId);
        if (coupon == null) {
            throw new CouponNotFoundException("Coupon ID: " + couponId + " not found.");
        }
        if (coupon.getTimesUsed() >= coupon.getMaxUsage()) {
            throw new CouponUsageLimitExceededException("Coupon ID: " + couponId + " usage limit exceeded.");
        }
        if (LocalDateTime.parse(coupon.getCouponEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .toLocalDate().isBefore(LocalDate.now())) {
            throw new CouponExpiredException("Coupon ID: " + couponId + " expired.");
        }
        return true;
    }
}
