package com.github.evgdim.oauth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/user")
class UserController {
    @GetMapping
    fun user(principal: Principal) : Principal {
        return principal
    }
}