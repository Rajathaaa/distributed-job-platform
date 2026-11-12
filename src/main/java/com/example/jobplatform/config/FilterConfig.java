package com.example.jobplatform.config;
import com.example.jobplatform.security.RateLimitFilter; import org.springframework.boot.web.servlet.FilterRegistrationBean; import org.springframework.context.annotation.*;
@Configuration public class FilterConfig { @Bean FilterRegistrationBean<RateLimitFilter> rateLimitRegistration(RateLimitFilter f){var b=new FilterRegistrationBean<>(f);b.setOrder(1);return b;} }
