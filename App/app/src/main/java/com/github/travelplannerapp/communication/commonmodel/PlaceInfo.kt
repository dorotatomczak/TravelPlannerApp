package com.github.travelplannerapp.ServerApp.datamodels.commonmodel

class PlaceInfo(val contacts: Contacts)

class Contacts(
        val phone: Array<Element>,
        val website: Array<Element>)
