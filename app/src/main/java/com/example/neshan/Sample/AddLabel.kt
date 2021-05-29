package com.example.neshan.Sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neshan.R
import org.neshan.core.LngLat
import org.neshan.core.Range
import org.neshan.graphics.ARGB
import org.neshan.layers.VectorElementLayer
import org.neshan.services.NeshanMapStyle
import org.neshan.services.NeshanServices
import org.neshan.styles.LabelStyleCreator
import org.neshan.ui.ClickData
import org.neshan.ui.ClickType
import org.neshan.ui.MapEventListener
import org.neshan.ui.MapView
import org.neshan.vectorelements.Label

class AddLabel : AppCompatActivity() {
    val BASE_MAP_INDEX = 0
    var map: MapView? = null
    var labelLayer: VectorElementLayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_label)
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
            NeshanMapStyle.DREAMY_GOLD,
            "$cacheDir/baseMap",
            10
        )
        map!!.layers.insert(BASE_MAP_INDEX, baseMap)
        map!!.setFocalPointPosition(LngLat(51.330743, 35.767234), 0f)
        map!!.setZoom(14f, 0f)
        map!!.mapEventListener = object : MapEventListener() {
            override fun onMapClicked(mapClickInfo: ClickData) {
                super.onMapClicked(mapClickInfo)
                if (mapClickInfo.clickType == ClickType.CLICK_TYPE_LONG) {
                    val clickLocation = mapClickInfo.clickPos
                    addLabel(clickLocation)
                }
            }
        }
    }

    private fun addLabel(clickLocation: LngLat) {
        labelLayer!!.clear()
        val lblstyleCreator = LabelStyleCreator()
        lblstyleCreator.fontSize = 18f
        lblstyleCreator.backgroundColor = ARGB(
            255.toShort(),
            150.toShort(),
            120.toShort(),
            80.toShort()
        )
        val labelStyle = lblstyleCreator.buildStyle()
        val label =
            Label(clickLocation, labelStyle, "مکان انتخاب شده")
        labelLayer!!.add(label)
    }
}