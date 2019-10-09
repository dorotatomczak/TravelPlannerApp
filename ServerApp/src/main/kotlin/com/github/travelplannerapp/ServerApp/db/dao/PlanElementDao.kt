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

    constructor(
            id: Int?,
            fromDateTime: Timestamp?,
            travelId: Int?,
            placeId: Int?) :
            this(
                    mutableMapOf(
                            "id" to id,
                            "fromDateTime" to fromDateTime,
                            "travelId" to travelId,
                            "placeId" to placeId)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf(
                            "id" to result.getInt("id"),
                            "fromDateTime" to result.getTimestamp("from_date_time"),
                            "travelId" to result.getInt("travel_id"),
                            "placeId" to result.getInt("place_id")
                    )
            )

    constructor(travelId: Int, planElement: PlanElement) :
            this(
                    planElement.id,
                    Timestamp(planElement.fromDateTimeMs),
                    travelId,
                    planElement.placeId
            )
}
