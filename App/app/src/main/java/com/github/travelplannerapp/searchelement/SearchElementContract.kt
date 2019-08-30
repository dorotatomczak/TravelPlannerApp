package com.github.travelplannerapp.searchelement

interface SearchElementContract {
    interface View{
        fun populateElementSpinner(items: Array<Int>)
    }
    interface Presenter{
        fun populateElementSpinner()
    }
}