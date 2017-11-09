package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterNotificationPaging;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterProperty;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationNotification;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationProperty;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.maps.GPSTracker;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swa on 2/24/2016.
 */
public class ListNotificationAct extends ActionBarActivity {

    SharedPreferences sharedpreferences;

    String iduser;

    List<InformationNotification> data = new ArrayList<>();

    Date then;

    private RecyclerView recyclerView;

    private AdapterNotificationPaging adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_notification);

        sharedpreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);
        iduser = sharedpreferences.getString("iduser", "");

        recyclerView = (RecyclerView) findViewById(R.id.rv_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        Toast.makeText(getApplicationContext(),dateNow().toString(),Toast.LENGTH_LONG).show();
//        Log.d("tgl",dateNow().toString());

        getNotifKotakMasuk();
    }


    public void getNotifKotakMasuk() {
        //final String data_pot_bencana = data_potensi_bencana;
        String tag_json_obj = "json_obj";
        String url = ConstantUtil.WEB_SERVICE.URL_NOTIF_PESAN;
        Log.v("URL", url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("List Notif Kotak Masuk", response.toString());

                JSONObject jobj = null;
                String title = "";
                try {
                    jobj = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response != null) {
                    try {
                        if (jobj.getString("success").equals("true")) {
                            JSONObject obj = jobj.getJSONObject("data");
                            JSONArray jarr = obj.getJSONArray("arr_data");
                            try {
                                if (jarr.length() > 0){
                                    for (int i = 0 ; i < jarr.length() ; i++) {
                                        JSONObject c = jarr.getJSONObject(i);
                                        InformationNotification current = new InformationNotification();
                                        current.idNotif = c.getString("idNotif");
                                        current.parent = c.getString("parent");
                                        current.content = c.getString("content");
                                        current.sender = c.getString("sender");
                                        current.typeTrx = c.getString("typeTrx");
                                        current.tgl = c.getString("tgl");

//                                        then = new Date(c.getString("tgl"));

//                                        Toast.makeText(getApplicationContext(), subStract(dateNow(),then),Toast.LENGTH_SHORT).show();

//                                        Log.d("subStract",subStract(dateNow(),then));

                                        data.add(current);
                                    }

                                    recyclerView.setHasFixedSize(true);
                                    adapter
                                            = new AdapterNotificationPaging(ListNotificationAct.this, data, recyclerView);
                                    recyclerView.setAdapter(adapter);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("paper", "Error: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("act", "2");
                params.put("user_id", iduser);
                params.put("typeNotif", "ALL_NOTIF");
                return params;
            }
        };
        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}

