package com.github.travelplannerapp.communication

import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import javax.inject.Singleton
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@Singleton
class CommunicationService {
    // Volley guide
    //https://guides.codepath.com/android/Networking-with-the-Volley-Library

    //10.0.2.2 is "localhost" but on computer
    //localhost via emulator is emulator itself
    private var serverUrl: String = "http://10.0.2.2:8080"

    fun getExampleData(type: Int, view: View): String {
        var response = "empty"
        val countDownLatch = CountDownLatch(1)

            val stringRequest = StringRequest(Request.Method.GET, serverUrl,
                    Response.Listener<String> { r ->
                        response = "Response is: $r"

                        countDownLatch.countDown()
                    },
                    Response.ErrorListener { response = "That didn't work!" })

        val queue = Volley.newRequestQueue(view.getContext())
        queue.add(stringRequest)
        countDownLatch.await(6000, TimeUnit.MILLISECONDS)

        if (response == "empty")
            throw Exception("Query unsuccessful!")

        return response
    }
}