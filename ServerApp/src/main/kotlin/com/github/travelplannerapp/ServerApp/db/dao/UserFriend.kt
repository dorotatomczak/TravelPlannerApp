package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class UserFriend(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var userId: Int? by defaultMap
    var friendId: Int? by defaultMap

    constructor(id: Int?,
                userId: Int?,
                friendId: Int?) :
            this(
                    mutableMapOf<String, Any?>(
                            "id" to id,
                            "userId" to userId,
                            "friendId" to friendId)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf<String, Any?>(
                            "id" to result.getInt("id"),
                            "userId" to result.getInt("userId"),
                            "friendId" to result.getInt("friendId")
                    )
            )
}