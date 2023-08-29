package kz.askar.shop.repository;

import kz.askar.shop.entity.Category;
import kz.askar.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {


    List<Product> findByCategory(Category category);


    @Override
    <S extends Product> S save(S entity);


    @Override
    void delete(Product entity);




}
