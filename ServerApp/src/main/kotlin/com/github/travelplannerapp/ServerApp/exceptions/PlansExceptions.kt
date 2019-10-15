package com.github.travelplannerapp.ServerApp.exceptions

import com.github.travelplannerapp.communication.commonmodel.ResponseCode

class AddPlanElementException(override val message: String,
                              override val code: ResponseCode = ResponseCode.ADD_PLAN_ELEMENT_ERROR): Exception(message), ApiException

class DeletePlanElementsException(override val message: String,
                                  override val code: ResponseCode = ResponseCode.DELETE_PLAN_ELEMENTS_ERROR): Exception(message), ApiException
