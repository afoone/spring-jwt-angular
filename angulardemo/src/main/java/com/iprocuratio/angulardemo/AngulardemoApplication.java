package com.iprocuratio.angulardemo;

import java.util.stream.Stream;

import com.iprocuratio.angulardemo.model.User;
import com.iprocuratio.angulardemo.repository.UserRepository;

import com.iprocuratio.angulardemo.security.JWTAuthorizationFilter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AngulardemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AngulardemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			Stream.of("Juan", "Julia", "Jesus", "Javier", "Jimena").forEach(name -> {
				User user = new User(name, name.toLowerCase() + "@iprocuratio.com");
				user.setPassword("password");
				user.setUser(name.toLowerCase());
				userRepository.save(user);
			});
			userRepository.findAll().forEach(System.out::println);
		};
	}

	// CORS
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}

	// @Bean
    // CorsFilter corsFilter() {
    //     CorsFilter filter = new CorsFilter();
    //     return filter;
    // }

	// Configuración de seguridad
	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
					.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
					.authorizeRequests()
						.antMatchers(HttpMethod.POST, "/login/**").permitAll()
					 	//.antMatchers(HttpMethod.GET, "/users/**").permitAll()
					.anyRequest().authenticated();
					http.cors();
		}
	}

}
