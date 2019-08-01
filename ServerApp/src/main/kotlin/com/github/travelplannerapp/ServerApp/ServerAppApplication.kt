package com.github.travelplannerapp.serverapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerAppApplication

fun main(args: Array<String>) {
	runApplication<ServerAppApplication>(*args)
}
