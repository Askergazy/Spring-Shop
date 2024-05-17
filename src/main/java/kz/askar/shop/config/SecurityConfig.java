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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrfConfigurer -> {
            csrfConfigurer.ignoringRequestMatchers("/rest/**");
        });

        httpSecurity.authorizeHttpRequests(authorizationConfigurer -> {
            authorizationConfigurer.requestMatchers(
                    "/review/moderate",
                    "/order/moderate",
                    "/products/admin",
                    "products/admin/category/{id}",
                    "products/admin/search",
                    "products/update",
                    "products/delete",
                    "products/create"
            ).hasRole(Role.ADMIN.name());
            authorizationConfigurer.requestMatchers("/cart", "/cart/add").authenticated();
            authorizationConfigurer.anyRequest().permitAll();
        });
        httpSecurity
                .logout().logoutSuccessUrl("/products/main")
                .and()
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer.defaultSuccessUrl("/products/main");
                });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
