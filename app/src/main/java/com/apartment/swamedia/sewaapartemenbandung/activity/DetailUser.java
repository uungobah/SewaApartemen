package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterPropertyUser;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationPropertyUser;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.style.Theme_Translucent;

/**
 * Created by swa on 1/13/2016.
 */
public class DetailUser extends ActionBarActivity {

    CircularImageView img;

    TextView tv_nama, tv_tgl_gabung, tv_tentang;

    ImageView iv_status_ktp, iv_status_hp;

    LinearLayout ll_property;

    JSONObject jobj;
    String status;

    String iduser, namaUser;

    RecyclerView rv;

    private List<InformationPropertyUser> data_property;

    private AdapterPropertyUser adapter;

    ImageView im_message;

    Dialog dialog;

    EditText et_ke, et_subject, et_isipesan;
    String id_receiver, id_sender;

    SharedPreferences sharedPreferences;

    AppCompatButton btn_send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_user);
        inisialisasi();
        getDetailUser();

        im_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });
        sharedPreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);
    }

    public void inisialisasi() {

        img = (CircularImageView) findViewById(R.id.img_user);

        tv_nama = (TextView) findViewById(R.id.user_nama);
        tv_tgl_gabung = (TextView) findViewById(R.id.user_tgl_bergabung);
        iv_status_hp = (ImageView) findViewById(R.id.user_status_hp);
        iv_status_ktp = (ImageView) findViewById(R.id.user_status_ktp);
        tv_tentang = (TextView) findViewById(R.id.user_about);

        im_message = (ImageView) findViewById(R.id.img_send_message);

        ll_property = (LinearLayout) findViewById(R.id.ll_property_user);

        rv = (RecyclerView) findViewById(R.id.rv_property_user);

        iduser = getIntent().getExtras().getString("iduser");
        namaUser = "";
        id_receiver = "";
        id_sender = "";
    }

    public void getDetailUser() {

        data_property = new ArrayList<>();

        String tag_json_obj = "json_obj_detailuser";
        String url = ConstantUtil.WEB_SERVICE.URL_DETAIL_USER;
        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(this);


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("data", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("message")) {
                                status = jobj.getString("message");


                                if (status.equals("Success")) {
                                    JSONObject obj = jobj.getJSONObject("data");
                                    Log.v("Masuk looping", "Masuk");

                                    tv_nama.setText("Hey, saya " + obj.getString("namauser"));
                                    namaUser = obj.getString("namauser");
                                    tv_tentang.setText(obj.getString("desk"));
                                    tv_tgl_gabung.setText("Bergabung sejak " + obj.getString("tanggaljoin"));
                                    id_receiver = obj.getString("iduser");

                                    Picasso.with(getApplicationContext()).load(obj.getString("fotouser")).into(img);


                                    JSONArray jarrVer = jobj.getJSONArray("verification");

                                    for (int i = 0; i < jarrVer.length(); i++) {
                                        JSONObject c = jarrVer.getJSONObject(i);
                                        if (c.getString("type").toString().equals("KTP")) {
                                            if (c.getString("codeStatus").equals("1")) {
                                                iv_status_ktp.setImageDrawable(getResources().getDrawable(R.drawable.check));
                                            } else {
                                                iv_status_ktp.setImageDrawable(getResources().getDrawable(R.drawable.cross));
                                            }

                                        } else if (c.getString("type").toString().equals("HP")) {
                                            if (c.getString("codeStatus").equals("1")) {
                                                iv_status_hp.setImageDrawable(getResources().getDrawable(R.drawable.check));
                                            } else {
                                                iv_status_hp.setImageDrawable(getResources().getDrawable(R.drawable.cross));
                                            }
                                        }
                                    }

                                    JSONArray jarr = jobj.getJSONArray("properties");
                                    HashMap<String, String> file_properties = new HashMap<String, String>();

                                    for (int i = 0; i < jarr.length(); i++) {
                                        JSONObject objPro = jarr.getJSONObject(i);
                                        InformationPropertyUser current = new InformationPropertyUser();
                                        current.img = objPro.getString("fotoapart");
                                        current.id = objPro.getString("id");
                                        current.harga = objPro.getString("harga");
                                        current.nama = objPro.getString("namaapart");
                                        data_property.add(current);
                                    }

                                    LinearLayoutManager lm = new LinearLayoutManager(DetailUser.this);
                                    lm.canScrollHorizontally();
                                    lm.setOrientation(LinearLayoutManager.HORIZONTAL);
                                    adapter = new AdapterPropertyUser(DetailUser.this, data_property);
                                    rv.setAdapter(adapter);
                                    rv.setLayoutManager(lm);

                                }
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("iduser", iduser);

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
        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    public void popup() {
        dialog = new Dialog(this, Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.popup_send_message);

        et_ke = (EditText) dialog.findViewById(R.id.et_message_tujuan);
        et_subject = (EditText) dialog.findViewById(R.id.et_message_subject);
        et_isipesan = (EditText) dialog.findViewById(R.id.et_message_isipesan);
        btn_send = (AppCompatButton) dialog.findViewById(R.id.btn_send_message);

        et_ke.setText(namaUser);
        et_ke.setEnabled(false);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        dialog.show();
    }

    public void sendMessage() {

        data_property = new ArrayList<>();
        id_sender = sharedPreferences.getString("iduser", "");

        String tag_json_obj = "json_obj_message";
        String url = ConstantUtil.WEB_SERVICE.URL_SEND_MESSAGE;
        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Mengirim pesan..");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("data", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");


                                if (status.equals("true")) {
                                    message();
                                    et_isipesan.setText("");
                                    et_subject.setText("");

                                }
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("subject", et_subject.getText().toString());
                params.put("content", et_isipesan.getText().toString());
                params.put("idSender", id_sender);
                params.put("idReceiver", id_receiver);
                params.put("parent", "0");
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

    public void message() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Informasi")
                .setMessage("Pesan berhasil dikirim")
                .setCancelable(false)
                .setPositiveButton("Tutup",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog2, int id) {
                                dialog2.dismiss();
                                dialog.dismiss();
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
