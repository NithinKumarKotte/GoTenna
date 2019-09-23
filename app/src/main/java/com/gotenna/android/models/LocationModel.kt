package com.gotenna.android.models

import java.io.Serializable

data class LocationModel (
    var name: String?,
    var latitude: Double?,
    var longitude: Double?,
    var description:String?
):Serializable