package br.edu.iff.ccc.bsi.dpskt_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final OAuth2LoginSuccessHandler successHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/oauth2/**", "/h2-console/**", "/main.css", "/images/**", "/icons/**")
            .permitAll()
            .anyRequest().authenticated())
        .oauth2Login(oauth -> oauth
            .loginPage("/login")
            .successHandler(successHandler))
        .logout(logout -> logout
            .logoutSuccessUrl("/login"))
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin()));
    return http.build();
  }
}
