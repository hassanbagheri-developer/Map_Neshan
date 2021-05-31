package com.example.neshan.Sample;

import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neshan.R;
import com.example.neshan.utils.CircularSeekBar;

import org.neshan.utils.*;
import org.neshan.core.LngLat;
import org.neshan.core.Range;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.ui.MapEventListener;
import org.neshan.ui.MapView;


public class ChangeCameraBearing extends AppCompatActivity {

    final int BASE_MAP_INDEX = 0;


    MapView map;

    CircularSeekBar bearingSeekBar;
    float cameraBearing;
    boolean isCameraRotationEnable = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_camera_bearing);
        initLayoutReferences();
    }



    private void initLayoutReferences() {

        initViews();
        initMap();


        bearingSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                map.setBearing(progress,0);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        map.setMapEventListener(new MapEventListener(){

            @Override
            public void onMapMoved() {
                super.onMapMoved();
                if (map.getBearing()<0){
                    cameraBearing=(180+map.getBearing())+180;

                }else{
                    cameraBearing=map.getBearing();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bearingSeekBar.setProgress(cameraBearing);
                    }
                });
            }
        });

    }

    private void initViews() {
        map = findViewById(R.id.map);
        bearingSeekBar = findViewById(R.id.bearing_seek_bar);
    }
    private void initMap() {

        map.getOptions().setZoomRange(new Range(4.5f, 18f));
        map.getLayers().insert(BASE_MAP_INDEX, NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));

        map.setFocalPointPosition(new LngLat(51.330743, 35.767234), 0);
        map.setZoom(14, 0);
    }

    public  void toggleCameraRotation(View view){
        ToggleButton toggleButton=(ToggleButton) view;
        isCameraRotationEnable=!isCameraRotationEnable;
        if (toggleButton.isChecked()){
             map.getOptions().setRotatable(true);

        }else{
            map.getOptions().setRotatable(false);
        }
    }

}
