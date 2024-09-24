package nl.hu.cisq2.hupol.security;

import io.jsonwebtoken.security.Keys;
import nl.hu.cisq2.hupol.security.application.AuthenticationService;
import nl.hu.cisq2.hupol.security.presentation.filter.JwtAuthenticationFilter;
import nl.hu.cisq2.hupol.security.presentation.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@Configuration
public class SecurityConfig {
    private final static String LOGIN_PATH = "/auth/login";
    private final static String REGISTER_PATH = "/auth/register";

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expiration-in-ms}")
    private Integer jwtExpirationInMs;

    @Bean
    protected AuthenticationManager authenticationManager(final PasswordEncoder passwordEncoder, final UserDetailsService userDetailsService) {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    protected SecurityFilterChain filterChain(final HttpSecurity http, final AuthenticationService authenticationService, final AuthenticationManager authenticationManager) throws Exception {
        SecretKey signingKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));

        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(antMatcher(POST, REGISTER_PATH)).permitAll()
                        .requestMatchers(antMatcher(POST, LOGIN_PATH)).permitAll()
                        .requestMatchers(antMatcher("/error")).anonymous()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(
                        LOGIN_PATH,
                        signingKey,
                        jwtExpirationInMs,
                        authenticationService
                ), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtAuthorizationFilter(signingKey, authenticationManager))
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS));
        return http.build();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
