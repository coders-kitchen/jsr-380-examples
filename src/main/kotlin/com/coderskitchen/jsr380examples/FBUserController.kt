package com.coderskitchen.jsr380examples

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RequestMapping("/fb-users")
@RestController
class FBUserController {
    @PostMapping
    fun addUser(@Valid @RequestBody user:FBUser) = ResponseEntity.ok("FB User is valid")
}