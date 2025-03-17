package com.practice.J.Rate.config;

import com.practice.J.Rate.filter.RateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterConfig {

    private final RateLimitFilter rateLimitFilter;
    private static final String[] PATHS = { "/rate/test", "/rate/test2", "/rate/test3" };

    public FilterConfig(RateLimitFilter rateLimitFilter) {
        this.rateLimitFilter = rateLimitFilter;
    }

    @Bean
    public FilterRegistrationBean<RateLimitFilter> filterBean() {

        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(rateLimitFilter);                //필터 설정
        registrationBean.setOrder(1);                               //필터 순위
        registrationBean.addUrlPatterns("/*");                      //전체 API 엔드포인트 호출 허용
        //registrationBean.setUrlPatterns(Arrays.asList(PATHS));    //특정 API 엔드포인트 호출 허용
        return registrationBean;

    }

}
