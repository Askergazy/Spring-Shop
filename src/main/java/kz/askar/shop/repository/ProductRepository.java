package kz.askar.shop.repository;

import kz.askar.shop.entity.Category;
import kz.askar.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {


    List<Product> findByCategory(Optional<Category> category);


    List<Product> findByNameIgnoreCaseContaining(String name);

    Product findByName(String name);

}
