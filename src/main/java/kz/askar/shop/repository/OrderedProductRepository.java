package kz.askar.shop.repository;

import kz.askar.shop.entity.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct,Long> {
}
