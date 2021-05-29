package com.example.neshan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.neshan.core.LngLat
import org.neshan.core.Range
import org.neshan.layers.VectorElementLayer
import org.neshan.services.NeshanMapStyle
import org.neshan.services.NeshanServices
import org.neshan.ui.MapView

class BaseSample : AppCompatActivity() {
    val BASE_MAP_INDEX = 0
    var map: MapView? = null
    var labelLayer: VectorElementLayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  setContentView(R.layout.activity_add_label);
        initLayoutReferences()
    }

    private fun initLayoutReferences() {
        initViews()
        initMap()
    }

    private fun initViews() {
        map = findViewById(R.id.map)
    }

    private fun initMap() {
        labelLayer = NeshanServices.createVectorElementLayer()
        map!!.layers.add(labelLayer)
        map!!.options.setZoomRange(Range(4.5f, 18f))
        val baseMap = NeshanServices.createBaseMap(
            NeshanMapStyle.STANDARD_DAY,
            "$cacheDir/baseMap",
            10
        )
        map!!.layers.insert(BASE_MAP_INDEX, baseMap)
        map!!.setFocalPointPosition(LngLat(51.330743, 35.767234), 0f)
        map!!.setZoom(14f, 0f)
    }
}