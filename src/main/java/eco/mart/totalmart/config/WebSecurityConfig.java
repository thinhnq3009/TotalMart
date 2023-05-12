package eco.mart.totalmart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static eco.mart.totalmart.auth.Permission.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }





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
                        .requestMatchers("/admin/**").hasAnyAuthority(ADMIN_READONLY.getValue())
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/account/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/account/login?error=true")
                        .failureHandler((request, response, exception) -> {
                            System.out.println("Error: " + exception.getMessage());
                            request.setAttribute("error", exception.getMessage());
                            response.sendRedirect("/account/login");
                        })
                        .permitAll())


                .logout()
                .permitAll()
        ;

        return http.build();
    }


}
