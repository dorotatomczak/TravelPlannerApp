package com.github.travelplannerapp.communication.appmodel

import com.github.travelplannerapp.R

enum class PlaceCategory(val categoryName: String, val categoryIcon: Int, val stringResourceId: Int) : java.io.Serializable {
    RESTAURANT("restaurant", R.drawable.ic_circle_restaurant, R.string.restaurant),
    COFFEE_TEA("coffee-tea", R.drawable.ic_circle_coffee, R.string.coffee_tea),
    GOING_OUT("going-out", R.drawable.ic_circle_going_out, R.string.going_out),
    SIGHTS_MUSEUM("museum", R.drawable.ic_circle_museum, R.string.sights_museum),
    TRANSPORT("transport", R.drawable.ic_circle_transport, R.string.transport),
    ACCOMMODATION("accommodation", R.drawable.ic_circle_hotel, R.string.accommodation),
    SHOPPING("shopping", R.drawable.ic_circle_shopping, R.string.shopping),
    LEISURE_OUTDOOR("leisure-outdoor", R.drawable.ic_circle_outdoor, R.string.leisure_outdoor),
    NATURAL_GEOGRAPHICAL("natural-geographical", R.drawable.ic_circle_natural, R.string.natural_geographical),

    // more: https://developer.here.com/documentation/places/topics/categories.html
}

