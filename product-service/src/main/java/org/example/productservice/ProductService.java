package org.example.productservice;

import org.example.productservice.model.Product;
import org.example.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo) { this.repo = repo; }

    public List<Product> findAll() { return repo.findAll(); }
    public Product findById(Long id) { return repo.findById(id).orElseThrow(); }
    public Product save(Product p) { return repo.save(p); }
}