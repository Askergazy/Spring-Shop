package kz.askar.shop.contoller;


import kz.askar.shop.humans.Human;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/view_controller")
public class ViewController {



    @RequestMapping(path =  "/first_resource")
    public String firstResource(Model model){

        model.addAttribute("message","Message from viewController.firstResource()");

        return "view/first_resource";
    }

//    @RequestMapping(path = "/second_resource")
//    public String secondResource(Model model){
//
//        Human human = new Human("Марк",4);
//        model.addAttribute("human",human);
//
//        return "view/first_resource";
//    }


    @RequestMapping(path = "/third_resource")
    public String thirdResource(Model model){

        List<Human> humans = new ArrayList<>();

        model.addAttribute("humans",humans);


        humans.add(new Human("Марк",23));
        humans.add(new Human("Джефф",45));
        humans.add(new Human("Макс",15));

        if (!humans.isEmpty()){
            int avg = 0;
            int sum = 0;
            for(Human human : humans){
                sum+= human.getAge();
            }
            avg = sum / humans.size();
            model.addAttribute("avg",avg);
        }


        return "view/third_resource";
    }


}
