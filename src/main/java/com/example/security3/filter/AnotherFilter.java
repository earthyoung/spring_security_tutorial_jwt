package com.example.security3.filter;

import jakarta.servlet.*;

import java.io.IOException;

public class AnotherFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Filter 2");
        chain.doFilter(request, response);
    }
}
