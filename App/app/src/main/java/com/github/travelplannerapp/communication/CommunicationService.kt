package com.github.travelplannerapp.communication


object CommunicationService {
    //10.0.2.2 is "localhost" but on computer
    //localhost via emulator is emulator itself
    private var serverUrl: String = "http://10.0.2.2:8080/"

    fun getUrl(): String {
        return serverUrl
    }
}
