package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterDetailMessaging;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterKotakMasuk;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationDetailMessaging;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationMessaging;
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
 * Created by swa on 1/20/2016.
 */
public class DetailMessage extends ActionBarActivity {

    private RecyclerView recyclerView;
    private AdapterDetailMessaging adapter;
    JSONObject jobj;
    SharedPreferences sharedPreferences;
    String iduser;
    String status;

    String id_message;

    EditText et_reply;
    ImageButton btnSend;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_messaging);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        iduser = "";
        id_message = getIntent().getStringExtra("id_message");
        sharedPreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        recyclerView = (RecyclerView) findViewById(R.id.rv_detail_message);
        et_reply = (EditText) findViewById(R.id.et_message_reply);
        btnSend = (ImageButton) findViewById(R.id.btn_sendreply);

        getDetail();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyMessage();
            }
        });


    }


    public void getDetail() {
        String tag_json_obj = "json_obj_req";

        final List<InformationDetailMessaging> data = new ArrayList<>();

        String url = ConstantUtil.WEB_SERVICE.URL_DETAIL_MESSAGE;

        final ProgressDialog pDialog = new ProgressDialog(this);
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

                                if (status.equals("true")) {

                                    JSONArray jarr = jobj
                                            .getJSONArray("data");
                                    Log.v("Masuk sukses", "Masuk");
//                                    int[] img_apartment = {R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment};
                                    for (int i = 0; i < jarr.length(); i++) {
                                        JSONObject c = jarr.getJSONObject(i);
                                        Log.v("Masuk looping", "Masuk");

                                        InformationDetailMessaging current = new InformationDetailMessaging();

                                        current.id = c.getString("idMsg");
                                        current.nama = c.getString("nameUser");
                                        current.img = c.getString("photo");
                                        current.isi = c.getString("content");
                                        current.tanggal = c.getString("sys_date");
                                        current.subject = c.getString("subjectMsg");

                                        data.add(current);

                                    }

                                    adapter = new AdapterDetailMessaging(getApplicationContext(), data);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
                params.put("message_id", id_message);
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

    public void replyMessage() {
        String tag_json_obj = "json_obj_req";

        final List<InformationDetailMessaging> data = new ArrayList<>();

        String url = ConstantUtil.WEB_SERVICE.URL_REPLY_MESSAGE;

        iduser = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(this);
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

                                if (status.equals("true")) {
                                    // listChat(array);
                                    getDetail();
                                    et_reply.setText("");

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
                params.put("parent_id", id_message);
                params.put("sender_id", iduser);
                params.put("content", et_reply.getText().toString());

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
