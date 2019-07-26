package com.github.evgdim.oauth

import com.github.evgdim.oauth.properties.OauthProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.*
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore


@EnableResourceServer
@Configuration
class ResourceServerConfig(val oauthProperties: OauthProperties, val env: Environment) : ResourceServerConfigurerAdapter() {

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources?.resourceId(oauthProperties.clientKey)
    }

    override fun configure(http: HttpSecurity?) {
        http
                ?.authorizeRequests()
                ?.antMatchers("/**")?.authenticated()
    }

    @Bean
    @Primary
    fun tokenService(): DefaultTokenServices {
        val defaultTokenServices = DefaultTokenServices()
        defaultTokenServices.setTokenStore(customTokenStore())
        return defaultTokenServices
    }

    @Bean
    fun customTokenStore(): TokenStore {
        val jwkSetUrl = env.getProperty("security.oauth2.resource.jwk.key-set-uri")
        return JwkTokenStore(jwkSetUrl, accessTokenConverter())
    }

    @Bean
    fun accessTokenConverter(): AccessTokenConverter {
        val converter = DefaultAccessTokenConverter()
        converter.setUserTokenConverter(userAuthenticationConverter())
        return converter
    }

    fun userAuthenticationConverter() : UserAuthenticationConverter{
        val defaultUserAuthenticationConverter = DefaultUserAuthenticationConverter()
        return object: UserAuthenticationConverter {
            override fun extractAuthentication(map: MutableMap<String, *>?): Authentication {
                if(map != null) {
                    val newMap = HashMap(map)
                    newMap.put("user_name", newMap.get("email") ?: "no-mail")
                    return defaultUserAuthenticationConverter.extractAuthentication(newMap)
                }
                return defaultUserAuthenticationConverter.extractAuthentication(HashMap<String, Any>())
            }

            override fun convertUserAuthentication(userAuthentication: Authentication?): MutableMap<String, *> {
                return defaultUserAuthenticationConverter.convertUserAuthentication(userAuthentication)
            }

        }
    }

}