package kz.askar.shop.contoller;

import kz.askar.shop.entity.User;
import kz.askar.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {


    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("")
    public String profile(Model model){

        User user = userService.getCurrentUser();

        model.addAttribute("user",user);


        return "view/data/profile_view";
    }
}
