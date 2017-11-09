package com.apartment.swamedia.sewaapartemenbandung.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.ManagementProperty;
import com.apartment.swamedia.sewaapartemenbandung.activity.RoundedImageView;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterProperty;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterUploadGalery;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationProperty;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationUploadGallery;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.maps.GPSTracker;
import com.apartment.swamedia.sewaapartemenbandung.maps.WorkaroundMapFragment;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.style.Theme_Translucent;

/**
 * Created by Nurul Akbar on 15/10/2015.
 */
public class FragmentNewProperty extends Fragment implements OnMapReadyCallback {


    View v;

    Context ctx;

    AppCompatSpinner spinRentType, spinProperty;


    ArrayList<HashMap<String, String>> data_fasilitas_dalam_ceklis;

    CheckBox[] arr_cb_fasil_dalam_ceklis;

    ArrayList<HashMap<String, String>> data_fasilitas_dalam;

    EditText[] arr_et_fasil_dalam;

    ArrayList<HashMap<String, String>> data_fasilitas_lainnya;

    EditText[] arr_et_fasil_lainnya;


    LinearLayout ll1, ll2, ll3, ll_new_fasil_dalam, ll_new_fasil_lainnya;

    JSONObject jobj;

    String status;


    ArrayList<HashMap<String, String>> data_tipe;

    List<String> list_tipe;

    ArrayList<HashMap<String, String>> data_property;

    List<String> list_property;

    SharedPreferences sharedPreferences;

    AppCompatButton btnCreate, btnSetLokasi, btnCancelMap, btnAddGalerry, btnSearchMap;

    String statusDetail;

    String idUser, role;

    public String getNewId;

    EditText et_judul, et_desc, et_harga, et_ukuran, et_alamat, et_latitude, et_longitude, et_search_map;

    AutoCompleteTextView et_kota;

    GoogleMap mMap;

    String stringLatitude, stringLongitude;

    LinearLayout ll_open_map, ll_galery;

    TextView tv_open_map;

    //
    TextView tv_error_galery;

    int inputMarker = 0;

    int id_gal;

    private static final int REQUEST_CODE_LIBRARY = 2;
    private static final int REQUEST_CODECAMERA = 3;

    private Bitmap bitmap;

    public int jum_upload_foto = 0;

    RecyclerView rv_galery;


    private AdapterUploadGalery adapter;

    private List<InformationUploadGallery> data_gallery;

    private static FragmentNewProperty instance = null;

    String statusFocus;

    ArrayList resultList;
//    GPSTracker gpsTracker;

    private TextInputLayout til_judul, til_harga, til_ukuran;

    TextView tv_err_rentType, tv_err_Prop;


    Marker currentMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragmentnewproperty, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedPreferences = getActivity().getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        ctx = getContext();


        inisialisasi();
        data_gallery = new ArrayList<>();

        setSpinnerRentType();
        setSpinnerProperty();
        setFasilitasDalamCeklis();
        setFasilitasDalam();
        setFasilitasLainnya();
        getGenerateId();
        setLatLong();

        et_judul.addTextChangedListener(new MyTextWatcher(et_judul));
        et_harga.addTextChangedListener(new MyTextWatcher(et_harga));
        et_ukuran.addTextChangedListener(new MyTextWatcher(et_ukuran));


        et_kota.setAdapter(new GooglePlacesAutocompleteAdapter(ctx, R.layout.list_item));

        et_kota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);

            }
        });


        LocationManager manager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (statusOfGPS) {
            if (mMap == null) {
                SupportMapFragment m = ((SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map4));
                m.getMapAsync(this);
            }
        }


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForm();

            }
        });

        tv_open_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByApiGoogle();
                ll_open_map.setVisibility(View.VISIBLE);

            }

        });

        btnSetLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_latitude.setText(stringLatitude);
                et_longitude.setText(stringLongitude);
                ll_open_map.setVisibility(View.GONE);
            }
        });

        btnCancelMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_open_map.setVisibility(View.GONE);
            }
        });

        btnAddGalerry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FragmentNewProperty.getInstance().getJum_upload_foto() == 5) {
                    Toast.makeText(ctx, "Maksimal 5 Foto", Toast.LENGTH_SHORT).show();
                } else {
                    selectImage();
                }

            }
        });

        btnSearchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByApiGoogle();
            }
        });

        spinProperty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tv_err_Prop.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinRentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tv_err_rentType.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    public void checkForm() {
        int x = 0;
        if (!validateJudul()) {
            x = x + 1;
            return;

        }
        if (!validateTipeProperty()) {
            x = x + 1;
            return;


        }
        if (!validateTipeSewa()) {
            x = x + 1;
            return;

        }
        if (!validateHarga()) {
            x = x + 1;
            return;

        }
        if (!validateUkuran()) {
            x = x + 1;
            return;

        }
        if (!validateGallery()) {
            x = x + 1;
            return;
        }

        if (x == 0) {
//            submitLogin();
            submitCreatePost();
        }

    }

    public synchronized static FragmentNewProperty getInstance() {
        if (instance == null) {
            instance = new FragmentNewProperty();
        }
        return instance;
    }

    public int getJum_upload_foto() {
        return jum_upload_foto;
    }

    public void setJum_upload_foto(int jum_upload_foto) {
        this.jum_upload_foto = jum_upload_foto;
    }

    public void setLatLong() {

        GPSTracker gpsTracker;
        gpsTracker = new GPSTracker(ctx);

        if (gpsTracker.canGetLocation()) {
            stringLatitude = String.valueOf(gpsTracker.getLatitude());

            et_latitude.setText(stringLatitude);

            stringLongitude = String.valueOf(gpsTracker.getLongitude());

            et_longitude.setText(stringLongitude);

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

    public void inisialisasi() {

        //AppCompatSpinner

        ll1 = (LinearLayout) v.findViewById(R.id.ll_new_cb1);
        ll2 = (LinearLayout) v.findViewById(R.id.ll_new_cb2);
//        ll3 = (LinearLayout) v.findViewById(R.id.ll_new_cb3);
        ll_new_fasil_dalam = (LinearLayout) v.findViewById(R.id.ll_new_fasil_dalam);
        ll_new_fasil_lainnya = (LinearLayout) v.findViewById(R.id.ll_new_fasil_lainnya);
        ll_open_map = (LinearLayout) v.findViewById(R.id.ll_map);
        ll_galery = (LinearLayout) v.findViewById(R.id.ll_galery);


        rv_galery = (RecyclerView) v.findViewById(R.id.rv_galery);


        btnCreate = (AppCompatButton) v.findViewById(R.id.btn_create);
        btnSetLokasi = (AppCompatButton) v.findViewById(R.id.btn_set_location);
        btnCancelMap = (AppCompatButton) v.findViewById(R.id.btn_cancelmap);
        btnAddGalerry = (AppCompatButton) v.findViewById(R.id.btn_add_galerry);
        btnSearchMap = (AppCompatButton) v.findViewById(R.id.btn_search_api_map);

        et_judul = (EditText) v.findViewById(R.id.et_judul);
        et_desc = (EditText) v.findViewById(R.id.et_deskripsi);
        et_harga = (EditText) v.findViewById(R.id.et_harga);
        et_ukuran = (EditText) v.findViewById(R.id.et_ukuran);
        et_alamat = (EditText) v.findViewById(R.id.et_new_alamat);
        et_kota = (AutoCompleteTextView) v.findViewById(R.id.et_new_kota);

        et_latitude = (EditText) v.findViewById(R.id.et_new_latitude);
        et_longitude = (EditText) v.findViewById(R.id.et_new_longitude);

        et_search_map = (EditText) v.findViewById(R.id.et_search_api_map);


        spinRentType = (AppCompatSpinner) v.findViewById(R.id.spin_new_rent_type);
        spinProperty = (AppCompatSpinner) v.findViewById(R.id.spin_new_type_property);

        tv_open_map = (TextView) v.findViewById(R.id.tv_open_map);
        tv_error_galery = (TextView) v.findViewById(R.id.tv_error_galery);

        tv_error_galery.setVisibility(View.GONE);

        idUser = "";
        role = "";
        statusDetail = "";

        id_gal = 0;

        statusFocus = "awal";

        til_judul = (TextInputLayout) v.findViewById(R.id.input_layout_new_judul);
        til_harga = (TextInputLayout) v.findViewById(R.id.input_layout_new_harga);
        til_ukuran = (TextInputLayout) v.findViewById(R.id.input_layout_new_ukuran);

        tv_err_rentType = (TextView) v.findViewById(R.id.tv_err_RentType);
        tv_err_Prop = (TextView) v.findViewById(R.id.tv_err_Property);


    }

    public void setSpinnerRentType() {
        String tag_json_obj = "json_obj_rent_type";
        String url = ConstantUtil.WEB_SERVICE.URL_RENT_TYPE;
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rent_type", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("message")) {
                                status = jobj.getString("message");
                                JSONObject obj = jobj.getJSONObject("data");

                                if (status.equals("Success")) {
                                    JSONArray jarr = obj
                                            .getJSONArray("tipeSewa");
                                    data_tipe = new ArrayList<HashMap<String, String>>();
                                    list_tipe = new ArrayList<String>();

                                    list_tipe.add("Pilih Tipe Sewa (Wajib)");

                                    HashMap<String, String> map_rent = new HashMap<String, String>();
                                    map_rent.put("name", "Rent Type");
                                    map_rent.put("id", "0");
                                    data_tipe.add(map_rent);

                                    for (int i = 0; i < jarr.length(); i++) {
                                        JSONObject c = jarr.getJSONObject(i);
                                        HashMap<String, String> map_renttype = new HashMap<String, String>();

                                        String name = c.getString("name");
                                        String id = c.getString("id");

                                        map_renttype.put("name", name);
                                        map_renttype.put("id", id);

                                        data_tipe.add(map_renttype);

                                        list_tipe.add(name);

                                    }

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                                            getActivity(),
                                            android.R.layout.simple_spinner_item,
                                            list_tipe);
                                    dataAdapter
                                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinRentType.setAdapter(dataAdapter);
                                    // sp_datel.setOnItemSelectedListener(new
                                    // CustomOnItemSelectedListenerWitel());

                                }
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

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void setSpinnerProperty() {
        String tag_json_obj = "json_obj_rent_type";
        String url = ConstantUtil.WEB_SERVICE.URL_PROPERTY;
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Property", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            JSONArray jarr = jobj
                                    .getJSONArray("data");

                            data_property = new ArrayList<HashMap<String, String>>();
                            list_property = new ArrayList<String>();

                            list_property.add("Pilih Jenis Properti (Wajib)");

                            HashMap<String, String> map_pro = new HashMap<String, String>();
                            map_pro.put("name", "Property");
                            map_pro.put("id", "0");
                            data_property.add(map_pro);

                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject c = jarr.getJSONObject(i);
                                HashMap<String, String> map_property = new HashMap<String, String>();

                                String name = c.getString("nama");
                                String id = c.getString("id");

                                map_property.put("name", name);
                                map_property.put("id", id);

                                data_property.add(map_property);

                                list_property.add(name);

                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                                    getActivity(),
                                    android.R.layout.simple_spinner_item,
                                    list_property);
                            dataAdapter
                                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinProperty.setAdapter(dataAdapter);
                            // sp_datel.setOnItemSelectedListener(new
                            // CustomOnItemSelectedListenerWitel());


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

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void setFasilitasDalamCeklis() {
        String tag_json_obj = "json_obj_fasilitas";
        String url = ConstantUtil.WEB_SERVICE.URL_GET_FASILITAS_DALAMCEKLIS_CREATE;

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Fasilitas Dalam Ceklis", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            JSONArray fasilDalamCeklis = jobj
                                    .getJSONArray("data");


                            data_fasilitas_dalam_ceklis = new ArrayList<HashMap<String, String>>();
                            arr_cb_fasil_dalam_ceklis = new CheckBox[fasilDalamCeklis.length()];


                            for (int i = 0; i < fasilDalamCeklis.length(); i++) {
                                JSONObject c = fasilDalamCeklis.getJSONObject(i);
                                HashMap<String, String> map_fasilitas = new HashMap<String, String>();
                                // String name = c.getString("NAME");
                                String id = c.getString("id");
                                String name = c
                                        .getString("nama");
                                arr_cb_fasil_dalam_ceklis[i] = new CheckBox(getActivity());
                                arr_cb_fasil_dalam_ceklis[i].setId(Integer.parseInt(id));
                                arr_cb_fasil_dalam_ceklis[i].setText(name);

                                map_fasilitas.put("id", id);
                                map_fasilitas.put("name", name);
                                if (i % 2 == 0) {
                                    ll1.addView(arr_cb_fasil_dalam_ceklis[i]);

                                } else {
                                    ll2.addView(arr_cb_fasil_dalam_ceklis[i]);
                                }
                                data_fasilitas_dalam_ceklis.add(map_fasilitas);
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
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void setFasilitasDalam() {
        String tag_json_obj = "json_obj_fasilitas";
        String url = ConstantUtil.WEB_SERVICE.URL_GET_FASILITAS_DALAM_CREATE;

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Fasilitas Dalam", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            JSONArray fasilDalam = jobj
                                    .getJSONArray("data");


                            data_fasilitas_dalam = new ArrayList<HashMap<String, String>>();
                            arr_et_fasil_dalam = new EditText[fasilDalam.length()];


                            for (int i = 0; i < fasilDalam.length(); i++) {
                                JSONObject c = fasilDalam.getJSONObject(i);
                                HashMap<String, String> map_fasilitas = new HashMap<String, String>();
                                // String name = c.getString("NAME");
                                String id = c.getString("id");
                                String name = c
                                        .getString("nama");

                                TextInputLayout til = new TextInputLayout(getActivity());

                                ll_new_fasil_dalam.addView(til);

                                arr_et_fasil_dalam[i] = new EditText(getActivity());
                                arr_et_fasil_dalam[i].setId(Integer.parseInt(id));
                                arr_et_fasil_dalam[i].setHint(name);
                                arr_et_fasil_dalam[i].setTextSize(15);
                                arr_et_fasil_dalam[i].setInputType(InputType.TYPE_CLASS_NUMBER);

                                til.addView(arr_et_fasil_dalam[i]);

                                map_fasilitas.put("id", id);
                                map_fasilitas.put("name", name);

                                data_fasilitas_dalam.add(map_fasilitas);
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
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void setFasilitasLainnya() {
        String tag_json_obj = "json_obj_fasilitas";
        String url = ConstantUtil.WEB_SERVICE.URL_GET_FASILITAS_LAINNYA_CREATE;

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Fasilitas Lainnya", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            JSONArray fasilJarak = jobj
                                    .getJSONArray("data");


                            data_fasilitas_lainnya = new ArrayList<HashMap<String, String>>();
                            arr_et_fasil_lainnya = new EditText[fasilJarak.length()];


                            for (int i = 0; i < fasilJarak.length(); i++) {
                                JSONObject c = fasilJarak.getJSONObject(i);
                                HashMap<String, String> map_fasilitas = new HashMap<String, String>();
                                // String name = c.getString("NAME");
                                String id = c.getString("id");
                                String name = c
                                        .getString("nama");

                                TextInputLayout til = new TextInputLayout(getActivity());

                                ll_new_fasil_lainnya.addView(til);

                                arr_et_fasil_lainnya[i] = new EditText(getActivity());
                                arr_et_fasil_lainnya[i].setId(Integer.parseInt(id));
                                arr_et_fasil_lainnya[i].setHint(name);
                                arr_et_fasil_lainnya[i].setTextSize(15);
//                                arr_et_fasil_lainnya[i].setInputType(InputType.TYPE_CLASS_NUMBER);

                                til.addView(arr_et_fasil_lainnya[i]);

                                map_fasilitas.put("id", id);
                                map_fasilitas.put("name", name);

                                data_fasilitas_lainnya.add(map_fasilitas);
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
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void submitCreatePost() {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_CREATE_PROPERTY;
        idUser = sharedPreferences.getString("iduser", "");
        role = sharedPreferences.getString("role", "");

        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(ctx);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response.toString());
                        try {

                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");
                                Log.v("success", jobj.getString("success"));
                            }

                            if (status.equals("true")) {
                                message();
                            } else {
                                Toast.makeText(ctx, status, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                         }


                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Erorr", "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                JSONArray jarrA = new JSONArray();
                for (int i = 0; i < arr_et_fasil_dalam.length; i++) {
                    try {
//                        JSONArray jr = new JSONArray();
                        JSONObject obj = new JSONObject();
                        obj.put(String.valueOf(arr_et_fasil_dalam[i].getId()), arr_et_fasil_dalam[i].getText());
//                        jr.put(obj);
                        jarrA.put(i, obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONArray jarrB = new JSONArray();
                for (int i = 0; i < arr_cb_fasil_dalam_ceklis.length; i++) {
                    if (arr_cb_fasil_dalam_ceklis[i].isChecked()) {
                        String a = String.valueOf(arr_cb_fasil_dalam_ceklis[i].getId());
                        jarrB.put(a);
                    } else {
                    }
                }


                JSONArray jarrC = new JSONArray();
                for (int i = 0; i < arr_et_fasil_lainnya.length; i++) {
                    try {
//                        JSONArray jr = new JSONArray();
                        JSONObject obj = new JSONObject();
                        obj.put(String.valueOf(arr_et_fasil_lainnya[i].getId()), arr_et_fasil_lainnya[i].getText());
//                        jr.put(obj);
                        jarrC.put(i, obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    JSONObject jo = new JSONObject();
                    jo.put("userid", idUser);
                    jo.put("roleid", role);
                    jo.put("nama", et_judul.getText().toString());
                    jo.put("deskripsi", et_desc.getText().toString());
                    jo.put("harga", et_harga.getText().toString());
                    jo.put("tipesewa", data_tipe.get(spinRentType.getSelectedItemPosition()).get("id").toString());
                    jo.put("tipeproperti", data_property.get(spinProperty.getSelectedItemPosition()).get("id").toString());
                    jo.put("ukuran", et_ukuran.getText().toString());
                    jo.put("id", getNewId);
                    jo.put("alamat", et_alamat.getText().toString());
                    jo.put("kota", et_kota.getText().toString());
                    jo.put("longitude", et_longitude.getText().toString());
                    jo.put("latitude", et_latitude.getText().toString());
                    jo.put("fastType1", jarrA);
                    jo.put("fastType2", jarrB);
                    jo.put("fastType3", jarrC);


                    params.put("act", jo.toString());


                    Log.d("JSON", jo.toString());
                } catch (JSONException e) {

                }
                Log.d("param", params.toString());
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void getGenerateId() {
        String tag_json_obj = "json_obj_req";


        String url = ConstantUtil.WEB_SERVICE.URL_GET_ID_NEW_PROPERTY;

        Log.v("URL", url);

//        pDialog.setMessage("Loading...");
//        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response.toString());
                        try {

                            jobj = new JSONObject(response);
                            JSONArray jarr = jobj.getJSONArray("data");
                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject c = jarr.getJSONObject(i);
                                getNewId = c.getString("postId");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Erorr", "Error: " + error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };


// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (statusFocus.equals("awal")) {
            GPSTracker gps = new GPSTracker(ctx);

            if (gps.canGetLocation()) {
                stringLatitude = Double.toString(gps.getLatitude());
                stringLongitude = Double.toString(gps.getLongitude());
                // \n is for new line
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
        }

        LatLng myCoordinates = new LatLng(Double.parseDouble(stringLatitude), Double.parseDouble(stringLongitude));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myCoordinates)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(15)                   // Sets the zoom 17
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                LatLng l = marker.getPosition();
                stringLatitude = String.valueOf(l.latitude);
                stringLongitude = String.valueOf(l.longitude);

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                LatLng l = marker.getPosition();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                LatLng l = marker.getPosition();
                stringLatitude = String.valueOf(l.latitude);
                stringLongitude = String.valueOf(l.longitude);

            }
        });


//        btnSearchMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchByApiGoogle();
//            }
//        });

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    Intent i = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(i, REQUEST_CODECAMERA);

                } else if (items[item].equals("Choose from Library")) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, REQUEST_CODE_LIBRARY);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;


        if (requestCode == REQUEST_CODE_LIBRARY && resultCode == Activity.RESULT_OK) {
            try {
                // recyle unused bitmaps

                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = ctx.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);

                Log.v("sab", filePath);
                cursor.close();

                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap = null;
                }

                bitmap = BitmapFactory.decodeFile(filePath);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                uploadGalery(getStringBase64Bitmap(bitmap), bitmap);


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } else if (requestCode == REQUEST_CODECAMERA && resultCode == Activity.RESULT_OK) {


            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.v("Set Image", "Set");

            uploadGalery(getStringBase64Bitmap(thumbnail), thumbnail);

        }

    }

    public void uploadGalery(final String gambar, final Bitmap btmp) {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_UPLOAD_GALERY;
        idUser = sharedPreferences.getString("iduser", "");
        role = sharedPreferences.getString("role", "");

        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(ctx);
        pDialog.setMessage("Upload...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response.toString());
                        try {

                            jobj = new JSONObject(response);
                            Log.v("Response",response);

                            if (jobj.has("success")) {
                                status = jobj.getString("success");
                                Log.v("success", jobj.getString("success"));
                            }
//
                            if (status.equals("true")) {
                                JSONObject obj = jobj.getJSONObject("id_img");

                                InformationUploadGallery current = new InformationUploadGallery();
                                current.img = btmp;
                                current.id = obj.getString("id");
                                data_gallery.add(current);

                                LinearLayoutManager lm = new LinearLayoutManager(ctx);
                                lm.canScrollHorizontally();
                                lm.setOrientation(LinearLayoutManager.HORIZONTAL);
                                adapter = new AdapterUploadGalery(ctx, data_gallery);
                                rv_galery.setAdapter(adapter);
                                rv_galery.setLayoutManager(lm);

//                                jum_upload_foto = jum_upload_foto + 1;
                                int awal = FragmentNewProperty.getInstance().getJum_upload_foto();
                                int akhir = awal + 1;
                                FragmentNewProperty.getInstance().setJum_upload_foto(akhir);

                            } else {
                                Toast.makeText(ctx, status, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Erorr", "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("act", "2");
                params.put("id", getNewId);
                params.put("gallery", gambar);


                Log.d("param", params.toString());
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void searchByApiGoogle() {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_GET_SEARCH_MAP + URLEncoder.encode(et_alamat.getText().toString() + " " + et_kota.getText().toString());


        Log.v("URL", url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response.toString());
                        try {

                            jobj = new JSONObject(response);
                            JSONArray jarrResult = jobj.getJSONArray("results");
                            if (jarrResult.length() > 0) {


                                for (int i = 0; i < jarrResult.length(); i++) {
                                    JSONObject c = jarrResult.getJSONObject(i);
                                    JSONObject geometry = c.getJSONObject("geometry");
                                    JSONObject location = geometry.getJSONObject("location");

                                    stringLatitude = location.getString("lat");
                                    stringLongitude = location.getString("lng");
                                }

                                if (inputMarker == 0) {

                                        MarkerOptions markerOption = new MarkerOptions().position(new LatLng(Double.parseDouble(stringLatitude), Double.parseDouble(stringLongitude)));
                                        currentMarker = mMap.addMarker(markerOption);
                                    currentMarker.setDraggable(true);
                                } else {
                                    currentMarker.setPosition(new LatLng(Double.parseDouble(stringLatitude), Double.parseDouble(stringLongitude)));
                                }


                                inputMarker = inputMarker + 1;

                                LatLng myCoordinates = new LatLng(Double.parseDouble(stringLatitude), Double.parseDouble(stringLongitude));
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(myCoordinates)      // Sets the center of the map to LatLng (refer to previous snippet)
                                        .zoom(15)                   // Sets the zoom 17
                                        .bearing(0)                // Sets the orientation of the camera to east
                                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Erorr", "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


//                params.put("act", "2");
//                params.put("id", getNewId);
////                params.put("gallery", gambar);


                Log.d("param", params.toString());
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public static String getStringBase64Bitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[] bitmapBytes = bos.toByteArray();
        String encodedImage = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        Log.i("getStringBase64Bitmap", encodedImage);
        return encodedImage;
    }

    private boolean validateGallery() {
        if (FragmentNewProperty.getInstance().getJum_upload_foto() == 0) {
            tv_error_galery.setVisibility(View.VISIBLE);
            requestFocus(tv_error_galery);
            return false;
        } else {

        }
        return true;
    }

    private boolean validateJudul() {
        if (et_judul.getText().toString().trim().isEmpty()) {
            til_judul.setError("Judul wajib diisi!");
            requestFocus(et_judul);
            return false;
        } else {
            til_judul.setErrorEnabled(false);

        }
        return true;
    }

    private boolean validateHarga() {
        if (et_harga.getText().toString().trim().isEmpty()) {
            til_harga.setError("Harga wajib diisi!");
            requestFocus(et_harga);
            return false;
        } else {
            til_harga.setErrorEnabled(false);

        }
        return true;
    }

    private boolean validateUkuran() {
        if (et_ukuran.getText().toString().trim().isEmpty()) {
            til_ukuran.setError("Ukuran wajib diisi!");
            requestFocus(et_ukuran);
            return false;
        } else {
            til_ukuran.setErrorEnabled(false);

        }
        return true;
    }

    private boolean validateTipeSewa() {

        if (spinRentType.getSelectedItemPosition() == 0) {//Grp is your radio group object
            tv_err_rentType.setVisibility(View.VISIBLE);
            requestFocus(spinRentType);
            return false;
        } else {
            tv_err_rentType.setVisibility(View.GONE);
        }

        return true;


    }

    private boolean validateTipeProperty() {

        if (spinProperty.getSelectedItemPosition() == 0) {//Grp is your radio group object
            tv_err_Prop.setVisibility(View.VISIBLE);
            requestFocus(spinProperty);
            return false;
        } else {
            tv_err_Prop.setVisibility(View.GONE);
        }

        return true;


    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_judul:
                    validateJudul();
                    break;
                case R.id.et_ukuran:
                    validateUkuran();
                    break;
                case R.id.et_harga:
                    validateHarga();
                    break;

            }
        }
    }


    public ArrayList setAutoCompleteLocationGoogle(String input) {
        String tag_json_obj = "json_obj_location";
        String url = null;
        try {
            url = ConstantUtil.WEB_SERVICE.URL_GET_AUTO_COMPLETE + URLEncoder.encode(input, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();


        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("auto_complete", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            JSONArray jarr = jobj.getJSONArray("predictions");

                            resultList = new ArrayList(jarr.length());


                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject jsonObject = jarr.getJSONObject(i);

                                JSONArray jarr2 = jsonObject.getJSONArray("terms");

                                JSONObject c = jarr2.getJSONObject(0);

                                resultList.add(c.getString("value"));


                            }

//                            ArrayAdapter<String> aa = new ArrayAdapter<String>(ctx, android.R.layout.simple_dropdown_item_1line, arrLocation);
//                            ac_searchtext.setAdapter(aa);
//                                }
//                            }
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
            }
        });

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = setAutoCompleteLocationGoogle(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public void message() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);

        alertDialogBuilder
                .setTitle("Informasi")
                .setMessage("Properti berhasil ditambahakan")
                .setCancelable(false)
                .setPositiveButton("Tambah Lagi",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Fragment frag = new Fragment();
                                FragmentManager fragManager;
                                frag = new FragmentNewProperty();
                                fragManager = getFragmentManager();
                                fragManager.beginTransaction()
                                        .replace(R.id.frame, frag).commit();

                            }
                        })
                .setNegativeButton("Selesai", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ctx, ManagementProperty
                                .class));
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

//    public void setGaleryAwal(){
//        InformationUploadGallery current = new InformationUploadGallery();
//        current.img = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_add_to_photos_black_48dp)).getBitmap();
//        current.id = "0";
//        data_gallery.add(current);
//
//        LinearLayoutManager lm = new LinearLayoutManager(ctx);
//        lm.canScrollHorizontally();
//        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
//        adapter = new AdapterUploadGalery(ctx, data_gallery,getActivity());
//        rv_galery.setAdapter(adapter);
//        rv_galery.setLayoutManager(lm);
//    }
}



