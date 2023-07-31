package com.example.security3.config;

import com.example.security3.config.jwt.JwtAuthenticationFilter;
import com.example.security3.config.jwt.JwtAuthorizationFilter;
import com.example.security3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private UserRepository userRepository;


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                // 세션을 사용하지 않음.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/hello").authenticated()
                        .anyRequest().permitAll()
                )
                .apply(new CustomFilter());
        return http.build();

    }

    public class CustomFilter extends AbstractHttpConfigurer<CustomFilter, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            System.out.println("configure 호출");
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
            System.out.println("configure 호출 끝");
        }
    }

}
