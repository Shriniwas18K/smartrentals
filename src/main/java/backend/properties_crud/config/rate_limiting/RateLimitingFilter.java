package backend.properties_crud.config.rate_limiting;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* Usually people use resilience4j which contain complex
 * algorthms and performance overhead so we will custom
 * rate limiting
 */

public class RateLimitingFilter implements Filter {

    private Map<String, RateLimit> ipRequestCounts = new ConcurrentHashMap<>();
    private final int maxRequestsPerSecond = 1;
    private long timeWindowMillis = 1000;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String ipAddress = httpRequest.getRemoteAddr();
        
        if (httpRequest.getRequestURI().contains("swagger")){
            chain.doFilter(httpRequest, httpResponse);
            return;
        }
        // this below condition removes rate limiting for localhost for testing purposes
        if (ipAddress.equals("0:0:0:0:0:0:0:1") || ipAddress.equals("127.0.0.1")) {
            chain.doFilter(request, response);
            return;
        }

        RateLimit rateLimit = ipRequestCounts.computeIfAbsent(ipAddress, k -> new RateLimit());

        synchronized (rateLimit) { // Synchronize access to rate limit data
            // this happens as multiple threads of respective requests alter this rateLimit
            // to prevent race conditions using synchronization
            long currentTime = System.currentTimeMillis();
            if (currentTime - rateLimit.getLastRequestTime() > timeWindowMillis) {
                rateLimit.reset();
            }
            if (rateLimit.getRequestCount().get() < maxRequestsPerSecond) {
                rateLimit.getRequestCount().incrementAndGet();
                rateLimit.setLastRequestTime(currentTime);
                chain.doFilter(request, response); // Allow the request
            } else {
                httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // HTTP 403
                httpResponse.getWriter().write("""
                        {
                            \"message\" : \"Too many requests. Please try again later.\"
                        }
                        """);
                httpResponse.setContentType("application/json");
            }
        }
    }

    private static class RateLimit {
        private AtomicInteger requestCount = new AtomicInteger(0);
        private long lastRequestTime = 0;

        public AtomicInteger getRequestCount() {
            return requestCount;
        }

        public long getLastRequestTime() {
            return lastRequestTime;
        }

        public void setLastRequestTime(long lastRequestTime) {
            this.lastRequestTime = lastRequestTime;
        }

        public void reset() {
            requestCount.set(0);
        }
    }

}
