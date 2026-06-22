package com.gabrielf.condoaccess.security;

import com.gabrielf.condoaccess.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> bucketCache;

    private static final List<String> RATE_LIMITED_PATHS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        boolean shouldLimit = RATE_LIMITED_PATHS.stream().anyMatch(path::equals);

        if (!shouldLimit) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = request.getRemoteAddr();
        Bucket bucket = bucketCache.computeIfAbsent(clientIp, key -> RateLimitConfig.createNewBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(
                    """
                            {"status": 429, "error": "TOO MANY REQUESTS", "message": "Limite de tentativas excedido. Aguarde e tente novamente."}
                            """
            );

        }

    }
}
