package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterComment;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterProperty;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationComment;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationProperty;
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
 * Created by swa on 1/26/2016.
 */
public class CommentActvity extends ActionBarActivity {
    private RecyclerView recyclerView;
    private AdapterComment adapter;
    JSONObject jobj;
    SharedPreferences sharedPreferences;
    String url;
    AppCompatButton btn_kirim;
    String post_id;

    EditText et_komentar;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getParameter();

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_coment);
        et_komentar = (EditText) findViewById(R.id.et_det_komentar);
        btn_kirim = (AppCompatButton) findViewById(R.id.btn_send_comment);

        sharedPreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        getListComment();
        status ="";

        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin() == true){
                    sendComment();
                }else{
                    messageCheckLogin();
                }

            }
        });
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

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getListComment() {

        final List<InformationComment> data = new ArrayList<>();
        String tag_json_obj = "json_obj_list_apartment";


        url = ConstantUtil.WEB_SERVICE.URL_LIST_COMMENT;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("data", response.toString());
                        try {
                            jobj = new JSONObject(response);
                            JSONArray jarr = jobj
                                    .getJSONArray("data");
                            Log.v("Masuk sukses", "Masuk");
                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject c = jarr.getJSONObject(i);
                                Log.v("Masuk looping", "Masuk");
                                InformationComment current = new InformationComment();
                                current.nama = c.getString("nameUser");
                                current.img = c.getString("imageUser");
                                current.isi = c.getString("konten");
                                current.tanggal = c.getString("insertedDate");

                                data.add(current);

                            }

                            adapter = new AdapterComment(getBaseContext(), data);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

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

                params.put("post_id", post_id);
                params.put("act", "2");

                Log.v("ParamSearch", params.toString());
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    public void sendComment() {

        final List<InformationComment> data = new ArrayList<>();
        String tag_json_obj = "json_obj_list_apartment";

        final String user = sharedPreferences.getString("iduser", "");

        url = ConstantUtil.WEB_SERVICE.URL_SEND_COMMENT;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("data", response.toString());
                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")){
                                status = jobj.getString("success");
                                if (status.equals("true")){
                                    getListComment();
                                    et_komentar.setText("");
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

                params.put("post_id", post_id);
                params.put("act", "2");
                params.put("usr_id", user);
                params.put("konten", et_komentar.getText().toString());

                Log.v("ParamSearch", params.toString());
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    private void getParameter() {
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            post_id = extra.getString("post_id");

        }

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
                                Intent i = new Intent(CommentActvity.this, FormLogin.class);
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
}
