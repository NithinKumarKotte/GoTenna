package com.gotenna.android.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gotenna.android.R
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_map.*
import com.mapbox.android.core.permissions.PermissionsManager
import android.widget.Toast
import com.gotenna.android.models.LocationModel
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.mapboxsdk.geometry.LatLng
import android.location.Location
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gotenna.android.support.MapBoxInstance
import com.gotenna.android.viewmodels.MapViewModel
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils

class MapActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {
    companion object{

        //Intent to MapActivity
        fun newIntent(context: Context,eachLocation:LocationModel): Intent {
            val intent = Intent(context, MapActivity::class.java)
            intent.putExtra("LocationModel",eachLocation)
            return intent
        }

        private const val MARKER_ICON = "marker_icon"
        private const val LAYER_ID= "layer_id"
        private const val SOURCE_ID = "source_id"

    }

    lateinit var permissionsManager:PermissionsManager
    lateinit var mapboxMap:MapboxMap
    private var featureCollection: FeatureCollection? = null
    private var geoJsonSource:GeoJsonSource? = null
    private var currLocation:Location? = null
    private var pinLocation:LocationModel? = null

    lateinit var locationViewModel:MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //up button setup
        val toolbar:ActionBar? = supportActionBar
        toolbar?.title = getString(R.string.map_view)
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setDisplayShowHomeEnabled(true)

        // MapboxInstance
        MapBoxInstance.getMapBoxInstance(this)

        setContentView(R.layout.activity_map)

        //Network call to access setup map
        mapView.getMapAsync(this)


    }

    // To retrieve pinlocation, setup feature collection
    fun setupIntent(currentLocation:Location?){
        pinLocation= intent.getSerializableExtra("LocationModel") as? LocationModel

        featureCollection = FeatureCollection.fromFeatures(
            arrayOf(
                Feature.fromGeometry(pinLocation?.longitude?.let {longitude ->
                    pinLocation?.latitude?.let { latitude ->
                        Point.fromLngLat(longitude, latitude) }
                }),

                Feature.fromGeometry(currentLocation?.longitude?.let {longitude ->
                    currentLocation?.latitude?.let { latitude ->
                        Point.fromLngLat(longitude, latitude) }
                })
            )
        )
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this@MapActivity.mapboxMap = mapboxMap

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            locationViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
            locationViewModel.load(this,true)

            locationViewModel.currentLocation.observe(this, Observer { currentLocation->

                setupIntent(currentLocation)

                currLocation = currentLocation

                geoJsonSource = GeoJsonSource(SOURCE_ID, featureCollection)

                //add map layer
                setupLayer(currentLocation)

            })
        } else {
            //Request Permissions
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    private fun enableLocationComponent(loadedMapStyle: Style, currentLocation: Location?) {
        // Check if permissions are enabled and if not request

        // Get an instance of the component
        val locationComponent = mapboxMap?.locationComponent

        // Activate with a built LocationComponentActivationOptions object
        locationComponent?.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build())

        // Enable to make component visible
       locationComponent?.isLocationComponentEnabled = true

        val userLocationLatLng = currentLocation?.longitude?.let { longitude ->
            currentLocation?.latitude?.let { latitude ->
                LatLng(latitude, longitude)
            }
        }

        val pinLocationLatLng = pinLocation?.longitude?.let { longitude ->
            pinLocation?.latitude?.let { latitude ->
                LatLng(latitude, longitude)
            }
        }

        val latLngBounds = userLocationLatLng?.let {userLocationLatLng ->
            pinLocationLatLng?.let { pinLocationLatLng ->
                LatLngBounds.Builder()
                    .include(userLocationLatLng)
                    .include(pinLocationLatLng)
                    .build()
            }
        }

        // Initiate Bounding box
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds!!, 100))

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty()) {
            onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {

    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            //Get previously set layer
            getLayer()
            Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG)
                .show()
        }
    }

    // Set new style
    fun setupLayer(currentLocation: Location?){
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            enableLocationComponent(style, currentLocation)

            val  bitmap = BitmapUtils.getBitmapFromDrawable(getDrawable(R.drawable.ic_map_marker))

            bitmap?.let { style.addImage(MARKER_ICON, it) }

            geoJsonSource?.let {
                style.addSource(it)
            }

            style.addLayer(SymbolLayer(LAYER_ID, SOURCE_ID)
                .withProperties(
                    PropertyFactory.iconImage("marker_icon"),
                    PropertyFactory.iconIgnorePlacement(true),
                    PropertyFactory.iconAllowOverlap(true)
                )
            )
        }
    }

    // To render the same layer when permissions are provided
    fun getLayer(){
        mapboxMap.getStyle{
            enableLocationComponent(it,currLocation)
        }
    }

    public override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView.onStop()
    }
}
