package com.consultas.agendamento_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/login").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasAnyRole("ADMIN", "ATENDENTE")
                        .requestMatchers(HttpMethod.GET, "/", "/api/**").hasAnyRole("ADMIN", "ATENDENTE")
                        .requestMatchers(HttpMethod.POST, "/pacientes", "/consultas").hasAnyRole("ADMIN", "ATENDENTE")
                        .requestMatchers(HttpMethod.POST, "/medicos", "/consultas/*/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/pacientes", "/api/consultas").hasAnyRole("ADMIN", "ATENDENTE")
                        .requestMatchers(HttpMethod.POST, "/api/medicos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/consultas/*/status").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(SecurityProperties properties, PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername(properties.getAdmin().getUsername())
                        .password(passwordEncoder.encode(properties.getAdmin().getPassword()))
                        .roles("ADMIN")
                        .build(),
                User.withUsername(properties.getAtendente().getUsername())
                        .password(passwordEncoder.encode(properties.getAtendente().getPassword()))
                        .roles("ATENDENTE")
                        .build());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Components securityComponents() {
        return new Components().addSecuritySchemes(
                "basicAuth",
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic"));
    }

    @Bean
    public SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList("basicAuth");
    }
}
