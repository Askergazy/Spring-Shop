package kz.askar.shop.contoller;

import kz.askar.shop.entity.CartItem;
import kz.askar.shop.entity.Order;
import kz.askar.shop.entity.OrderedProduct;
import kz.askar.shop.entity.User;
import kz.askar.shop.service.CartItemService;
import kz.askar.shop.service.OrderService;
import kz.askar.shop.service.OrderedProductService;
import kz.askar.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final UserService userService;
    private final CartItemService cartItemService;
    private final OrderedProductService orderedProductService;
    private final OrderService orderService;

    public OrderController(UserService userService, CartItemService cartItemService, OrderedProductService orderedProductService, OrderService orderService) {
        this.userService = userService;
        this.cartItemService = cartItemService;
        this.orderedProductService = orderedProductService;
        this.orderService = orderService;
    }


    @GetMapping("")
    public String mainPage(Model model) {

        User user = userService.getCurrentUser();

        List<CartItem> cartItemList = cartItemService.getCartItemsByUser(user);


        model.addAttribute("cartItemList", cartItemList);

        int sum = 0;

        for (CartItem cartItem : cartItemList){
            sum+= cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }

        model.addAttribute("sum",sum);


        return "view/data/order-view/order_main";
    }


    @PostMapping("")
    private String mainPagePost(@RequestParam("address") String addressParam) {
        User user = userService.getCurrentUser();
        List<CartItem> cartItemList = cartItemService.getCartItemsByUser(user);


        String address = addressParam;


        Order order = orderService.createOrder(user, address);
        orderedProductService.createOrderedProduct(cartItemList, order);


        cartItemService.deleteAllCartItems(user);


        return "redirect:/cart";
    }


}
