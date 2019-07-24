package com.github.travelplannerapp.serverapp


import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class ServerController {

    val counter = AtomicLong()
    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")

    @GetMapping("/db")
    fun getUser() = Greeting(counter.incrementAndGet(), "Hello, ${userRepository.get(18)!!.name}")

}