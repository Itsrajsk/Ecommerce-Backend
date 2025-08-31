package com.shop.ecommerce.service;

import com.shop.ecommerce.exception.ProductException;
import com.shop.ecommerce.model.Category;
import com.shop.ecommerce.model.Product;
import com.shop.ecommerce.repository.CategoryRepository;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final UserService userService;

    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, UserService userService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product createProduct(CreateProductRequest req) {
        Category topLevel = categoryRepository.findByName(req.getTopLevelCategory());

        if (topLevel == null) {
            Category topLevelCategory = new Category();
            topLevelCategory.setName(req.getTopLevelCategory());
            topLevelCategory.setLevel(1);

            topLevel = categoryRepository.save(topLevelCategory);
        }

        Category secondLevel = categoryRepository.findByName(req.getSecondLevelCategory());

        if (secondLevel == null) {
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(req.getSecondLevelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);

            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        Category thirdLevel = categoryRepository.findByName(req.getThirdLevelCategory());

        if (thirdLevel == null) {
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(req.getThirdLevelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);

            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice((double) req.getDiscountedPrice());
        product.setDiscountPercent(req.getDiscountPersent());
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(req.getPrice());
        product.setSizes(req.getSize());
        product.setQuantity(req.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findByProductId(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product Deleted Successfully";
    }

    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {
        Product product = findByProductId(productId);
        if (req.getQuantity() != 0) {
            product.setQuantity(req.getQuantity());
        }
        return productRepository.save(product);
    }

    @Override
    public Product findByProductId(Long id) throws ProductException {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new ProductException("Product not found with id - " + id);
    }

    @Override
    public List<Product> findByProductByCategory(String category) {
        return List.of();
    }

    @Override
    public Page<Product> getAllProducts(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
        if (!colors.isEmpty()) {
            products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor()))).toList();
        }

        if (stock != null) {
            if (stock.equals("in_stock")) {
                products = products.stream().filter(p -> p.getQuantity() > 0).toList();
            } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(p -> p.getQuantity() < 1).toList();
            }
        }

        int startIndex = Math.toIntExact(pageable.getOffset());
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        List<Product> pageContent = products.subList(startIndex, endIndex);
        Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());

        return filteredProducts;
    }
}
