package kz.askar.shop.config;

import kz.askar.shop.entity.Role;
import kz.askar.shop.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private UserDetailService userDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .and()
                .authorizeRequests()
                .requestMatchers("/review/moderate","/order/moderate","/products/admin",
                        "products/admin/category/{id}","products/admin/search","products/update","products/delete").hasRole(Role.ADMIN.name())
                .requestMatchers("/auth/login", "/auth/registration","/products/user","/products/view").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin().loginPage("/auth/login")
                .usernameParameter("login")
                .defaultSuccessUrl("/products/user")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login");
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
