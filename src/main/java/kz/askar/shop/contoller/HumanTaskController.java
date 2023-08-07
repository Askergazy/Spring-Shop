package kz.askar.shop.contoller;


import kz.askar.shop.humans.Human;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(path = "/human_task_controller")
public class HumanTaskController {


    Human[] humans = new Human[]{
            new Human("Max", 20),
            new Human("Sam", 30),
            new Human("Tom", 40)
    };


    @RequestMapping(path = "/human_resource")
    public String humanResource(
            Model model,
            @RequestParam(name = "ageTo", required = false) Integer ageTo,
            @RequestParam(name = "ageFrom", required = false) Integer ageFrom
//            @RequestParam(name = "firstName",required = false)String nameFromParam
    ) {

        List<Human> humans = new ArrayList<>();

        humans.add(new Human("Марк",23));
        humans.add(new Human("Джефф",45));
        humans.add(new Human("Макс",15));

//        model.addAttribute("name",nameFromParam);
        model.addAttribute("ageFrom",ageFrom);
        model.addAttribute("ageTo",ageTo);

//        if (ageFrom == null && ageTo == null) return Arrays.toString(humans);

        if (ageFrom == null) ageFrom = Integer.MIN_VALUE;
        if (ageTo == null) ageTo = Integer.MAX_VALUE;

        List<Human> humanList = new ArrayList<>();
        model.addAttribute("humanList",humanList);


        for (Human human : humans) {
            if (human.getAge() >= ageFrom && human.getAge() <= ageTo) {
                humanList.add(human);
            }
        }


        return "view/human_task_page";
    }


}




