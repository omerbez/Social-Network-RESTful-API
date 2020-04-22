package com.omer.socialapp.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omer.socialapp.exceptions.AnyAuthenticationException;
import com.omer.socialapp.exceptions.ErrorMessage;
import com.omer.socialapp.model.AuthenticationRequestParams;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter 
{
    private AuthenticationManager authenticationManager;
    private final String SECRET;
    private final long EXPIRATION_TIME;
    

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String loginUrl, 
    		final String SECRET, final long EXP) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(loginUrl);  // change the default login path
        this.SECRET = SECRET;
        this.EXPIRATION_TIME = EXP;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Grab the login arguments from the login request
    	AuthenticationRequestParams credentials = null;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequestParams.class);
        } catch (IOException e) {
        	// Trigger unsuccessfulAuthentication() methoed
            throw new AnyAuthenticationException();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword());

        // Authenticate user
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        
        return auth;
    }
    

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Grab principal
        UserDetails principal = (UserDetails) authResult.getPrincipal();

        // Create JWT Token
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        
        Map<String, String> map = new HashMap<>();
        map.put("token", SecurityConfig.TOKEN_PREFIX+token);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(map);
        
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
    		AuthenticationException failedException) throws IOException, ServletException {
    	
        ObjectMapper mapper = new ObjectMapper();
        
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(new ErrorMessage(failedException.getMessage())));
    }
}