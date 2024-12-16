package backend.properties_crud.config;

import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomRequestMatcher implements RequestMatcher{
    /* all urls containing "public" will not be asked for token */
    @Override
    public boolean matches(HttpServletRequest request){
        String url=request.getRequestURL().toString();
        return url.contains("public");
    }
}
