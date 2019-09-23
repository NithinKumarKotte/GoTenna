package com.gotenna.android.support

import android.app.Application
import android.content.Context
import com.mapbox.mapboxsdk.Mapbox

class MapBoxInstance : Application() {
    companion object{

        // To have a common MapBox Instance across app
        fun getMapBoxInstance (context: Context){
            Mapbox.getInstance(context, "pk.eyJ1Ijoibml0aGlua3VtYXIxMTg5MSIsImEiOiJjazBzaml1aXAwMGlmM2VwbDd1c2R6YzNjIn0.lqfKhBmqZuHGoniearVpig");
        }
    }
}