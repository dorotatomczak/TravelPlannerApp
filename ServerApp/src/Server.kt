import data.HTTPCharger
import java.net.ServerSocket
import java.util.*

class Server {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val server = ServerSocket(8080)
            println("Listening on port 8080...")
            val messageByte = ByteArray(1000)
            var end = false
            var dataString = ""
            val h=HTTPCharger()
            h.testURLs()

            while (true) {
                server.accept().use { socket ->

                    val today = Date()
                    val httpResponse = "HTTP/1.1 200 OK\r\n\r\n$today"
                    println(dataString)
                    socket.getOutputStream().write(httpResponse.toByteArray(charset("UTF-8")))
                    println("Send an answer")
                }
            }

        }
    }
}