package com.gotenna.android.support

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gotenna.android.models.LocationModel
import com.gotenna.android.R
import com.gotenna.android.views.MapActivity

class CustomListAdapter(val context: Context, val placelist: ArrayList<LocationModel>):
    RecyclerView.Adapter<CustomListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.row_place, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return placelist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.placeName.text = placelist[holder.adapterPosition].name
       holder.placeDescription.text = placelist[holder.adapterPosition].description

       // start MapActivity with the required place details
       holder.card.setOnClickListener {
           context.startActivity(MapActivity.newIntent(context,placelist[position]))
       }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val placeName: TextView = itemView.findViewById(R.id.place_name)
        val placeDescription: TextView = itemView.findViewById(R.id.place_description)
        val card:LinearLayout= itemView.findViewById(R.id.item_place)

    }
}