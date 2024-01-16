package ru.Bogachev.pet.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class HttpMethodFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        String method = request.getParameter("_method");
        if (method != null) {
            String newMethod = method.toUpperCase();
            HttpMethodRequestWrapper modifiedRequest =
                    new HttpMethodRequestWrapper(request, newMethod);
            filterChain.doFilter(modifiedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
