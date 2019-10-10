package com.github.travelplannerapp.ServerApp.exceptions

import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.ResponseCode

class AddPlanElementException(override val message: String,
                              override val code: ResponseCode = ResponseCode.ADD_PLAN_ERROR): Exception(message), ApiException
