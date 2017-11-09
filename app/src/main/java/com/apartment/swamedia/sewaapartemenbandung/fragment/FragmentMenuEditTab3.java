package com.apartment.swamedia.sewaapartemenbandung.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.EditRekening;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterRekening;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationRekening;
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
 * Created by Nurul Akbar on 02/12/2015.
 */
public class FragmentMenuEditTab3 extends Fragment {
    View v;

    Context ctx;

    RecyclerView recyclerView;

    SharedPreferences sharedPreferences;

    JSONObject jobj;
    String status;

    List<InformationRekening> data ;

    private AdapterRekening adapter;

    AppCompatButton btnAdd,btnSave;

    ArrayList<HashMap<String, String>> data_rek;

    List<String> list_rek;

    AppCompatSpinner spinRek;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.tab3_edit_profil, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ctx = getContext();
        sharedPreferences = getActivity().getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        btnAdd = (AppCompatButton) v.findViewById(R.id.btn_addrekening);
        btnSave = (AppCompatButton) v.findViewById(R.id.btn_simpan_rekutama);
        spinRek = (AppCompatSpinner) v.findViewById(R.id.spin_rek);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_rekening);

        getRekening();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, EditRekening.class);
                i.putExtra("aksi", "Tambah");
                startActivity(i);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idrek = data_rek.get(spinRek.getSelectedItemPosition()).get("id");
                saveUtama(idrek);
            }
        });

        return v;
    }


    public void getRekening() {
        String tag_json_obj = "json_obj_profile_rekening";
        String url = ConstantUtil.WEB_SERVICE.URL_PROFIL_REKENING;


        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();
        

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("List Rekening", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");
                                JSONArray jarr = jobj.getJSONArray("data");

                                if (status.equals("true")) {

                                    data_rek = new ArrayList<HashMap<String, String>>();
                                    list_rek = new ArrayList<String>();

                                    list_rek.add("Pilih Rekening Utama");

                                    HashMap<String, String> map_rent = new HashMap<String, String>();
                                    map_rent.put("name", "Rekening");
                                    map_rent.put("id", "0");
                                    data_rek.add(map_rent);

                                    data = new ArrayList<>();
                                    for (int i = 0; i < jarr.length(); i++) {
                                        JSONObject c = jarr.getJSONObject(i);
                                        InformationRekening current = new InformationRekening();
                                        current.namabank = c.getString("bank");
                                        current.nama_pemilik = c.getString("nama_rekening");
                                        current.norek = c.getString("no_rekening");
                                        current.cabang = c.getString("cabang");
                                        current.idrek = c.getString("id_rek");
                                        current.logo = c.getString("logo");
                                        data.add(current);


                                        String name = c.getString("no_rekening")+","+c.getString("bank");
                                        String id = c.getString("id_rek");

                                        HashMap<String, String> map_rek = new HashMap<String, String>();

                                        map_rek.put("name", name);
                                        map_rek.put("id", id);

                                        data_rek.add(map_rek);

                                        list_rek.add(name);
                                    }

                                    adapter = new AdapterRekening(ctx, data);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(ctx));

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                                            getActivity(),
                                            R.layout.list_item,
                                            list_rek);
                                    dataAdapter
                                            .setDropDownViewResource(R.layout.list_item);
                                    spinRek.setAdapter(dataAdapter);

                                    setUtama();

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

                params.put("user_id", id_user);

//                params.put("remember_me","false");
                params.put("act", "2");

                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void setUtama() {
        String tag_json_obj = "json_obj_profile_rekening";
        String url = ConstantUtil.WEB_SERVICE.URL_SET_REK_UTAMA;


        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("List Bank", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");
                                JSONObject c = jobj.getJSONObject("data");

                                if (status.equals("true")) {
                                        for (int j = 0 ; j< data_rek.size() ; j++){
                                            if (c.getString("id_rek").equals(data_rek.get(j).get("id"))){
                                                spinRek.setSelection(j);
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", id_user);

//                params.put("remember_me","false");
                params.put("act", "2");

                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void saveUtama(final String id_rek) {
        String tag_json_obj = "json_obj_profile_rekening";
        String url = ConstantUtil.WEB_SERVICE.URL_SAVE_REK_UTAMA;


        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("List Bank", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");
                                if (status.equals("true")){
                                    setUtama();
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

                params.put("user_id", id_user);
                params.put("id_rek", id_rek);

//                params.put("remember_me","false");
                params.put("act", "2");

                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onResume() {
        super.onResume();
        getRekening();
    }
}
