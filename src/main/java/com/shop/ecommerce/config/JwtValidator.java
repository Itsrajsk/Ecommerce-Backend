package com.shop.ecommerce.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
        if (jwt != null) {
            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                String email = String.valueOf(claims.get("email"));
                String authorites = String.valueOf(claims.get("authorities"));
                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorites);
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                throw new BadCredentialsException("invalid token... from jwt validator");
            }
        }
        filterChain.doFilter(request, response);
    }
}
