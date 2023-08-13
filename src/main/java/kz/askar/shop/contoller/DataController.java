package kz.askar.shop.contoller;


import kz.askar.shop.entity.Category;
import kz.askar.shop.entity.Product;
import kz.askar.shop.repository.ProductRepository;
import org.hibernate.validator.constraints.ModCheck;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import kz.askar.shop.repository.CategoryRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/data_controller")
public class DataController {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @RequestMapping(path = "/first_resource")
    public String firstResource(Model model) {
        Category category = categoryRepository.findById(1L).orElseThrow();
        model.addAttribute("category", category);
        return "view/data/first_resource_page";
    }


    @RequestMapping(path = "/second_resource")
    public String secondResource(Model model) {

        List<Product> productList = productRepository.findAll();
        model.addAttribute("productList", productList);

        return "view/data/second_resource_page";
    }

    @RequestMapping(path = "/third_resource")
    public String thirdResource(@RequestParam(name = "categoryId", required = false) Long categoryId,
                                Model model) {

        List<Product> productList = new ArrayList<>();

        if (categoryId == null) {
            productList = productRepository.findAll();
        } else {
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            productList = productRepository.findByCategory(category);
        }
        model.addAttribute(productList);

        return "view/data/third_resource_page";
    }


}