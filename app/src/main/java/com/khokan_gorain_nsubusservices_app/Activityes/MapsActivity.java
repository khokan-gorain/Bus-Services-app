package com.khokan_gorain_nsubusservices_app.Activityes;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.khokan_gorain_nsubusservices_app.ApiController.ApiController;
import com.khokan_gorain_nsubusservices_app.ModelClass.SearchingBusList;
import com.khokan_gorain_nsubusservices_app.R;
import com.khokan_gorain_nsubusservices_app.databinding.ActivityMapsBinding;

import java.util.List;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private int busId;
    Marker busLocationMarker;
    Double latitude,longitude;
    int busStatus = 1;
    int busNumber;
    Handler handler;
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        busId = bundle.getInt("busId");
       // Toast.makeText(this, "Bus id is "+busId, Toast.LENGTH_LONG).show();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    sleep(5000);
//                    Toast.makeText(MapsActivity.this, "Hi", Toast.LENGTH_LONG).show();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

         handler = new Handler();
         runnable = new Runnable() {
            @Override
            public void run() {
               // setBusLocationMarker(22.8082139,86.2633623);
                getBusLocation();
                handler.postDelayed(this,4000);
            }
        };
        handler.post(runnable);

    }

    public void getBusLocation(){

        Call<List<SearchingBusList>> call = ApiController
                .getInstance()
                .getApi()
                .getBusCurrentLocation(busId);

        call.enqueue(new Callback<List<SearchingBusList>>() {
            @Override
            public void onResponse(Call<List<SearchingBusList>> call, Response<List<SearchingBusList>> response) {
                List<SearchingBusList> data = response.body();
                if(data != null){
                    int i=0;
                    latitude = data.get(i).getLatitude_gmap();
                    longitude = data.get(i).getLongitude_gmap();
                    busStatus = data.get(i).getStatus();
                    busNumber = data.get(i).getBus_number();
                    //Toast.makeText(MapsActivity.this, latitude.toString()+" "+longitude.toString(), Toast.LENGTH_LONG).show();
                    setBusLocationMarker(latitude,longitude);
                } else {
                    Toast.makeText(MapsActivity.this, "Something went wrong. Please try again...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<SearchingBusList>> call, Throwable t) {
                Toast.makeText(MapsActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setBusLocationMarker(Double latitude,Double longitude){

        LatLng latLng = new LatLng(latitude, longitude);
        Location location = new Location("service Provider");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
       // Toast.makeText(MapsActivity.this, latitude.toString()+" "+longitude.toString(), Toast.LENGTH_LONG).show();
        if(busLocationMarker == null){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.mynewbus));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            markerOptions.title("Bus Number "+ String.valueOf(busNumber));
            busLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        } else{
            busLocationMarker.setPosition(latLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
