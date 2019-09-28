package com.github.travelplannerapp.communication.model

class PlaceInfo(val contacts: Contacts)

class Contacts(
        val phone: Array<Element>,
        val website: Array<Element>)

class Element(
        val value: String,
        val label: String
)
