package com.github.travelplannerapp.ServerApp.datamodels.commonmodel

class Routes(var routes: Array<Route>)

class Route(var legs: Array<Leg>)

class Leg(
    var arrival_time: Time,
    var departure_time: Time,
    var distance: Element,
    var duration: Element,
    var end_address: String,
    var start_address: String,
    var steps: Array<Step>
)

class Time(
    var text: String,
    var time_zone: String,
    var varue: Long
)

class Step(
    var distance: Element,
    var duration: Element,
    var html_instructions: String,
    var travel_mode: String,
    var transit_details: TransitDetails?
)

class TransitDetails(
    var arrival_stop: Stop,
    var arrival_time: Time,
    var departure_stop: Stop,
    var departure_time: Time,
    var headsign: String,
    var line: Line,
    var vehicle: Vehicle,
    var short_name: String,
    var num_stops: Int,
    var trip_short_name: String
)

class Stop(var name: String)

class Line(var agencies: Array<Agency>)

class Agency(
    var name: String,
    var url: String
)

class Vehicle(
    var icon: String,
    var name: String,
    var type: String
)
