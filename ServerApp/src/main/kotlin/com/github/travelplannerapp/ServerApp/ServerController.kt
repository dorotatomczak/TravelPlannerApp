package com.github.travelplannerapp.serverapp

import com.github.travelplannerapp.ServerApp.HereCharger.HereLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class ServerController {

    val counter = AtomicLong()

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")

    @GetMapping("/here")
    fun getExampleDataFromHere() {
        val connector = HereLoader()
        connector.findPlaceByText("chrysler", "40.74917", "-73.98529")
        connector.findBestWay("40.74917", "-73.98529", "45.74917",
                "-72.98529", "fastest", "car", "disabled")
    }
}