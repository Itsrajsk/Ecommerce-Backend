package com.shop.ecommerce.service;

import com.shop.ecommerce.exception.ProductException;
import com.shop.ecommerce.model.Cart;
import com.shop.ecommerce.model.CartItem;
import com.shop.ecommerce.model.Product;
import com.shop.ecommerce.model.User;
import com.shop.ecommerce.repository.CartRepository;
import com.shop.ecommerce.request.AddItemRequest;

public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartItemService cartItemService;

    private final ProductService productService;

    public CartServiceImpl(CartRepository cartRepository, CartItemService cartItemService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findByProductId(req.getProductId());

        CartItem isPresent = cartItemService.isCartItemExist(cart, product, req.getSize(), userId);

        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);
        }
        return "";
    }

    @Override
    public Cart findUserCart(Long userId) {
        return null;
    }
}
