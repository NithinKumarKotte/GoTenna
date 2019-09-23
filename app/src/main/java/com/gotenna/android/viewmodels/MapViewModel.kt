package com.gotenna.android.viewmodels

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gotenna.android.models.CallbackListener
import com.gotenna.android.repositories.MapRepository

class MapViewModel : ViewModel()  {

    var currentLocation : MutableLiveData<Location> = MutableLiveData()

    // Changes in the userlocatin is addressed in locationInfoCallback
    private val locationInfoCallback:CallbackListener<Location> = { info ->
        info.let {
            currentLocation.value = it
        }
    }

    // Call to Load current address details
    fun load (context:Context, permissionGranted:Boolean) {
        MapRepository.getLastKnownLocation (context, locationInfoCallback, permissionGranted)
    }
}