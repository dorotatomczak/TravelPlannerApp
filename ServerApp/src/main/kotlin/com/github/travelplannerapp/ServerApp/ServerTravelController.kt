package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.exceptions.UpdateTravelException
import com.github.travelplannerapp.ServerApp.services.FileStorageService
import com.github.travelplannerapp.communication.commonmodel.PlanElement
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
class ServerTravelController {

    @Autowired
    lateinit var userManagement: UserManagement
    @Autowired
    lateinit var travelManagement: TravelManagement
    @Autowired
    lateinit var fileStorageService: FileStorageService

    @GetMapping("users/{userId}/travels")
    fun getTravels(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int
    ): Response<List<Travel>> {
        userManagement.verifyUser(token)
        val travels = travelManagement.getTravels(userId)
        return Response(ResponseCode.OK, travels)
    }

    @PostMapping("users/{userId}/travels")
    fun addTravel(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestBody travelName: String
    ): Response<Travel> {
        userManagement.verifyUser(token)
        val newTravel = travelManagement.addTravel(userId, travelName)
        return Response(ResponseCode.OK, newTravel)
    }

    @PutMapping("users/{userId}/travels", consumes = ["application/json"])
    fun changeTravelName(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestBody travel: Travel
    ): Response<Travel> {
        userManagement.verifyUser(token)
        val updatedTravel = travelManagement.updateTravel(travel.id!!, mutableMapOf("name" to travel.name))
        return Response(ResponseCode.OK, updatedTravel)
    }

    @PutMapping("users/{userId}/travels", consumes = ["multipart/form-data"])
    fun uploadTravelImage(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int,
            @RequestParam("travelId") travelId: Int,
            @RequestParam("file") file: MultipartFile
    ): Response<Travel> {
        userManagement.verifyUser(token)
        var fileName: String? = null
        try {
            val travel = travelManagement.getTravel(travelId)
            fileName = fileStorageService.storeFile(file)
            val updatedTravel = travelManagement.updateTravel(travelId, mutableMapOf("imageUrl" to fileName))
            travel?.imageUrl?.let { fileStorageService.deleteFile(it) }
            return Response(ResponseCode.OK, updatedTravel)
        } catch (ex: Exception) {
            fileName?.let { fileStorageService.deleteFile(it) }
            throw UpdateTravelException(ex.localizedMessage)
        }
    }

    @GetMapping("users/{userId}/travels/{fileName:.+}")
    fun downloadTravelImage(@PathVariable userId: Int, @PathVariable fileName: String, request: HttpServletRequest): ResponseEntity<Resource> {
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

    @DeleteMapping("users/{userId}/travels")
    fun deleteTravels(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestBody travelIds: MutableSet<Int>
    ): Response<Unit> {
        userManagement.verifyUser(token)
        travelManagement.deleteTravels(userId, travelIds)
        return Response(ResponseCode.OK, Unit)
    }

    @GetMapping("users/{userId}/travels/{travelId}/plans")
    fun getPlans(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int,
            @PathVariable travelId: Int
    ): Response<List<PlanElement>> {
        userManagement.verifyUser(token)

        val planElements = travelManagement.getPlanElements(travelId)
        return Response(ResponseCode.OK, planElements)
    }

    @PostMapping("users/{userId}/travels/{travelId}/plans")
    fun addPlan(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @PathVariable travelId: Int,
        @RequestBody planElement: PlanElement
    ): Response<PlanElement> {
        userManagement.verifyUser(token)

        val addedPlanElement = travelManagement.addPlanElement(travelId, planElement)
        return Response(ResponseCode.OK, addedPlanElement)
    }

    @PutMapping("users/{userId}/travels/{travelId}/plans")
    fun updatePlanElement(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @PathVariable travelId: Int,
        @RequestBody planElement: PlanElement
    ): Response<Unit> {
        userManagement.verifyUser(token)
        travelManagement.updatePlanElement(travelId, planElement)
        return Response(ResponseCode.OK, Unit)
    }

    @DeleteMapping("users/{userId}/plans")
    fun deletePlanElements(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int,
            @RequestBody planElementIds: List<Int>
    ): Response<Unit> {
        userManagement.verifyUser(token)
        travelManagement.deletePlanElements(planElementIds)
        return Response(ResponseCode.OK, Unit)
    }
}
