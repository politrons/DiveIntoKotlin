package main.kotlin.spring

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ServerController {

    @GetMapping("/home")
    fun index(): List<User> = listOf(
        User("1", "Jack",20),
        User("2", "Politrons",40),
        User("3", "Matusalen",200),
    )
}

data class User(val id: String?, val name: String, val age:Int)

