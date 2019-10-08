package com.github.travelplannerapp.traveldetails

class Category (var type: CategoryType, var name: Int, var icon: Int, var color: Int, var minHeight: Int) {

    enum class CategoryType {
        DAY_PLANS, TRANSPORT, ACCOMMODATION, SCANS
    }
}
