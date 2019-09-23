package com.gotenna.android.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gotenna.android.R
import com.gotenna.android.models.LocationModel
import com.gotenna.android.support.CustomListAdapter
import com.gotenna.android.viewmodels.ListViewModel
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {
    companion object{
        // Intent to  List activity
        fun newIntent(context:Context):Intent{
            return Intent(context, ListActivity::class.java)
        }
    }

    lateinit var listViewModel: ListViewModel

    private val listObserver = Observer<ArrayList<LocationModel>>{ list ->
        setupAdapter(list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setupViewModel()
    }

    fun setupViewModel(){
        listViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()

        listViewModel.load(this)

        //Observer
        listViewModel.list.observe(this,listObserver)
    }

    private fun setupAdapter(list:ArrayList<LocationModel>){
        val adapter = CustomListAdapter(this,list)

        list_places.adapter = adapter
        list_places.layoutManager = LinearLayoutManager(this)
    }
}
