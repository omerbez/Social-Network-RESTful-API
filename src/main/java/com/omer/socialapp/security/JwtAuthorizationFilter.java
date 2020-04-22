package com.omer.socialapp.security;

import com.auth0.jwt.JWT;
import com.omer.socialapp.model.User;
import com.omer.socialapp.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter
{
    private UserRepository userRepository;
    private final String SECRET;
    
    
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, final String SECRET) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.SECRET = SECRET;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Read the Authorization header, where the JWT token should be
        String header = request.getHeader(SecurityConfig.TOKEN_HEADER);

        if (header != null && header.startsWith(SecurityConfig.TOKEN_PREFIX)) {        
	        Authentication authentication = getUsernamePasswordAuthentication(request);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // Continue filter execution
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
    	// remove the token prefix.. (The "Brearer " str..)
        String token = request.getHeader(SecurityConfig.TOKEN_HEADER).replace(SecurityConfig.TOKEN_PREFIX,"");

        if (token != null) {
            // parse the token and validate it
            String userName = JWT.require(HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();

            // Search in the DB if we find the user by token subject (username)
            // If so, then grab user details and create spring auth token using username, pass, authorities/roles
            if (userName != null) {
                User user = userRepository.findByUsername(userName).orElse(null);
                
                if(user == null)
                	return null;
                
                UserDetails principal = new UserDetailsImpl(user);
                
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userName, 
                		principal.getPassword(), principal.getAuthorities());
                
                return auth;
            }
        }
        return null;
    }
}