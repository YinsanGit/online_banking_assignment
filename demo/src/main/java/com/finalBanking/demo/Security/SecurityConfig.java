package com.finalBanking.demo.Security;

import com.finalBanking.demo.Jwt.JwtAuthenticationFilter;
import com.finalBanking.demo.Jwt.JwtTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@Service
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;

    public SecurityConfig(UserDetailsService userDetailsService, JwtTokenService jwtTokenService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())        // <— wire in CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/register").hasRole("ADMIN")
                        .requestMatchers("/api/auth/delete/{id}").hasRole("ADMIN")
                        .requestMatchers("/roles", "/permissions").permitAll()
                        .requestMatchers("/api/roles/**", "/api/permissions/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()
                        .requestMatchers( "/accounts").hasAuthority("CREATE_ACCOUNT")
                        .requestMatchers( "/accounts/getAll").permitAll()
                        .requestMatchers( "/accounts/{id}").hasRole("MANAGER")
                        .requestMatchers("api/users/{id}/roles").permitAll()
                        .requestMatchers("/api/transfer").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jsonAuthEntryPoint())
                        .accessDeniedHandler(jsonAccessDeniedHandler())
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenService, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .formLogin(Customizer.withDefaults())
                .authenticationProvider(daoAuthenticationProvider());
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // injected bean, not "new"
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationSuccessHandler jsonAuthSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(200);
            response.setContentType("application/json");
            String body = String.format("{\"message\":\"Success\",\"user\":\"%s\"}", escape(authentication.getName()));
            response.getWriter().write(body);
        };
    }
    // Build AuthenticationManager from the configured providers
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    private AuthenticationEntryPoint jsonAuthEntryPoint() {
        return (request,
                response,
                authException)
                -> writeJson(response,
                401,
                "Unauthorized"
                , authException.getMessage());
    }
    private AccessDeniedHandler jsonAccessDeniedHandler() {
        return (request,
                response,
                accessDeniedException)
                -> writeJson(response,
                403, "Forbidden", accessDeniedException.getMessage());
    }

    private void writeJson(jakarta.servlet.http.HttpServletResponse response, int status, String error, String details) throws IOException, IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        String body = String.format("{\"error\":\"%s\",\"details\":\"%s\"}", escape(error), escape(details == null ? "" : details));
        response.getWriter().write(body);
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }


}







