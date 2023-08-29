package kz.askar.shop.contoller;


import kz.askar.shop.entity.CartItem;
import kz.askar.shop.entity.User;
import kz.askar.shop.service.CartItemService;
import kz.askar.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Controller
@RequestMapping(path = "/cart")
public class CartController {

    private final CartItemService cartItemService;
    private final UserService userService;


    public CartController(CartItemService cartItemService, UserService userService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
    }


    @RequestMapping(path = "")
    public String cart(Model model) {

        User user = userService.getCurrentUser();

        List<CartItem> list = cartItemService.getCartItemsByUser(user);


        Collections.sort(list,new Comparator<CartItem>(){

            @Override
            public int compare(CartItem o1, CartItem o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
        int sum = 0;

        for (CartItem cartItem : list){
            sum+= cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }

        model.addAttribute("sum",sum);



        model.addAttribute("cartItems", list);
        model.addAttribute("user",user);


        return "view/data/cart-view/cart_main";
    }




    @RequestMapping(path = "/add",method = {RequestMethod.GET,RequestMethod.POST})
    public String add(@RequestParam("productId")Long productId){

        Long userId = userService.getCurrentUser().getId();

        cartItemService.cartAdd(userId,productId);

        return "redirect:/products";
    }



   @RequestMapping(path = "/delete")
  public String delete(@RequestParam("cartItemId")Long cartItemId){



        cartItemService.deleteCartItem(cartItemId);



       return "redirect:/cart";
   }



   @RequestMapping(path = "/deleteAll")
   public String delete(){

        User user = userService.getCurrentUser();

        cartItemService.deleteAllCartItems(user);

        return "redirect:/cart" ;
   }



   @RequestMapping(path = "/increaseQuantity")
    public String increase(
            @RequestParam("product")Long cartItemId){

       Long userId = userService.getCurrentUser().getId();

        cartItemService.increaseQuantity(cartItemId);

        return "redirect:/cart";
   }

    @RequestMapping(path = "/decreaseQuantity")
    public String decrease(@RequestParam("product")Long cartItemId){

        Long userId = userService.getCurrentUser().getId();
        cartItemService.decreaseQuantity(cartItemId);

        return "redirect:/cart";
    }




}








