package com.github.travelplannerapp.ServerApp.services

import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.FirebaseApp
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseOptions
import org.springframework.stereotype.Service
import org.tinylog.kotlin.Logger
import java.io.FileInputStream
import javax.annotation.PostConstruct

@Service
class FirebaseService {

    @PostConstruct
    fun initializeSdk() {
        try {
            val serviceAccount = FileInputStream("travel-planner-1a784-firebase-adminsdk-9g8t7-b7f660e216.json")

            val options = FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://travel-planner-1a784.firebaseio.com")
                    .build()

            FirebaseApp.initializeApp(options)
        } catch (e: Exception) {
            Logger.error(e.stackTrace)
        }
    }

    fun sendMessage(firebaseToken: String, recommendedPlaces: List<PlaceDao>) {
        val data = recommendedPlaces.associate { "$it.id" to "${it.title}, ${it.vicinity}" }

        val message = Message.builder()
                .putAllData(data)
                .setToken(firebaseToken)
                .build()

        val response = FirebaseMessaging.getInstance().send(message)
        Logger.info("Successfully sent message: $response")
    }
}
