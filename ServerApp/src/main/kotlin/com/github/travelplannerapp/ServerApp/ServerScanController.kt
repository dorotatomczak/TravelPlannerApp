package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.ScanManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.dao.Scan
import com.github.travelplannerapp.ServerApp.exceptions.UploadScanException
import com.github.travelplannerapp.ServerApp.services.FileStorageService
import com.github.travelplannerapp.communication.commonmodel.Response
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

@RestController
class ServerScanController {

    @Autowired
    lateinit var userManagement: UserManagement
    @Autowired
    lateinit var fileStorageService: FileStorageService
    @Autowired
    lateinit var scanManagement: ScanManagement

    @PostMapping("users/{userId}/scans")
    fun uploadFile(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestParam("travel-id") travelId: Int,
        @RequestParam("file") file: MultipartFile
    ): Response<Scan> {
        userManagement.verifyUser(token)
        var fileName: String? = null
        try {
            fileName = fileStorageService.storeFile(file)
            val scan = scanManagement.addScan(userId, travelId, fileName)
            return Response(ResponseCode.OK, scan)
        } catch (ex: Exception) {
            fileName?.let { fileStorageService.deleteFile(it) }
            throw UploadScanException(ex.localizedMessage)
        }
    }

    @GetMapping("users/{userId}/scans")
    fun getScans(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestParam("travel-id") travelId: Int
    ): Response<List<Scan>> {
        userManagement.verifyUser(token)
        val scans = scanManagement.getScans(userId, travelId)
        return Response(ResponseCode.OK, scans)
    }

    @GetMapping("users/{userId}/scans/{fileName:.+}")
    fun downloadFile(@PathVariable userId: Int, @PathVariable fileName: String, request: HttpServletRequest): ResponseEntity<Resource> {
        // Load file as Resource
        val resource = fileStorageService.loadFileAsResource(fileName)

        // Try to determine file's content type
        var contentType = request.servletContext.getMimeType(resource.file.absolutePath)

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream"
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
                .body(resource)
    }

    @DeleteMapping("users/{userId}/scans")
    fun deleteScans(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestBody scans: List<Scan>
    ): Response<Unit> {
        userManagement.verifyUser(token)

        scanManagement.deleteScans(scans)

        return Response(ResponseCode.OK, Unit)
    }
}
