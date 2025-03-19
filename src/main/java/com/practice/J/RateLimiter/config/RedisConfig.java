package com.practice.J.RateLimiter.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    //redis 클라이언트 설정
    //@Bean(destroyMethod = "shutdown")
    @Bean
    public RedisClient redisClient() {
        return RedisClient.create(
                RedisURI.builder()
                        .withHost(host)
                        .withPort(port)
                        .build());
    }

    //Bucket4j가 Redis 같은 외부 저장소를 사용하여 Rate Limiting 데이터를 관리할 수 있도록 도와주는 인터페이스
    @Bean
    public ProxyManager<String> proxyManager(RedisClient redisClient) {

        //redis 연결 세션
        StatefulRedisConnection<String, byte[]> connection =
                redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
        
        //bucket 관리
        return LettuceBasedProxyManager
                .builderFor(connection)
                //.withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(1L)))
                .withExpirationStrategy(ExpirationAfterWriteStrategy.fixedTimeToLive(Duration.ofSeconds(60L)))  //TTL 무제한 증가 방지
                .build();

    }

}