package com.github.travelplannerapp.ServerApp.services

import com.github.travelplannerapp.ServerApp.exceptions.FileStorageException
import java.nio.file.StandardCopyOption
import org.springframework.web.multipart.MultipartFile
import com.github.travelplannerapp.ServerApp.properties.FileStorageProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.net.MalformedURLException
import org.springframework.core.io.UrlResource
import java.io.FileNotFoundException


@Service
class FileStorageService @Autowired
constructor(fileStorageProperties: FileStorageProperties) {

    private val fileStorageLocation: Path = Paths.get(fileStorageProperties.uploadDir)
            .toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(this.fileStorageLocation)
        } catch (ex: Exception) {
            throw FileStorageException("Could not create the directory where the uploaded files will be stored.")
        }
    }

    fun storeFile(file: MultipartFile): String {
        // Normalize file name
        val fileName: String = StringUtils.cleanPath(file.originalFilename!!)

        try {
            // Copy file to the target location (Replacing existing file with the same name)
            val targetLocation = this.fileStorageLocation.resolve(fileName)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            return fileName
        } catch (ex: Exception) {
            throw FileStorageException("Could not store file $fileName")
        }
    }

    fun loadFileAsResource(fileName: String): Resource {
        try {
            val filePath = this.fileStorageLocation.resolve(fileName).normalize()
            val resource = UrlResource(filePath.toUri())
            return if (resource.exists()) {
                resource
            } else {
                throw FileNotFoundException("File not found $fileName")
            }
        } catch (ex: MalformedURLException) {
            throw FileNotFoundException("File not found $fileName")
        }

    }
}