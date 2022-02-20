package com.coderskitchen.jsr380examples

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RequestMapping("/users")
@RestController
class UserController {
    @PostMapping
    fun addUser(@Valid @RequestBody user:User) = ResponseEntity.ok("User is valid")
}