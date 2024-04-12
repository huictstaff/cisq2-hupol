package nl.hu.cisq2.hupol.security.presentation.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.hu.cisq2.hupol.security.application.AuthenticationService;
import nl.hu.cisq2.hupol.security.domain.UserProfile;
import nl.hu.cisq2.hupol.security.presentation.dto.RegisterRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Tries to authenticate a user (confirm credentials and identity),
 * based on the incoming request.
 * <p>
 * Once authenticated, it will return a Bearer token (JWT) set in the
 * Authorization header of the 200 Response.
 * <p>
 * This exact Bearer has to be added in the Authorization header of subsequent
 * requests to restricted endpoints.
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final String secret;
    private final Integer expirationInMs;

    private final AuthenticationService authenticationService;

    public JwtAuthenticationFilter(
            String path,
            String secret,
            Integer expirationInMs,
            AuthenticationService authenticationService
    ) {
        super(new AntPathRequestMatcher(path));

        this.secret = secret;
        this.expirationInMs = expirationInMs;
        this.authenticationService = authenticationService;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        var credentials =
                new ObjectMapper().readValue(request.getInputStream(), RegisterRequest.class);

        var userProfile = this.authenticationService
                .login(credentials.username(), credentials.password());

        return new UsernamePasswordAuthenticationToken(userProfile, null, userProfile.authorities());
    }
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) {
        UserProfile userProfile = (UserProfile) authResult.getPrincipal();

        List<String> roles = userProfile.authorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(this.secret.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .setIssuer("hupoll")
                .setAudience("auth")
                .setSubject(userProfile.username())
                .setExpiration(new Date(System.currentTimeMillis() + this.expirationInMs))
                .claim("roles", roles)
                .compact();

        response.addHeader("Authorization", "Bearer " + token);
    }
}
