package com.shop.ecommerce.service;

import com.shop.ecommerce.exception.OrderException;
import com.shop.ecommerce.model.Address;
import com.shop.ecommerce.model.Order;
import com.shop.ecommerce.model.User;
import com.shop.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;

    private final CartService cartService;

    private final ProductService productService;

    public OrderServiceImpl(CartRepository cartRepository, CartService cartService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    @Override
    public Order createOrder(User user, Address shippingAddress) {
        return null;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return List.of();
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return List.of();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {

    }
}
