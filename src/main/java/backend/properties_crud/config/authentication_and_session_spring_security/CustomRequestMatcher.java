package backend.properties_crud.config.authentication_and_session_spring_security;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CustomRequestMatcher implements RequestMatcher{

    @Override
    public boolean matches(HttpServletRequest request) {
        String url=request.getRequestURI();
        return url.contains("public");
    }
}
