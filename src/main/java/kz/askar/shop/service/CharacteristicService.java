package kz.askar.shop.service;


import kz.askar.shop.entity.Category;
import kz.askar.shop.entity.Characteristic;
import kz.askar.shop.repository.CharacteristicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacteristicService {


    private final CharacteristicRepository characteristicRepository;


  public List<Characteristic>findCharacteristicsByCategory(Category category){
      return  characteristicRepository.findCharacteristicByCategory(category);
  }


}
