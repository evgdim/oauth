package com.github.evgdim.oauth

import com.github.evgdim.oauth.properties.OauthProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer

@EnableResourceServer
@Configuration
class ResourceServerConfig(val oauthProperties: OauthProperties) : ResourceServerConfigurerAdapter() {

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources?.resourceId(oauthProperties.clientKey)
    }

    override fun configure(http: HttpSecurity?) {
        http
                ?.authorizeRequests()
                ?.antMatchers("/**")?.authenticated()
    }
}