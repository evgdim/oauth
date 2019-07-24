package com.github.evgdim.oauth.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("oauth")
class OauthProperties {
    lateinit var clientKey: String
    lateinit var checkTokenUrl: String
}