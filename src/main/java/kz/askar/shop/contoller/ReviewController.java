package kz.askar.shop.contoller;


import kz.askar.shop.entity.Product;
import kz.askar.shop.entity.Review;
import kz.askar.shop.entity.User;
import kz.askar.shop.repository.ProductRepository;
import kz.askar.shop.repository.ReviewRepository;
import kz.askar.shop.service.ReviewService;
import kz.askar.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;



@Controller
@RequestMapping(path = "/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewService reviewService, UserService userService, ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }


    @RequestMapping("")
    public String saveReview(@RequestParam("rating") Integer rating,
                             @RequestParam("reviewText") String reviewText,
                             @RequestParam("productId") Long productId,
                             Model model) {

        Product product = productRepository.findById(productId).orElseThrow();
        User user = userService.getCurrentUser();

        reviewService.addReview(user, product, rating, reviewText);

        return "redirect:products/view?productId=" + product.getId();
    }


    @RequestMapping("/moderate")
    public String moderate(Model model) {


        List<Review> reviewList = reviewRepository.findAll();

        model.addAttribute("reviewList", reviewList);


        return "view/data/admin-view/moderator_page";
    }

    @RequestMapping("/delete")
    public String deleteReview(@RequestParam("reviewId") Long reviewId) {


        Review review = reviewRepository.findById(reviewId).orElseThrow();

        reviewRepository.delete(review);


        return "redirect:/review/moderate";
    }


    @RequestMapping("/approve")
    public String approveReview(@RequestParam("reviewId") Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow();

        review.setStatus(true);

        reviewRepository.save(review);

        return "redirect:/review/moderate";
    }

}
