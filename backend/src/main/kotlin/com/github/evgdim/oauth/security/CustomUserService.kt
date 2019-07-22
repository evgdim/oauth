package com.github.evgdim.oauth.security

import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomUserService : DefaultOAuth2UserService() {
    val logger = LoggerFactory.getLogger(CustomUserService::class.java)
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        val user = super.loadUser(userRequest)
        logger.info("[loadUser] $user")
        return user
    }
}