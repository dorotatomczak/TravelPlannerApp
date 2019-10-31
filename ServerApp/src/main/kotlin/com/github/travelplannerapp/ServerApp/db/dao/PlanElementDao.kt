package com.github.travelplannerapp.ServerApp.db.dao

import com.github.travelplannerapp.communication.commonmodel.PlanElement
import java.sql.ResultSet
import java.sql.Timestamp


class PlanElementDao(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var fromDateTime: Timestamp? by defaultMap
    var travelId: Int? by defaultMap
    var placeId: Int? by defaultMap
    var completion: Boolean? by defaultMap

    constructor(
            id: Int?,
            fromDateTime: Timestamp?,
            travelId: Int?,
            placeId: Int?,
            completion: Boolean?) :
            this(
                    mutableMapOf(
                            "id" to id,
                            "fromDateTime" to fromDateTime,
                            "travelId" to travelId,
                            "placeId" to placeId,
                            "completion" to completion)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf(
                            "id" to result.getInt("id"),
                            "fromDateTime" to result.getTimestamp("from_date_time"),
                            "travelId" to result.getInt("travel_id"),
                            "placeId" to result.getInt("place_id"),
                            "completion" to result.getBoolean("completion")
                    )
            )

    constructor(travelId: Int, planElement: PlanElement) :
            this(
                    planElement.id,
                    Timestamp(planElement.fromDateTimeMs),
                    travelId,
                    planElement.placeId,
                    planElement.completion
            )
}
