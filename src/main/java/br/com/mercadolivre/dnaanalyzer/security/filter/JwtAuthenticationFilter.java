package br.com.mercadolivre.dnaanalyzer.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private String jwtSecretKey;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    public JwtAuthenticationFilter(final AuthenticationManager authenticationManager, final String jwtSecretKey) {

        super(authenticationManager);
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request, final HttpServletResponse response,
        final FilterChain chain) throws IOException, ServletException {

        final String headerAuthorizationValue = request.getHeader(HEADER_AUTHORIZATION);

        if (headerAuthorizationValue == null || !headerAuthorizationValue.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        final UsernamePasswordAuthenticationToken authentication = this.getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(
        final HttpServletRequest request) throws IOException {

        final String headerAuthorizationValue = request.getHeader(HEADER_AUTHORIZATION);

        final String jwtSecretKeyBase64 =
            Base64.getEncoder().encodeToString(jwtSecretKey.getBytes(StandardCharsets.UTF_8.displayName()));

        if (headerAuthorizationValue != null) {
            try {
                final Claims data = Jwts.parser()
                    .setSigningKey(jwtSecretKeyBase64)
                    .parseClaimsJws(headerAuthorizationValue.replace(TOKEN_PREFIX, ""))
                    .getBody();

                if (data != null) {
                    return new UsernamePasswordAuthenticationToken(data, null, new ArrayList<>());
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }
        return null;
    }

}
