package kz.askar.shop.service;


import kz.askar.shop.entity.Product;
import kz.askar.shop.entity.Review;
import kz.askar.shop.entity.User;
import kz.askar.shop.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    public void addReview(User user, Product product,Integer rating,String reviewText){

        Review review = new Review();

        review.setReviewDate(Timestamp.valueOf(LocalDateTime.now()));
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setReviewText(reviewText);
        review.setStatus(false);



        reviewRepository.save(review);

    }

    public Review findByUserAndProduct(User user, Product product) {
        return reviewRepository.findByUserAndProduct(user,product);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Optional<Review> findById(Long reviewId) {
       return   reviewRepository.findById(reviewId);
    }

    public void delete(Review review) {
        reviewRepository.delete(review);
    }

    public void save(Review review) {
        reviewRepository.save(review);
    }
}
