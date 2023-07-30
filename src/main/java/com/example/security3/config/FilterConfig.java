package com.example.security3.config;

import com.example.security3.filter.AnotherFilter;
import com.example.security3.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    // SecurityFilterChain 메소드에서 addFilterBefore 등의 메소드를 안 쓰고, 따로 필터를 정의할 수 있다.
    // SecurityFilterChain 내부에 등록된 필터가 먼저 실행되고, 따로 정의된 필터들이 그 후에 실행된다.

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> bean = new FilterRegistrationBean<>(new JwtFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(0);   // 숫자가 작을수록 우선순위 높음
        return bean;
    }

    @Bean
    public FilterRegistrationBean<AnotherFilter> anotherFilter() {
        FilterRegistrationBean<AnotherFilter> bean = new FilterRegistrationBean<>(new AnotherFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(1);   // 숫자가 작을수록 우선순위 높음
        return bean;
    }

}
