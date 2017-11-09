package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterUploadGaleryEdit;
import com.apartment.swamedia.sewaapartemenbandung.adapter.CalendarAdapterEditProperty;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationMessaging;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationUploadGallery;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.maps.GPSTracker;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Nurul Akbar on 15/10/2015.
 */
public class EditProperty extends ActionBarActivity implements OnMapReadyCallback {

    View v;

    Context ctx;

    AppCompatSpinner spinRentType, spinProperty;

    ArrayList<HashMap<String, String>> data_fasilitas_dalam_ceklis;

    CheckBox[] arr_cb_fasil_dalam_ceklis;

    ArrayList<HashMap<String, String>> data_fasilitas_dalam;

    EditText[] arr_et_fasil_dalam;

    ArrayList<HashMap<String, String>> data_fasilitas_lainnya;

    EditText[] arr_et_fasil_lainnya;


    LinearLayout ll1, ll2, ll_edit_fasil_dalam, ll_edit_fasil_lainnya;

    JSONObject jobj;

    String status;


    ArrayList<HashMap<String, String>> data_tipe;

    List<String> list_tipe;

    ArrayList<HashMap<String, String>> data_property;

    List<String> list_property;

    SharedPreferences sharedPreferences;

    AppCompatButton btnEdit, btnSetLokasi, btnCancelMap, btnAddGalerry;


    String statusDetail;

    String idUser, idPost, role;

    EditText et_judul, et_desc, et_harga, et_ukuran, et_alamat, et_latitude, et_longitude,et_url;

    AutoCompleteTextView et_kota;

    GoogleMap mMap;


    LinearLayout ll_open_map, ll_galery;

    TextView tv_open_map;

    TextView tv_error_galery;


    private static final int REQUEST_CODE_LIBRARY = 2;
    private static final int REQUEST_CODECAMERA = 3;

    private Bitmap bitmap;

    public int jum_upload_foto = 0;

    RecyclerView rv_galery;


    private AdapterUploadGaleryEdit adapter;

    private List<InformationUploadGallery> data_gallery;

    private static EditProperty instance = null;

    String sLatitude, sLongitude;

    ArrayList resultList;

    String stringLatitude, stringLongitude;

    private TextInputLayout til_judul, til_harga, til_ukuran;

    TextView tv_err_rentType, tv_err_Prop;

    SupportMapFragment mapFragment;


    public GregorianCalendar month, itemmonth;// calendar instances.

    public CalendarAdapterEditProperty adapterCal;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_property);

        inisialisasi();

        data_gallery = new ArrayList<>();

        getParameter();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedPreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        setSpinnerProperty();

        setFasilitasDalam();

        setFasilitasDalamCeklis();

        setFasilitasLainnya();

//        checkGPS();

        getGalery();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForm();
            }
        });

        tv_open_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_open_map.setVisibility(View.VISIBLE);

            }

        });

        btnSetLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_latitude.setText(sLatitude);
                et_longitude.setText(sLongitude);
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
                if (EditProperty.getInstance().getJum_upload_foto() == 5) {
                    Toast.makeText(getApplicationContext(), "Maksimal 5 Foto", Toast.LENGTH_SHORT).show();
                } else {
                    selectImage();
                }

            }
        });

        et_kota.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));

        et_kota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
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


        // Untulk Kalender
        /*
        * sasasdfd
        * */

        Locale.setDefault(Locale.US);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();
        adapterCal = new CalendarAdapterEditProperty(this, month);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapterCal);

        handler = new Handler();
        handler.post(calendarUpdater);

        TextView title = (TextView) findViewById(R.id.edit_title_cal);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

//                SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");

                ((CalendarAdapterEditProperty) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapterEditProperty.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }

                if (((CalendarAdapterEditProperty) parent.getAdapter()).getCount() > 0 && items != null && items.contains(selectedGridDate)) {
                    ((CalendarAdapterEditProperty) parent.getAdapter()).setSelected(v);
                    enabled(selectedGridDate);
                    showToast(selectedGridDate);
                } else {
                    ((CalendarAdapterEditProperty) parent.getAdapter()).setSelected(v);
                    disabled(selectedGridDate);
                    showToast(selectedGridDate);
                }
            }
        });


    }


    public synchronized static EditProperty getInstance() {
        if (instance == null) {
            instance = new EditProperty();
        }
        return instance;
    }

    public int getJum_upload_foto() {
        return jum_upload_foto;
    }

    public void setJum_upload_foto(int jum_upload_foto) {
        this.jum_upload_foto = jum_upload_foto;
    }


    public void checkGPS() {

        GPSTracker gpsTracker;
        gpsTracker = new GPSTracker(getApplicationContext());

        if (gpsTracker.canGetLocation()) {

        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    public void inisialisasi() {

        //AppCompatSpinner

        ll1 = (LinearLayout) findViewById(R.id.ll_edit_cb1);
        ll2 = (LinearLayout) findViewById(R.id.ll_edit_cb2);
        ll_edit_fasil_dalam = (LinearLayout) findViewById(R.id.ll_edit_fasil_dalam);
        ll_edit_fasil_lainnya = (LinearLayout) findViewById(R.id.ll_edit_fasil_lainnya);
        ll_open_map = (LinearLayout) findViewById(R.id.ll_edit_map);
        ll_galery = (LinearLayout) findViewById(R.id.ll_edit_galery);

        rv_galery = (RecyclerView) findViewById(R.id.rv_edit_galery);

        btnEdit = (AppCompatButton) findViewById(R.id.btn_edit_property);
        btnSetLokasi = (AppCompatButton) findViewById(R.id.btn_edit_set_location);
        btnCancelMap = (AppCompatButton) findViewById(R.id.btn_edit_cancelmap);
        btnAddGalerry = (AppCompatButton) findViewById(R.id.btn_edit_add_galerry);

        et_judul = (EditText) findViewById(R.id.et_edit_judul);
        et_desc = (EditText) findViewById(R.id.et_edit_deskripsi);
        et_harga = (EditText) findViewById(R.id.et_edit_harga);
        et_ukuran = (EditText) findViewById(R.id.et_edit_ukuran);
        et_alamat = (EditText) findViewById(R.id.et_edit_alamat);
        et_kota = (AutoCompleteTextView) findViewById(R.id.et_edit_kota);
        et_latitude = (EditText) findViewById(R.id.et_edit_latitude);
        et_longitude = (EditText) findViewById(R.id.et_edit_longitude);
        et_url = (EditText) findViewById(R.id.et_edit_urlvideo);

        tv_open_map = (TextView) findViewById(R.id.tv_edit_open_map);
        tv_error_galery = (TextView) findViewById(R.id.tv_edit_error_galery);

        tv_error_galery.setVisibility(View.GONE);

        spinRentType = (AppCompatSpinner) findViewById(R.id.spin_edit_rent_type);
        spinProperty = (AppCompatSpinner) findViewById(R.id.spin_edit_type_property);

        idUser = "";
        idPost = "";
        role = "";
        statusDetail = "";
        sLatitude = "";
        sLongitude = "";

        til_judul = (TextInputLayout) findViewById(R.id.input_layout_edit_judul);
        til_harga = (TextInputLayout) findViewById(R.id.input_layout_edit_harga);
        til_ukuran = (TextInputLayout) findViewById(R.id.input_layout_edit_ukuran);

        tv_err_rentType = (TextView) findViewById(R.id.tv_err_edit_RentType);
        tv_err_Prop = (TextView) findViewById(R.id.tv_err_edit_Property);


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
            submitEditPost();
        }
    }

    public void setSpinnerRentType() {
        String tag_json_obj = "json_obj_rent_type";
        String url = ConstantUtil.WEB_SERVICE.URL_RENT_TYPE;
        final ProgressDialog pDialog = new ProgressDialog(this);

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
                                            EditProperty.this,
                                            android.R.layout.simple_spinner_item,
                                            list_tipe);

                                    dataAdapter
                                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinRentType.setAdapter(dataAdapter);


                                    getDetail();
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
        final ProgressDialog pDialog = new ProgressDialog(this);
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
                                    EditProperty.this,
                                    android.R.layout.simple_spinner_item,
                                    list_property);
                            dataAdapter
                                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinProperty.setAdapter(dataAdapter);
                            // sp_datel.setOnItemSelectedListener(new
                            // CustomOnItemSelectedListenerWitel());

                            setSpinnerRentType();

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

        final ProgressDialog pDialog = new ProgressDialog(this);
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
                                arr_cb_fasil_dalam_ceklis[i] = new CheckBox(EditProperty.this);
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

                            getDetailFasilitasDalamCeklis();

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

        final ProgressDialog pDialog = new ProgressDialog(this);
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

                                TextInputLayout til = new TextInputLayout(EditProperty.this);

                                ll_edit_fasil_dalam.addView(til);

                                arr_et_fasil_dalam[i] = new EditText(EditProperty.this);
                                arr_et_fasil_dalam[i].setId(Integer.parseInt(id));
                                arr_et_fasil_dalam[i].setHint(name);
                                arr_et_fasil_dalam[i].setTextSize(15);
                                arr_et_fasil_dalam[i].setInputType(InputType.TYPE_CLASS_NUMBER);

                                til.addView(arr_et_fasil_dalam[i]);

                                map_fasilitas.put("id", id);
                                map_fasilitas.put("name", name);

                                data_fasilitas_dalam.add(map_fasilitas);


                            }

                            getDetailFasilitasDalam();

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

        final ProgressDialog pDialog = new ProgressDialog(getApplicationContext());
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
                                String id = c.getString("id");
                                String name = c
                                        .getString("nama");

                                TextInputLayout til = new TextInputLayout(EditProperty.this);

                                ll_edit_fasil_lainnya.addView(til);

                                arr_et_fasil_lainnya[i] = new EditText(EditProperty.this);
                                arr_et_fasil_lainnya[i].setId(Integer.parseInt(id));
                                arr_et_fasil_lainnya[i].setHint(name);
                                arr_et_fasil_lainnya[i].setTextSize(15);

                                til.addView(arr_et_fasil_lainnya[i]);

                                map_fasilitas.put("id", id);
                                map_fasilitas.put("name", name);

                                data_fasilitas_lainnya.add(map_fasilitas);


                            }

                            getDetailFasilitasLainnya();


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


    public void submitEditPost() {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_EDIT_PROPERTY;
        idUser = sharedPreferences.getString("iduser", "");
        role = sharedPreferences.getString("role", "");

        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(EditProperty.this);
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
                        JSONObject obj = new JSONObject();
                        obj.put(String.valueOf(arr_et_fasil_dalam[i].getId()), arr_et_fasil_dalam[i].getText());
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
                    jo.put("id", idPost);
                    jo.put("alamat", et_alamat.getText().toString());
                    jo.put("kota", et_kota.getText().toString());
                    jo.put("longitude", et_longitude.getText());
                    jo.put("latitude", et_latitude.getText());
                    jo.put("fastType1", jarrA);
                    jo.put("fastType2", jarrB);
                    jo.put("fastType3", jarrC);
                    jo.put("status", statusDetail);
                    jo.put("urlVideo", et_url.getText().toString());


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

    public void message() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Informasi")
                .setMessage("Properti berhasil diubah")
                .setCancelable(false)
                .setPositiveButton("Ubah lagi?",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Selesai", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(EditProperty.this, ManagementProperty
                                .class));
                        finish();
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void getDetail() {
        String tag_json_obj = "json_obj_req";

        idUser = sharedPreferences.getString("iduser", "");
        role = sharedPreferences.getString("role", "");

        String url = ConstantUtil.WEB_SERVICE.URL_DETAIL_PROPERTY;

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
                            if (jobj.has("success")) {
                                status = jobj.getString("success");
                            }

                            if (status.equals("true")) {
                                JSONObject c = jobj.getJSONObject("data");

                                et_alamat.setText(c.getString("postAlamat"));
                                et_kota.setText(c.getString("postKota"));
                                et_harga.setText(c.getString("postHarga"));
                                et_judul.setText(c.getString("postNama"));
                                et_desc.setText(c.getString("postDesk"));
                                et_ukuran.setText(c.getString("postUkuran"));
                                et_latitude.setText(c.getString("postLatitude"));
                                et_longitude.setText(c.getString("postLongitude"));

                                sLatitude = c.getString("postLatitude");
                                sLongitude = c.getString("postLongitude");

                                statusDetail = c.getString("statusId");


                                String idproperty = c.getString("propertiId");
                                for (int j = 0; j < data_property.size(); j++) {
                                    if (data_property.get(j).get("id").equals(idproperty)) {
                                        spinProperty.setSelection(j);
                                    }
                                }

                                String idtipe = c.getString("tipeSewaId");
                                for (int j = 0; j < data_tipe.size(); j++) {
                                    if (data_tipe.get(j).get("id").equals(idtipe)) {
                                        spinRentType.setSelection(j);
                                    }
                                }

                                for (int j = 0; j < data_fasilitas_dalam.size(); j++) {
                                    if (data_tipe.get(j).get("id").equals(idtipe)) {
                                        spinRentType.setSelection(j);
                                    }
                                }


                            }


                            // Set Map
                            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
                            if (statusOfGPS) {
                                if (mMap == null) {
                                    mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                            .findFragmentById(R.id.edit_map5);
                                    mapFragment.getMapAsync(EditProperty.this);
                                }
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
                params.put("id_post_property", idPost);
                params.put("user_id", idUser);
                params.put("role_id", role);
                params.put("act", "2");
                return params;
            }

        };


// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public void getDetailFasilitasDalam() {
        String tag_json_obj = "json_obj_fasilitas";
        String url = ConstantUtil.WEB_SERVICE.URL_GET_DETAIL_FASILITAS + idPost + "/1";

        Log.v("Detail Fasil Dalam", url);

        final ProgressDialog pDialog = new ProgressDialog(this);
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Fasilitas Dalam Detail", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            JSONArray fasilDalam = jobj
                                    .getJSONArray("data");

                            for (int i = 0; i < fasilDalam.length(); i++) {
                                JSONObject c = fasilDalam.getJSONObject(i);
                                HashMap<String, String> map_fasilitas = new HashMap<String, String>();

                                String id = c.getString("id");
                                String total = c.getString("total");
                                String name = c
                                        .getString("nama");

                                for (int j = 0; j < data_fasilitas_dalam.size(); j++) {
                                    if (id.equals(data_fasilitas_dalam.get(j).get("id"))) {
                                        arr_et_fasil_dalam[j].setText(total);
                                    }
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

    public void getDetailFasilitasDalamCeklis() {
        String tag_json_obj = "json_obj_fasilitas";
        String url = ConstantUtil.WEB_SERVICE.URL_GET_DETAIL_FASILITAS + idPost + "/2";

        final ProgressDialog pDialog = new ProgressDialog(this);
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


                            for (int i = 0; i < fasilDalamCeklis.length(); i++) {
                                JSONObject c = fasilDalamCeklis.getJSONObject(i);
                                HashMap<String, String> map_fasilitas = new HashMap<String, String>();
                                // String name = c.getString("NAME");
                                String id = c.getString("id");
                                String total = c.getString("total");
                                String name = c
                                        .getString("nama");

                                for (int j = 0; j < data_fasilitas_dalam_ceklis.size(); j++) {
                                    if (id.equals(data_fasilitas_dalam_ceklis.get(j).get("id"))) {
                                        if (total.equals("0")) {
                                            arr_cb_fasil_dalam_ceklis[j].setChecked(false);
                                        } else {
                                            arr_cb_fasil_dalam_ceklis[j].setChecked(true);
                                        }

                                    }
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

    public void getDetailFasilitasLainnya() {
        String tag_json_obj = "json_obj_fasilitas";
        String url = ConstantUtil.WEB_SERVICE.URL_GET_DETAIL_FASILITAS + idPost + "/3";

        final ProgressDialog pDialog = new ProgressDialog(this);
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Fasilitas Lainnya", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            JSONArray fasilLainnya = jobj
                                    .getJSONArray("data");


                            for (int i = 0; i < fasilLainnya.length(); i++) {
                                JSONObject c = fasilLainnya.getJSONObject(i);
                                HashMap<String, String> map_fasilitas = new HashMap<String, String>();
                                // String name = c.getString("NAME");
                                String id = c.getString("id");
                                String description = c.getString("description");
                                String name = c
                                        .getString("nama");

                                for (int j = 0; j < data_fasilitas_lainnya.size(); j++) {
                                    if (id.equals(data_fasilitas_lainnya.get(j).get("id"))) {
                                        arr_et_fasil_lainnya[j].setText(description);
                                    }
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

    private void getParameter() {
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            idPost = extra.getString("idPost");

        }

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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
        final ProgressDialog pDialog = new ProgressDialog(this);
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

                                LinearLayoutManager lm = new LinearLayoutManager(EditProperty.this);
                                lm.canScrollHorizontally();
                                lm.setOrientation(LinearLayoutManager.HORIZONTAL);
                                adapter = new AdapterUploadGaleryEdit(EditProperty.this, data_gallery);
                                rv_galery.setAdapter(adapter);
                                rv_galery.setLayoutManager(lm);

                                jum_upload_foto = jum_upload_foto + 1;
                                int awal = EditProperty.getInstance().getJum_upload_foto();
                                int akhir = awal + 1;
                                EditProperty.getInstance().setJum_upload_foto(akhir);

                            } else {
                                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
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
                params.put("id", idPost);
                params.put("gallery", gambar);


                Log.d("param", params.toString());
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getGalery() {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_LIST_GALERY + idPost;
        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Upload...");
//        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response.toString());
                        try {

                            jobj = new JSONObject(response);

                            JSONArray jarr = jobj.getJSONArray("data");
                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject obj = jarr.getJSONObject(i);
                                InformationUploadGallery current = new InformationUploadGallery();
                                current.img_url = obj.getString("postImage");
                                current.id = obj.getString("imageId");
                                data_gallery.add(current);
//                                jum_upload_foto = jum_upload_foto + 1;
                                int awal = EditProperty.getInstance().getJum_upload_foto();
                                int akhir = awal + 1;
                                EditProperty.getInstance().setJum_upload_foto(akhir);
                            }


                            LinearLayoutManager lm = new LinearLayoutManager(EditProperty.this);
                            lm.canScrollHorizontally();
                            lm.setOrientation(LinearLayoutManager.HORIZONTAL);
                            adapter = new AdapterUploadGaleryEdit(EditProperty.this, data_gallery);
                            rv_galery.setAdapter(adapter);
                            rv_galery.setLayoutManager(lm);


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
                params.put("id", idPost);
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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public void searchByApiGoogle() {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_GET_SEARCH_MAP + URLEncoder.encode(et_alamat.getText().toString() + " " + et_kota.getText().toString());

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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        MarkerOptions markerOption = new MarkerOptions().position(new LatLng(Double.parseDouble(sLatitude), Double.parseDouble(sLongitude)));
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.property));

        Marker currentMarker = mMap.addMarker(markerOption);
        currentMarker.setDraggable(true);

        LatLng myCoordinates = new LatLng(Double.parseDouble(sLatitude), Double.parseDouble(sLongitude));
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
                sLatitude = String.valueOf(l.latitude);
                sLongitude = String.valueOf(l.longitude);

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
                sLatitude = String.valueOf(l.latitude);
                sLongitude = String.valueOf(l.longitude);

            }
        });

    }

    private boolean validateGallery() {
        if (EditProperty.getInstance().getJum_upload_foto() == 0) {
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditProperty.this, ManagementProperty
                .class));
        finish();
    }

    public ArrayList setAutoCompleteLocationGoogle(String input) {
        String tag_json_obj = "json_obj_location";
        String url = null;
        try {
            url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?types=(cities)&key=AIzaSyC4vnTHkhzFB9v8nr8LTkdRmG51S8XpbGI&language=id&input=" + URLEncoder.encode(input, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final ProgressDialog pDialog = new ProgressDialog(this);
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


    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    protected void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

    }

    public void refreshCalendar() {
        TextView title = (TextView) findViewById(R.id.edit_title_cal);

        adapterCal.refreshDays();
        adapterCal.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            // Print dates of the current week
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

//            for (int i = 0; i < 7; i++) {
//                itemvalue = df.format(itemmonth.getTime());
//                itemmonth.add(GregorianCalendar.DATE, 1);
//                items.add("2016-01-12");
//                items.add("2016-01-13");
//                items.add("2016-01-14");
//                items.add("2016-01-21");
//                items.add("2016-01-22");
//                items.add("2016-01-25");
//
//            }

            String tag_json_obj = "json_obj_req";


            final List<InformationMessaging> data = new ArrayList<>();

            String url = ConstantUtil.WEB_SERVICE.URL_DISABLED_CALENDER + idPost;

            Log.d("url", url);

            StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response.toString());
                            try {
                                jobj = new JSONObject(response);

                                if (jobj.has("success")) {
                                    status = jobj.getString("success");
                                    if (status.equals("true")) {
                                        JSONArray jarr = jobj.getJSONArray("data");

                                        String itemvalue;
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject c = jarr.getJSONObject(i);
                                            itemvalue = df.format(itemmonth.getTime());
                                            itemmonth.add(GregorianCalendar.DATE, 1);
                                            items.add(c.getString("disabled_date"));

                                        }
                                    }
                                }

                                adapterCal.setItems(items);
                                adapterCal.notifyDataSetChanged();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Erorr", "Error: " + error.getMessage());
                }
            });
// Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }
    };


    public void disabled(final String tgl) {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_DISABLED_EDIT_CALENDER;

        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(EditProperty.this);
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

                            refreshCalendar();


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


                params.put("id_post",idPost);
                params.put("start",tgl);

                Log.d("param", params.toString());
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void enabled(final String tgl) {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_ENABLED_EDIT_CALENDER;

        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(EditProperty.this);
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

                            refreshCalendar();


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


                params.put("id_post",idPost);
                params.put("start",tgl);

                Log.d("param", params.toString());
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}


