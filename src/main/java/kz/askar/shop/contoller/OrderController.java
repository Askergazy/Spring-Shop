package kz.askar.shop.contoller;

import kz.askar.shop.entity.*;
import kz.askar.shop.repository.OrderRepository;
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

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final UserService userService;
    private final CartItemService cartItemService;
    private final OrderedProductService orderedProductService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(UserService userService, CartItemService cartItemService, OrderedProductService orderedProductService, OrderService orderService, OrderRepository orderRepository) {
        this.userService = userService;
        this.cartItemService = cartItemService;
        this.orderedProductService = orderedProductService;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
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



    @GetMapping("/view")
    public String orderView(@RequestParam("orderId") Long orderId,
                            Model model){
        Order order = orderRepository.findById(orderId).orElseThrow();


        int sum = 0;

        for (OrderedProduct orderedProduct :order.getOrderedProducts()){
            sum += orderedProduct.getProduct().getPrice() * orderedProduct.getCount();
        }

        model.addAttribute("order",order);
        model.addAttribute("sum",sum);

        List<Status> statuses = List.of(Status.values());
        model.addAttribute("statuses",statuses);

        User user = userService.getCurrentUser();
        model.addAttribute("user",user);
        return "view/data/order-view/order-view";
    }

    @PostMapping("/view")
    public String changeStatus(@RequestParam("orderId")Long orderId,
                               @RequestParam("status")Status status,
                               Model model){




        Order order = orderRepository.findById(orderId).orElseThrow();

        order.setStatus(status);
        orderRepository.save(order);

        return "redirect:/order/view?orderId=" + orderId;
    }





    @GetMapping("/moderate")
    public String moderateOrders(Model model){

        List<Order> orders = orderRepository.findAll();

        model.addAttribute("orders",orders);


     return "view/data/admin-view/order_moderator_page";
    }


}
