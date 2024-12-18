package backend.properties_crud.config.security;

import java.security.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.RequestMatcher;

import backend.properties_crud.services.users.UserService;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private UserService userService;
    private CustomRequestMatcher customRequestMatcher;
    private CustomUnauthorizedHandler customUnauthorizedHandler;

    @Autowired
    public void setUserService(UserService userService,CustomRequestMatcher customRequestMatcher,
    CustomUnauthorizedHandler customUnauthorizedHandler) {
        this.userService = userService;
        this.customRequestMatcher=customRequestMatcher;
        this.customUnauthorizedHandler=customUnauthorizedHandler;
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth=new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        return auth;
    }
    
    //NOTE : For testing effect of below configs use proper url only else we always get forbidden response
    //Only one config gets applied to one request from below two configs
    //http://localhost:8080/public was tested with GET method and the -1 order config was applied there
    // response was {
    //   "message": "    Greetings!! welcome to properties-crud apis ,have look at the docs at https://localhost:8080/docs\n"
    // } with status code 200 OK
    @Bean
    @Order(-1) 
    SecurityFilterChain apiSecurityFilterChainForPublicURLs(HttpSecurity http) throws Exception{
        return http.
        authorizeHttpRequests(authorize -> authorize
            .requestMatchers(customRequestMatcher).permitAll()
        ).sessionManagement(session-> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ).requestCache((cache) -> cache
            .requestCache(new NullRequestCache())
        ).build();
    }
    // above config will intercept all requests containing 'public' word and 
    // allow stateless communication , no session will be maintained
    // such requests are not cached as well
    @Bean
    @Order(0)
    SecurityFilterChain apiSecurityFilterChainForPrivateURLs(HttpSecurity http) throws Exception{
        return http
        .authorizeHttpRequests(authorize->authorize
            .anyRequest().authenticated()
        ).sessionManagement(session->session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1)
            .maxSessionsPreventsLogin(true)
        ).build();
    }// this configuration is applied to all remaining urls
}