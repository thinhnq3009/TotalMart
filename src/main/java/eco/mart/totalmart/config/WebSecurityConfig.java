package eco.mart.totalmart.config;

import eco.mart.totalmart.auth.Role;
import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.services.NotificationService;
import eco.mart.totalmart.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/public/**",
                                "/",
                                "/home",
                                "/detail/**",
                                "/account/**",
                                "/reset-password/**",
                                "/forgot-password",
                                "/api/v1/**",
                                "/search/**",
                                "/show"
                        ).permitAll()
                        .requestMatchers(
                                "/cart/**",
                                "/checkout/**",
                                "/order/all"
                        ).hasAnyRole(Role.CUSTOMER.name(), Role.ADMIN.name())

                        .requestMatchers(
                                "/admin/**",
                                "/user-manager"
                        ).hasAnyRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
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
            if (exception instanceof UsernameNotFoundException) {
                notificationService.addError(exception.getMessage());
            } else {
                notificationService.addError("Password is incorrect");
            }
            response.sendRedirect("/account/login");
        };
    }

    private AuthenticationSuccessHandler getCustomAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {

            String redirect = request.getParameter("to");
            User user = (User) userService.loadUserByUsername(authentication.getName());

            notificationService.addInfo("Welcome back " + user.getFullname() + " !");
            response.sendRedirect(redirect != null ? redirect : "/");
        };
    }


}
