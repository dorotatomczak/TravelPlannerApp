package com.github.travelplannerapp.ServerApp.service

import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonScanUploadResponse
import com.github.travelplannerapp.ServerApp.jsondatamodels.ScanUploadResponse
import java.net.MalformedURLException
import org.springframework.core.io.UrlResource
import java.nio.file.StandardCopyOption
import org.springframework.web.multipart.MultipartFile
import com.github.travelplannerapp.ServerApp.property.FileStorageProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class FileStorageService @Autowired
constructor(fileStorageProperties: FileStorageProperties) {

    private val fileStorageLocation: Path = Paths.get(fileStorageProperties.uploadDir)
            .toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(this.fileStorageLocation)
        } catch (ex: Exception) {
            throw Exception("Could not create the directory where the uploaded files will be stored.", ex)
        }
    }

    fun storeFile(file: MultipartFile, email: String): JsonScanUploadResponse {
        // Normalize file name
        val fileName: String = StringUtils.cleanPath(file.originalFilename!!)
        val dir = email.replace(".", "_")

        return try {
            // Copy file to the target location (Replacing existing file with the same name)
            val targetLocation = Paths.get(this.fileStorageLocation.toString(), dir)
            Files.createDirectories(targetLocation)
            val path = targetLocation.resolve(fileName)
            Files.copy(file.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
            JsonScanUploadResponse(ScanUploadResponse.OK)
        } catch (ex: Exception) {
            JsonScanUploadResponse(ScanUploadResponse.ERROR)
        }
    }
}