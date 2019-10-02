package com.github.travelplannerapp.ServerApp.exceptions

import com.github.travelplannerapp.ServerApp.datamodels.Response
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.tinylog.kotlin.Logger

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationException::class, WrongCredentialsException::class, EmailAlreadyExistsException::class,
            AddTravelException::class, AddScanException::class, UploadScanException::class)
    fun handleApiExceptions(exception: ApiException): Response<Any> {
        Logger.info(exception.message)
        return Response(exception.code, null)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handlePredefinedExceptions(exception: Exception): Response<Any> {
        Logger.error(exception)
        return Response(ResponseCode.OTHER_ERROR, null)
    }
}
