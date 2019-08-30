package com.github.travelplannerapp.searchelement

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R

class SearchElementPresenter (view: SearchElementContract.View) : BasePresenter<SearchElementContract.View>(view), SearchElementContract.Presenter{

    override fun populateElementSpinner(){
        var items = arrayOf(
       R.string.eat_drink,
       R.string.restaurant,
       R.string.coffee_tea,
       R.string.snack_fast_food,
       R.string.going_out,
       R.string.museum,
       R.string.airport,
       R.string.shopping,
       R.string.outdoor,
       R.string.natural_geographical,
       R.string.rest_area,
       R.string.health_care)
        view.populateElementSpinner(items)
    }

}