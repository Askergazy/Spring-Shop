package kz.askar.shop.service;


import kz.askar.shop.entity.CartItem;
import kz.askar.shop.entity.Order;
import kz.askar.shop.entity.OrderedProduct;
import kz.askar.shop.repository.OrderedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderedProductService {


    private final OrderedProductRepository orderedProductRepository;



    public  void createOrderedProduct(List<CartItem> cartItemList, Order order){

        for (CartItem cartItem : cartItemList) {
            OrderedProduct orderedProduct = new OrderedProduct();

            orderedProduct.setProduct(cartItem.getProduct());

            orderedProduct.setCount(cartItem.getQuantity());

            orderedProduct.setOrder(order);

            orderedProductRepository.save(orderedProduct);
        }
    }

    private void save(OrderedProduct orderedProduct) {
        orderedProductRepository.save(orderedProduct);
    }
}
