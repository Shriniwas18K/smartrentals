package backend.properties_crud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import backend.properties_crud.services.users.UserService;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

/* we will enable jwt auth only because it has become convention
 * and most features of spring security are only for the stateless
 * jwt authentication where sessions are not stored on server side
 * it is defacto to store sessions on client side only
 */

    private UserService userService;
    private RequestMatcher requestMatcher;
    private CustomUnauthorizedHandler customUnauthorizedHandler;

    @Autowired
    public void setUserService(UserService userService,RequestMatcher requestMatcher,
    CustomUnauthorizedHandler customUnauthorizedHandler) {
        this.userService = userService;
        this.requestMatcher=requestMatcher;
        this.customUnauthorizedHandler=customUnauthorizedHandler;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth=new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        return auth;
    }
    
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(requestMatcher).permitAll()
            .anyRequest().authenticated()
        );

        http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin())
        );

        http.exceptionHandling(handling -> handling.authenticationEntryPoint(customUnauthorizedHandler));

        return http.build();
    }
}

