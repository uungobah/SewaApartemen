package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterPermintaanSewa1;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationPermintaanSewa1;
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
 * Created by Nurul Akbar on 20/11/2015.
 */
public class PermintaanSewaAct extends ActionBarActivity {
    private RecyclerView recyclerView;



    private AdapterPermintaanSewa1 adapter;


    JSONObject jobj;

    SharedPreferences sharedPreferences;
    String iduser;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_permintaan_sewa);

        iduser="";

        sharedPreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        recyclerView = (RecyclerView) findViewById(R.id.rv_permintaan_sewa);

        getPermintaanSewa();

    }

    public void getPermintaanSewa() {
        String tag_json_obj = "json_obj_req";

        iduser = sharedPreferences.getString("iduser", "");

        final List<InformationPermintaanSewa1> data = new ArrayList<>();

        String url = ConstantUtil.WEB_SERVICE.URL_PERMINTAAN_SEWA;

        Log.v("URL", url);
        Log.v("IdUser", iduser);

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

                                        InformationPermintaanSewa1 current = new InformationPermintaanSewa1();

                                        current.penyewa = c.getString("penyewa");
                                        current.judul = c.getString("judul");
                                        current.tanggalawal = c.getString("checkin");
                                        current.tanggalakhir = c.getString("checkout");
                                        current.tanggalpengajuan = c.getString("tgl_pengajuan");
                                        current.img = c.getString("foto_profil");
                                        current.no_order = c.getString("kode_transaksi");
                                        current.id = c.getString("id");
                                        data.add(current);

                                    }

                                    adapter = new AdapterPermintaanSewa1(getBaseContext(), data);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

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

                return params;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
}
