package kz.askar.shop.contoller;

import kz.askar.shop.entity.*;
import kz.askar.shop.service.CategoryService;
import kz.askar.shop.service.ProductService;
import kz.askar.shop.service.ReviewService;
import kz.askar.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/products")
public class ProductController {


    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ReviewService reviewService;




    @RequestMapping(path = "/admin")
    public String showProductsAdmin(Model model) {

        List<Product> products = new ArrayList<>();

        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories",categories);

        products = productService.findAll();

        if (!products.isEmpty()) {
            model.addAttribute("productList", products);
        }

        return "/view/data/product-view/product_page_main_admin";
    }

    @RequestMapping(path = "/user")
    public String showProductsUser(Model model) {

        User user = userService.getCurrentUser();

        List<Product> products =productService.findAll();

        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories",categories);

        if (!products.isEmpty()) {
            model.addAttribute("productList", products);
        }

        if (user == null){
            boolean userIsEmpty = true;
            model.addAttribute("userIsEmpty",userIsEmpty);
        }

        return "/view/data/product-view/product_page_user";
    }



    @RequestMapping("/user/category/{id}")
    public String showProductsByCategory(Model model,@PathVariable Long id){

        User user = userService.getCurrentUser();

        if (user == null){
            boolean userIsEmpty = true;
            model.addAttribute("userIsEmpty",userIsEmpty);
        }

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories",categories);

        Optional<Category> category = categoryService.findById(id);

        List<Product> products = productService.findByCategory(category);

        model.addAttribute("products",products);

        return "/view/data/product-view/product_page_by_category";
    }


    @RequestMapping("/admin/category/{id}")
    public String showProductsByCategoryAdmin(Model model,@PathVariable Long id){

        Optional<Category> category = categoryService.findById(id);

        List<Product> products = productService.findByCategory(category);

        model.addAttribute("products",products);

        return "/view/data/product-view/product_page_by_category_admin";
    }




    @GetMapping("/user/search")
    public String searchByName(@RequestParam("name") String name, Model model) {
        List<Category> categories =categoryService.findAll();

        model.addAttribute("categories",categories);

        User user = userService.getCurrentUser();

        if (user == null){
            boolean userIsEmpty = true;
            model.addAttribute("userIsEmpty",userIsEmpty);
        }

        List<Product> products = productService.findByNameIgnoreCaseContaining(name);
        model.addAttribute("products", products);
        model.addAttribute("name",name);
        return "/view/data/product-view/product_result_search";
    }



    @GetMapping("/admin/search")
    public String searchByNameForAdmin(@RequestParam("name") String name, Model model) {
        List<Product> products = productService.findByNameIgnoreCaseContaining(name);
        model.addAttribute("products", products);
        model.addAttribute("name",name);
        return "/view/data/product-view/product_result_search_forAdmin";
    }






    @GetMapping(path = "/create")
    public String showEmptyForm(Model model) {
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);


        return "view/data/product-view/product_create_page";
    }


    @PostMapping(path = "/create")
    public String submitForm(Model model,
                             @RequestParam(name = "name") String name,
                             @RequestParam(name = "price") Integer price,
                             @RequestParam(name = "categoryId") Long categoryId) {


        Category category = categoryService.findById(categoryId).orElseThrow();

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);

        productService.save(product);

        return "redirect:/products/user";
    }


    @GetMapping(path = "/view")
    public String viewProduct(Model model,
                              @RequestParam(name = "productId", required = false) Long productId) {


        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories",categories);


        Product product = productService.findById(productId).orElseThrow();


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


        Review review = reviewService.findByUserAndProduct(user, product);

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


        Product product = productService.findById(productId).orElseThrow();
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


        Product product = productService.findById(productId).orElseThrow();


        product.setName(name);
        product.setPrice(price);

        List<CharacteristicValue> characteristicValues = product.getCharacteristicValues();


        for (int i = 0; i < characteristicValues.size(); i++) {
            characteristicValues.get(i).setValue(values.get(i));
        }


        product.setCharacteristicValues(characteristicValues);


        productService.save(product);


        return "redirect:/products/view?productId=" + productId;
    }


    @GetMapping(path = "/delete")
    public String deleteProductPost(@PathVariable("productId") Long productId) {

        Product product = productService.findById(productId).orElseThrow();

        productService.delete(product);

        return "redirect:product-view/products/admin";
    }


}
