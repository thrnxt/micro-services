package org.example.productservice;


import org.example.productservice.feign.UserDto;
import org.example.productservice.feign.UserServiceClient;
import org.example.productservice.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final UserServiceClient userClient;

    public ProductController(ProductService productService, UserServiceClient userClient) {
        this.productService = productService;
        this.userClient = userClient;
    }

    @GetMapping
    public List<Product> all() {
        return productService.findAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.save(product);
    }

    @GetMapping("/{id}/with-owner")
    public ProductWithOwner getWithOwner(@PathVariable Long id) {
        Product p = productService.findById(id);
        UserDto owner = userClient.getUserById(p.getOwnerId());
        return new ProductWithOwner(p, owner);
    }

    public record ProductWithOwner(Product product, UserDto owner) { }
}
