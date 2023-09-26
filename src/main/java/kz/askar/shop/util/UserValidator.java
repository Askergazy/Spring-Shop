package kz.askar.shop.util;

import kz.askar.shop.entity.User;
import kz.askar.shop.service.UserDetailService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {


    private final UserDetailService userDetailService;

    public UserValidator(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;

        try {
            userDetailService.loadUserByUsername(user.getLogin());
        } catch (UsernameNotFoundException ignored) {
            return;
        }


        errors.rejectValue("login", ""
                , "Челвек с таким логином уже уже существует");
    }
}
