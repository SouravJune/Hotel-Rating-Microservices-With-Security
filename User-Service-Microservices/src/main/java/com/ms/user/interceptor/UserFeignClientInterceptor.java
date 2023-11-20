package com.ms.user.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class UserFeignClientInterceptor implements RequestInterceptor {

    @Autowired
    private final OAuth2AuthorizedClientManager manager;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer ";

    public UserFeignClientInterceptor(OAuth2AuthorizedClientManager manager) {
        this.manager = manager;
    }

    @Override
    public void apply(RequestTemplate template) {

        String token = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId("user-service-client")
                .principal("internal")
                .build()).getAccessToken().getTokenValue();

        template.header(AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE + token);
    }
}
