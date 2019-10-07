package com.github.travelplannerapp.communication.appmodel

import com.github.travelplannerapp.R

enum class PlaceCategory(val categoryName: String, val categoryIcon: Int, val stringResurceId: Int) : java.io.Serializable {
    EAT_DRINK("eat-drink", R.drawable.ic_circle_restaurant, R.string.eat_drink),
    SNACKS_FAST_FOOD("snacks-fast-food", R.drawable.ic_circle_fast_food, R.string.snacks_fast_food),
    RESTAURANT("restaurant", R.drawable.ic_circle_restaurant, R.string.restaurant),
    COFFEE_TEA("coffee-tea", R.drawable.ic_circle_coffee, R.string.coffee_tea),
    GOING_OUT("going-out", R.drawable.ic_circle_going_out, R.string.going_out),
    SIGHTS_MUSEUM("museum", R.drawable.ic_circle_museum, R.string.sights_museum),
    TRANSPORT("transport", R.drawable.ic_circle_transport, R.string.transport),
    AIRPORT("airport", R.drawable.ic_circle_plane, R.string.airport),
    ACCOMMODATION("accommodation", R.drawable.ic_circle_hotel, R.string.accommodation),
    SHOPPING("shopping", R.drawable.ic_circle_plane, R.string.shopping),
    LEISURE_OUTDOOR("leisure-outdoor", R.drawable.ic_circle_outdoor, R.string.leisure_outdoor),
    NATURAL_GEOGRAPHICAL("natural-geographical", R.drawable.ic_circle_natural, R.string.natural_geographical),
    PETROL_STATION("petrol-station", R.drawable.ic_circle_petrol_station, R.string.petrol_station),
    ATM_BANK_EXCHANGE("atm-bank-exchange", R.drawable.ic_circle_atm, R.string.atm_bank_exchange),
    TOILET_REST_AREA("toilet-rest-area", R.drawable.ic_circle_toilet, R.string.toilet_rest_area),
    HOSPITAL_HEALTH_CARE_FACILITY("hospital-health-care-facility", R.drawable.ic_circle_hospital, R.string.hospital_health_care_facility)

    // more: https://developer.here.com/documentation/places/topics/categories.html
}

