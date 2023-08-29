package kz.askar.shop.contoller;


import kz.askar.shop.entity.Product;
import kz.askar.shop.entity.User;
import kz.askar.shop.repository.ProductRepository;
import kz.askar.shop.service.ReviewService;
import kz.askar.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/review")
public class ReviewController {

     private final ReviewService reviewService;
     private final UserService userService;
     private final ProductRepository productRepository;

    public ReviewController(ReviewService reviewService, UserService userService, ProductRepository productRepository) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.productRepository = productRepository;

    }


    @RequestMapping("")
     public String saveReview(@RequestParam("rating")Integer rating,
                              @RequestParam("reviewText") String reviewText,
                              @RequestParam("productId")Long productId){

        Product product = productRepository.findById(productId).orElseThrow();
        User user = userService.getCurrentUser();

        reviewService.addReview(user,product,rating,reviewText);

        return "redirect:products/view?productId="+ product.getId();
     }

}
