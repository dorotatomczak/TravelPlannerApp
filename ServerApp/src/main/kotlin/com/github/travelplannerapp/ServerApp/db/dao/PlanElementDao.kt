package com.github.travelplannerapp.ServerApp.db.dao

import com.github.travelplannerapp.communication.commonmodel.PlanElement
import java.sql.ResultSet
import java.sql.Timestamp


class PlanElementDao(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var locale: String? by defaultMap
    var fromDateTime: Timestamp? by defaultMap
    var toDateTime: Timestamp? by defaultMap
    var travelId: Int? by defaultMap
    var placeId: Int? by defaultMap

    constructor(
            id: Int?,
            locale: String?,
            fromDateTime: Timestamp?,
            toDateTime: Timestamp?,
            travelId: Int?,
            placeId: Int?) :
            this(
                    mutableMapOf(
                            "id" to id,
                            "locale" to locale,
                            "fromDateTime" to fromDateTime,
                            "toDateTime" to toDateTime,
                            "travelId" to travelId,
                            "placeId" to placeId)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf(
                            "id" to result.getInt("id"),
                            "locale" to result.getString("locale"),
                            "fromDateTime" to result.getTimestamp("from_date_time"),
                            "toDateTime" to result.getTimestamp("to_date_time"),
                            "travelId" to result.getInt("travel_id"),
                            "placeId" to result.getInt("place_id")
                    )
            )

    constructor(travelId: Int, planElement: PlanElement) :
            this(
                    planElement.id,
                    planElement.locale,
                    Timestamp(planElement.fromDateTimeMs),
                    Timestamp(planElement.toDateTimeMs),
                    travelId,
                    planElement.placeId
            )
}
