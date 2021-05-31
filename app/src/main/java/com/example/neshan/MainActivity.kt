package com.example.neshan

import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.neshan.Sample.AddLabel
import com.example.neshan.Sample.AddMarker
import com.example.neshan.Sample.ChangeCameraBearing
import com.example.neshan.Sample.ChangeCameraTilt
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.neshan.core.LngLat
import org.neshan.services.NeshanMapStyle
import org.neshan.services.NeshanServices
import org.neshan.ui.MapView




class MainActivity : AppCompatActivity() ,PopupMenu.OnMenuItemClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        main_menu.setOnClickListener {
            menuup()
        }


        map.setFocalPointPosition(LngLat(51.33800,35.69997), 0f)
        map.setZoom(14f,0f)
        //add basemap layer
        map.layers.add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY))
    }

    //region handel menu
    fun menuup() {
        val popup = PopupMenu(this, main_menu)
        popup.menuInflater.inflate(R.menu.item_menu, popup.menu)
        popup.setOnMenuItemClickListener(this)
        popup.show()
    }

    // handel items munu
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.item_label -> {
                startActivity<AddLabel>()
                true
            }
            R.id.item_marker -> {
                startActivity<AddMarker>()
                true
            }
            R.id.item_changeTilt -> {
                startActivity<ChangeCameraTilt>()
                true
            }
            R.id.item_changeBearing -> {
                startActivity<ChangeCameraBearing>()
                true
            }


            else -> false
        }


    }


//    endregion




}