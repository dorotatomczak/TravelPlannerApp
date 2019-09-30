package com.github.travelplannerapp.ServerApp.exceptions

class AddPlanException(override val message: String,
                       override val code: ResponseCode = ResponseCode.ADD_PLAN_ERROR): Exception(message), ApiException
