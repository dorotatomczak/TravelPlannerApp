package com.github.travelplannerapp.communication.model

import com.github.travelplannerapp.R

enum class Category(val categoryName: String, val stringResurceId: Int) {
    SIGHTS_MUSEUM("museum", R.string.museum),
    RESTAURANT("restaurant", R.string.restaurant),
    COFFEE_TEA("coffee-tea", R.string.coffee_tea),
    GOING_OUT("going-out", R.string.going_out),
    AIRPORT("airport", R.string.airport),
    SHOPPING("shopping", R.string.shopping),
    LEISURE_OUTDOOR("leisure-outdoor", R.string.outdoor),
    NATURAL_GEOGRAPHICAL("natural-geographical", R.string.natural_geographical),
    TOILET_REST_AREA("toilet-rest-area", R.string.rest_area),
    HOSPITAL_HEALTH_CARE_FACILITY("hospital-health-care-facility", R.string.health_care);

    // more: https://developer.here.com/documentation/places/topics/categories.html
}

