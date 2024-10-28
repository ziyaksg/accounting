package com.uydev.config;

import com.uydev.services.SecurityService;
import com.uydev.services.impl.SecurityServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@Log4j2
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

    private final AuthSuccessHandler authSuccessHandler;
    private final SecurityServiceImpl securityService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authrize->{

                    authrize
                            .requestMatchers("/companies/**").hasAnyAuthority("Root User");
                    authrize
                            .requestMatchers("/", "/login", "fragments", "/assets/**", "/img/**")
                            .permitAll();
                    authrize.anyRequest().authenticated();
                    })
                .formLogin(form->form
                        .loginPage("/login")
                        .successHandler(authSuccessHandler)
                        .failureUrl("/login?error=true")

                )
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                )
                .rememberMe(token->token.tokenValiditySeconds(3600).key("uydev")
                        .userDetailsService(securityService));

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    ApplicationListener<AuthenticationSuccessEvent> successEvent(){
        return event ->
                log.info("success: {}", event.getAuthentication());
    }
    @Bean
    ApplicationListener<AuthenticationFailureBadCredentialsEvent> failureEnet(){
        return event ->
                log.info("failure: {}", event.getAuthentication());
    }

}
