package com.github.travelplannerapp.communication.model

import com.github.travelplannerapp.R

enum class PlaceCategory(val categoryName: String, val categoryIcon: Int) : java.io.Serializable {
    EAT_DRINK("eat-drink", R.drawable.ic_circle_restaurant),
    RESTAURANT("restaurant", R.drawable.ic_circle_restaurant),
    COFFEE_TEA("coffee-tea", R.drawable.ic_circle_coffee),
    SNACKS_FAST_FOOD("snack-fast-food", R.drawable.ic_circle_fast_food),
    GOING_OUT("going-out", R.drawable.ic_circle_going_out),
    SIGHTS_MUSEUM("sights-museum", R.drawable.ic_circle_museum),
    TRANSPORT("transport", R.drawable.ic_circle_transport),
    AIRPORT("airport", R.drawable.ic_circle_plane),
    ACCOMMODATION("accommodation", R.drawable.ic_circle_hotel),
    SHOPPING("shopping", R.drawable.ic_circle_plane),
    LEISURE_OUTDOOR("leisure-outdoor", R.drawable.ic_circle_outdoor),
    NATURAL_GEOGRAPHICAL("natural-geographical", R.drawable.ic_circle_natural),
    PETROL_STATION("petrol-station", R.drawable.ic_circle_petrol_station),
    ATM_BANK_EXCHANGE("atm-bank-exchange", R.drawable.ic_circle_atm),
    TOILET_REST_AREA("toilet-rest-area", R.drawable.ic_circle_toilet),
    HOSPITAL_HEALTH_CARE_FACILITY("hospital-health-care-facility", R.drawable.ic_circle_hospital)
}
