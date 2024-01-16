package ru.Bogachev.pet.config.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
    private final String method;

    public HttpMethodRequestWrapper(
            final HttpServletRequest request,
            final String method
    ) {
        super(request);
        this.method = method;
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}
