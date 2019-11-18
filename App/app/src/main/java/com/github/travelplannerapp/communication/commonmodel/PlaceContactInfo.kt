package com.github.travelplannerapp.communication.commonmodel

class PlaceContactInfo(val contacts: Contacts)

class Contacts(
        val phone: Array<Element>,
        val website: Array<Element>)
