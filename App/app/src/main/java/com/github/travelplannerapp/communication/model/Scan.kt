package com.github.travelplannerapp.communication.model

import java.io.Serializable

data class Scan (
        val id: Int,
        val userId: Int,
        val travelId: Int,
        val name: String
) : Serializable
