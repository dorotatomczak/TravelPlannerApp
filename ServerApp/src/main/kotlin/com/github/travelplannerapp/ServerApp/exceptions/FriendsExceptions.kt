package com.github.travelplannerapp.ServerApp.exceptions

import com.github.travelplannerapp.communication.commonmodel.ResponseCode

class DeleteFriendsException(override val message: String,
                       override val code: ResponseCode = ResponseCode.DELETE_FRIENDS_ERROR): Exception(message), ApiException

class AddFriendException(override val message: String,
                            override val code: ResponseCode = ResponseCode.ADD_FRIEND_ERROR): Exception(message), ApiException