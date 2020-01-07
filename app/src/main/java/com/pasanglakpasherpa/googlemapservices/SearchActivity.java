package com.pasanglakpasherpa.googlemapservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pasanglakpasherpa.googlemapservices.model.Latitudelongitude;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private AutoCompleteTextView etCity;
    private Button btnSearch;
    private List<Latitudelongitude> latitudelongitudeList;
    Marker markerName;
    CameraUpdate center, zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        etCity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);

        fillArrayListAndSetAdapter();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etCity.getText().toString()))
                {
                    etCity.setError("Please Enter a Place Name");
                    return;
                }
                // Get the current location of the place
                int position = SearchArrayList(etCity.getText().toString());

                //System.out.println("the searched position is: "+position);
                if (position > -1)
                    loadMap(position);
                else
                    Toast.makeText(SearchActivity.this, "Location not found by name :"
                            + etCity.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //this function will fill arraylist with static data and set autocompleteview with the marker
    private void fillArrayListAndSetAdapter()
    {
        latitudelongitudeList = new ArrayList<>();
        latitudelongitudeList.add(new Latitudelongitude(27.7134481, 85.3241922, "Naagpokhari"));
        latitudelongitudeList.add(new Latitudelongitude(27.7181749, 85.3173212, "Narayanhiti Palace Museum"));
        latitudelongitudeList.add(new Latitudelongitude(27.7127827, 85.3265391, "Hotel Brihaspati"));

        String[] data = new String[latitudelongitudeList.size()];

        for (int i = 0; i < data.length; i++) {
            data[i] = latitudelongitudeList.get(i).getMarker();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                SearchActivity.this,
                android.R.layout.simple_list_item_1,
                data
            );
        etCity.setAdapter(adapter);//Setting marker values in AutoCompleteTextView
        etCity.setThreshold(1);
    }

    //This function will check weather the location is in list or not
    public int SearchArrayList(String name) {
        for (int i = 0; i < latitudelongitudeList.size(); i++) {
            if (latitudelongitudeList.get(i).getMarker().contains(name)) {
                return 1;
            }
        }
        return -1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //load kathmandu city when application launches
        mMap = googleMap;
        center = CameraUpdateFactory.newLatLng(new LatLng(27.7172453,85.3239605));
        zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
    public void loadMap(int position){
        //Remove old marker from map
        if (markerName!=null)
        {
            markerName.remove();
        }
        double latitude  =latitudelongitudeList.get(position).getLat();
        double longitude =latitudelongitudeList.get(position).getLon();
        String marker = latitudelongitudeList.get(position).getMarker();

        System.out.println("the marker is : " +marker);

        center =CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude));
        zoom =CameraUpdateFactory.zoomTo(17);
        markerName = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,
                longitude)).title(marker));
        mMap.moveCamera(center);
        mMap.animateCamera(center);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}

