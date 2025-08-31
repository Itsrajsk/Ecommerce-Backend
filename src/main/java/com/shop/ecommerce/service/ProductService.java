package com.shop.ecommerce.service;

import com.shop.ecommerce.exception.ProductException;
import com.shop.ecommerce.model.Product;
import com.shop.ecommerce.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest req);
    public String deleteProduct(Long productId) throws ProductException;
    public Product updateProduct(Long productId, Product product) throws ProductException;
    public Product findByProductId(Long id) throws ProductException;
    public List<Product> findByProductByCategory(String category);
    public Page<Product> getAllProducts(String category, List<String>colors, List<String>sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);
}
