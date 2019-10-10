package com.github.travelplannerapp.communication.commonmodel

class PlaceInfo(val contacts: Contacts)

class Contacts(
        val phone: Array<Element>,
        val website: Array<Element>)
