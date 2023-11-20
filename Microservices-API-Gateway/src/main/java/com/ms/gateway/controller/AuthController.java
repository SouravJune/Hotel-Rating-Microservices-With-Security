package com.ms.gateway.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.ms.gateway.response.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     *
     OAuth2AuthorizedClient:
     In Spring Security for OAuth 2.0, OAuth2AuthorizedClient represents an OAuth 2.0 client that has been authorized to access a user's resources.
     It encapsulates information about the authorized client, such as the client's registration (client ID, client secret), the user's authentication details, and the access token.
     Spring Security uses OAuth2AuthorizedClient to manage the details of the OAuth 2.0 authorization and provides a convenient way to interact with
     the authorized client within a Spring application.

     OidcUser:
     OidcUser represents a user authenticated through OpenID Connect (OIDC). OpenID Connect is an identity layer built on top of OAuth 2.0,
     providing additional features for authentication and user information.
     In the context of Spring Security and OIDC, OidcUser is an interface that represents the authenticated user's details,
     including claims obtained from the OIDC Provider (such as user ID, email, etc.).

     OAuth2AuthenticationToken:
     OAuth2AuthenticationToken is a class in Spring Security that represents an authentication token for an OAuth 2.0 client.
     It extends AbstractAuthenticationToken and is used to carry the authentication details (such as the principal and authorities) for an authenticated OAuth 2.0 client.
     This token is often used in the Spring Security framework when dealing with OAuth 2.0 authentication flows.
     It may contain information about the OAuth 2.0 client, the authenticated user, and the granted authorities.
     In summary, OAuth2AuthorizedClient is used to manage an authorized OAuth 2.0 client, OidcUser represents a user authenticated through OpenID Connect,
     and OAuth2AuthenticationToken is an authentication token for an OAuth 2.0 client in Spring Security.
     These concepts are often used together in Spring Security applications to handle OAuth 2.0 and OIDC authentication and authorization.
     */

    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient oAuth2AuthorizedClient,
            @AuthenticationPrincipal OidcUser oidcUser,
            Model model) {

        logger.info("User email id: {} ", oidcUser.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserId(oidcUser.getEmail());
        authResponse.setAccessToken(oAuth2AuthorizedClient.getAccessToken().getTokenValue());
        authResponse.setRefreshToken(Objects.requireNonNull(oAuth2AuthorizedClient.getRefreshToken()).getTokenValue());
        authResponse.setExpireAt(Objects.requireNonNull(oAuth2AuthorizedClient.getAccessToken().getExpiresAt()).getEpochSecond());

        List<String> authorities = oidcUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        authResponse.setAuthorities(authorities);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<String> indexPage(Authentication authentication){
        return ResponseEntity.ok("Hello" + authentication.getName());
    }
}
