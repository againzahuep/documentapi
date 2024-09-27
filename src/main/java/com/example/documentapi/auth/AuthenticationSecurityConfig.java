package com.example.documentapi.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableMethodSecurity
@Configuration
public class AuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	@Bean
	public UserDetailsService userDetailsService() {
		var manager = new InMemoryUserDetailsManager();

		var user1 = User.withUsername("john")
				.password("12345")
				.roles("ADMIN")
				.build();

		var user2 = User.withUsername("bill")
				.password("12345")
				.roles("MANAGER")
				.build();

		manager.createUser(user1);
		manager.createUser(user2);

		return manager;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Override
	public void configure(HttpSecurity http) throws Exception {

		http
				.authorizeRequests()
//				.antMatchers("/** ","/api/users/**","/api/documents/**", "/api/documents/upload", "/api/documents/download/**", "/api/documents/delete/**")
//				.permitAll()
				.anyRequest()
				.authenticated()
//				.and()
//				.oauth2Login()
//				.and()
//				.exceptionHandling()
//				.authenticationEntryPoint(
//						new LoginUrlAuthenticationEntryPoint("/"))
//				.and()
//				.logout()
//				.logoutSuccessUrl("/")
//				.permitAll()
				.and()

				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.oauth2ResourceServer()
				.jwt(); // O puedes usar Opaque Token dependiendo de tu configuración

		http.csrf().disable();
	}

}
