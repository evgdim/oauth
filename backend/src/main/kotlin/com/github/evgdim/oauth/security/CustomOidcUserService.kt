package com.github.evgdim.oauth.security

import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class CustomOidcUserService : OidcUserService() {
    val logger = LoggerFactory.getLogger(CustomOidcUserService::class.java)
    override fun loadUser(userRequest: OidcUserRequest?): OidcUser {
        val user = super.loadUser(userRequest);
        logger.info("[loadUser] $user")
        return user
    }
}