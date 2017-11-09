package com.apartment.swamedia.sewaapartemenbandung.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by swa on 12/15/2015.
 */
public class FragmentTab2Verifikasi extends Fragment {

    View v;
    Context ctx;

    JSONObject jobj;
    String status;

    String iduser;

    TextView tv_status_ver;
    SharedPreferences sharedPreferences;

    EditText et_nohp, et_kode;
    AppCompatButton btnKirimKode, btnVerifikasi;

    CardView cv;
    ImageView iv2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab2_verifikasi, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ctx = v.getContext();
        inisialisasi();

        sharedPreferences = ctx.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        getProfile();
        cekVerifikasi();

        btnKirimKode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_nohp.getText().toString().equals("")) {
                    Toast.makeText(ctx, "No Hp harus diisi", Toast.LENGTH_SHORT).show();
                } else {
                    kirimKode();
                }
            }
        });

        btnVerifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekKode();
            }
        });

        return v;

    }

    public void inisialisasi() {
        tv_status_ver = (TextView) v.findViewById(R.id.tv_status_vernohp);
        et_kode = (EditText) v.findViewById(R.id.et_tab2_verifikasi_kode);
        et_nohp = (EditText) v.findViewById(R.id.et_verifikasi_nohp);

        btnVerifikasi = (AppCompatButton) v.findViewById(R.id.btn_verifikasi_nohp);
        btnKirimKode = (AppCompatButton) v.findViewById(R.id.btn_verifikasi_send_code);

        cv = (CardView) v.findViewById(R.id.cv_verifikasi2);
        iv2 = (ImageView) v.findViewById(R.id.img_verifikasi2);
    }

    public void cekVerifikasi() {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_POST_CEK_VERIFIKASI;
        iduser = sharedPreferences.getString("iduser", "");
        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(ctx);
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
                            if (jobj.has("success")) {
                                status = jobj.getString("success");

                            }

                            if (status.equals("true")) {
                                JSONArray jarr = jobj.getJSONArray("data");
                                for (int i = 0; i < jarr.length(); i++) {
                                    JSONObject c = jarr.getJSONObject(i);
                                    if (c.getString("type").toString().equals("HP")) {
                                        tv_status_ver.setText("NO HP (" + c.getString("status") + ")");
                                    }
                                    if (c.get("codeStatus").toString().equals("1") && c.getString("type").toString().equals("HP")) {
                                        cv.setVisibility(View.GONE);
                                        iv2.setVisibility(View.VISIBLE);
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

    public void kirimKode() {
        String tag_json_obj = "json_obj_kirim_kode";
        String url = ConstantUtil.WEB_SERVICE.URL_KIRIM_KODE;
        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Meminta kode verifikasi..");
        pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Kirim Kode", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");
                                if (status.equals("true")) {
                                    Toast.makeText(ctx, "Permintaan terkirim", Toast.LENGTH_SHORT).show();
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

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void cekKode() {
        String tag_json_obj = "json_obj_cek_kode";
        String url = ConstantUtil.WEB_SERVICE.URL_CEK_KODE;
        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
//         pDialog.setMessage("Searching Supervisor's Data...");
//         pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Cek Kode", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");

                                if (status.equals("true")) {
                                    message(jobj.getString("message"));
                                } else {
                                    message(jobj.getString("message"));
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
                params.put("kode_sms", et_kode.getText().toString());
                params.put("phone1", et_nohp.getText().toString());

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

    public void message(String pesan) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);

        alertDialogBuilder
                .setTitle("Informasi")
                .setMessage(pesan)
                .setCancelable(false)
                .setPositiveButton("Tutup",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getProfile();
                                cekVerifikasi();
                                dialog.dismiss();

                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void getProfile() {
        String tag_json_obj = "json_obj_profile";
        String url = ConstantUtil.WEB_SERVICE.URL_PROFIL;
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
                                JSONObject obj = jobj.getJSONObject("data");

                                if (status.equals("true")) {
//                                    http://172.17.1.31:89/sab/uploads/profile/
                                    et_nohp.setText(obj.getString("hp"));
                                    et_nohp.setEnabled(false);

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

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
