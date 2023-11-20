package com.ms.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestHandler;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;


import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {

        ServerCsrfTokenRequestHandler requestHandler = new ServerCsrfTokenRequestAttributeHandler();

        httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange((exchanges) -> exchanges
                        .pathMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/user/save",
                                "/api/v1/rating/save",
                                "/api/v1/hotel/save",
                                "/api/v1/staff/**",
                                "/actuator/**").permitAll()
                        .anyExchange().authenticated())

                .oauth2Login(withDefaults())
                .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                        oAuth2ResourceServerSpec.jwt(Customizer.withDefaults()));

        return httpSecurity.build();
    }
}
