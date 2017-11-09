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
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterManagementProperty;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterManagementPropertyPaging;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterProperty;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationManagementProperty;
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
 * Created by Nurul Akbar on 05/11/2015.
 */
public class ManagementProperty extends ActionBarActivity {
    private RecyclerView recyclerView;


    private AdapterManagementPropertyPaging adapter;

    String s_lokasi, s_tipeSewa, s_min, s_max, s_kamarTidur, s_tempatTidur, status;

    JSONObject jobj;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences2;
    String iduser, role;

    int page = 1;

    List<InformationManagementProperty> data = new ArrayList<>();

    protected Handler handler;

    public ManagementProperty() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_list_property);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        recyclerView = (RecyclerView) findViewById(R.id.rv_management_apartment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        handler = new Handler();

        // Set Data
//        adapter = new AdapterManagementProperty(this,getData());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getPropertyPost();

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagementProperty.this, FormLogin.class));

            }
        });

    }


    public List<InformationManagementProperty> getData() {
        List<InformationManagementProperty> data = new ArrayList<>();


        String[] judul = {"Apartment A", "Apartment B", "Apartment C", "Apartment D", "Apartment E"};
        String[] tipe_sewa = {"Harian", "Bulanan", "Bulanan",
                "Tahunan", "Tahunan"};
        String[] harga = {"1000000", "45000000", "30000000",
                "12000000", "7000000"};


        for (int i = 0; i < judul.length && i < judul.length; i++) {
            InformationManagementProperty current = new InformationManagementProperty();

//            current.img_apartment = img_apartment[i];
            current.judul = judul[i];
            current.tipesewa = tipe_sewa[i];
            current.harga = harga[i];

            data.add(current);
        }
        return data;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
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
            page = 1;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        page = 1;
    }

    public static Bitmap getBitmapFromStringBase64(String imageString) {
        byte[] imageAsBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        return bitmap;
    }

    public void getPropertyPost() {
        String tag_json_obj = "json_obj_req";

        iduser = sharedPreferences.getString("iduser", "");
        role = sharedPreferences.getString("role", "");

        String url = ConstantUtil.WEB_SERVICE.URL_MANAGE_PROPERTY;

        Log.v("URL", url);
        Log.v("IdUser", iduser);
        Log.v("Role", role);

        final ProgressDialog pDialog = new ProgressDialog(this);
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
                            if (jobj.has("message")) {
                                status = jobj.getString("message");

                                if (status.equals("Success")) {
                                    JSONObject obj = jobj.getJSONObject("data");
                                    JSONArray jarr = obj
                                            .getJSONArray("arr_data");
                                    Log.v("Masuk sukses", "Masuk");
//                                    int[] img_apartment = {R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment};

                                    if (jarr.length() > 0) {
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject c = jarr.getJSONObject(i);
                                            Log.v("Masuk looping", "Masuk");

                                            InformationManagementProperty current = new InformationManagementProperty();

                                            current.img = c.getString("postImage");
                                            current.judul = c.getString("postName");
                                            current.tipesewa = c.getString("postTipe");
                                            current.harga = c.getString("postHarga");
                                            current.status = c.getString("postStatus");
                                            current.statusReject = c.getString("postKeterangan");
                                            current.id = c.getString("postId");

                                            data.add(current);

                                        }

                                        if (page < 2){
                                            recyclerView.setHasFixedSize(true);
                                            adapter = new AdapterManagementPropertyPaging(ManagementProperty.this, data, recyclerView);
                                            recyclerView.setAdapter(adapter);
                                        }else{
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
                VolleyLog.d("Erorr", "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("act", "2");
                params.put("user_id", iduser);
                params.put("role_id", role);
                params.put("start", String.valueOf(page));

                Log.d("params",params.toString());
                return params;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    void refreshItems() throws JSONException {
        // Load items
        // ...

        getPropertyPost();

    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        adapter.notifyDataSetChanged();

    }


}
