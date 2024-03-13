package org.caneroksuz.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class OrderServiceSecurityConfig {

  private static final String[] WHITELIST = {
		  "/swagger-ui/**",
		  "/v3/api-docs/**",
		  "/api/v1/order/**",
		  "/api/v1/product/**",


  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	httpSecurity.csrf().disable();
	httpSecurity.authorizeRequests().antMatchers(WHITELIST).permitAll().anyRequest().authenticated();
	httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	return httpSecurity.build();
  }
}
