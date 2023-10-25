package kz.askar.shop.contoller;

import kz.askar.shop.entity.*;
import kz.askar.shop.repository.CategoryRepository;
import kz.askar.shop.repository.CharacteristicValueRepository;
import kz.askar.shop.repository.ProductRepository;
import kz.askar.shop.repository.ReviewRepository;
import kz.askar.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/products")
public class ProductController {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CharacteristicValueRepository characteristicValueRepository;
    private final UserService userService;
    private final ReviewRepository reviewRepository;

    public ProductController(CategoryRepository categoryRepository, ProductRepository productRepository, CharacteristicValueRepository characteristicValueRepository, UserService userService, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.characteristicValueRepository = characteristicValueRepository;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }


    @RequestMapping(path = "/admin")
    public String showProductsAdmin(Model model) {

        List<Product> products = new ArrayList<>();

        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("categories",categories);

        products = productRepository.findAll();

        if (!products.isEmpty()) {
            model.addAttribute("productList", products);
        }

        return "/view/data/product-view/product_page_main_admin";
    }

    @RequestMapping(path = "/user")
    public String showProductsUser(Model model) {

        List<Product> products = productRepository.findAll();

        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("categories",categories);

        if (!products.isEmpty()) {
            model.addAttribute("productList", products);
        }

        return "/view/data/product-view/product_page_user";
    }



    @RequestMapping("/user/category/{id}")
    public String showProductsByCategory(Model model,@PathVariable Long id){

        Optional<Category> category = categoryRepository.findById(id);

        List<Product> products = productRepository.findByCategory(category);

        model.addAttribute("products",products);

        return "/view/data/product-view/product_page_by_category";
    }


    @RequestMapping("/admin/category/{id}")
    public String showProductsByCategoryAdmin(Model model,@PathVariable Long id){

        Optional<Category> category = categoryRepository.findById(id);

        List<Product> products = productRepository.findByCategory(category);

        model.addAttribute("products",products);

        return "/view/data/product-view/product_page_by_category_admin";
    }




    @GetMapping("/user/search")
    public String searchByName(@RequestParam("name") String name, Model model) {
        List<Product> products = productRepository.findByNameIgnoreCaseContaining(name);
        model.addAttribute("products", products);
        model.addAttribute("name",name);
        return "/view/data/product-view/product_result_search";
    }



    @GetMapping("/admin/search")
    public String searchByNameForAdmin(@RequestParam("name") String name, Model model) {
        List<Product> products = productRepository.findByNameIgnoreCaseContaining(name);
        model.addAttribute("products", products);
        model.addAttribute("name",name);
        return "/view/data/product-view/product_result_search_forAdmin";
    }






    @GetMapping(path = "/create")
    public String showEmptyForm(Model model) {
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categoryList", categoryList);


        return "view/data/product-view/product_create_page";
    }


    @PostMapping(path = "/create")
    public String submitForm(Model model,
                             @RequestParam(name = "name") String name,
                             @RequestParam(name = "price") Integer price,
                             @RequestParam(name = "categoryId") Long categoryId) {


        Category category = categoryRepository.findById(categoryId).orElseThrow();

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);

        productRepository.save(product);

        return "redirect:/products/user";
    }


    @GetMapping(path = "/view")
    public String viewProduct(Model model,
                              @RequestParam(name = "productId", required = false) Long productId) {

        Product product = productRepository.findById(productId).orElseThrow();


        User user = userService.getCurrentUser();
        model.addAttribute("user",user);

        model.addAttribute("product", product);

        int sum = 0;
        float avg = 0;
        int uploadedReviews = 0;


        for (Review review : product.getReviews()) {
            if (review.isStatus()) {
                sum += review.getRating();
                uploadedReviews++;
            }
        }


        if (uploadedReviews != 0) {
            for (Review review : product.getReviews()) {
                if (review.isStatus()) {
                    sum += review.getRating();
                    uploadedReviews++;
                }
            }
            avg = (float) sum / uploadedReviews;
        } else {
            avg = 0;
            boolean reviewsIsEmpty = true;
            model.addAttribute("reviewIsEmpty", reviewsIsEmpty);
        }

        model.addAttribute("avg", avg);


        Review review = reviewRepository.findByUserAndProduct(user, product);

        boolean check = false;

        if (review == null && user != null) {
            check = true;
        }

        boolean userIsEmpty = false;
        if (user == null){
            userIsEmpty = true;
        }

        model.addAttribute("userIsEmpty",userIsEmpty);

        model.addAttribute("check",check);

        return "view/data/product-view/product_view_page";
    }


    @GetMapping(path = "/update")
    public String updateProduct(Model model,
                                @RequestParam(name = "productId") Long productId) {


        Product product = productRepository.findById(productId).orElseThrow();
        model.addAttribute("product", product);


        return "view/data/product-view/product_update_page";
    }


    @PostMapping(path = "/update")
    public String updateProduct(Model model,
                                @RequestParam(name = "productId") Long productId,
                                @RequestParam(name = "name") String name,
                                @RequestParam(name = "price") Integer price,
                                @RequestParam(name = "values") List<String> values) {


        model.addAttribute("productId", productId);


        Product product = productRepository.findById(productId).orElseThrow();


        product.setName(name);
        product.setPrice(price);

        List<CharacteristicValue> characteristicValues = product.getCharacteristicValues();


        for (int i = 0; i < characteristicValues.size(); i++) {
            characteristicValues.get(i).setValue(values.get(i));
        }


        product.setCharacteristicValues(characteristicValues);


        productRepository.save(product);


        return "redirect:/products/view?productId=" + productId;
    }


    @GetMapping(path = "/delete")
    public String deleteProductPost(@PathVariable("productId") Long productId) {

        Product product = productRepository.findById(productId).orElseThrow();

        productRepository.delete(product);

        return "redirect:product-view/products/admin";
    }


}
