package com.github.evgdim.oauth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/user")
class UserController() {
    @GetMapping
    fun user(principal: Principal) : Principal {
        return principal
    }

//    @GetMapping("/token")
//    fun token() : OAuth2AuthorizedClient {
//        val authentication = SecurityContextHolder
//                .getContext()
//                .authentication
//
//        val oauthToken = authentication as OAuth2AuthenticationToken
//
//        val authClient = oauthClientService.loadAuthorizedClient<OAuth2AuthorizedClient>(oauthToken.authorizedClientRegistrationId, oauthToken.name)
//        return authClient
//    }
}