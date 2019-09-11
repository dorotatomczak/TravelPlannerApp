package com.github.travelplannerapp.dayplans.searchelement

interface SearchElementContract {
    interface View{
        fun returnResultAndFinish(messageCode: Int)
        fun showSnackbar(messageCode: Int)
    }
    interface Presenter{
        fun search(category: String, west: String, south: String, east: String, north: String)
    }
}