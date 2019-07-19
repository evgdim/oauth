package com.github.evgdim.oauth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService


@RestController
@RequestMapping("/user")
class UserController(val oauthClientService: OAuth2AuthorizedClientService) {
    @GetMapping
    fun user(principal: Principal) : Principal {
        return principal
    }

    @GetMapping("/token")
    fun token() : OAuth2AuthorizedClient {
        val authentication = SecurityContextHolder
                .getContext()
                .authentication

        val oauthToken = authentication as OAuth2AuthenticationToken

        val authClient = oauthClientService.loadAuthorizedClient<OAuth2AuthorizedClient>(oauthToken.authorizedClientRegistrationId, oauthToken.name)
        return authClient
    }
}