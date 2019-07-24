package com.github.evgdim.oauth

import com.github.evgdim.oauth.properties.OauthProperties
import com.github.evgdim.oauth.security.GoogleRemoteTokenService
import org.apache.catalina.filters.RequestDumperFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(val oauthProperties: OauthProperties) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .cors()
        .and()
            .csrf()
                .disable()
    }

    override fun configure(web: WebSecurity?) {
        web?.ignoring()?.mvcMatchers("/actuator/health")
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        val authenticationManager = OAuth2AuthenticationManager()
        authenticationManager.setTokenServices(tokenService())
        return authenticationManager
    }

    @Bean
    fun tokenService(): ResourceServerTokenServices {
        return GoogleRemoteTokenService(oauthProperties.checkTokenUrl,DefaultAccessTokenConverter())
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("authorization", "content-type", "x-auth-token")
        configuration.exposedHeaders = listOf("x-auth-token")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun requestDumperFilter(): RequestDumperFilter {
        return RequestDumperFilter()
    }
}