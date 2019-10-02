package com.github.travelplannerapp.ServerApp.properties

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "file")
class FileStorageProperties {
    var uploadDir: String? = null
}