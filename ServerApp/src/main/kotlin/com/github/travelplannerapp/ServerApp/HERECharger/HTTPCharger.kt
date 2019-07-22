package com.github.travelplannerapp.ServerApp
import com.github.travelplannerapp.ServerApp.HERECharger.UserLocation
import java.net.URL
import java.net.URLConnection

public class HTTPCharger {

    val us=UserLocation()
public var response:String="";
    val urls="https://mobile.here.com/p/h-aHR0cHM6Ly9wbGFjZXMuYXBpLmhlcmUuY29tL3BsYWNlcy92MS9wbGFjZXMvNjE2dTN0bTAtNjg5NDIyYmFlYzliNDg4NDhmMTBiOGM2ZjkzMjUzYWI7Y29udGV4dD1abXh2ZHkxcFpEMHdZbUkwTm1FNFpTMWxOMlkwTFRVM1pXUXRZakUxWkMweFlqRmhNRGt5T1RBME5UTmZNVFUyTXpneE1qRTJPRGczTmw4ek5qSTJYelExTVRrbVltSnZlRDB4T0M0ME1qazFKVEpETlRRdU1qYzBPRGtsTWtNeE9DNDVOVEUyT1NVeVF6VTBMalEwTmpVMEpuSmhibXM5TUE%252FYXBwX2lkPTNLSVFsczJEU0tsTldmZGFzcEI5JmFwcF9jb2RlPWYydkRuMVRVWUVWbjRrWXR3SzM3eXc%253D?msg=Gda%C5%84sk";


    public fun testURLs()
    {
        this.getData(urls)
        us.decodeLocation(response);
        us.printUserLocation();
    }
    private fun getData(address: String) {


        var url = URL(address)


        with(url.openConnection() as URLConnection) {

            println("\nSent 'GET' request to URL : $url;")
            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                     if(line.contains("country",false)&&line.contains("position",false)){

                         response=line;

                }
                    }
                }
            }

        }
    }

