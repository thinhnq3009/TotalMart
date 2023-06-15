package eco.mart.totalmart.config;

import eco.mart.totalmart.auth.Role;
import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.services.NotificationService;
import eco.mart.totalmart.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserService userService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/public/**",
                                "/",
                                "detail/**",
                                "/account/register"
                        ).permitAll()
                        .requestMatchers("/api/v1/**").permitAll()
//                        .requestMatchers("/admin/**").hasAnyAuthority(ADMIN_READONLY.getValue())
                        .requestMatchers(
                                "/admin/**",
                                "/checkout/**",
                                "/cart/**",
                                "/profile/**"
                        ).permitAll()
                        .requestMatchers("/demo/**").hasRole(Role.CUSTOMER.name())
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/account/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/account/login?error=true")
                        .failureHandler(getCustomAuthenticationFailureHandler())
                        .successHandler(getCustomAuthenticationSuccessHandler())
                        .permitAll())
                .logout()
                .permitAll()
        ;

        return http.build();
    }

    private AuthenticationFailureHandler getCustomAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            notificationService.addError(exception.getClass().getName());
            response.sendRedirect("/account/login");
        };
    }

    private AuthenticationSuccessHandler getCustomAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {

            User user = (User) userService.loadUserByUsername(authentication.getName());

            notificationService.addInfo("Welcome back " + user.getFullname() + " !");
            response.sendRedirect("/");
        };
    }


}
