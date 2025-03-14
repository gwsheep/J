package com.practice.J.Rate.config;

import com.practice.J.Rate.filter.RateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterConfig {

    private final RateLimitFilter rateLimitFilter;
    private static final String[] INCLUDE_PATHS = { "/rate/test", "/rate/test2", "/rate/test3" };

    public FilterConfig(RateLimitFilter rateLimitFilter) {
        this.rateLimitFilter = rateLimitFilter;
    }

    @Bean
    public FilterRegistrationBean<RateLimitFilter> filterBean() {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(rateLimitFilter);
        registrationBean.setOrder(1);
        registrationBean.setUrlPatterns(Arrays.asList(INCLUDE_PATHS));
        return registrationBean;
    }

}
