package data
import java.net.URL
import java.net.URLConnection
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.util.*

class HTTPCharger {
    var user=1;
    val urls="https://wego.here.com/norwegia/oslo/city-town-village/oslo--578u4xsu-7d8e54dc28fa4fccadfc59ef6dfbba1a?map=59.9123,10.74998,12,satellite&msg=Oslo"
   //val urls=arrayOf("https://wego.here.com/norwegia/oslo/city-town-village/oslo--578u4xsu-7d8e54dc28fa4fccadfc59ef6dfbba1a?map=59.9123,10.74998,12,satellite&msg=Oslo",
               // "https://wego.here.com/polska/toru%C5%84/city-town-village/toru%C5%84--loc-dmVyc2lvbj0xO3RpdGxlPVRvcnUlQzUlODQ7bGFuZz1wbDtsYXQ9NTMuMDEyNzY7bG9uPTE4LjYxMjc4O2NpdHk9VG9ydSVDNSU4NDtwb3N0YWxDb2RlPTg3LTEwMDtjb3VudHJ5PVBPTDtzdGF0ZT1Xb2ouK0t1amF3c2tvLVBvbW9yc2tpZTtjb3VudHk9VG9ydSVDNSU4NDtjYXRlZ29yeUlkPWNpdHktdG93bi12aWxsYWdlO3NvdXJjZVN5c3RlbT1pbnRlcm5hbDtwZHNDYXRlZ29yeUlkPTkwMC05MTAwLTAwMDA?map=53.01276,18.61278,15,satellite&msg=Toru%C5%84",
//"https://wego.here.com/stany-zjednoczone/nowy-jork,-nowy-jork/city-town-village/new-york--840dr5re-2cd682b2965e4f4aba4bbfbf54ceb069?map=40.71453,-74.00713,11,satellite&msg=New%20York");

    public fun testURLs()
    {
       /*for (i in urls.indices) {
           this.getData(urls[i])
        }*/
        this.getData(urls)

    }
    private fun getData(address: String) {


        var url = URL(address)
        var response:String
        var us=UserLocation()


        with(url.openConnection() as URLConnection) {


            println("\nSent 'GET' request to URL : $url;")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                   if(line.contains("country",false)&&line.contains("position",false)){
                   // println(line)
                    us.decodeLocation(line)
                    }
                }
            }

        }
    }
}
