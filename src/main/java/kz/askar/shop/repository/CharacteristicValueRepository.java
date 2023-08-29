package kz.askar.shop.repository;

import kz.askar.shop.entity.CharacteristicValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacteristicValueRepository extends JpaRepository<CharacteristicValue,Long> {

}
