package com.github.evgdim.oauth

import com.github.evgdim.oauth.security.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.Base64.getUrlDecoder
import org.springframework.util.SerializationUtils
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import java.util.Base64.getUrlEncoder
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.util.StringUtils
import java.util.*
import javax.servlet.http.Cookie
import org.apache.tomcat.jni.Lock.name
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler


@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .cors()
        .and()
            .csrf()
                .disable()
        .authorizeRequests()
            .mvcMatchers("/login/oauth2/code/google", "/oauth2/authorization/google")
                .permitAll()
            .anyRequest()
                .authenticated()
        .and()
            .oauth2Login()
                .authorizationEndpoint()
                    .authorizationRequestRepository(HttpCookieOAuth2AuthorizationRequestRepository())
            .and()
            .userInfoEndpoint()
                .oidcUserService(CustomOidcUserService())
                .userService(CustomUserService())
            .and()
            .successHandler(OAuth2AuthenticationSuccessHandler())
            .failureHandler(OAuth2AuthenticationFailureHandler())
    }

    override fun configure(web: WebSecurity?) {
        web?.ignoring()?.mvcMatchers("/actuator/health")
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
}