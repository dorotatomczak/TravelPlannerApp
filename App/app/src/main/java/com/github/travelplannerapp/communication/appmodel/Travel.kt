package com.github.travelplannerapp.communication.appmodel

import java.io.Serializable

data class Travel (
        val id: Int,
        var name: String,
        var imageUrl: String? = null
) : Serializable
