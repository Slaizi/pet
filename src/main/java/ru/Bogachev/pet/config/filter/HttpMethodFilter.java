package ru.Bogachev.pet.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class HttpMethodFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getParameter("_method");
        if (method != null) {
            String originalMethod = request.getMethod();
            String newMethod = method.toUpperCase();

            request = new HttpMethodRequestWrapper(request, newMethod);
        }
        filterChain.doFilter(request, response);
    }
}
