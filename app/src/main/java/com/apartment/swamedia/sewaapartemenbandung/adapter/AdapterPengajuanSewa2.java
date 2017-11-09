package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.DetailPembayaran;
import com.apartment.swamedia.sewaapartemenbandung.activity.DetailProperty;
import com.apartment.swamedia.sewaapartemenbandung.activity.DetailUser;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nurul Akbar on 20/11/2015.
 */
public class AdapterPengajuanSewa2 extends RecyclerView.Adapter<AdapterPengajuanSewa2.MyViewHolder> {

    private LayoutInflater inflater;
    List<InformationPengajuanSewa2> data = Collections.emptyList();
    private Context context;
    SharedPreferences sharedPreferences;
    JSONObject jobj;
    String status;

    public AdapterPengajuanSewa2(Context context, List<InformationPengajuanSewa2> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        sharedPreferences = context.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

    }


    @Override
    public AdapterPengajuanSewa2.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_pengajuan_sewa2, viewGroup, false);
        MyViewHolder hoder = new MyViewHolder(view);
        return hoder;
    }

    @Override
    public void onBindViewHolder(AdapterPengajuanSewa2.MyViewHolder holder, int i) {
        InformationPengajuanSewa2 current = data.get(i);

        holder.judul.setText("Nama properti : " + current.judul);
        holder.pemilik.setText(current.pemilik);
        holder.tanggalawal.setText("Check In : " + current.tanggalawal);
        holder.tanggalakhir.setText("Check Out : " + current.tanggalakhir);
        holder.tglpengajuan.setText("Pengajuan : " + current.tanggalpengajuan);
        holder.noorder.setText("No order : " + current.no_order);
        holder.statusPengajuan.setText("Status : " + current.status);
        holder.durasi.setText("Durasi : "+ current.durasi);

        Double harga = Double.parseDouble(current.total_harga);
        holder.totalHarga.setText("Biaya : "+ currencyRupiah(harga));


        Picasso.with(context).load(current.img).into(holder.img);

        if (!current.payId.equals("null")){
            holder.btnLanjutPembayaran.setText("Detail");
        }

//        holder.img.setImageBitmap(current.img);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView judul;
        TextView tglpengajuan;
        TextView pemilik;
        TextView tanggalawal;
        TextView tanggalakhir;
        TextView noorder;
        TextView statusPengajuan;
        TextView durasi;
        TextView totalHarga;
        ImageView img;
        AppCompatButton btnLanjutPembayaran;

        public MyViewHolder(View itemView) {

            super(itemView);
            context = itemView.getContext();
            judul = (TextView) itemView.findViewById(R.id.tv_pengajuan2_judul);
            tglpengajuan = (TextView) itemView.findViewById(R.id.tv_pengajuan2_tanggalpengajuan);
            tanggalawal = (TextView) itemView.findViewById(R.id.tv_pengajuan2_tanggalawal);
            tanggalakhir = (TextView) itemView.findViewById(R.id.tv_pengajuan2_tanggalakhir);
            pemilik = (TextView) itemView.findViewById(R.id.tv_pengajuan2_pemilik);
            noorder = (TextView) itemView.findViewById(R.id.tv_pengajuan2_noorder);
            statusPengajuan = (TextView) itemView.findViewById(R.id.tv_pengajuan2_status);
            durasi = (TextView) itemView.findViewById(R.id.tv_pengajuan2_durasi);
            totalHarga = (TextView) itemView.findViewById(R.id.tv_pengajuan2_totalharga);

            img = (ImageView) itemView.findViewById(R.id.img_picture_pemilik2);

            btnLanjutPembayaran = (AppCompatButton) itemView.findViewById(R.id.btn_lanjutkan_pembayaran);


            btnLanjutPembayaran.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailPembayaran.class);
                    InformationPengajuanSewa2 current = data.get(getPosition());
                    String id = current.id;
                    i.putExtra("idsewa", id);
                    context.startActivity(i);
                }
            });

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InformationPengajuanSewa2 c = data.get(getPosition());
                    String iduser = c.idUser;
                    Intent i = new Intent(context, DetailUser.class);
                    i.putExtra("iduser",iduser);
                    context.startActivity(i);
                }
            });
        }


        @Override
        public void onClick(View v) {
            // Log.d("Tes","Tes :"+getPosition());

//            Toast.makeText(context,"Item Clicked at :" +getPosition(),Toast.LENGTH_SHORT).show();
//            delete(getPosition());

        }

        public void actionSewa(final String id_sewa, final String aksi, final int posisi) {

            String tag_json_obj = "json_obj_list_apartment";

            String url = ConstantUtil.WEB_SERVICE.URL_ACTION_SEWA;
            Log.v("URL", url);
            final ProgressDialog pDialog = new ProgressDialog(context);
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("data", response.toString());

                            try {
                                jobj = new JSONObject(response);
                                if (jobj.has("status")) {
                                    status = jobj.getString("status");

                                    if (status.equals("T")) {
                                        delete(posisi);

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


                    params.put("action", aksi);
                    params.put("act", "2");
                    params.put("id_sewa", id_sewa);
                    params.put("comment", "");


                    Log.d("param", params.toString());
                    return params;
                }

            };


            // AppController.getInstance().getRequestQueue().getCache().remove(url);
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


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
