package kz.askar.shop.service;

import kz.askar.shop.entity.Category;
import kz.askar.shop.entity.Product;
import kz.askar.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService{

    private final ProductRepository productRepository;


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByCategory(Optional<Category> category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> findByNameIgnoreCaseContaining(String name) {
        return productRepository.findByNameIgnoreCaseContaining(name);
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }


    public void save(Product product) {

        productRepository.save(product);
    }

    public void delete(Product product) {

        productRepository.save(product);
    }
}
