package com.github.travelplannerapp.ServerApp.datamodels.commonmodel

import com.github.travelplannerapp.communication.commonmodel.Contacts
import com.github.travelplannerapp.communication.commonmodel.OpeningHours
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
