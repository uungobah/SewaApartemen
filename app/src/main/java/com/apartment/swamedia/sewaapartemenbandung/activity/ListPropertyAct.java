package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterProperty;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationProperty;
import com.apartment.swamedia.sewaapartemenbandung.adapter.OnLoadMoreListener;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nurul Akbar on 09/10/2015.
 */
public class ListPropertyAct extends ActionBarActivity {
    private RecyclerView recyclerView;

    private AdapterProperty adapter;

    String s_searchtext, s_tipeSewa, s_tipeProperti, s_startDate, s_endDate, s_min, s_max, s_kamarTidur, s_kamarMandi, status;

    JSONObject jobj;

    SharedPreferences sharedPreferences;

    String url;

    AppCompatButton btn_compare;

    FloatingActionButton fab;

    SwipeRefreshLayout mSwipeRefreshLayout;

    JSONArray jarrData;
    JSONObject c;

    int page = 1;

    List<InformationProperty> data = new ArrayList<>();

    protected Handler handler;

//    int offset = 5;

    public ListPropertyAct() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_property);

        recyclerView = (RecyclerView) findViewById(R.id.rv_apartment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
//        getSupportActionBar().setIcon(R.drawable.logohijau2);


        handler = new Handler();

//        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);


//        btn_compare = (AppCompatButton) findViewById(R.id.btn_compare);

        s_searchtext = "";
        s_tipeSewa = "";
        s_tipeProperti = "";
        s_startDate = "";
        s_endDate = "";
        s_min = "";
        s_max = "";
        s_kamarTidur = "";
        s_kamarMandi = "";
        url = "";


        sharedPreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.SEARCH, Context.MODE_PRIVATE);

        getParameter();
        getDataApartment();


        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListPropertyAct.this, FormLogin.class));

            }
        });


//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Refresh items
//                Log.d("Swipe", "Refreshing Number");
//                ( new Handler()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            refreshItems();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, 3000);
//            }
//
//        });

        fab = (FloatingActionButton) findViewById(R.id.fab_compare);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AdapterProperty.getInstance().getX() < 2 || AdapterProperty.getInstance().getCom1() == 0 || AdapterProperty.getInstance().getCom2() == 0) {
                    Toast.makeText(getApplicationContext(), "Ceklis 2 Apartement", Toast.LENGTH_LONG).show();

                } else {
                    Intent i = new Intent(ListPropertyAct.this, CompareAct.class);
                    Log.v("Comp 1 ", "Comp1" + AdapterProperty.getInstance().getCom1());
                    Log.v("Comp 2 ", "Comp2" + AdapterProperty.getInstance().getCom2());
                    i.putExtra("id_comp1", AdapterProperty.getInstance().getCom1());
                    i.putExtra("id_comp2", AdapterProperty.getInstance().getCom2());
                    AdapterProperty.getInstance().setCom1(0);
                    AdapterProperty.getInstance().setCom2(0);
                    startActivity(i);

                }
            }
        });
    }

    void refreshItems() throws JSONException {
        // Load items
        // ...

        getDataApartment();

//        if (output < jarrData.length()) {
//            int x = output + offset;
//            Log.d("x", "x" + x);
//            outerloop:
//            for (int i = output; i < x; i++) {
//                c = jarrData.getJSONObject(i);
//                Log.v("Masuk looping", "Masuk");
//                InformationProperty current = new InformationProperty();
//                current.img_apartment = c.getString("image");
//                current.nama_apartment = c.getString("nama");
//                current.lokasi = c.getString("alamat");
//                current.desc = c.getString("desk");
//                current.tipe_property = c.getString("tipe_properti");
//                current.tipe_sewa = c.getString("tipe_sewa");
//                current.jumlah_kamar_tidur = c.getString("kamar_tidur");
//                current.jumlah_kamar_mandi = c.getString("kamar_mandi");
//                current.harga = c.getString("harga");
//                current.id = c.getString("id");
//                data.add(current);
//                output = output + 1;
//                if (output == jarrData.length()) {
//                    break outerloop;
//                }
//            }
//
//            adapter.setLoaded();
//        }

        // Load complete

    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        adapter.notifyDataSetChanged();

//        adapter = new AdapterProperty(getBaseContext(), data);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

//        recyclerView.notify();

        // Stop refresh animation
//        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
            page = 1;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
        finish();
        page = 1;
    }

    private void getParameter() {

        s_searchtext = sharedPreferences.getString("searchtext", "");
        s_tipeProperti = sharedPreferences.getString("tipe_properti", "");
        s_tipeSewa = sharedPreferences.getString("tipe_sewa", "");
        s_startDate = sharedPreferences.getString("date_start", "");
        s_endDate = sharedPreferences.getString("date_end", "");
        s_kamarMandi = sharedPreferences.getString("kamar_mandi", "");
        s_kamarTidur = sharedPreferences.getString("kamar_tidur", "");
        s_max = sharedPreferences.getString("harga_max", "");
        s_min = sharedPreferences.getString("harga_min", "");


    }

    public void getDataApartment() {


        String tag_json_obj = "json_obj_list_apartment";


        url = ConstantUtil.WEB_SERVICE.URL_SEARCH_STANDAR;


        StringRequest jsonObjReq = new StringRequest(Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("data", response.toString());
                        try {
                            jobj = new JSONObject(response);
                            jarrData = jobj
                                    .getJSONArray("data");

                            Log.v("Masuk sukses", "Masuk");

                            outerloop:
                            if (jarrData.length() > 0) {
                                for (int i = 0; i < jarrData.length(); i++) {
                                    c = jarrData.getJSONObject(i);

                                    Log.v("Masuk looping", "Masuk");
                                    InformationProperty current = new InformationProperty();
                                    current.img_apartment = c.getString("image");
                                    current.nama_apartment = c.getString("nama");
                                    current.lokasi = c.getString("alamat");
                                    current.desc = c.getString("desk");
                                    current.tipe_property = c.getString("tipe_properti");
                                    current.tipe_sewa = c.getString("tipe_sewa");
                                    current.jumlah_kamar_tidur = c.getString("kamar_tidur");
                                    current.jumlah_kamar_mandi = c.getString("kamar_mandi");
                                    current.harga = c.getString("harga");
                                    current.id = c.getString("id");
                                    data.add(current);

//                                    output = output + 1;
//                                    Log.d("Output", "" + output);
//                                    if (output == jarrData.length()) {
//                                        break outerloop;
//                                    }
                                }

                                if (page < 2) {
                                    recyclerView.setHasFixedSize(true);
                                    adapter
                                            = new AdapterProperty(ListPropertyAct.this, data, recyclerView);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.setLoaded();
                                    onItemsLoadComplete();
                                }


                                adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        data.add(null);
                                        adapter.notifyItemInserted(data.size() - 1);

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    data.remove(data.size() - 1);
                                                    adapter.notifyItemRemoved(data.size());
                                                    refreshItems();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, 5000);
                                    }
                                });
                                page++;
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("searchtext", s_searchtext);
                params.put("tipe_properti", s_tipeProperti);
                params.put("date_start", s_startDate);
                params.put("date_end", s_endDate);
                params.put("tipe_sewa", s_tipeSewa);
                params.put("kamar_mandi", s_kamarMandi);
                params.put("kamar_tidur", s_kamarTidur);
                params.put("harga_min", s_min);
                params.put("harga_max", s_max);
                params.put("page", String.valueOf(page));

                Log.v("ParamSearch", params.toString());
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

    public static Bitmap getBitmapFromStringBase64(String imageString) {
        byte[] imageAsBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        return bitmap;
    }


}


