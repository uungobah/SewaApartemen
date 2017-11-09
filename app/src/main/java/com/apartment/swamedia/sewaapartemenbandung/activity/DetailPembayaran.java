package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationProperty;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swa on 1/7/2016.
 */
public class DetailPembayaran extends ActionBarActivity {

    TextView tv_noorder, tv_total, tv_tgl_jatuhtempo, tv_status_sewa, tv_tgl_pengajuan, tv_namaprop, tv_tgl_masuk, tv_tgl_keluar;

    SharedPreferences sharedPreferences;

    JSONObject jobj;
    String status;

    String idSewa;

    AppCompatButton btnKonfirmasi;

    LinearLayout ll_listbank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_pembayaran);
        inisialisai();
        getParameter();
        sharedPreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN,MODE_PRIVATE);
        getDetailPembayaran();
        getListBank();

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailPembayaran.this, KonfirmasiPembayaran.class);
                i.putExtra("idsewa",idSewa);
                Log.d("idSewa",idSewa);
                i.putExtra("orderid",tv_noorder.getText().toString());
                startActivity(i);

            }
        });

    }

    public void inisialisai() {
        tv_noorder = (TextView) findViewById(R.id.detailpembayaran_noorder);
        tv_tgl_jatuhtempo = (TextView) findViewById(R.id.detailpembayaran_tanggaljatuhtempo);
        tv_tgl_pengajuan = (TextView) findViewById(R.id.detailpembayaran_tanggalpengajuan);
        tv_total = (TextView) findViewById(R.id.detailpembayaran_totalpembayaran);
        tv_status_sewa = (TextView) findViewById(R.id.detailpembayaran_statuspembayaran);

        tv_namaprop = (TextView) findViewById(R.id.detailpembayaran_namaproperti);
        tv_tgl_masuk = (TextView) findViewById(R.id.detailpembayaran_tglmasuk);
        tv_tgl_keluar = (TextView) findViewById(R.id.detailpembayaran_tglkeluar);

        ll_listbank = (LinearLayout) findViewById(R.id.ll_listbankswa);

        btnKonfirmasi = (AppCompatButton) findViewById(R.id.btn_konfirmasi_pembayaran);

        idSewa = "";

    }

    public void getDetailPembayaran() {

        final List<InformationProperty> data = new ArrayList<>();
        String tag_json_obj = "json_obj_list_apartment";

        final String idUser = sharedPreferences.getString("iduser","");

        String url = ConstantUtil.WEB_SERVICE.URL_DETAIL_PEMBAYARAN;
        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(this);


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
                                    JSONArray jarr = jobj.getJSONArray("data");
                                    for (int i = 0 ; i < jarr.length() ; i++){
                                        JSONObject c = jarr.getJSONObject(i);
                                        tv_noorder.setText(c.getString("sewaNoOrder"));
                                        tv_tgl_masuk.setText(c.getString("sewaTglMasuk"));
                                        tv_tgl_keluar.setText(c.getString("sewaTglKeluar"));

                                        Double harga = Double.parseDouble(c.getString("sewaHargaTotal"));
                                        tv_total.setText(currencyRupiah(harga));


                                        tv_namaprop.setText(c.getString("postName"));
                                        tv_status_sewa.setText(c.getString("sewaStatus"));
                                        tv_tgl_pengajuan.setText(c.getString("sewaTglPemesanan"));
                                        tv_tgl_jatuhtempo.setText(c.getString("sewaTglJatuhTempo"));

                                        if (c.getString("statusButton").equals("-1")){
                                            btnKonfirmasi.setVisibility(View.GONE);
                                        }else{

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
            int i = 0;

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("paper", "Error: " + error.getMessage());
                pDialog.hide();
                i = i + 1;

                Log.v("Jumlah Erorr", "Erorr " + i);


            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("act", "2");
                params.put("user_id",idUser );
//                params.put("remember_me","false");
                params.put("sewaId",idSewa);
                return params;
            }

        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getListBank() {

        String tag_json_obj = "json_obj_list_bank_swa";
        String url = ConstantUtil.WEB_SERVICE.URL_LIST_BANK_SWA;
        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(this);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        Log.d("data", response.toString());

                        try {
                            jobj = new JSONObject(response);
                                    JSONArray jarr = jobj.getJSONArray("data");
                                    for (int i = 0 ; i < jarr.length() ; i++){
                                        JSONObject c = jarr.getJSONObject(i);

                                        Log.d("List Bank", "Msuk");


//                                        LinearLayout.LayoutParams paramsl = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                                        paramsl.setMargins(10,10,10,10);
//                                        TableRow tr = new TableRow(DetailPembayaran.this);
//                                        tr.setLayoutParams(paramsl);
//                                        tr.setOrientation(LinearLayout.VERTICAL);
//                                        ll_listbank.addView(tr);

                                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);

                                        ImageView im = new ImageView(DetailPembayaran.this);
                                        im.setScaleType(ImageView.ScaleType.FIT_START);
                                        Picasso.with(getApplicationContext()).load(c.getString("logo")).into(im);
//                                        im.setLayoutParams(param);
                                        ll_listbank.addView(im);
//
//                                        LinearLayout ll2 = new LinearLayout(DetailPembayaran.this);
////                                        ll2.setLayoutParams(param);
//                                        ll2.setOrientation(LinearLayout.VERTICAL);
//                                        tr.addView(ll2);

                                        LinearLayout.LayoutParams paramsl = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        paramsl.setMargins(0, 0, 0, 30);

                                        TextView tv1 = new TextView(DetailPembayaran.this);
                                        tv1.setText(c.getString("nameBank"));
                                        tv1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                                        tv1.setTextAppearance(android.R.style.TextAppearance_Holo_Medium_Inverse);
                                        ll_listbank.addView(tv1);

                                        TextView tv3 = new TextView(DetailPembayaran.this);
                                        tv3.setText("No rekening");
                                        tv3.setTextColor(getResources().getColor(R.color.colorAccent));
                                        ll_listbank.addView(tv3);
                                        TextView tv4 = new TextView(DetailPembayaran.this);
                                        tv4.setText(c.getString("rekeningNo"));
                                        ll_listbank.addView(tv4);
                                        TextView tv5 = new TextView(DetailPembayaran.this);
                                        tv5.setText("Atas Nama");
                                        tv5.setTextColor(getResources().getColor(R.color.colorAccent));
                                        ll_listbank.addView(tv5);
                                        TextView tv6 = new TextView(DetailPembayaran.this);
                                        tv6.setText(c.getString("bankOwnerName"));
                                        tv6.setLayoutParams(paramsl);
                                        ll_listbank.addView(tv6);




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
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void getParameter() {
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            idSewa = extra.getString("idsewa");

        }

    }

    public String currencyRupiah (double harga){
        String hasil = "";
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        hasil = kursIndonesia.format(harga);
//        System.out.printf("Harga Rupiah: %s %n", kursIndonesia.format(harga));

        return hasil;
    }


}
