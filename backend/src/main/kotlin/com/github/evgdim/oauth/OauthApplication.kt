package com.github.evgdim.oauth

import com.github.evgdim.oauth.properties.OauthProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer


@SpringBootApplication
@EnableConfigurationProperties(OauthProperties::class)
class OauthApplication

fun main(args: Array<String>) {
	runApplication<OauthApplication>(*args)
}