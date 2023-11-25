package com.ms.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.http.HttpMethod.GET;

@Configuration
public class RouteConfiguration {

    @Bean
    public RouteLocator routeConfig(RouteLocatorBuilder builder) {
        return builder.routes().build();
    }
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id ->
                new Resilience4JConfigBuilder(id)
                        .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                        .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(20))
                                .build())
                        .build());
    }
    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10,20, 50);
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just("1");
    }

    @Bean
    public RouteLocator routeLocatorCircuitBreaker(RouteLocatorBuilder builder) {

        return (RouteLocator) builder.routes()
                .route(p -> p
                        .path("/api/v1/user/**")
                        .and()
                        .method(GET)
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("userCircuitBreaker")
                                        .setFallbackUri("forward:/api/error/fallback/contactSupport"))
                                .requestRateLimiter()
                                .configure(config -> config
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(userKeyResolver())))
                        .uri("lb://USER-SERVICE-MS"))

                .route(p -> p
                        .path("/api/v1/hotel/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("hotelCircuitBreaker")
                                        .setFallbackUri("forward:/api/error/fallback/contactSupport")))
                        .uri("lb://HOTEL-SERVICE-MS"))

                .route(p -> p
                        .path("/api/v1/rating/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("ratingCircuitBreaker")
                                        .setFallbackUri("forward:/api/error/fallback/contactSupport")))
                        .uri("lb://RATING-SERVICE-MS"))
                .build();
    }
}
