package com.practice.J.Rate.filter;

import io.github.bucket4j.*;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.distributed.proxy.RecoveryStrategy;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
public class RateLimitFilter implements Filter {

    private final ProxyManager<String> proxyManager;
    private final Map<String, Bucket> bucketPool = new ConcurrentHashMap<>();

    // 호출 횟수 카운트
    int requestNum = 0;

    @Autowired
    public RateLimitFilter(ProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,FilterChain filterChain) throws IOException, ServletException {

        log.info("=============" + ++requestNum + "번째 요청 ==============");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String address = httpServletRequest.getRemoteAddr();

        log.info("address 확인 = {}", address);

        //bucket 설정
        Supplier<BucketConfiguration> bucketConfiguration = getBucketConfiguration();

        //요청마다 Bucket 생성 O
        //Bucket bucket = proxyManager.builder().withRecoveryStrategy(RecoveryStrategy.RECONSTRUCT).build(address, bucketConfiguration);

        //요청마다 Bucket 생성 X
        Bucket bucket = bucketPool.computeIfAbsent(address,
                //1) redis : proxyManager 활용 - RecoveryStrategy.RECONSTRUCT : Redis에서 기존 bucket 정보 불러옴
                k -> proxyManager.builder().withRecoveryStrategy(RecoveryStrategy.RECONSTRUCT).build(address, bucketConfiguration));
                //2) jvm : 메모리 활용
                //k -> Bucket.builder().addLimit(getBandwidthIntervally()).build());

        log.info("사용 중인 bucket = {}", bucket);
        log.info("=== 가능 token = {} ", bucket.getAvailableTokens());

        //경쟁 조건(race condition)과 동기화 문제 해결
        ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);

        if (consumptionProbe.isConsumed()) {
            log.info("=== 남은 token = {}", consumptionProbe.getRemainingTokens());
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            log.info("=== 요청이 거절되었습니다.");
            HttpServletResponse httpServletResponse = makeRateLimitResponse(servletResponse, consumptionProbe);
        }

    }

    //Bucket 설정 : 토큰 개수, Refill 전략
    public Supplier<BucketConfiguration> getBucketConfiguration() {
        return () -> BucketConfiguration.builder().addLimit(getBandwidthIntervally()).build();
    }

    //Bucket 내 token 활용 - interval 전략
    public Bandwidth getBandwidthIntervally() {
        //return Bandwidth.builder().capacity(50).refillIntervally(1, Duration.ofSeconds(10L)).build();
        return Bandwidth.builder().capacity(50).refillIntervally(1, Duration.ofSeconds(10L)).build();
    }

    //bucket 내 token 활용 - greedy 전략
    public Bandwidth getBandwidthGreedy() {
        return Bandwidth.builder().capacity(50).refillGreedy(1, Duration.ofSeconds(10L)).build();
    }

    //token 이 없다면 : 429 응답
    private HttpServletResponse makeRateLimitResponse(ServletResponse servletResponse, ConsumptionProbe probe) throws IOException {

        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setContentType("text/plain");

        //1) http 헤더에 Rate-limit 정책 알림
        httpResponse.setHeader("X-Rate-Limit-Retry-After-Seconds", "" + TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()));

        //2) http 헤더에 429 추가
        httpResponse.setStatus(429);
        httpResponse.getWriter().append("Too many requests");

        return httpResponse;

    }

}