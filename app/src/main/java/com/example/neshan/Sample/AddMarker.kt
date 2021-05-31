package com.example.neshan.Sample

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.neshan.R
import org.neshan.core.LngLat
import org.neshan.core.LngLatVector
import org.neshan.core.Range
import org.neshan.core.Variant
import org.neshan.geometry.LineGeom
import org.neshan.geometry.PolygonGeom
import org.neshan.graphics.ARGB
import org.neshan.layers.VectorElementEventListener
import org.neshan.layers.VectorElementLayer
import org.neshan.services.NeshanMapStyle
import org.neshan.services.NeshanServices
import org.neshan.styles.*
import org.neshan.ui.*
import org.neshan.utils.BitmapUtils
import org.neshan.vectorelements.Line
import org.neshan.vectorelements.Marker
import org.neshan.vectorelements.Polygon

class AddMarker : AppCompatActivity() {
    val BASE_MAP_INDEX = 0
    val POI_INDEX = 1
    val TRAFFIC_INDEX = 1
    var map: MapView? = null
    var labelLayer: VectorElementLayer? = null
    var marker: Marker? = null
    var markerId: Long = 0
    var animationStyle: AnimationStyle? = null
    var mapStyle: NeshanMapStyle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_marker)
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
            NeshanMapStyle.STANDARD_NIGHT,
            "$cacheDir/baseMap",
            10
        )
        map!!.layers.insert(BASE_MAP_INDEX, baseMap)
        mapStyle = NeshanMapStyle.STANDARD_DAY


        //add Poi
        map!!.layers.insert(
            POI_INDEX,
            NeshanServices.createPOILayer(mapStyle == NeshanMapStyle.STANDARD_DAY)
        )
        //Remove Poi
        //  map.getLayers().remove(map.getLayers().get(POI_INDEX));


        //Add traffic layer
        map!!.layers.insert(TRAFFIC_INDEX, NeshanServices.createTrafficLayer())
        //remove
        //map.getLayers().remove(map.getLayers().get(TRAFFIC_INDEX));
        map!!.setFocalPointPosition(LngLat(51.325525, 35.762294), 0f)
        map!!.setZoom(14f, 0f)
        map!!.mapEventListener = object : MapEventListener() {
            override fun onMapClicked(mapClickInfo: ClickData) {
                super.onMapClicked(mapClickInfo)
                if (mapClickInfo.clickType == ClickType.CLICK_TYPE_LONG) {
                    val clickLocation = mapClickInfo.clickPos
                    addMarker(clickLocation, markerId)
                    markerId++
                } else if (mapClickInfo.clickType == ClickType.CLICK_TYPE_SINGLE) {
                       drawLine();
//                    drawPloygone()
                } else if (mapClickInfo.clickType == ClickType.CLICK_TYPE_DOUBLE) {
                    //Log.e(TAG, "onMapClicked: ", );
                }
            }
        }
    }

    private fun drawPloygone(): PolygonGeom {
        labelLayer!!.clear()
        val latVector = LngLatVector()
        latVector.add(LngLat(51.325525, 35.762294))
        latVector.add(LngLat(51.323768, 35.756548))
        latVector.add(LngLat(51.328617, 35.755394))
        latVector.add(LngLat(51.330666, 35.760905))
        val polygonGeom = PolygonGeom(latVector)
        val polygon =
            Polygon(polygonGeom, polygoneStyle)
        labelLayer!!.add(polygon)
        return polygonGeom
    }

    private val polygoneStyle: PolygonStyle
        private get() {
            val creator = PolygonStyleCreator()
            creator.lineStyle = lineStyle
            return creator.buildStyle()
        }

    private fun addMarker(clickLocation: LngLat, markerId: Long) {
        val builder = AnimationStyleBuilder()
        builder.fadeAnimationType = AnimationType.ANIMATION_TYPE_SMOOTHSTEP
        builder.sizeAnimationType = AnimationType.ANIMATION_TYPE_SPRING
        builder.phaseInDuration = 0.5f
        builder.phaseOutDuration = 0.5f
        animationStyle = builder.buildStyle()
        val markerStyleCreator = MarkerStyleCreator()
        markerStyleCreator.size = 30f
        markerStyleCreator.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(
            BitmapFactory.decodeResource(
                resources, R.drawable.ic_marker
            )
        )
        markerStyleCreator.animationStyle = animationStyle
        val markerStyle = markerStyleCreator.buildStyle()
        marker = Marker(clickLocation, markerStyle)
        marker!!.setMetaDataElement("id", Variant(markerId))
        labelLayer!!.add(marker)
        labelLayer!!.vectorElementEventListener = object : VectorElementEventListener() {
            override fun onVectorElementClicked(clickInfo: ElementClickData): Boolean {
                if (clickInfo.clickType == ClickType.CLICK_TYPE_LONG) {
                    val removeId =
                        clickInfo.vectorElement.getMetaDataElement("id").long
                    labelLayer!!.remove(clickInfo.vectorElement)
                    runOnUiThread {
                        Toast.makeText(
                            this@AddMarker,
                            "Marker " + removeId + "Deleted!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                return true
            }
        }
    }
// region کشیدن خط
    private fun drawLine(): LineGeom {
        val lngLatVector = LngLatVector()
        lngLatVector.add(LngLat(51.327650, 35.700368))
        lngLatVector.add(LngLat(51.323889, 35.756670))
        val lineGeom = LineGeom(lngLatVector)
        val line =
            Line(lineGeom, lineStyle)
        labelLayer!!.add(line)
        return lineGeom
    }

//    endregion

    private val lineStyle: LineStyle
        private get() {
            val creator = LineStyleCreator()
            creator.color = ARGB(
                255.toShort(),
                150.toShort(),
                120.toShort(),
                80.toShort()
            )
            creator.width = 12f
            return creator.buildStyle()
        }
}