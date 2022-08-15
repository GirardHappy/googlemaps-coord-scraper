/* 
This Kotlin function allows you to scrap from google maps the coordinates of the nearest place

FOR STUDY PURPOSE ONLY, BECAUSE BREAK THE GOOGLE MAPS TOS
--------------------------------------------------------------------------------------
Parameters

currentLocation: Location | current location with latitude and longitude

f:String string to search on google maps
--------------------------------------------------------------------------------------
"Returned" data coordinates of the searched point

destinationLocation: Location | destination location with latitude and longitude
--------------------------------------------------------------------------------------
To run this function you must run it on another thread, in kotlin it's easy:

thread{
    Coord(ArrayOfCoord,StringToSearch)
}
--------------------------------------------------------------------------------------
This function use the jsoup library (https://jsoup.org/)
and for this reason to run this function you must import:

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
--------------------------------------------------------------------------------------
If you are using Gradle you also must add this implementation to your gradle.build file

implementation 'org.jsoup:jsoup:1.15.1'
--------------------------------------------------------------------------------------
Devoloper: Girard
*/

lateinit var destinationLocation:Location

private fun Coord(currentLocation:Location, f:String){
    Thread{

        //Init var
        var latp: String = currentLocation.latitude.toString()
        var lngp: String = currentLocation.longitude.toString()
        var i: Int = 0
        var s: String
        var d: Document?=null

        //Request to google maps
        //header("Cookie","CONSENT=YES+cb.20210418-17-p0.it+FX+917;") is used to bypass the google consent agreement page
        try {
            d = Jsoup.connect("https://www.google.com/maps/search/${f}/@${latp},${lngp},20z/data=!4m4!2m3!5m1!2e1!6e5").header("Cookie", "CONSENT=YES+cb.20210418-17-p0.it+FX+917;").get()
        } catch (e: java.net.UnknownHostException) {
            Log.w("Coord in MainActy", "Connessione a Internet Assente")
        } catch (e: Exception) {
            Log.w("Coord in MainActy", "$e")
        }
        if(d!=null) {
            s = d.toString()
            s = s.substring(s.indexOf("\"categorical-search-results-injection\""))
            for (n in 0..7)
                i = s.indexOf('[', i + 1)
            s = s.substring(i + 1)
            s = s.substring(0, s.indexOf(']'))

            var latd: String = s.substring(0, s.indexOf(","))
            var lngd: String = s.substring(s.indexOf(",") + 1)
            latd = latd.substring(0, 2) + "." + latd.substring(2)
            lngd = lngd.substring(0, 2) + "." + lngd.substring(2)
            destinationLocation = Location("")
            destinationLocation.latitude = latd.toDouble()
            destinationLocation.longitude = lngd.toDouble()
        }
    }.start()
}
