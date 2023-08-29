package kz.askar.shop.service;

import kz.askar.shop.entity.CartItem;
import kz.askar.shop.entity.Product;
import kz.askar.shop.entity.User;
import kz.askar.shop.repository.CartItemRepository;
import kz.askar.shop.repository.ProductRepository;
import kz.askar.shop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartItemService {


    private final CartItemRepository cartItemRepository;
    private  final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartItemService(CartItemRepository cartItemRepository, UserRepository userRepository,  ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;

        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    public List<CartItem> getCartItemsByUser(User user){
        return cartItemRepository.findByUser(user);
    }


    public void cartAdd(Long userId,Long productId){

        User user = userRepository.findById(userId).orElseThrow();

        Product product = productRepository.findById(productId).orElseThrow();



        Boolean check = true;

        for (int i = 0; i < cartItemRepository.findByUser(user).size(); i++) {
             if (cartItemRepository.findByUser(user).get(i).getProduct().equals(product)){
                 check = false;
                 cartItemRepository.findByUser(user).get(i).setQuantity(cartItemRepository.findByUser(user).get(i).getQuantity() + 1);
                 cartItemRepository.save(cartItemRepository.findByUser(user).get(i));
             }
        }

        if (check){
            CartItem cartItem = new CartItem();

            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);

            cartItemRepository.save(cartItem);
        }


    }


    public void deleteCartItem(Long cartItemId){

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();

        cartItemRepository.delete(cartItem);
    }


    public void deleteAllCartItems(User user){


        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        cartItemRepository.deleteAll(cartItems);

    }

    public void increaseQuantity(Long cartItemId){

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();

        Integer quantity = cartItem.getQuantity() + 1;

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

    }


    public void decreaseQuantity(Long cartItemId){

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();

        Integer quantity = cartItem.getQuantity() -1;

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

        if (cartItem.getQuantity() < 1){


          cartItemRepository.delete(cartItem);
        }





    }
}
