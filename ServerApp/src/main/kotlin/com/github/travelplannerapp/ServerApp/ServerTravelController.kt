package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.ScanManagement
import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.exceptions.AddTravelException
import com.github.travelplannerapp.ServerApp.exceptions.ApiException
import com.github.travelplannerapp.ServerApp.exceptions.UploadScanException
import com.github.travelplannerapp.ServerApp.jsondatamodels.*
import com.github.travelplannerapp.ServerApp.services.FileStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.http.ResponseEntity
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping



@RestController
class ServerTravelController {

    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var userManagement: UserManagement
    @Autowired
    lateinit var travelManagement: TravelManagement
    @Autowired
    lateinit var fileStorageService: FileStorageService
    @Autowired
    lateinit var scanManagement: ScanManagement

    @GetMapping("/travels")
    fun travels(@RequestHeader("authorization") token: String,
                @RequestParam("userId") userId: Int): Response<List<Travel>> {
        userManagement.verifyUser(userId, token)
        val travels = travelRepository.getAllTravelsByUserId(userId)
        return Response(200, travels)
    }

    @PostMapping("/addtravel")
    fun addTravel(@RequestHeader("authorization") token: String,
                  @RequestBody request: AddTravelRequest): Response<Travel> {
        userManagement.verifyUser(request.userId, token)
        val newTravel = travelManagement.addTravel(request)
        return Response(200, newTravel)
    }

    @PostMapping("/uploadScan")
    fun uploadFile(
            @RequestHeader("authorization") token: String,
            @RequestParam("userId") userId: Int,
            @RequestParam("travelId") travelId: Int,
            @RequestParam("file") file: MultipartFile): Response<String> {
        userManagement.verifyUser(userId, token)
        try {
            val fileName = fileStorageService.storeFile(file)
            scanManagement.addScan(userId, travelId, fileName)
            return Response(200, fileName)
        } catch (ex: Exception) {
            //TODO [Dorota] Remove file from disk if added
            throw UploadScanException(ex.localizedMessage)
        }
    }

    @GetMapping("/scans")
    fun scans(@RequestHeader("authorization") token: String,
              @RequestParam userId: Int,
              @RequestParam travelId: Int): Response<List<String>> {
        userManagement.verifyUser(userId, token)
        val scanNames = scanManagement.getScanNames(userId, travelId)
        return Response(200, scanNames)
    }

    @GetMapping("/scans/{fileName:.+}")
    fun downloadFile(@PathVariable fileName: String, request: HttpServletRequest): ResponseEntity<Resource> {
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource)
    }

    @ExceptionHandler(AddTravelException::class)
    fun handleApiExceptions(exception: ApiException): Response<Any> {
        return Response(exception.code, null)
    }

    @ExceptionHandler(Exception::class)
    fun handlePredefinedExceptions(exception: Exception): Response<Any> {
        return Response(999, null)
    }
}