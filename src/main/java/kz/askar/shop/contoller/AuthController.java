package kz.askar.shop.contoller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kz.askar.shop.entity.User;
import kz.askar.shop.service.RegistrationService;
import kz.askar.shop.util.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    public AuthController(UserValidator userValidator, RegistrationService registrationService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }


    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {


        return "/view/data/auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {


        return "/view/data/auth/registration";
    }


    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/view/data/auth/registration";
        }

        registrationService.register(user);



        return "redirect:/view/data/auth/login";
    }


}
