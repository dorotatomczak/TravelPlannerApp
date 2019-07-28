package com.github.travelplannerapp.serverapp
import com.github.travelplannerapp.ServerApp.HERECharger.HEREConnector
import com.github.travelplannerapp.ServerApp.HERECharger.UserLocation
import com.github.travelplannerapp.ServerApp.HTTPCharger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong
import com.github.travelplannerapp.ServerApp.Data.Place

@RestController
class ServerController {

    val counter = AtomicLong()

//    @GetMapping("/greeting")
//    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
//            Greeting(counter.incrementAndGet(), "Hello, $name")

    @GetMapping("/HERE")
    fun getDataFromHERE()
    {
        println("Downloading data from HERE")
        val charger= HTTPCharger();
        charger.testURLs();
        //////////////////////
        println("-------------------------")
        val connector=HEREConnector()
        connector.printKeys()
        connector.verifyKeys()
    }
    @GetMapping("/PLACE")
    fun getPLACE()
    {
        val connector=HEREConnector()
        connector.printKeys()
        connector.findPlaceByText("chrysler","40.74917","-73.98529")


    }


}