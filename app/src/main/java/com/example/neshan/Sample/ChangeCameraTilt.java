package com.example.neshan.Sample;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.neshan.R;
import org.neshan.core.LngLat;
import org.neshan.core.Range;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.ui.MapEventListener;
import org.neshan.ui.MapView;


public class ChangeCameraTilt extends AppCompatActivity {


    final int BASE_MAP_INDEX = 0;

    MapView map;

    SeekBar tiltSeekBar;
    boolean isCameraTiltEnable = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_camera_tilt);
        initLayoutReferences();
    }


    private void initLayoutReferences() {
        initViews();
        initMap();


        tiltSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                map.setTilt(progress+30,0f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        map.setMapEventListener(new MapEventListener(){
            @Override
            public void onMapMoved() {
                super.onMapMoved();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tiltSeekBar.setProgress(Math.round(map.getTilt())-30);
                    }
                });

            }
        });
    }


    private void initViews(){
        map = findViewById(R.id.map);
        tiltSeekBar = findViewById(R.id.tilt_seek_bar);
    }

    private void initMap(){

        map.getOptions().setZoomRange(new Range(4.5f, 18f));
        map.getLayers().insert(BASE_MAP_INDEX, NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));


        map.setFocalPointPosition(new LngLat(51.330743, 35.767234),0 );
        map.setZoom(14,0);
    }



    public  void toggleCameraTilt(View view){
        ToggleButton toggleButton=(ToggleButton) view;
        isCameraTiltEnable=!isCameraTiltEnable;
        if (toggleButton.isChecked()){
            map.getOptions().setTiltRange(new Range(30,90));

        }else{
            map.getOptions().setTiltRange(new Range(map.getTilt(),map.getTilt()));
        }
    }

}
