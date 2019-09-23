package com.gotenna.android.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gotenna.android.models.CallbackListener
import com.gotenna.android.models.LocationModel
import com.gotenna.android.repositories.ListRepository

class ListViewModel : ViewModel() {
    var list : MutableLiveData<ArrayList<LocationModel>> = MutableLiveData()

    //To handle list from repository
    private val listInfoCallback:CallbackListener<ArrayList<LocationModel>> = { info ->
        info.let {
            list.value = it
        }
    }

    //Call to load place list
    fun load (context: Context){
        ListRepository.getLocationDetails(context,listInfoCallback)
    }
}