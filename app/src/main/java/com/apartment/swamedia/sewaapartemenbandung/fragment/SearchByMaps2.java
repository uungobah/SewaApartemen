package com.apartment.swamedia.sewaapartemenbandung.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.DetailProperty;
import com.apartment.swamedia.sewaapartemenbandung.activity.RoundedImageView;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterProperty;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationProperty;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.maps.GPSTracker;
import com.apartment.swamedia.sewaapartemenbandung.maps.MyMarker;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nurul Akbar on 26/10/2015.
 */
public class SearchByMaps2 extends ActionBarActivity implements OnMapReadyCallback {


    // Text Input Layout
    private TextInputLayout til_location;
    //AppCompatButton
    private AppCompatButton btnSearch;
    //AppCompatAutoComplete
    private AutoCompleteTextView ac_location;
    private Toolbar toolbar;
    GoogleMap mMap;
    Double stringLatitude, stringLongitude;
    private HashMap<Marker, MyMarker> mMarkersHashMap;
    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();

    SupportMapFragment mapFragment;

    JSONArray jarr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentsearchbymap);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        inisialisasi();

        setLatLong();

        setAutoCompleteLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (statusOfGPS) {
            if (mMap == null) {
                mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map2);
                mapFragment.getMapAsync(this);
            }
        }


    }


    public void inisialisasi() {
        // Button
        btnSearch = (AppCompatButton) findViewById(R.id.btn_search_maps);
        til_location = (TextInputLayout) findViewById(R.id.input_layout_location_maps);
        ac_location = (AutoCompleteTextView) findViewById(R.id.et_location_maps);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

//        MarkerOptions markerOption = new MarkerOptions().position(new LatLng(-7.090910999999999, 107.668887)).title("Test");
//        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.property));
//
//        Marker currentMarker = mMap.addMarker(markerOption);
//        currentMarker.setDraggable(true);

        mMarkersHashMap = new HashMap<Marker, MyMarker>();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(final LatLng latLng) {
                Double longi = latLng.longitude;
                Double lat = latLng.latitude;

//                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(lat, longi));
//                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_domain_black_24dp));
//
//                Marker currentMarker = mMap.addMarker(markerOption);
//                currentMarker.setDraggable(true);
//
//                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//                    @Override
//                    public void onMarkerDragStart(Marker marker) {
//
//                    }
//
//                    @Override
//                    public void onMarkerDrag(Marker marker) {
//
//                    }
//
//                    @Override
//                    public void onMarkerDragEnd(Marker marker) {
//                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
//                        LatLng l = marker.getPosition();
//                        Toast.makeText(getApplicationContext(), l.toString(), Toast.LENGTH_LONG).show();
//                    }
//                });

            }
        });


        // Untuk Zoom
        LatLng myCoordinates = new LatLng(-7.090910999999999, 107.668887);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myCoordinates)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(8)                   // Sets the zoom 17
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchByMap();

            }
        });


//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sumbar));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent1 = new Intent(SearchByMaps2.this, DetailProperty.class);
                MyMarker myMarker = mMarkersHashMap.get(marker);
                String id = myMarker.getmId();
//                String title = marker.get

                intent1.putExtra("id", id);
                intent1.putExtra("action", "1");
                startActivity(intent1);
                finish();
            }
        });
    }


    public void setLatLong() {

        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.canGetLocation()) {
            stringLatitude = gpsTracker.getLatitude();

//            et_latitude.setText(stringLatitude);

            stringLongitude = gpsTracker.getLongitude();

//            et_longitude.setText(stringLongitude);

//            String country = gpsTracker.getCountryName(this);
//            textview = (TextView)findViewById(R.id.fieldCountry);
//            textview.setText(country);
//
//            String city = gpsTracker.getLocality(this);
//            textview = (TextView)findViewById(R.id.fieldCity);
//            textview.setText(city);
//
//            String postalCode = gpsTracker.getPostalCode(this);
//            textview = (TextView)findViewById(R.id.fieldPostalCode);
//            textview.setText(postalCode);
//
//            String addressLine = gpsTracker.getAddressLine(this);
//            textview = (TextView)findViewById(R.id.fieldAddressLine);
//            textview.setText(addressLine);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
    }

    private void plotMarkers(ArrayList<MyMarker> markers) {

         // Untuk clear map yg sebelumnya
        mMarkersHashMap.clear();
        if (markers.size() > 0) {
            for (MyMarker myMarker : markers) {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
//                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.property));
                markerOption.title(myMarker.getmTitle());

                Marker currentMarker = mMap.addMarker(markerOption);

                mMarkersHashMap.put(currentMarker, myMarker);

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());

            }

//                mapFragment.getMapAsync(this);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validateLocation() {
        if (ac_location.getText().toString().trim().isEmpty()) {
            til_location.setError(getString(R.string.err_msg_location));
//            requestFocus(ac_location);
            return false;
        } else {
            til_location.setErrorEnabled(false);
            return true;
        }
    }

    public void setAutoCompleteLocation() {
        String[] arrLocation = {"Bandung", "Bali", "Jakarta", "Jogja"};
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrLocation);
        ac_location.setAdapter(aa);
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getLayoutInflater().inflate(R.layout.infomap_layout, null);

            MyMarker myMarker = mMarkersHashMap.get(marker);

            ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);

            TextView markerTitle = (TextView) v.findViewById(R.id.marker_titel);
            TextView markerHarga = (TextView) v.findViewById(R.id.marker_harga);
            TextView markerAlamat = (TextView) v.findViewById(R.id.marker_alamat);
            TextView markerTipe = (TextView) v.findViewById(R.id.marker_tipe);

//            markerIcon.setImageResource(manageMarkerIcon(myMarker.getmIcon()));



            markerTitle.setText(myMarker.getmTitle());
            markerHarga.setText(currencyRupiah(Double.parseDouble(myMarker.getmHarga())));
            markerAlamat.setText(myMarker.getmAlamat());
            markerTipe.setText(myMarker.getmTipe());
            Picasso.with(getApplicationContext()).load(myMarker.getmIcon()).into(markerIcon);

            return v;
        }

        public String currencyRupiah(double harga) {
            String hasil = "";
            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');

            kursIndonesia.setDecimalFormatSymbols(formatRp);
            hasil = kursIndonesia.format(harga);
//        System.out.printf("Harga Rupiah: %s %n", kursIndonesia.format(harga));
            return hasil;
        }

//        private int manageMarkerIcon(String markerIcon) {
//            if (markerIcon.equals("Icon1")) {
//                return R.drawable.property;
//            } else if (markerIcon.equals("Icon2")) {
//                return R.drawable.property;
//            } else {
//                return R.drawable.property;
//            }
//        }

    }


    public void getSearchByMap() {

        final List<InformationProperty> data = new ArrayList<>();
        String tag_json_obj = "json_obj_list_property";

        String url = ConstantUtil.WEB_SERVICE.URL_SEARCH_BY_MAP;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Searching Property Data...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("data", response.toString());
                        try {
                            jarr = new JSONArray(response);

                            mMyMarkersArray.clear();

                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject c = jarr.getJSONObject(i);
                                Log.v("Masuk looping", "Masuk");
                                String title = c.getString("title");
                                String harga = c.getString("harga");
                                String alamat = c.getString("alamat");
                                String tipe = c.getString("tipe");
                                String image = c.getString("image");
                                Double lat = c.getDouble("latitude");
                                Double lng = c.getDouble("longitude");
                                String id = c.getString("id");

                                mMyMarkersArray.add(new MyMarker(title, image, harga, alamat, tipe, lat, lng, id));

                            }


                            mMap.clear();
                            if (mMyMarkersArray.size() > 0) {

                                plotMarkers(mMyMarkersArray);
                                LatLng myCoordinates = new LatLng(mMyMarkersArray.get(mMyMarkersArray.size()-1).getmLatitude(), mMyMarkersArray.get(mMyMarkersArray.size()-1).getmLongitude());
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(myCoordinates)      // Sets the center of the map to LatLng (refer to previous snippet)
                                        .zoom(8)                   // Sets the zoom 17
                                        .bearing(0)                // Sets the orientation of the camera to east
                                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }else {
                                Toast.makeText(getApplicationContext(),"Data tidak ditemukan",Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("paper", "Error: " + error.getMessage());
                pDialog.hide();


                Log.v("Jumlah Erorr", "Erorr ");


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("loc", ac_location.getText().toString());

                Log.v("ParamSearch", params.toString());
                return params;
            }

        };
        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

