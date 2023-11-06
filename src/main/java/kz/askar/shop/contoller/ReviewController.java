package kz.askar.shop.contoller;


import kz.askar.shop.entity.Product;
import kz.askar.shop.entity.Review;
import kz.askar.shop.entity.User;
import kz.askar.shop.service.ProductService;
import kz.askar.shop.service.ReviewService;
import kz.askar.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;



@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;




    @RequestMapping("")
    public String saveReview(@RequestParam("rating") Integer rating,
                             @RequestParam("reviewText") String reviewText,
                             @RequestParam("productId") Long productId,
                             Model model) {

        Product product = productService.findById(productId).orElseThrow();
        User user = userService.getCurrentUser();

        reviewService.addReview(user, product, rating, reviewText);

        return "redirect:products/view?productId=" + product.getId();
    }


    @RequestMapping("/moderate")
    public String moderate(Model model) {


        List<Review> reviewList = reviewService.findAll();

        model.addAttribute("reviewList", reviewList);


        return "view/data/admin-view/moderator_page";
    }

    @RequestMapping("/delete")
    public String deleteReview(@RequestParam("reviewId") Long reviewId) {


        Review review = reviewService.findById(reviewId).orElseThrow();

        reviewService.delete(review);


        return "redirect:/review/moderate";
    }


    @RequestMapping("/approve")
    public String approveReview(@RequestParam("reviewId") Long reviewId) {

        Review review = reviewService.findById(reviewId).orElseThrow();

        review.setStatus(true);

        reviewService.save(review);

        return "redirect:/review/moderate";
    }

}
