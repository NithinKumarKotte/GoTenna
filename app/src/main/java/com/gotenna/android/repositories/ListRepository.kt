package com.gotenna.android.repositories

import android.content.Context
import com.android.volley.toolbox.Volley
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import org.json.JSONException
import com.android.volley.toolbox.JsonArrayRequest
import com.gotenna.android.models.CallbackListener
import com.gotenna.android.models.LocationModel
import org.json.JSONArray

class ListRepository {

    companion object {
        private const val URL = "https://annetog.gotenna.com/development/scripts/get_map_pins.php"

        fun getLocationDetails(context: Context, listInfoCallBack: CallbackListener<ArrayList<LocationModel>>) {
            // Perform network queue operation
            val requestQueue = Volley.newRequestQueue(context)
            val jsonArrayRequest = object : JsonArrayRequest(Request.Method.GET,
                URL,
                null,
                Response.Listener<JSONArray> { response ->
                    parseData(response,listInfoCallBack) },
                Response.ErrorListener { error ->
                    //displaying the error in toast if occurs
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT)
                        .show()
                }){}

            requestQueue.add(jsonArrayRequest)
        }

        fun parseData(response: JSONArray, listInfoCallBack: CallbackListener<ArrayList<LocationModel>>) {
            try {
                val list:ArrayList<LocationModel> = ArrayList()
                for (index in 0 until response.length()) {
                        val dataobj = response.getJSONObject(index)

                        list.add(LocationModel(dataobj.getString("name"),
                            dataobj.getDouble("latitude"),
                            dataobj.getDouble("longitude"),
                            dataobj.getString("description")))
                    }
                // calback to return list to view
                listInfoCallBack.invoke(list)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}