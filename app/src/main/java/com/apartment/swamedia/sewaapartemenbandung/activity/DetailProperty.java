package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.MainActivity;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterKotakMasuk;
import com.apartment.swamedia.sewaapartemenbandung.adapter.CalendarAdapter;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationMessaging;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationProperty;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.apartment.swamedia.sewaapartemenbandung.welcomescreen.WelcomeScreen;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.R.style.Theme_Translucent;

/**
 * Created by Nurul Akbar on 20/10/2015.
 */


public class DetailProperty extends ActionBarActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    TextView tv_tipesewa, tv_tipe_property, tv_harga, tv_ukuran, tv_nama, tv_jalan, tv_kota, tv_desc, tv_comment;

    String id;

    ArrayList<HashMap<String, String>> data_fasilitas_dalamceklis;

    TextView[] arr_tv_fasil_dalamceklis;

    ArrayList<HashMap<String, String>> data_fasilitas_dalam;

    TextView[] arr_tv_fasil_dalam;

    ArrayList<HashMap<String, String>> data_fasilitas_lainnya;

    TextView[] arr_tv_fasil_lainnya;


    LinearLayout ll1, ll2, ll_det_fasildalam1, ll_det_fasildalam2, ll_det_fasillain1, ll_det_fasillain2;

    JSONObject jobj;

    Button booking;

    String status;

    private SliderLayout mDemoSlider;

    ImageView img_pemilik;

    TextView tv_nama_pemilik, tv_status_hp, tv_status_ktp;

    String idProp, idUser, idPemilik;

    // Untuk booking
    EditText et_checkin, et_checkout;
    TextView tv_book_ttl_hari, tv_hargabooking, tv_total_harga, tv_book_tipesewa, tv_biaya_jasa, tv_title_biayajasa;
    String sHarga;

    SharedPreferences sharedPreferences;

    int hour, minute, mYear, mMonth, mDay;

    static final int DATE_DIALOG_ID = 1;

    private String[] arrMonth = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Des"};
    private String[] arrMonth2 = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    String tglAwal, tglAkhir;

    TextView tv_err_book;

    TextInputLayout til_checkin, til_checkout;

    // Untul Kalender
    Dialog dialog;

    public GregorianCalendar month, itemmonth;// calendar instances.

    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker

    String idWaktu;

    double totalHarga;
    double totalHargaDiluarJasa;

    String tampilharga;
    String canBooking;
    int harga_awal;

    private static int SPLASH_TIME_OUT = 2000;

    double biaya_jasa;
    double persenSewa;

    String action;

    CardView cv_booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_property);

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        sharedPreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, MODE_PRIVATE);

        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);

        mMonth = c.get(Calendar.MONTH);

        mDay = c.get(Calendar.DAY_OF_MONTH);

        inisialisasi();
        getParameter();
        getDetailFasilitasDalamCeklis();
        getDetailFasilitasDalam();
        getDetailFasilitasLainnya();
        getDetailProperty();

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkLogin() == true) {
                    submitForm();
                } else {
                    messageCheckLogin();
                }


            }
        });

        img_pemilik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailProperty.this, DetailUser.class);
                i.putExtra("iduser", idUser);
                startActivity(i);
            }
        });

        et_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new DatePickerDialog(DetailProperty.this, mDateSetListener1, mYear, mMonth, mDay).show();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                popupCheckIn();

            }
        });
        et_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new DatePickerDialog(DetailProperty.this, mDateSetListener2, mYear, mMonth, mDay).show();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                popupCheckOut();

            }
        });

        tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailProperty.this, CommentActvity.class);
                i.putExtra("post_id", idProp);
                startActivity(i);
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void inisialisasi() {

        tv_tipesewa = (TextView) findViewById(R.id.tv_det_tipesewa);
        tv_tipe_property = (TextView) findViewById(R.id.tv_det_tipeproperty);
        tv_harga = (TextView) findViewById(R.id.tv_det_harga);
        tv_ukuran = (TextView) findViewById(R.id.tv_det_ukuran);
        tv_nama = (TextView) findViewById(R.id.tv_det_namaapartemen);
        tv_kota = (TextView) findViewById(R.id.tv_det_kaborkota);
        tv_desc = (TextView) findViewById(R.id.tv_det_desc);
        tv_jalan = (TextView) findViewById(R.id.tv_det_alamat);
        tv_err_book = (TextView) findViewById(R.id.tv_err_booking);
        tv_comment = (TextView) findViewById(R.id.tv_view_comment);

        ll1 = (LinearLayout) findViewById(R.id.ll_cb_1);
        ll2 = (LinearLayout) findViewById(R.id.ll_cb_2);
        ll_det_fasildalam1 = (LinearLayout) findViewById(R.id.ll_det_fasildalam1);
        ll_det_fasildalam2 = (LinearLayout) findViewById(R.id.ll_det_fasildalam2);
        ll_det_fasillain1 = (LinearLayout) findViewById(R.id.ll_det_fasillainnya1);
        ll_det_fasillain2 = (LinearLayout) findViewById(R.id.ll_det_fasillainnya2);


        booking = (Button) findViewById(R.id.btn_det_booking);

        img_pemilik = (ImageView) findViewById(R.id.img_detail_pemilik);

        tv_nama_pemilik = (TextView) findViewById(R.id.tv_det_namapemilik);
        tv_status_ktp = (TextView) findViewById(R.id.tv_det_statusktp);
        tv_status_hp = (TextView) findViewById(R.id.tv_det_statusnohp);

        //Form booking
        et_checkin = (EditText) findViewById(R.id.et_checkin);
        et_checkin.setInputType(InputType.TYPE_NULL);
        et_checkout = (EditText) findViewById(R.id.et_checkout);
        et_checkout.setInputType(InputType.TYPE_NULL);
        tv_book_ttl_hari = (TextView) findViewById(R.id.book_ttl_hari);
        tv_book_tipesewa = (TextView) findViewById(R.id.book_tipesewa);
        tv_hargabooking = (TextView) findViewById(R.id.book_biaya_sewa);
        tv_biaya_jasa = (TextView) findViewById(R.id.book_biaya_jasa);
        tv_total_harga = (TextView) findViewById(R.id.book_ttl_harga);
        tv_title_biayajasa = (TextView) findViewById(R.id.book_title_biaya_jasa);

        til_checkin = (TextInputLayout) findViewById(R.id.input_layout_checkin);
        til_checkout = (TextInputLayout) findViewById(R.id.input_layout_checkout);

        cv_booking = (CardView) findViewById(R.id.cv_booking);

        idProp = "";
        idUser = "";

        tglAwal = "";
        tglAkhir = "";
        idWaktu = "";

        totalHarga = 0.0;
        totalHargaDiluarJasa = 0.0;
        tampilharga = "";
        harga_awal = 0;

        canBooking = "";

    }

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

        }

        return super.onOptionsItemSelected(item);
    }

    private void getParameter() {
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            id = extra.getString("id");
            action = extra.getString("action");

            if (action.equals("0")) {
                booking.setVisibility(View.GONE);
                cv_booking.setVisibility(View.GONE);
            }

        }

    }


    public void getDetailProperty() {

        final List<InformationProperty> data = new ArrayList<>();
        String tag_json_obj = "json_obj_list_apartment";
        String url = ConstantUtil.WEB_SERVICE.URL_GET_DETAIL + id;
        Log.v("URL", url);



        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("data", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("message")) {
                                status = jobj.getString("message");


                                if (status.equals("Success")) {
                                    JSONObject obj = jobj.getJSONObject("data");
                                    Log.v("Masuk looping", "Masuk");

                                    tv_tipesewa.setText(obj.getString("apWaktu"));
                                    tv_tipe_property.setText(obj.getString("tipeProperti"));


                                    // Ubah format harga
                                    double harga = Double.parseDouble(obj.getString("apHarga"));
                                    tv_harga.setText(currencyRupiah(harga));
                                    tv_hargabooking.setText(currencyRupiah(harga));

                                    harga_awal = Integer.parseInt(obj.getString("apHarga"));


                                    tv_ukuran.setText(obj.getString("apUkuran"));
                                    tv_nama.setText(obj.getString("apNama"));
                                    tv_jalan.setText(obj.getString("alamat"));
                                    tv_kota.setText(obj.getString("kota"));
                                    tv_desc.setText(obj.getString("apDesk"));
                                    tv_nama_pemilik.setText(obj.getString("apUserName"));
                                    idProp = obj.getString("apId");
                                    idUser = obj.getString("apUserId");
                                    idWaktu = obj.getString("apWaktuId");


                                    JSONObject obj2 = jobj.getJSONObject("biaya");
                                    persenSewa = Double.parseDouble(obj2.getString("jasa"));

                                    tv_title_biayajasa.setText("Biaya Jasa (" + obj2.getString("jasa") + "%)");

                                    Picasso.with(getApplicationContext()).load(obj.getString("apUserFoto")).into(img_pemilik);


                                    JSONArray jarrVer = jobj.getJSONArray("verifikasi");

                                    for (int i = 0; i < jarrVer.length(); i++) {
                                        JSONObject c = jarrVer.getJSONObject(i);
                                        if (c.getString("type").toString().equals("KTP")) {
                                            tv_status_ktp.setText("KTP (" + c.getString("status") + ")");
                                        } else if (c.getString("type").toString().equals("HP")) {
                                            tv_status_hp.setText("HP (" + c.getString("status") + ")");
                                        }
                                    }

                                    JSONArray jarr = jobj.getJSONArray("gallery");
                                    HashMap<String, String> file_maps = new HashMap<String, String>();

                                    for (int i = 0; i < jarr.length(); i++) {
                                        try {
                                            int x = i + 1;
                                            JSONObject c = jarr.getJSONObject(i);
                                            file_maps.put("Gambar " + x, c.getString("img"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    for (String name : file_maps.keySet()) {
                                        TextSliderView textSliderView = new TextSliderView(DetailProperty.this);
                                        // initialize a SliderLayout
                                        textSliderView
                                                .description(name)
                                                .image(file_maps.get(name))
                                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                                .setOnSliderClickListener(DetailProperty.this);

                                        //add your extra information
                                        textSliderView.bundle(new Bundle());
                                        textSliderView.getBundle()
                                                .putString("extra", name);

                                        mDemoSlider.addSlider(textSliderView);
                                    }
                                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Fade);
                                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                                    mDemoSlider.setDuration(4000);
                                    mDemoSlider.addOnPageChangeListener(DetailProperty.this);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            int i = 0;

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("paper", "Error: " + error.getMessage());

                i = i + 1;

                Log.v("Jumlah Erorr", "Erorr " + i);


            }
        });


        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    public void getDetailFasilitasDalam() {
        String tag_json_obj = "json_obj_fasilitas";
        String url = ConstantUtil.WEB_SERVICE.URL_GET_DETAIL_FASILITAS + id + "/1";

        Log.v("Detail Fasil Dalam", url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Fasilitas Dalam Detail", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            JSONArray fasilDalam = jobj
                                    .getJSONArray("data");


                            data_fasilitas_dalam = new ArrayList<HashMap<String, String>>();
                            arr_tv_fasil_dalam = new TextView[fasilDalam.length()];

                            for (int i = 0; i < fasilDalam.length(); i++) {
                                JSONObject c = fasilDalam.getJSONObject(i);
                                HashMap<String, String> map_fasilitas = new HashMap<String, String>();

                                String id = c.getString("id");
                                String total = c.getString("total");
                                String name = c
                                        .getString("nama");

                                arr_tv_fasil_dalam[i] = new TextView(DetailProperty.this);
                                arr_tv_fasil_dalam[i].setId(Integer.parseInt(id));
                                arr_tv_fasil_dalam[i].setText(name);
                                arr_tv_fasil_dalam[i].setTextColor(getResources().getColor(R.color.colorAccent));

                                TextView tv = new TextView(DetailProperty.this);
                                tv.setText(total);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                params.setMargins(0, 10, 0, 10);


                                arr_tv_fasil_dalam[i].setLayoutParams(params);
                                tv.setLayoutParams(params);

                                ll_det_fasildalam1.addView(arr_tv_fasil_dalam[i]);
                                ll_det_fasildalam2.addView(tv);

                                map_fasilitas.put("id", id);
                                map_fasilitas.put("name", name);

                                data_fasilitas_dalam.add(map_fasilitas);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("paper", "Error: " + error.getMessage());

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getDetailFasilitasDalamCeklis() {
        String tag_json_obj = "json_obj_fasilitas";
        String url = ConstantUtil.WEB_SERVICE.URL_GET_DETAIL_FASILITAS + id + "/2";



        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Fasilitas Dalam Ceklis", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            JSONArray fasilDalamCeklis = jobj
                                    .getJSONArray("data");

                            data_fasilitas_dalamceklis = new ArrayList<HashMap<String, String>>();
                            arr_tv_fasil_dalamceklis = new TextView[fasilDalamCeklis.length()];

                            for (int i = 0; i < fasilDalamCeklis.length(); i++) {
                                JSONObject c = fasilDalamCeklis.getJSONObject(i);
                                HashMap<String, String> map_fasilitas = new HashMap<String, String>();

                                String id = c.getString("id");
                                String total = c.getString("total");
                                String name = c
                                        .getString("nama");

                                arr_tv_fasil_dalamceklis[i] = new TextView(DetailProperty.this);
                                arr_tv_fasil_dalamceklis[i].setId(Integer.parseInt(id));
                                arr_tv_fasil_dalamceklis[i].setText(name);
                                arr_tv_fasil_dalamceklis[i].setTextColor(getResources().getColor(R.color.colorAccent));


                                TextView tv = new TextView(DetailProperty.this);

                                if (total.equals("0")) {
                                    tv.setText("Tidak tersedia");
                                } else {
                                    tv.setText("Tersedia");
                                }

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                params.setMargins(0, 10, 0, 10);


                                arr_tv_fasil_dalamceklis[i].setLayoutParams(params);
                                tv.setLayoutParams(params);

                                ll1.addView(arr_tv_fasil_dalamceklis[i]);
                                ll2.addView(tv);

                                map_fasilitas.put("id", id);
                                map_fasilitas.put("name", name);

                                data_fasilitas_dalamceklis.add(map_fasilitas);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("paper", "Error: " + error.getMessage());

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getDetailFasilitasLainnya() {
        String tag_json_obj = "json_obj_fasilitas";
        String url = ConstantUtil.WEB_SERVICE.URL_GET_DETAIL_FASILITAS + id + "/3";



        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Fasilitas Lainnya", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            JSONArray fasilLainnya = jobj
                                    .getJSONArray("data");

                            data_fasilitas_lainnya = new ArrayList<HashMap<String, String>>();
                            arr_tv_fasil_lainnya = new TextView[fasilLainnya.length()];

                            for (int i = 0; i < fasilLainnya.length(); i++) {
                                JSONObject c = fasilLainnya.getJSONObject(i);
                                HashMap<String, String> map_fasilitas = new HashMap<String, String>();
                                // String name = c.getString("NAME");
                                String id = c.getString("id");
                                String description = c.getString("description");
                                String name = c
                                        .getString("nama");

                                arr_tv_fasil_lainnya[i] = new TextView(DetailProperty.this);
                                arr_tv_fasil_lainnya[i].setId(Integer.parseInt(id));
                                arr_tv_fasil_lainnya[i].setText(name);
                                arr_tv_fasil_lainnya[i].setTextColor(getResources().getColor(R.color.colorAccent));


                                TextView tv = new TextView(DetailProperty.this);
                                tv.setText(description);

                                ll_det_fasillain1.addView(arr_tv_fasil_lainnya[i]);
                                ll_det_fasillain2.addView(tv);

                                map_fasilitas.put("id", id);
                                map_fasilitas.put("name", name);

                                data_fasilitas_lainnya.add(map_fasilitas);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("paper", "Error: " + error.getMessage());

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
//        startActivity(new Intent(getApplicationContext(), ListPropertyAct.class));
        finish();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    public void bookingAct() {
        String tag_json_obj = "json_obj_booking";
        String url = ConstantUtil.WEB_SERVICE.URL_POST_BOOKING;

        final String iduser = sharedPreferences.getString("iduser", "");
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Process..");
        pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Cek KTP", response.toString());
                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("status")) {
                                status = jobj.getString("status");

                                if (status.equals("T")) {
//                                    Toast.makeText(getApplicationContext(),"Pemesanan berhasil",Toast.LENGTH_SHORT).show();
                                    message();
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("act", "2");
                params.put("user_id", iduser);
                params.put("property_id", idProp);
//                params.put("remember_me","false");
                params.put("total_harga", tampilharga);
                params.put("startDate", tglAwal);
                params.put("targetDate", tglAkhir);

                Log.d("param", params.toString());
                return params;
            }
        };
        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );


        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public long checkDurasi() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        long diff = 0;

        try {
            Date date1 = myFormat.parse(tglAwal);
            Date date2 = myFormat.parse(tglAkhir);
            diff = date2.getTime() - date1.getTime();
//            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

            if (diff > 0) {
                if (idWaktu.equals("1")) {
                    String hari = "" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    tv_book_ttl_hari.setText(hari);
                    tv_book_tipesewa.setText(" Hari");
                    int totalhari = Integer.valueOf(hari);
                    totalHargaDiluarJasa = (totalhari * harga_awal);
                    biaya_jasa = (totalHargaDiluarJasa * persenSewa) / 100;
                    tv_biaya_jasa.setText(currencyRupiah(biaya_jasa));
                    totalHarga = (totalhari * harga_awal) + biaya_jasa;
                    tv_total_harga.setText(currencyRupiah(totalHarga));


                } else if (idWaktu.equals("2")) {
                    String totalhari = "" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    // Bulat ke atas
                    double x = Double.parseDouble(totalhari) / 31;
                    double ttlbulan = Math.ceil(x);
                    tv_book_ttl_hari.setText(String.valueOf(ttlbulan).replace(".0", ""));
                    tv_book_tipesewa.setText(" Bulan");

                    totalHargaDiluarJasa = (ttlbulan * harga_awal);
                    biaya_jasa = (totalHargaDiluarJasa * persenSewa) / 100;
                    tv_biaya_jasa.setText(currencyRupiah(biaya_jasa));
                    totalHarga = (ttlbulan * harga_awal) + biaya_jasa;
                    tv_total_harga.setText(currencyRupiah(totalHarga));

                } else if (idWaktu.equals("3")) {

                    String totalhari = "" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    double x = Double.parseDouble(totalhari) / 365;
                    double ttltahun = Math.ceil(x);
                    tv_book_ttl_hari.setText(String.valueOf(ttltahun).replace(".0", ""));
                    tv_book_tipesewa.setText(" Tahun");

                    tampilharga = String.valueOf(totalHarga).replace(".0", "");

                    totalHargaDiluarJasa = (ttltahun * harga_awal);
                    biaya_jasa = (totalHargaDiluarJasa * persenSewa) / 100;
                    tv_biaya_jasa.setText(currencyRupiah(biaya_jasa));
                    totalHarga = (ttltahun * harga_awal) + biaya_jasa;
                    tv_total_harga.setText(currencyRupiah(totalHarga));

                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return diff;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            String sdate = LPad(mDay + "", "0", 2) + " " + arrMonth[mMonth] + ", " + mYear;

            tglAwal = mYear + "-" + arrMonth2[mMonth] + "-" + LPad(mDay + "", "0", 2);

            if (checkTgl(tglAwal) == true) {
                et_checkin.setText(sdate);
                checkDurasi();
            }

        }
    };

    private DatePickerDialog.OnDateSetListener mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            String sdate = LPad(mDay + "", "0", 2) + " " + arrMonth[mMonth] + ", " + mYear;
            tglAkhir = mYear + "-" + arrMonth2[mMonth] + "-" + LPad(mDay + "", "0", 2);

            if (checkTgl(tglAkhir) == true) {
                et_checkout.setText(sdate);
                checkDurasi();
            }

        }
    };

    public String LPad(String schar, String spad, int len) {
        String sret = schar;

        for (int i = sret.length(); i < len; i++) {
            sret = spad + sret;
        }

        return new String(sret);
    }

    public void message() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Informasi")
                .setMessage("Pengajuan sewa berhasil")
                .setCancelable(false)
                .setPositiveButton("Tutup",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                resetForm();
                                dialog.dismiss();

                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public void resetForm() {
        et_checkin.setText("");
        et_checkout.setText("");
        tv_total_harga.setText("");
        tv_book_ttl_hari.setText("");
    }


    private boolean validateCheckIn() {
        if (et_checkin.getText().toString().trim().isEmpty()) {
            til_checkin.setError("Check In wajib diisi!");
            requestFocus(et_checkin);
            return false;
        } else {
            til_checkin.setErrorEnabled(false);

        }
        return true;
    }

    private boolean validateCheckOut() {
        if (et_checkout.getText().toString().trim().isEmpty()) {
            til_checkout.setError("Check Out wajib diisi!");
            requestFocus(et_checkout);
            return false;
        } else {
            til_checkout.setErrorEnabled(false);

        }
        return true;
    }

    private boolean validateCheckDurasi() {
        if (Integer.parseInt(tv_book_ttl_hari.getText().toString()) < 1) {
            tv_err_book.setVisibility(View.VISIBLE);
            tv_err_book.setText("Periksa kembali inputan tanggal anda");
            requestFocus(tv_err_book);
            return false;
        } else {
            til_checkout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void submitForm() {
        int x = 0;
        if (!validateCheckIn()) {
            x = x + 1;
            return;
        }
        if (!validateCheckOut()) {
            x = x + 1;
            return;
        }
        if (!validateCheckDurasi()) {
            x = x + 1;
            return;
        }

        if (x == 0) {
//            submitLogin();
            bookingAct();
        }

    }

    public boolean checkTgl(String tglInput) {

        boolean status = false;

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        String tglSkrg = myFormat.format(c.getTime());

        long diff = 0;
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = myFormat.parse(tglInput);
            date2 = myFormat.parse(tglSkrg);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        diff = date1.getTime() - date2.getTime();

        if (diff < 0) {
            tv_err_book.setText("Tanggal tidak valid!");
            tv_err_book.setVisibility(View.VISIBLE);
            status = false;
        } else {
            tv_err_book.setVisibility(View.GONE);
            status = true;
        }

        return status;

    }

    public boolean checkLogin() {
        String iduser = sharedPreferences.getString("iduser", "");

        boolean status = false;
        if (iduser.equals("")) {
            status = false;
        } else {
            status = true;
        }

        return status;
    }

    public void messageCheckLogin() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Informasi")
                .setMessage("Anda harus login")
                .setCancelable(false)
                .setPositiveButton("Login",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(DetailProperty.this, FormLogin.class);
                                startActivity(i);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public void popupCheckIn() {
        dialog = new Dialog(this, Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.calendar);

        Locale.setDefault(Locale.US);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();
        adapter = new CalendarAdapter(this, month);

        GridView gridview = (GridView) dialog.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        handler = new Handler();
        handler.post(calendarUpdater);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout) dialog.findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) dialog.findViewById(R.id.next);
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

                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
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
                } else if (((CalendarAdapter) parent.getAdapter()).getCount() > 0 && items != null && items.contains(selectedGridDate)) {
//                    refreshCalendar();
                } else {
                    ((CalendarAdapter) parent.getAdapter()).setSelected(v);
//                    showToast(selectedGridDate);

                    tglAwal = selectedGridDate;

                    if (checkTgl(selectedGridDate)) {
                        if (!et_checkout.getText().toString().equals("")) {
                            Log.d("Can", canBooking);


                            checkCanBooking();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {



                                    if (canBooking.equals("T")) {
                                        if (checkDurasi() < 1) {
                                            tv_err_book.setText("Tanggal tidak valid");
                                            tv_err_book.setVisibility(View.VISIBLE);

                                        } else {
                                            et_checkin.setText(tglAwal);
                                        }
                                    } else {
                                        tv_err_book.setText("Tidak bisa booking");
                                        tv_err_book.setVisibility(View.VISIBLE);
                                        et_checkin.setText("");
                                        tv_total_harga.setText("");
                                        tv_book_ttl_hari.setText("");
                                    }

                                }


                            }, SPLASH_TIME_OUT);


                        } else {
                            et_checkin.setText(selectedGridDate);
                        }

                    }
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }


    public void popupCheckOut() {
        dialog = new Dialog(this, Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.calendar);

        Locale.setDefault(Locale.US);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();
        adapter = new CalendarAdapter(this, month);

        GridView gridview = (GridView) dialog.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        handler = new Handler();
        handler.post(calendarUpdater);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout) dialog.findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) dialog.findViewById(R.id.next);
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

                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
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
                } else if (((CalendarAdapter) parent.getAdapter()).getCount() > 0 && items != null && items.contains(selectedGridDate)) {
//                    refreshCalendar();
                } else {
                    ((CalendarAdapter) parent.getAdapter()).setSelected(v);
//                    showToast(selectedGridDate);
                    tglAkhir = selectedGridDate;
                    if (checkTgl(selectedGridDate)) {
                        if (!et_checkin.getText().toString().equals("")) {


                            checkCanBooking();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Log.d("can", canBooking);
                                    if (canBooking.equals("T")) {
                                        if (checkDurasi() < 1) {
                                            tv_err_book.setText("Tanggal tidak valid");
                                            tv_err_book.setVisibility(View.VISIBLE);
                                        } else {
                                            et_checkout.setText(tglAkhir);
                                        }
                                    } else {
                                        tv_err_book.setText("Tanggal tidak tersedia");
                                        tv_err_book.setVisibility(View.VISIBLE);
                                        et_checkout.setText("");
                                        tv_total_harga.setText("");
                                        tv_book_ttl_hari.setText("");
                                    }

                                }
                            }, SPLASH_TIME_OUT);


                        } else {
                            et_checkout.setText(selectedGridDate);
                        }


                    }
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
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
        TextView title = (TextView) dialog.findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
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

            String url = ConstantUtil.WEB_SERVICE.URL_DISABLED_CALENDER + idProp;

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

                                adapter.setItems(items);
                                adapter.notifyDataSetChanged();


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

    public void checkCanBooking() {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_CHECK_TANGGAL;
        Log.v("URL", url);

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
                                if (status.equals("true")) {
                                    JSONArray jarr = jobj.getJSONArray("data");
                                    JSONObject c = jarr.getJSONObject(0);
                                    canBooking = c.getString("pout_code");
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


                params.put("postId", idProp);
                params.put("startDate", tglAwal);
                params.put("endDate", tglAkhir);
//                params.put("remember_me","false");
                params.put("act", "2");
                Log.d("params", params.toString());
                return params;
            }

        };

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
