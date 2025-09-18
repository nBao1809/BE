package tnb.project.restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tnb.project.restaurant.Filter.JwtAuthenticationFilter;
import tnb.project.restaurant.Filter.ApiLoggingFilter;

import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public ApiLoggingFilter apiLoggingFilter() {
        return new ApiLoggingFilter();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://your-frontend-domain.com")); // FE domain
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter, ApiLoggingFilter apiLoggingFilter) throws Exception {
        http.cors(withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Auth endpoints
//                .requestMatchers(POST, "/api/auth").permitAll()
//                // Category endpoints
//                .requestMatchers(GET, "/api/categories").permitAll()
//                .requestMatchers(GET, "/api/categories/{categoryId}").permitAll()
//                .requestMatchers(GET, "/api/categories/with-dishes").permitAll()
//                .requestMatchers(POST, "/api/categories").hasRole("ADMIN")
//                .requestMatchers(PATCH, "/api/categories/{categoryId}").hasRole("ADMIN")
//                .requestMatchers(DELETE, "/api/categories/{categoryId}").hasRole("ADMIN")
//                // Customer endpoints
//                .requestMatchers(GET, "/api/customers").permitAll()
//                .requestMatchers(GET, "/api/customers/{customerId}").permitAll()
//                .requestMatchers(POST, "/api/customers").hasAnyRole("ADMIN", "CASHIER")
//                .requestMatchers(PATCH, "/api/customers/{customerId}").hasRole("ADMIN")
//                .requestMatchers(DELETE, "/api/customers/{customerId}").hasRole("ADMIN")
//                // Dish endpoints
//                .requestMatchers(GET, "/api/dishes").permitAll()
//                .requestMatchers(GET, "/api/dishes/{id}").permitAll()
//                .requestMatchers(POST, "/api/dishes").hasRole("ADMIN")
//                .requestMatchers(PATCH, "/api/dishes/{id}").hasRole("ADMIN")
//                .requestMatchers(PATCH, "/api/dishes/{id}/status").hasAnyRole("ADMIN", "CASHIER", "CHEF")
//                .requestMatchers(DELETE, "/api/dishes/{id}").hasAnyRole("ADMIN")
//                // Employee endpoints
//                .requestMatchers(GET, "/api/employees").hasRole("ADMIN")
//                .requestMatchers(GET, "/api/employees/{employeeId}").hasRole("ADMIN")
//                .requestMatchers(POST, "/api/employees").hasRole("ADMIN")
//                .requestMatchers(PATCH, "/api/employees/{employeeId}").hasRole("ADMIN")
//                .requestMatchers(DELETE, "/api/employees/{employeeId}").hasRole("ADMIN")
//                // Feedback endpoints
//                .requestMatchers(GET, "/api/feedbacks").permitAll()
//                .requestMatchers(GET, "/api/feedbacks/{feedbackId}").permitAll()
//                .requestMatchers(POST, "/api/feedbacks").permitAll()
//                .requestMatchers(PUT, "/api/feedbacks/{feedbackId}").hasRole("ADMIN")
//                .requestMatchers(DELETE, "/api/feedbacks/{feedbackId}").hasRole("ADMIN")
//                // OptionDetail endpoints
//                .requestMatchers(GET, "/api/option-details").permitAll()
//                .requestMatchers(GET, "/api/option-details/{optionDetailId}").permitAll()
//                .requestMatchers(POST, "/api/option-details").hasAnyRole("ADMIN", "CASHIER", "CHEF")
//                .requestMatchers(PATCH, "/api/option-details/{optionDetailId}").hasAnyRole("ADMIN", "CASHIER", "CHEF")
//                .requestMatchers(DELETE, "/api/option-details/{optionDetailId}").hasAnyRole("ADMIN", "CASHIER", "CHEF")
//                // OptionType endpoints
//                .requestMatchers(GET, "/api/option-types").permitAll()
//                .requestMatchers(GET, "/api/option-types/{optionTypeId}").permitAll()
//                .requestMatchers(POST, "/api/option-types").hasAnyRole("ADMIN")
//                .requestMatchers(PATCH, "/api/option-types/{optionTypeId}").hasAnyRole("ADMIN")
//                .requestMatchers(DELETE, "/api/option-types/{optionTypeId}").hasAnyRole("ADMIN")
//                // OrderDetail endpoints
//                .requestMatchers(GET, "/api/order-details").permitAll()
//                .requestMatchers(GET, "/api/order-details/{orderDetailId}").permitAll()
//                .requestMatchers(POST, "/api/order-details").permitAll()
//                .requestMatchers(PATCH, "/api/order-details/{orderDetailId}").permitAll()
//                .requestMatchers(PATCH, "/api/order-details/{orderDetailId}/quantity-status").permitAll()
//                // Orders endpoints
//                .requestMatchers(GET, "/api/orders").permitAll()
//                .requestMatchers(GET, "/api/orders/{orderId}").permitAll()
//                .requestMatchers(POST, "/api/orders").permitAll()
//                .requestMatchers(PATCH, "/api/orders/{orderId}").permitAll()
//                .requestMatchers(DELETE, "/api/orders/{orderId}").permitAll()
//                .requestMatchers(POST, "/api/orders/{orderId}/order-details").permitAll()
//                .requestMatchers(GET, "/api/orders/kitchen/order-details/group-by-status").hasRole("CHEF")
//                .requestMatchers(PATCH, "/api/orders/{orderId}/status").hasAnyRole("ADMIN", "CASHIER", "CHEF")
//                .requestMatchers(GET, "/api/orders/by-session/{sessionId}").permitAll()
//                // Payment endpoints
//                .requestMatchers(GET, "/api/payments").hasAnyRole("ADMIN", "CASHIER")
//                .requestMatchers(GET, "/api/payments/{paymentId}").hasAnyRole("ADMIN", "CASHIER")
//                .requestMatchers(POST, "/api/payments").hasAnyRole("ADMIN", "CASHIER")
//                .requestMatchers(PATCH, "/api/payments/{paymentId}").hasAnyRole("ADMIN", "CASHIER")
//                .requestMatchers(PATCH, "/api/payments/{paymentId}/status").hasAnyRole("ADMIN", "CASHIER")
//                .requestMatchers(DELETE, "/api/payments/{paymentId}").hasAnyRole("ADMIN", "CASHIER")
//                .requestMatchers(GET, "/api/payments/statistics/revenue").hasRole("ADMIN")
//                .requestMatchers(POST, "/api/payments/cash").hasAnyRole("ADMIN", "CASHIER")
//                .requestMatchers(POST, "/api/payments/pay").hasAnyRole("ADMIN", "CASHIER")
//                // ServiceRequest endpoints
//                .requestMatchers(GET, "/api/service-requests").permitAll()
//                .requestMatchers(GET, "/api/service-requests/{serviceRequestId}").hasAnyRole("ADMIN", "CASHIER", "CHEF")
//                .requestMatchers(POST, "/api/service-requests").permitAll()
//                .requestMatchers(PATCH, "/api/service-requests/{serviceRequestId}/handle").hasAnyRole("ADMIN", "CASHIER", "CHEF")
//                .requestMatchers(GET, "/api/service-requests/session/{sessionId}").permitAll()
//                // ServiceType endpoints
//                .requestMatchers(GET, "/api/service-types").permitAll()
//                .requestMatchers(GET, "/api/service-types/{serviceTypeId}").permitAll()
//                .requestMatchers(POST, "/api/service-types").hasRole("ADMIN")
//                .requestMatchers(PATCH, "/api/service-types/{serviceTypeId}").hasRole("ADMIN")
//                .requestMatchers(DELETE, "/api/service-types/{serviceTypeId}").hasRole("ADMIN")
//                // Session endpoints
//                .requestMatchers(GET, "/api/sessions").permitAll()
//                .requestMatchers(GET, "/api/sessions/{sessionId}").permitAll()
//                .requestMatchers(POST, "/api/sessions").permitAll()
//                .requestMatchers(PATCH, "/api/sessions/{sessionId}").hasAnyRole("ADMIN", "EMPLOYEE")
//                .requestMatchers(POST, "/api/sessions/{sessionId}/end").hasAnyRole("ADMIN", "EMPLOYEE")
//                .requestMatchers(DELETE, "/api/sessions/{sessionId}").hasAnyRole("ADMIN", "EMPLOYEE")
//                // Tables endpoints
//                .requestMatchers(GET, "/api/tables").permitAll()
//                .requestMatchers(GET, "/api/tables/{tableId}").permitAll()
//                .requestMatchers(POST, "/api/tables").hasRole("ADMIN")
//                .requestMatchers(PATCH, "/api/tables/{tableId}").hasRole("ADMIN")
//                .requestMatchers(DELETE, "/api/tables/{tableId}").hasRole("ADMIN")
                .anyRequest().permitAll()
            )
            .addFilterBefore(apiLoggingFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
