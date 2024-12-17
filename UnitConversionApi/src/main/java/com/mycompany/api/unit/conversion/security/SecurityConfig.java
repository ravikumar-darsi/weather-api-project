package com.mycompany.api.unit.conversion.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		
		provider.setUserDetailsService(userDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
		.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.httpBasic(Customizer.withDefaults())
		.csrf(csrf -> csrf.disable());
		
		return http.build();
	}
}
