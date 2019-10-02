package com.github.travelplannerapp.serverapp

import com.github.travelplannerapp.ServerApp.properties.FileStorageProperties
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@EnableConfigurationProperties(FileStorageProperties::class)
@EnableAutoConfiguration(exclude=[SecurityAutoConfiguration::class])
@ComponentScan(basePackages=["com.github.travelplannerapp.serverapp","com.github.travelplannerapp.ServerApp"])
class ServerAppApplication

fun main(args: Array<String>) {
    runApplication<ServerAppApplication>(*args)
}