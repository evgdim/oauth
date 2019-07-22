package com.github.evgdim.oauth.security

import com.github.evgdim.oauth.commons.deleteCookies
import com.github.evgdim.oauth.commons.fetchCookie
import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.Payload
import com.nimbusds.jwt.JWT
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.openid.connect.sdk.ClaimsRequest.Entry.toJSONObject
import com.nimbusds.jose.EncryptionMethod.A128CBC_HS256
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWEHeader
import com.nimbusds.jose.crypto.DirectEncrypter
import org.springframework.util.SerializationUtils.serialize
import com.nimbusds.jose.JWEObject




class OAuth2AuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {
    override fun determineTargetUrl(request: HttpServletRequest?, response: HttpServletResponse?): String {

        val targetUrl = fetchCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.LEMON_REDIRECT_URI_COOKIE_PARAM_NAME)?.value
        if(request != null && response != null) {
            deleteCookies(request, response)
        }

        return targetUrl + "?token=" + createToken()
    }

    private fun createToken() : String {
        val claims = JWTClaimsSet.Builder()
                .claim("email", "sanjay@example.com")
                .claim("name", "Sanjay Patel")
                .build()
        val payload = Payload(claims.toJSONObject())
        val header = JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)

        val secret = "841D8A6C80CBA4FCAD32D5367C18C53B"
        val secretKey = secret.toByteArray()
        val encrypter = DirectEncrypter(secretKey);


        val jweObject = JWEObject(header, payload)
        jweObject.encrypt(encrypter)
        val token = jweObject.serialize()
        return token
    }
}