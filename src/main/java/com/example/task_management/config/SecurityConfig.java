package com.example.task_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui/index.html",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/v3/api-docs.json",
                    "/h2-console/**"
                ).permitAll()
                .anyRequest().authenticated()
            .and()
            .headers().frameOptions().disable()
            .and()
            .httpBasic();

        return http.build();
    }

	@Bean
	public OpenAPI openAPI() {
	    return new OpenAPI()
	        .components(new Components().addSecuritySchemes("basicScheme",
	            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
	        .addSecurityItem(new SecurityRequirement().addList("basicScheme"));
	}

}
