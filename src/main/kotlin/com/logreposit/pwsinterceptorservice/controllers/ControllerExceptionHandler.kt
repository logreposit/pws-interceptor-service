package com.logreposit.pwsinterceptorservice.controllers

import com.logreposit.pwsinterceptorservice.exceptions.DeviceNotAllowedException
import com.logreposit.pwsinterceptorservice.mappers.PwsDataValidationException
import com.logreposit.pwsinterceptorservice.util.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptionHandler {

    private val logger = logger()

    @ExceptionHandler(PwsDataValidationException::class)
    fun handlePwsDataValidationException(e: PwsDataValidationException) =
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST).also { logger.warn(e.message) }

    @ExceptionHandler(DeviceNotAllowedException::class)
    fun handleDeviceNotAllowedException(e: DeviceNotAllowedException) =
            ResponseEntity(e.message, HttpStatus.FORBIDDEN).also { logger.warn(e.message) }
}
