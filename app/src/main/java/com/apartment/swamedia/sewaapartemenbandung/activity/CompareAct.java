package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Created by Nurul Akbar on 16/10/2015.
 */
public class CompareAct extends ActionBarActivity {
    // Compare 1
    TextView tv_title1, tv_tipesewa1, tv_tipe_properti1, tv_harga1, tv_ukuran1, tv_fasil1, tv_fasillain1;

    // Compare 2
    TextView tv_title2, tv_tipesewa2, tv_tipe_properti2, tv_harga2, tv_ukuran2, tv_fasil2, tv_fasillain2;

    JSONObject jobj;

    String status;

    String id_comp1, id_comp2;

    ImageView img1, img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare);
        inisialisai();
        getParameter();
        compare();


    }

    public void inisialisai() {
        tv_title1 = (TextView) findViewById(R.id.title_compare1);
        tv_tipesewa1 = (TextView) findViewById(R.id.tipesewa_compare1);
        tv_tipe_properti1 = (TextView) findViewById(R.id.tipe_property_compare1);
        tv_harga1 = (TextView) findViewById(R.id.harga_compare1);
        tv_ukuran1 = (TextView) findViewById(R.id.ukuran_compare1);
        tv_fasil1 = (TextView) findViewById(R.id.fasilitas_compare1);
        tv_fasillain1 = (TextView) findViewById(R.id.fasilitas_laiinyya_compare1);


        tv_title2 = (TextView) findViewById(R.id.title_compare2);
        tv_tipesewa2 = (TextView) findViewById(R.id.tipesewa_compare2);
        tv_tipe_properti2 = (TextView) findViewById(R.id.tipe_property_compare2);
        tv_harga2 = (TextView) findViewById(R.id.harga_compare2);
        tv_ukuran2 = (TextView) findViewById(R.id.ukuran_compare2);
        tv_fasil2 = (TextView) findViewById(R.id.fasilitas_compare2);
        tv_fasillain2 = (TextView) findViewById(R.id.fasilitas_lainnya_compare2);

        img1 = (ImageView) findViewById(R.id.img_compare1);
        img2 = (ImageView) findViewById(R.id.img_compare2);

    }

    public void compare() {
        String tag_json_obj = "json_obj_compare";
        String url = ConstantUtil.WEB_SERVICE.URL_COMPARE;

        Log.d("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Searching Supervisor's Data...");
//        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Fasilitas", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("message")) {
                                status = jobj.getString("message");
                                JSONObject obj = jobj.getJSONObject("data");

                                if (status.equals("Success")) {
                                    JSONArray arr_data = obj
                                            .getJSONArray("arr_data");


                                    for (int i = 0; i < arr_data.length(); i++) {
                                        JSONObject c = arr_data.getJSONObject(i);

                                        if (i % 2 == 0) {
                                            tv_title1.setText(c.getString("judul"));
                                            tv_tipesewa1.setText(c.getString("tipe_sewa"));
                                            tv_harga1.setText(c.getString("harga"));
                                            tv_ukuran1.setText(c.getString("ukuran") + " M^2");
                                            tv_tipe_properti1.setText(c.getString("tipe_properti"));
                                            Picasso.with(getApplicationContext()).load(c.getString("images")).into(img1);

                                        } else {
                                            tv_title2.setText(c.getString("judul"));
                                            tv_tipesewa2.setText(c.getString("tipe_sewa"));
                                            tv_harga2.setText(c.getString("harga"));
                                            tv_ukuran2.setText(c.getString("ukuran") + " M^2");
                                            tv_tipe_properti2.setText(c.getString("tipe_properti"));
                                            Picasso.with(getApplicationContext()).load(c.getString("images")).into(img2);
                                        }
                                    }


                                    JSONObject fasilitas1 = obj
                                            .getJSONObject("fasilitas1");

                                    JSONArray fasil1 = fasilitas1.getJSONArray("fasilitas");
                                    JSONArray fasillain1 = fasilitas1.getJSONArray("fasilitas_lainnya");

                                    String s_fasil1 = "";

                                    for (int i = 0; i < fasil1.length(); i++) {
                                        JSONObject c = fasil1.getJSONObject(i);
                                        if (i != fasil1.length() - 1) {
                                            s_fasil1 = s_fasil1 + c.getString("name") + " (" + c.getString("total") + ") " + ", ";
                                        } else {
                                            s_fasil1 = s_fasil1 + c.getString("name") + " (" + c.getString("total") + ") ";
                                        }
                                    }

                                    tv_fasil1.setText(s_fasil1);

                                    String s_fasillain1 = "";

                                    for (int i = 0; i < fasillain1.length(); i++) {
                                        JSONObject c = fasillain1.getJSONObject(i);
                                        if (i != fasillain1.length() - 1) {
                                            s_fasillain1 = s_fasillain1 + c.getString("deskripsi") + "' ";
                                        } else {
                                            s_fasillain1 = s_fasillain1 + c.getString("deskripsi");
                                        }
                                    }
                                    tv_fasillain1.setText(s_fasillain1);


                                    JSONObject fasilitas2 = obj
                                            .getJSONObject("fasilitas2");
                                    JSONArray fasil2 = fasilitas2.getJSONArray("fasilitas");
                                    JSONArray fasillain2 = fasilitas2.getJSONArray("fasilitas_lainnya");


                                    String s_fasil2 = "";
                                    for (int i = 0; i < fasil2.length(); i++) {
                                        JSONObject c = fasil2.getJSONObject(i);
                                        if (i != fasil2.length() - 1) {
                                            s_fasil2 = s_fasil2 + c.getString("name") + " (" + c.getString("total") + ") "+ ", ";
                                        } else {
                                            s_fasil2 = s_fasil2 + c.getString("name") + " (" + c.getString("total") + ") ";
                                        }
                                    }

                                    tv_fasil2.setText(s_fasil2);

                                    String s_fasillain2 = "";
                                    for (int i = 0; i < fasillain2.length(); i++) {
                                        JSONObject c = fasillain2.getJSONObject(i);
                                        if (i != fasillain2.length() - 1) {
                                            s_fasillain2 = s_fasillain2 + c.getString("deskripsi") + "' ";
                                        } else {
                                            s_fasillain2 = s_fasillain2 + c.getString("deskripsi");
                                        }
                                    }
                                    tv_fasillain2.setText(s_fasillain2);


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

                params.put("comp1", id_comp1);
                params.put("comp2", id_comp2);
//                params.put("remember_me","false");
                params.put("act", "2");

                Log.v("Params", params.toString());

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

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

    private void getParameter() {
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            id_comp1 = String.valueOf(extra.getInt("id_comp1"));
            id_comp2 = String.valueOf(extra.getInt("id_comp2"));

        }

    }
}
