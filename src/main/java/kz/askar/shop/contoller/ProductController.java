package kz.askar.shop.contoller;

import kz.askar.shop.entity.*;
import kz.askar.shop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    private final CharacteristicService characteristicService;
    private final static String UPLOAD_DIR = "C:\\Users\\askar\\OneDrive\\Desktop\\images";


    @RequestMapping(path = "/admin")
    public String showProductsAdmin(Model model) {


        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories", categories);

        List<Product> products = productService.findAll();

        if (!products.isEmpty()) {
            model.addAttribute("productList", products);
        }

        return "/view/data/product-view/product_page_main_admin";
    }


    @RequestMapping(path = "/main")
    public String showProductsUser(Model model) {




        User user = userService.getCurrentUser();

        List<Product> products = productService.findAll();

        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories", categories);

        if (!products.isEmpty()) {
            model.addAttribute("productList", products);
        }

        if (user == null) {
            boolean userIsEmpty = true;
            model.addAttribute("userIsEmpty", userIsEmpty);
        }



        return "/view/data/product-view/product_page_main";
    }


    @RequestMapping("/main/category/{id}")
    public String showProductsByCategory(Model model, @PathVariable Long id) {

        User user = userService.getCurrentUser();

        if (user == null) {
            boolean userIsEmpty = true;
            model.addAttribute("userIsEmpty", userIsEmpty);
        }

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        Optional<Category> category = categoryService.findById(id);

        List<Product> products = productService.findByCategory(category);

        model.addAttribute("products", products);

        return "/view/data/product-view/product_page_by_category";
    }


    @RequestMapping("/admin/category/{id}")
    public String showProductsByCategoryAdmin(Model model, @PathVariable Long id) {

        Optional<Category> category = categoryService.findById(id);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        List<Product> products = productService.findByCategory(category);

        model.addAttribute("products", products);

        return "/view/data/product-view/product_page_by_category";
    }

    @GetMapping("/main/search")
    public String searchByName(@RequestParam("name") String name, Model model) {
        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories", categories);

        User user = userService.getCurrentUser();

        if (user == null) {
            boolean userIsEmpty = true;
            model.addAttribute("userIsEmpty", userIsEmpty);
        }

        List<Product> products = productService.findByNameIgnoreCaseContaining(name);
        model.addAttribute("products", products);
        model.addAttribute("name", name);
        return "/view/data/product-view/product_result_search";
    }


    @GetMapping("/admin/search")
    public String searchByNameForAdmin(@RequestParam("name") String name, Model model) {
        List<Product> products = productService.findByNameIgnoreCaseContaining(name);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories",categories);
        model.addAttribute("products", products);
        model.addAttribute("name", name);
        return "/view/data/product-view/product_result_search";
    }


    @GetMapping(path = "/create")
    public String showEmptyForm(Model model) {

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories",categories);



        return "view/data/product-view/product_create_page";
    }





    @GetMapping(path = "/createByCategory{id}" )
    public String createProductByCategory(@PathVariable (name = "id") Long id,Model model){

        Category category = categoryService.findById(id).orElseThrow();

        model.addAttribute("categoryId",category.getId());

        List<Characteristic> characteristics = characteristicService.findCharacteristicsByCategory(category);
        model.addAttribute("characteristics",characteristics);

        return "view/data/product-view/product_create_characteristics";
    }


    @PostMapping(path = "/createByCategory{id}")
    public String submitForm(Model model,
                             @RequestParam(name = "name") String name,
                             @RequestParam(name = "price") Integer price,
                             @RequestParam(name = "categoryId") Long categoryId,
                             @RequestPart(name = "image") MultipartFile file,
                             @RequestParam(name = "values") List<String> values) {

        Product product = new Product();
        Category category = categoryService.findById(categoryId).orElseThrow();

        List<Characteristic> characteristics = characteristicService.findCharacteristicsByCategory(category);
        model.addAttribute("characteristics",characteristics);

        System.out.println(values);
        List<CharacteristicValue> characteristicValues = new ArrayList<>();

        System.out.println(characteristicValues);



        System.out.println(characteristicValues);

        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);

        for (Characteristic characteristic : characteristics){
            CharacteristicValue characteristicValue = new CharacteristicValue();
            for (String value : values){
                characteristicValue.setValue(value);
                characteristicValue.setProduct(product);
                characteristicValue.setCharacteristic(characteristic);
                characteristicValues.add(characteristicValue);
            }
        }


        product.setCharacteristicValues(characteristicValues);

        try {
            InputStream inputStream = file.getInputStream();

            String uploadDirectory = "C:\\Users\\askar\\OneDrive\\Desktop\\images";
            String fileName = file.getOriginalFilename();
            String filePath = uploadDirectory + File.separator + fileName;

            try (OutputStream outputStream = new FileOutputStream(filePath)) {
                FileCopyUtils.copy(inputStream, outputStream);
            }

            product.setImage(fileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        productService.save(product);


        return "redirect:/products/main";
    }



    @GetMapping(path = "/view")
    public String viewProduct(Model model,
                              @RequestParam(name = "productId", required = false) Long productId) {


        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);


        Product product = productService.findById(productId).orElseThrow();


        User user = userService.getCurrentUser();
        model.addAttribute("user", user);


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
        if (user == null) {
            userIsEmpty = true;
        }

        model.addAttribute("userIsEmpty", userIsEmpty);

        model.addAttribute("check", check);

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


    @RequestMapping(path = "/delete")
    public String deleteProductPost(@RequestParam(name = "productId") Long productId,Model model) {

        Product product = productService.findById(productId).orElseThrow();


        productService.delete(product);

        return "redirect:/products/admin";
    }


}
