package com.github.evgdim.oauth.security

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.util.SerializationUtils
import org.springframework.util.StringUtils
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HttpCookieOAuth2AuthorizationRequestRepository() : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private val cookieExpirySecs: Int = 300 //should be as long as the jwt

    /**
     * Load authorization request from cookie
     */
    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        Objects.requireNonNull(request, "request cannot be null")
        return fetchCookie(request, AUTHORIZATION_REQUEST_COOKIE_NAME)
                ?.let { this.deserialize(it) }
    }

    /**
     * Save authorization request in cookie
     */
    override fun saveAuthorizationRequest(authorizationRequest: OAuth2AuthorizationRequest?, request: HttpServletRequest,
                                          response: HttpServletResponse) {
        Objects.requireNonNull(request, "request cannot be null")
        Objects.requireNonNull(response, "response cannot be null")

        if (authorizationRequest == null) {
            deleteCookies(request, response)
            return
        }

        var cookie = Cookie(AUTHORIZATION_REQUEST_COOKIE_NAME, serialize(authorizationRequest))
        cookie.setPath("/")
        cookie.setHttpOnly(true)
        cookie.setMaxAge(cookieExpirySecs)
        response.addCookie(cookie)

        val lemonRedirectUri = request.getParameter(LEMON_REDIRECT_URI_COOKIE_PARAM_NAME)
        if (!StringUtils.isEmpty(lemonRedirectUri)) {
            cookie = Cookie(LEMON_REDIRECT_URI_COOKIE_PARAM_NAME, lemonRedirectUri)
            cookie.setPath("/")
            cookie.setHttpOnly(true)
            cookie.setMaxAge(cookieExpirySecs)
            response.addCookie(cookie)
        }
    }

    override fun removeAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return loadAuthorizationRequest(request)
    }

    private fun serialize(authorizationRequest: OAuth2AuthorizationRequest): String {
        return Base64.getUrlEncoder().encodeToString(
                SerializationUtils.serialize(authorizationRequest))
    }

    private fun deserialize(cookie: Cookie): OAuth2AuthorizationRequest? {
        return SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())) as OAuth2AuthorizationRequest?
    }

    companion object {

        private val AUTHORIZATION_REQUEST_COOKIE_NAME = "lemon_oauth2_authorization_request"
        val LEMON_REDIRECT_URI_COOKIE_PARAM_NAME = "lemon_redirect_uri"

        /**
         * Utility for deleting related cookies
         */
        fun deleteCookies(request: HttpServletRequest, response: HttpServletResponse) {

            val cookies = request.cookies

            if (cookies != null && cookies.size > 0)
                for (i in cookies.indices)
                    if (cookies[i].name.equals(AUTHORIZATION_REQUEST_COOKIE_NAME) || cookies[i].name.equals(LEMON_REDIRECT_URI_COOKIE_PARAM_NAME)) {

                        cookies[i].value = ""
                        cookies[i].path = "/"
                        cookies[i].maxAge = 0
                        response.addCookie(cookies[i])
                    }
        }

        fun fetchCookie(request: HttpServletRequest, authorizationRequestCookieName: String): Cookie? {
            val cookies = request.cookies

            if (cookies != null && cookies.size > 0)
                for (i in cookies.indices)
                    if (cookies[i].name == authorizationRequestCookieName)
                        return cookies[i]

            return null
        }
    }
}