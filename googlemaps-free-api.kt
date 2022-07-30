/* 
This Kotlin function allows you to scrap from google maps the coordinates of the nearest place
--------------------------------------------------------------------------------------
Parameters

p:Array<Int> coordinates of the start point
length: 2
p[0] = latitude
p[1] = longitude

f:String string to search on google maps
--------------------------------------------------------------------------------------
Returned data coordinates of the searched point

type: Array<Int>?
lenght: 2
[0]: latitude
[1]: longitude
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

public fun Coord(p:Array<Int>,f:String):Array<Int>?
{
    //Init var
    var latp:String = p[0].toString()
    var lngp:String = p[1].toString()
    var r:Array<Int>? = null
    var i:Int=0
    var s:String
    var d:Document = null

    //Simple coords control before start
    if(p.size!=2)
        return r

    //Convert to string the given coords in the right format
    latp = latp.substring(0,latp.length-6)+"."+latp.substring(latp.length-6);
    lngp = lngp.substring(0,lngp.length-6)+"."+lngp.substring(lngp.length-6);

    //Request to google maps
    //header("Cookie","CONSENT=YES+cb.20210418-17-p0.it+FX+917;") is used to bypass the google consent agreement page
    try {
        d=Jsoup.connect("https://www.google.com/maps/search/${f}/@${latp},${lngp},14z").header("Cookie","CONSENT=YES+cb.20210418-17-p0.it+FX+917;").get()
    }catch(e:java.net.UnknownHostException){
        Log.w("Coord in MainActy","Connessione a Internet Assente")
    }
    catch(e:Exception){
        Log.w("Coord in MainActy", "$e")
    }

    //Parse the document to extract the coordinates
    s = d.getElementsByTag("script")[6].toString()
    s = s.substring(s.indexOf("window.APP_INITIALIZATION_STATE=")+32,s.indexOf("window.APP_FLAGS=")-1)
    s = s.substring(s.indexOf("\"categorical-search-results-injection\""))
    for(n in 0..5)
        i=s.indexOf('[',i+1)
    s=s.substring(i+1)
    s=s.substring(0,s.indexOf(']'))
    r=arrayOf(s.substring(0,s.indexOf(",")).toInt(),s.substring(s.indexOf(",")+1).toInt())

    return r
}