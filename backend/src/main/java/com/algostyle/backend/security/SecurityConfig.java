package com.algostyle.backend.security;


import com.algostyle.backend.utils.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration  // Indique que cette classe contient la configuration de sécurité

@EnableWebSecurity  // Activer la configuration web de Spring Security

public class SecurityConfig {





    // Configuration de l'encodeur de password  (BCrypt recommandé)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }






    // Configuratin principale de la sécurité HTTP
    @Bean
    public SecurityFilterChain filterchain(HttpSecurity http) throws Exception{
        http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/signup","/api/auth/login").permitAll()
                .anyRequest().authenticated()
        );

        // Ajouter un filtre personnalisé JWT (jwtAuthenticationFilter()) dans la chaîne de sécurité Spring Security, avant le filtre 'UsernamePasswordAuthenticationFilter'
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }






    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }



}
