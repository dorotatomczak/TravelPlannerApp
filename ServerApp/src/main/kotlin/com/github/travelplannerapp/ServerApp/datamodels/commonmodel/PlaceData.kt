package com.github.travelplannerapp.communication.commonmodel

import java.io.Serializable

class PlaceData(
        var name: String?,
        var placeId: String?,
        var location: Location?,
        var contacts: Contacts?,
        var extended: Extended?

) : Serializable

class Location(
        var address: Address,
        var position: Array<Double>
) : Serializable

class Address(
        var text: String
) : Serializable

class Extended(
        var openingHours: OpeningHours? = null
)
