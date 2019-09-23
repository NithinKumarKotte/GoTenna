package com.gotenna.android.repositories

import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.gotenna.android.models.CallbackListener

object MapRepository  {
    fun getLastKnownLocation(context: Context, locationInfoCallBack: CallbackListener<Location>,
                             permissionGranted:Boolean){

        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val netEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        try {
            if (netEnabled && permissionGranted) {
                //Get the last known location
                val location: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                locationInfoCallBack.invoke(location as @ParameterName(name = "currentLocation") Location)
            }

        } catch( e:SecurityException) {
            e.printStackTrace()
        }
    }
}