package kz.askar.shop.contoller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/test_controller")
@RestController
public class TestController {

    @RequestMapping(path = "/first_resource",produces =  "text/plain")
    public String firstResource(){

        return "<h1>ShopApplication</h1>";
    }


    @RequestMapping(path = "/second_resource")
    public String secondResource(
            @RequestParam(name = "name",required = false) String name,
            @RequestParam(name = "age",required = false) Integer age

    ){
        return  "Имя : " + name + " Возраст: " + age;
    }

}
