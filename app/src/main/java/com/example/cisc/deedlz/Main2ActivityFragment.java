package com.example.cisc.deedlz;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class Main2ActivityFragment extends Fragment {
    private DoodleView doodleView;
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private boolean dialogOnScreen = false;

    private static final int ACCELERATION_THRESHOLD = 100000;

    private static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE = 1;

    public void onResume() {
        super.onResume();
        enableAccelerometerListening();
    }

    private void enableAccelerometerListening(){

        SensorManager sensorManager =
                (SensorManager) getActivity().getSystemService(
                        Context.SENSOR_SERVICE);

        sensorManager.registerListener(sensorEventListener,
                SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void onPause(){
        super.onPause();
        disableAccelerometerListening();
    }

    private void disableAccelerometerListening() {
        SensorManager sensorManager =
                (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);


        sensorManager.unregisterListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (!dialogOnScreen) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];


                lastAcceleration = currentAcceleration;

                currentAcceleration = x * x + y * y + z * z;

                acceleration = currentAcceleration *
                        (currentAcceleration - lastAcceleration);

                if (acceleration > ACCELERATION_THRESHOLD)
                    confirmErase();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void confirmErase() {
        EraseImageDialogFragment fragment = new EraseImageDialogFragment();
        fragment.show(getFragmentManager(), "erase dialog");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.doodle_fragment_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemID()) {
            case R.id.color:
                ColorDialogFragment colorDialog = new ColorDialogFragment();
                colorDialog.show(getFragmentManager(), "colorDialog");
                return true;
            case R.id.Line_width:
                LineWidthDialogFragment widthDialog =
                        newLineWidthDialogFragment();
                widthDialog.show(getFragmentManager(), "line width dialog");
                return true;
            case R.id.delete_drawing:
                confirmErase();
                return true;
            case R.id.save:
                saveImage();
                return true;
            case R.id.print:
                doodleView.printImage();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveImage() {
        if (getContext().checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)  {
            
        }

    }


    public Main2ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main2, container, false);
    }
}
