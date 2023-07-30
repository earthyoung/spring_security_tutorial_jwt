package com.example.security3.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String headerAuthorization = req.getHeader("Authorization");
        System.out.println("headerAuthorization: " + headerAuthorization);

        System.out.println("Filter 1");
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
