package com.ms.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {

        httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange((exchanges) -> exchanges

                        .pathMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/user/save",
                                "/api/v1/staff/**",
                                "api/error/fallback/contactSupport",
                                "/actuator/**").permitAll()

                        .pathMatchers(
                                /* USER-SERVICE-MS */
                                "/api/v1/user/all",
                                "/api/v1/user/all-users-with-ratings",

                                /* RATING-SERVICE-MS */
                                "/api/v1/rating/all",
                                "/api/v1/rating/getrating-by-hotelid",
                                "/api/v1/rating/delete-rating-by-hotel",

                                /* HOTEL-SERVICE-MS */
                                "/api/v1/hotel/save",
                                "/api/v1/hotel/all",
                                "/api/v1/hotel/gethotel",
                                "/api/v1/hotel/update",
                                "/api/v1/hotel/delete").hasRole("Admin")

                        .pathMatchers(
                                /* USER-SERVICE-MS */
                                "/api/v1/user/getuser",
                                "/api/v1/user/get-user-with-ratings",

                                /* RATING-SERVICE-MS */
                                "/api/v1/rating/getrating-by-userid").hasAnyRole("Admin", "Normal_Users")

                        .pathMatchers(
                                /* USER-SERVICE-MS */
                                "/api/v1/user/update",
                                "/api/v1/user/delete-user-and-ratings",

                                /* RATING-SERVICE-MS */
                                "/api/v1/rating/save",
                                "/api/v1/rating/update",
                                "/api/v1/rating/delete").hasRole("Normal_Users")

                        .anyExchange().authenticated())

                .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                        oAuth2ResourceServerSpec.jwt(jwtSpec -> jwtSpec
                                .jwtAuthenticationConverter(grantedAuthoritiesConverter())));

        return httpSecurity.build();
    }

    private Converter<Jwt,? extends Mono<? extends AbstractAuthenticationToken>> grantedAuthoritiesConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
                (new GrantedAuthoritiesExtractor());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}
