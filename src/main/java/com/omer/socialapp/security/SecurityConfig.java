package com.omer.socialapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.omer.socialapp.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter 
{
	private UserDetailsService userDetailsService; 
	private UserRepository userRepository;  
	// injected from the properties file
	private final String SECRET;
	private final long EXPIRATION_TIME;
	
    private final String loginPath = "/auth/signin";
    // conventions args, but we can define whatever we wants..
	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
    
    
    @Autowired
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
    		UserRepository userRepository, @Value("${SECRET}") String secret, @Value("${EXPIRATION_TIME}") long exp) {
    	this.userDetailsService = userDetailsService;
    	this.userRepository = userRepository;
    	this.SECRET = secret;
    	this.EXPIRATION_TIME = exp;
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) {
    	authManagerBuilder.authenticationProvider(authenticationProvider());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	String admin = Roles.ADMIN.getRoleName();
        http.httpBasic().disable()
        	.formLogin().disable()
        	.cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), loginPath, SECRET, EXPIRATION_TIME))
            .addFilter(new JwtAuthorizationFilter(authenticationManager(),  userRepository, SECRET))        
            .authorizeRequests()
            
            .antMatchers(HttpMethod.POST, "/users").permitAll() //allow anyone to create an account..
            .antMatchers("/users/{userId}/**").access("hasRole('"+admin+"') or @pathVariableSecurity.checkUserId(authentication, #userId)")
            
            .antMatchers("/pages/plain").authenticated()
            .antMatchers(HttpMethod.GET, "/pages/{id}").authenticated()
            .antMatchers(HttpMethod.DELETE, "/pages/{pageId}/**").access("hasRole('"+admin+"') or @pathVariableSecurity.checkIfPageOwner(authentication, #pageId)")
            .antMatchers("/pages/{pageId}/**").access("hasRole('"+admin+"') or @pathVariableSecurity.checkIfLikePage(authentication, #pageId)") 
                    
            .antMatchers(HttpMethod.DELETE, "/groups/{groupId}/**").access("hasRole('"+admin+"') or @pathVariableSecurity.checkIfGroupOwner(authentication, #groupId)")
            .antMatchers(HttpMethod.GET, "/groups/{groupId}").authenticated()
            .antMatchers("/groups/{groupId}/**").access("hasRole('"+admin+"') or @pathVariableSecurity.checkIfInGroup(authentication, #groupId)")
            
            .anyRequest().authenticated();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}