package kz.askar.shop.repository;

import kz.askar.shop.entity.Category;
import kz.askar.shop.entity.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CharacteristicRepository extends JpaRepository<Characteristic,Long> {


    List<Characteristic> findCharacteristicByCategory(Category category);


}
