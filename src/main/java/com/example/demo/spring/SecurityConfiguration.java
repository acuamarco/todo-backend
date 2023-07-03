package com.example.demo.spring;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .authorizeHttpRequests(auth ->  auth.requestMatchers("/**").authenticated())
        .httpBasic(withDefaults());

    return http.build();
  }

  @Bean
  public UserDetailsService users() {
    var users = User.withDefaultPasswordEncoder();
    var user = users
        .username("user")
        .password("secret")
        .roles("USER")
        .build();
    var admin = users
        .username("admin")
        .password("super-secret")
        .roles("USER", "ADMIN")
        .build();
    return new InMemoryUserDetailsManager(user, admin);
  }
}
