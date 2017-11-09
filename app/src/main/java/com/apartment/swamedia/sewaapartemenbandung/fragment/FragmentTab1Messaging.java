package com.apartment.swamedia.sewaapartemenbandung.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterKotakMasuk;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterPengajuanSewa1;
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
public class FragmentTab1Messaging extends Fragment {
    private RecyclerView recyclerView;
    private AdapterKotakMasuk adapter;
    JSONObject jobj;
    SharedPreferences sharedPreferences;
    String iduser;
    String status;

    View v;
    Context ctx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab1_messaging, container, false);
        ctx = getContext();
        iduser="";

        sharedPreferences = ctx.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_kotak_masuk);

        getKotakMasuk();

        return v;
    }


    public void getKotakMasuk() {
        String tag_json_obj = "json_obj_req";

        iduser = sharedPreferences.getString("iduser", "");

        final List<InformationMessaging> data = new ArrayList<>();

        String url = ConstantUtil.WEB_SERVICE.URL_KOTAK_MASUK;

        Log.v("URL", url);
        Log.v("IdUser", iduser);

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

                                if (status.equals("true")) {

                                    JSONArray jarr = jobj
                                            .getJSONArray("data");
                                    Log.v("Masuk sukses", "Masuk");
//                                    int[] img_apartment = {R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment};
                                    for (int i = 0; i < jarr.length(); i++) {
                                        JSONObject c = jarr.getJSONObject(i);
                                        Log.v("Masuk looping", "Masuk");

                                        InformationMessaging current = new InformationMessaging();


                                        current.id = c.getString("idSubject");
                                        current.nama = c.getString("userName");
                                        current.img= c.getString("photo");
                                        current.isi = c.getString("content");
                                        current.tanggal = c.getString("sys_date");
                                        current.subject = c.getString("subjectMsg");

                                        data.add(current);

                                    }

                                    adapter = new AdapterKotakMasuk(ctx, data);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(ctx));

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
    public void onResume() {
        super.onResume();
        getKotakMasuk();
    }
}
