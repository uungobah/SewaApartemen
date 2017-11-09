package com.apartment.swamedia.sewaapartemenbandung.fragment;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.ListPropertyAct;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationProperty;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nurul Akbar on 08/10/2015.
 */
public class FragmentHome extends Fragment {


    Context ctx;
    View v, v2;

    // Edit Text
    private EditText et_location, et_jum_kamar, et_harga_min, et_harga_max, et_rent_type, et_tgl_awal, et_tgl_akhir;

    // Text Input Layout
    private TextInputLayout til_location, til_jum_kamar, til_harga_min, til_harga_max, til_rent_type;

    //AppCompatButton
    private AppCompatButton btnSearch;

    //AppCompatSpinner
    private AppCompatSpinner spinRentType, spinProperty, spinKamarTidur, spinTempatTidur, spinKamarMandi;

    //AppCompatAutoComplete
    private AutoCompleteTextView ac_searchtext;

    // LinearLayout
    private LinearLayout ll_more_search;

    //TextView
    private TextView tv_more_option;

    String s_searchtext, s_tipeSewa, s_tipeProperty, s_min, s_max, s_kamarTidur, s_kamarMandi;

    JSONObject jobj;

    String status;

    ArrayList<HashMap<String, String>> data_rent_type;

    List<String> list_rent_type;

    ArrayList<HashMap<String, String>> data_property;

    List<String> list_property;


    SharedPreferences sharedPreferences;

    String[] arrLocation;

    LinearLayout ll1, ll2, ll3, ll_new_fasil_dalam, ll_new_fasil_lainnya;

//    ArrayList<HashMap<String, String>> data_fasilitas_dalam;
//
//    EditText[] arr_et_fasil_dalam;
//
//    ArrayList<HashMap<String, String>> data_fasilitas_lainnya;
//
//    EditText[] arr_et_fasil_lainnya;

    ArrayList<HashMap<String, String>> data_fasilitas_dalam_ceklis;

    CheckBox[] arr_cb_fasil_dalam_ceklis;

    int hour, minute, mYear, mMonth, mDay;

    static final int DATE_DIALOG_ID = 1;

    private String[] arrMonth = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Des"};
    private String[] arrMonth2 = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    private SliderLayout mDemoSlider;

    ArrayList resultList;

    String tglAwal,tglAkhir;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragmenthome, container, false);

        ctx = v.getContext();
        inisialisasi();

        setSpinnerRentType();
        setSpinnerProperty();
        setSpinnerKamarTidur();
        setSpinnerKamarMandi();
        setAutoCompleteLocation();

        setFasilitasDalamCeklis();

        imageSlider();

//        displayCurrency(Locale.FRANCE);

        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);

        mMonth = c.get(Calendar.MONTH);

        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Clear Data Pencarian Sebelumnya
        sharedPreferences = getActivity().getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.SEARCH, Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();

//        ac_location.addTextChangedListener(new MyTextWatcher(et_location));
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                submitForm();
            }
        });

        tv_more_option.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                visibleMoreOption();
            }
        });

        et_tgl_awal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ctx, mDateSetListener1, mYear, mMonth, mDay).show();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

//        et_tgl_awal.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                new DatePickerDialog(ctx,mDateSetListener1,mYear,mMonth,mDay).show();
////                et_tgl_awal.setShowSoftInputOnFocus(false);
//                return false;
//            }
//        });

        et_tgl_akhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ctx, mDateSetListener2, mYear, mMonth, mDay).show();
            }
        });

        // Biar Keyoard g langsung keluar
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return v;
    }
//    static public void displayCurrency( Locale currentLocale) {
//
//        Double currencyAmount = new Double(9876543.21);
//        Currency currentCurrency = Currency.getInstance(currentLocale);
//        NumberFormat currencyFormatter =
//                NumberFormat.getCurrencyInstance(currentLocale);
//
//        System.out.println(
//                currentLocale.getDisplayName() + ", " +
//                        currentCurrency.getDisplayName() + ": " +
//                        currencyFormatter.format(currencyAmount));
//    }

    public void inisialisasi() {

        mDemoSlider = (SliderLayout) v.findViewById(R.id.slider);
        // Button
        btnSearch = (AppCompatButton) v.findViewById(R.id.btn_search);
        //EditText
//        et_location = (EditText) v.findViewById(R.id.et_location);
        et_jum_kamar = (EditText) v.findViewById(R.id.et_sum);

        et_harga_max = (EditText) v.findViewById(R.id.et_max_price);
        et_harga_min = (EditText) v.findViewById(R.id.et_min_price);

        et_tgl_awal = (EditText) v.findViewById(R.id.et_tanggal_awal);
        et_tgl_awal.setInputType(InputType.TYPE_NULL);
        et_tgl_akhir = (EditText) v.findViewById(R.id.et_tanggal_akhir);
        et_tgl_akhir.setInputType(InputType.TYPE_NULL);
        // TextInputLayout
        til_location = (TextInputLayout) v.findViewById(R.id.input_layout_location);
        til_jum_kamar = (TextInputLayout) v.findViewById(R.id.input_layout_sum);
        til_harga_max = (TextInputLayout) v.findViewById(R.id.input_layout_harga_max);
        til_harga_min = (TextInputLayout) v.findViewById(R.id.input_layout_harga_min);
        //AppCompatSpinner
        spinRentType = (AppCompatSpinner) v.findViewById(R.id.spin_rent_type);
        spinProperty = (AppCompatSpinner) v.findViewById(R.id.spin_tipe_property);
        spinKamarTidur = (AppCompatSpinner) v.findViewById(R.id.spin_kamar_tidur);
        spinKamarMandi = (AppCompatSpinner) v.findViewById(R.id.spin_kamar_mandi);

        ac_searchtext = (AutoCompleteTextView) v.findViewById(R.id.ac_searchtext);

        ll_more_search = (LinearLayout) v.findViewById(R.id.ll_more_search);

        tv_more_option = (TextView) v.findViewById(R.id.tv_more_option);

        ll1 = (LinearLayout) v.findViewById(R.id.ll_home_cb1);
        ll2 = (LinearLayout) v.findViewById(R.id.ll_home_cb2);
//        ll3 = (LinearLayout) v.findViewById(R.id.ll_new_cb3);
        ll_new_fasil_dalam = (LinearLayout) v.findViewById(R.id.ll_home_fasil_dalam);


        s_searchtext = "";
        s_tipeSewa = "";
        s_tipeProperty = "";
        s_min = "";
        s_max = "";
        s_kamarTidur = "";
        s_kamarMandi = "";
        tglAwal="";
        tglAkhir="";

    }

    public void visibleMoreOption() {
        if (ll_more_search.getVisibility() == View.GONE) {
            ll_more_search.setVisibility(View.VISIBLE);
            tv_more_option.setText("Hide more options..");
        } else if (ll_more_search.getVisibility() == View.VISIBLE) {
            ll_more_search.setVisibility(View.GONE);
            tv_more_option.setText("More options..");
        }
    }

    public void submitForm() {
//        if (!validateLocation()) {
//            return;
//        } else {
        Intent i = new Intent(ctx, ListPropertyAct.class);
//        Intent i = new Intent(ctx, DetailProperty.class);

        getDataForm();

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("searchtext", s_searchtext);
        edit.putString("tipe_properti", s_tipeProperty);
        edit.putString("tipe_sewa", s_tipeSewa);
        edit.putString("date_start", tglAwal);
        edit.putString("date_end", tglAkhir);
        edit.putString("kamar_tidur", s_kamarTidur);
        edit.putString("kamar_mandi", s_kamarMandi);
        edit.putString("harga_min", s_min);
        edit.putString("harga_max", s_max);

        edit.commit();


//        edit.putString("min", s_min);
//        edit.putString("max", s_max);
//        edit.putString("kamartidur", s_kamarTidur);
//        edit.putString("tempattidur", s_tempatTidur);
//        edit.putString("status", "Search");


//            i.putExtra("lokasi", s_lokasi);
//            i.putExtra("tipesewa", s_tipeSewa);
//            i.putExtra("min", s_min);
//            i.putExtra("max", s_max);
//            i.putExtra("kamartidur", s_kamarTidur);
//            i.putExtra("tempattidur", s_tempatTidur);

        startActivity(i);
//            startActivity(new Intent(ctx, ListPropertyAct.class));
//        }
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

                                if (status.equals("Success")) {
                                    JSONObject obj = jobj.getJSONObject("data");
                                    JSONArray jarr = obj
                                            .getJSONArray("tipeSewa");
                                    data_rent_type = new ArrayList<HashMap<String, String>>();
                                    list_rent_type = new ArrayList<String>();

                                    list_rent_type.add("Pilih Tipe Sewa");

                                    HashMap<String, String> map_rent = new HashMap<String, String>();
                                    map_rent.put("name", "Rent Type");
                                    map_rent.put("id", "0");
                                    data_rent_type.add(map_rent);

                                    for (int i = 0; i < jarr.length(); i++) {
                                        JSONObject c = jarr.getJSONObject(i);
                                        HashMap<String, String> map_renttype = new HashMap<String, String>();

                                        String name = c.getString("name");
                                        String id = c.getString("id");

                                        map_renttype.put("name", name);
                                        map_renttype.put("id", id);

                                        data_rent_type.add(map_renttype);

                                        list_rent_type.add(name);

                                    }

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                                            getActivity(),
                                            android.R.layout.simple_spinner_item,
                                            list_rent_type);
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
                if(error instanceof NoConnectionError) {
                    Toast.makeText(ctx,"No internet Access, Check your internet connection.",Toast.LENGTH_SHORT).show();
                }
                pDialog.hide();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void setSpinnerKamarTidur() {
        String[] data = {"Kamar Tidur", "1", "2", "3", "4", "5"};
        ArrayAdapter<String> aa = new ArrayAdapter<String>
                (ctx, android.R.layout.simple_spinner_item, data);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Data Yang Ditampung didalam aa di masukan ke spinner
        spinKamarTidur.setAdapter(aa);
    }

    public void setSpinnerKamarMandi() {
        String[] data = {"Kamar Mandi", "1", "2", "3", "4", "5"};
        ArrayAdapter<String> aa = new ArrayAdapter<String>
                (ctx, android.R.layout.simple_spinner_item, data);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Data Yang Ditampung didalam aa di masukan ke spinner
        spinKamarMandi.setAdapter(aa);
    }


//    public void setAutoCompleteLocation() {
//        String[] arrLocation = {"Bandung utara", "Bali", "Jakarta", "Jogja"};
//        ArrayAdapter<String> aa = new ArrayAdapter<String>(ctx, android.R.layout.simple_dropdown_item_1line, arrLocation);
//        ac_location.setAdapter(aa);
//    }


    private boolean validateLocation() {
        if (ac_searchtext.getText().toString().trim().isEmpty()) {
            til_location.setError(getString(R.string.err_msg_location));
            requestFocus(ac_searchtext);
            return false;
        } else {
            til_location.setErrorEnabled(false);
            return true;
        }


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
                case R.id.ac_searchtext:
                    validateLocation();
                    break;
//                case R.id.et_login_password:
//                    validatePassword();
//                    break;

            }
        }
    }

    public void getDataForm() {
        s_searchtext = ac_searchtext.getText().toString();
//        s_tipeSewa = spinRentType.getSelectedItem().toString();
        s_max = et_harga_max.getText().toString();
        s_min = et_harga_min.getText().toString();

        if (spinKamarTidur.getSelectedItemPosition() > 0) {
            s_kamarTidur = spinKamarTidur.getSelectedItem().toString();
        } else {
            s_kamarTidur = "";
        }
        if (spinKamarMandi.getSelectedItemPosition() > 0) {
            s_kamarMandi = spinKamarMandi.getSelectedItem().toString();
        } else {
            s_kamarMandi = "";
        }

        if (spinProperty.getSelectedItemPosition() > 0) {
            s_tipeProperty = data_property.get(spinProperty.getSelectedItemPosition()).get("id");
        } else {
            s_tipeProperty = "";
        }

        if (spinRentType.getSelectedItemPosition() > 0) {
            s_tipeSewa = data_rent_type.get(spinRentType.getSelectedItemPosition()).get("id");
        } else {
            s_tipeSewa= "";
        }


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

                            list_property.add("Pilih Jenis Property");

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


    public void setAutoCompleteLocation() {
        String tag_json_obj = "json_obj_location";
        String url = ConstantUtil.WEB_SERVICE.URL_LOCATION;
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
//                            jobj = new JSONObject(response);
//                            if (jobj.has("message")) {
//                                status = jobj.getString("message");
//                                JSONObject obj = jobj.getJSONObject("data");
//
//                                if (status.equals("Success")) {
                            JSONArray jarr = new
                                    JSONArray(response);

                            arrLocation = new String[jarr.length()];
                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject c = jarr.getJSONObject(i);
                                arrLocation[i] = c.getString("name");
                            }

                            ArrayAdapter<String> aa = new ArrayAdapter<String>(ctx, android.R.layout.simple_dropdown_item_1line, arrLocation);
                            ac_searchtext.setAdapter(aa);
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
        // {
        // @Override
        // protected Map<String, String> getParams() {
        // Map<String, String> params = new HashMap<String, String>();
        //
        // return params;
        // }
        // };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            String sdate = LPad(mDay + "", "0", 2) + " " + arrMonth[mMonth] + ", " + mYear;

            tglAwal = mYear+"-"+arrMonth2[mMonth]+"-"+LPad(mDay + "", "0", 2);

            et_tgl_awal.setText(sdate);
        }
    };


    private DatePickerDialog.OnDateSetListener mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            String sdate = LPad(mDay + "", "0", 2) + " " + arrMonth[mMonth] + ", " + mYear;
            tglAkhir = mYear+"-"+arrMonth2[mMonth]+"-"+LPad(mDay + "", "0", 2);
            et_tgl_akhir.setText(sdate);
        }
    };

    public String LPad(String schar, String spad, int len) {
        String sret = schar;

        for (int i = sret.length(); i < len; i++) {
            sret = spad + sret;
        }

        return new String(sret);
    }


    public void imageSlider() {

        String tag_json_obj = "json_obj_imageslider";
        String url = ConstantUtil.WEB_SERVICE.URL_FRONT_IMAGE_SLIDER;
        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(ctx);
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

                                }
                                JSONArray jarr = jobj.getJSONArray("data");
                                final HashMap<String, String> file_maps = new HashMap<String, String>();

                                for (int i = 0; i < jarr.length(); i++) {
                                    try {
                                        int x = i + 1;
                                        JSONObject c = jarr.getJSONObject(i);
                                        file_maps.put(c.getString("kota"), c.getString("image"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                for (final String name : file_maps.keySet()) {
                                    final TextSliderView textSliderView = new TextSliderView(ctx);
                                    // initialize a SliderLayout
                                    textSliderView
                                            .description(name)
                                            .image(file_maps.get(name))
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                                @Override
                                                public void onSliderClick(BaseSliderView slider) {
                                                    Log.d("Name", textSliderView.getDescription());
                                                    Intent i = new Intent(ctx, ListPropertyAct.class);
                                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                                    edit.putString("searchtext", textSliderView.getDescription());
                                                    edit.commit();
                                                    startActivity(i);
                                                }
                                            });

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
                                mDemoSlider.addOnPageChangeListener(null);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {
            int i = 0;

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("paper", "Error: " + error.getMessage());
                pDialog.hide();
                i = i + 1;

                Log.v("Jumlah Erorr", "Erorr " + i);


            }
        });


        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}

