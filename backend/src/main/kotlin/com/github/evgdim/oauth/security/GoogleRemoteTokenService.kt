package com.github.evgdim.oauth.security

import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.client.ClientHttpResponse
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.exceptions.InvalidClientException
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.AccessTokenConverter
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices
import org.springframework.util.MultiValueMap
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.io.IOException

@Deprecated("Call Google to verify the token. " +
        "Usage: override WebSecurityConfigurerAdapter::authenticationManagerBean() " +
        "by creating OAuth2AuthenticationManager and set a new instance of GoogleRemoteTokenService to the property tokenServices")
class GoogleRemoteTokenService : ResourceServerTokenServices {
    val logger = LoggerFactory.getLogger(GoogleRemoteTokenService::class.java)

    private val restTemplate: RestTemplate
    private val accessTokenConverter: AccessTokenConverter
    private val checkTokenUrl: String

    constructor(checkTokenUrl: String, accessTokenConverter: AccessTokenConverter) {
        restTemplate = RestTemplate()
        restTemplate.errorHandler = object : DefaultResponseErrorHandler() {
            override fun handleError(response: ClientHttpResponse) {
                if (response.rawStatusCode != 400) {
                    super.handleError(response)
                }
            }
        }
        this.accessTokenConverter = accessTokenConverter
        this.checkTokenUrl = checkTokenUrl
    }

    override fun loadAuthentication(accessToken: String?): OAuth2Authentication {
        val map = callGoogleCheckTokenEndpoint(accessToken)

        if (map.containsKey("error")) {
            if (logger.isDebugEnabled()) {
                logger.debug("check_token returned error: " + map["error"])
            }
            throw InvalidTokenException(accessToken)
        }
        map.computeIfAbsent("user_name", { key -> map.get("email") ?: throw InvalidClientException("claims does not contain email")})
        return accessTokenConverter.extractAuthentication(map)
    }

    override fun readAccessToken(accessToken: String?): OAuth2AccessToken {
        throw UnsupportedOperationException("Not supported: read access token")
    }

    private fun callGoogleCheckTokenEndpoint(accessToken: String?): MutableMap<String, Object>{
        val httpEntity = HttpEntity.EMPTY
        val url = "${this.checkTokenUrl}?access_token=$accessToken"
        val map = restTemplate.exchange(url, HttpMethod.GET, httpEntity,MutableMap::class.java).getBody()
        return map as MutableMap<String, Object>
    }
}