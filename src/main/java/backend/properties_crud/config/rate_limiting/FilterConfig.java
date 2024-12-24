package backend.properties_crud.config.rate_limiting;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean
    FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter() {
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter());
        registrationBean.addUrlPatterns("/*"); // Apply to all URLs
        registrationBean.setName("rateLimitingFilter"); // Optional name
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Set filter execution order as very first upon recieving request
        return registrationBean;
    }
}
