package com.github.evgdim.oauth.commons

import com.github.evgdim.oauth.security.HttpCookieOAuth2AuthorizationRequestRepository
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


fun deleteCookies(request: HttpServletRequest, response: HttpServletResponse) {

    val cookies = request.cookies

    if (cookies != null && cookies.size > 0)
        for (i in cookies.indices)
            if (cookies[i].name.equals(HttpCookieOAuth2AuthorizationRequestRepository.AUTHORIZATION_REQUEST_COOKIE_NAME) || cookies[i].name.equals(HttpCookieOAuth2AuthorizationRequestRepository.LEMON_REDIRECT_URI_COOKIE_PARAM_NAME)) {

                cookies[i].value = ""
                cookies[i].path = "/"
                cookies[i].maxAge = 0
                response.addCookie(cookies[i])
            }
}

fun fetchCookie(request: HttpServletRequest?, authorizationRequestCookieName: String): Cookie? {
    val cookies = request?.cookies

    if (cookies != null && cookies.size > 0)
        for (i in cookies.indices)
            if (cookies[i].name == authorizationRequestCookieName)
                return cookies[i]

    return null
}