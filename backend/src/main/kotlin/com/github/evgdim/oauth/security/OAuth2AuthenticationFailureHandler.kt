package com.github.evgdim.oauth.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class OAuth2AuthenticationFailureHandler : SimpleUrlAuthenticationFailureHandler() {
    override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse?, exception: AuthenticationException?) {
        if(request != null && response != null) {
            HttpCookieOAuth2AuthorizationRequestRepository.deleteCookies(request, response)
        }
        super.onAuthenticationFailure(request, response, exception)
    }
}