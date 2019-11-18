package com.github.travelplannerapp.communication.appmodel

import java.io.Serializable

data class Scan (
        val id: Int,
        val userId: Int,
        val travelId: Int,
        val name: String
) : Serializable
