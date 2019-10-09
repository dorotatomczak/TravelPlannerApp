package com.github.travelplannerapp.communication.appmodel

import com.github.travelplannerapp.R

enum class TransportCategory(val categoryName: String, val categoryIcon: Int): java.io.Serializable {
    DRIVING("DRIVING", R.drawable.ic_circle_directions_car),
    BICYCLING("BICYCLING", R.drawable.ic_circle_directions_bike),
    TRANSIT ("TRANSIT", R.drawable.ic_circle_directions_transit),
    WALKING("WALKING", R.drawable.ic_circle_directions_walk)

}