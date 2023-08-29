package kz.askar.shop.contoller;

import kz.askar.shop.entity.*;
import kz.askar.shop.repository.CategoryRepository;
import kz.askar.shop.repository.CharacteristicValueRepository;
import kz.askar.shop.repository.ProductRepository;
import kz.askar.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/products")
public class ProductController {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CharacteristicValueRepository characteristicValueRepository;
    private final UserService userService;

    public ProductController(CategoryRepository categoryRepository, ProductRepository productRepository, CharacteristicValueRepository characteristicValueRepository, UserService userService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.characteristicValueRepository = characteristicValueRepository;
        this.userService = userService;
    }


    @RequestMapping(path = "")
    public String showProducts(Model model) {

        List<Product> products = new ArrayList<>();

        products = productRepository.findAll();

        if (!products.isEmpty()) {
            model.addAttribute("productList", products);
        }

        return "view/data/product-view/product_page_main";
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

        return "redirect:product-view/products";
    }


    @GetMapping(path = "/view")
    public String viewProduct(Model model,
                              @RequestParam(name = "productId", required = false) Long productId) {

        Product product = productRepository.findById(productId).orElseThrow();


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



         if (uploadedReviews != 0){
             for (Review review : product.getReviews()) {
                 if (review.isStatus()) {
                     sum += review.getRating();
                     uploadedReviews++;
                 }
             }
             avg =(float) sum / uploadedReviews;
         }else {
             avg = 0;
             boolean reviewsIsEmpty = true;
             model.addAttribute("reviewIsEmpty",reviewsIsEmpty);
         };




        model.addAttribute("avg", avg);

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

        return "redirect:product-view/products";
    }








}
