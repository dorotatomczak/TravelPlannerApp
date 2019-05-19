import java.net.ServerSocket
import java.util.*

class Server {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val server = ServerSocket(8080)
            println("Listening on port 8080...")

            while (true) {
                server.accept().use { socket ->
                    println("Got a message")
                    val today = Date()
                    val httpResponse = "HTTP/1.1 200 OK\r\n\r\n$today"
                    socket.getOutputStream().write(httpResponse.toByteArray(charset("UTF-8")))
                    println("Send an answer")
                }
            }

        }
    }
}