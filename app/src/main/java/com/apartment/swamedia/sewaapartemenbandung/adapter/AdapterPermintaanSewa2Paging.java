package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
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
public class AdapterPermintaanSewa2Paging extends RecyclerView.Adapter {

    private LayoutInflater inflater;
    List<InformationPermintaanSewa2> data = Collections.emptyList();
    private Context context;
    SharedPreferences sharedPreferences;
    JSONObject jobj;
    String status;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public AdapterPermintaanSewa2Paging(Context context, List<InformationPermintaanSewa2> data, RecyclerView recyclerView) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        sharedPreferences = context.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }

                        }
                    });
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder hoder;
        if (i == VIEW_ITEM) {
            View view = inflater.inflate(R.layout.custom_permintaan_sewa2, viewGroup, false);
            hoder = new MyViewHolder(view);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progress_item, viewGroup, false);
            hoder = new ProgressViewHolder(v);
        }

        return hoder;
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return data.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hoder, int i) {

        if (hoder instanceof MyViewHolder) {
            AdapterPermintaanSewa2Paging.MyViewHolder holder = (AdapterPermintaanSewa2Paging.MyViewHolder) hoder;
            InformationPermintaanSewa2 current = data.get(i);
            holder.judul.setText("Nama properti : " + current.judul);
            holder.penyewa.setText(current.penyewa);
            holder.tanggalawal.setText("Check In : " + current.tanggalawal);
            holder.tanggalakhir.setText("Check Out : " + current.tanggalakhir);
            holder.tglpengajuan.setText("Pengajuan : " + current.tanggalpengajuan);
            holder.noorder.setText("No order : " + current.no_order);
            holder.statusPembayaran.setText("Status : " + current.status);
            holder.durasi.setText("Durasi : " + current.durasi);
            Double harga = Double.parseDouble(current.total_harga);
            holder.totalHarga.setText("Biaya : " + currencyRupiah(harga));

            Picasso.with(context).load(current.img).into(holder.img);

        } else {
            ((ProgressViewHolder) hoder).progressBar.setIndeterminate(true);
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
        TextView penyewa;
        TextView tanggalawal;
        TextView tanggalakhir;
        TextView noorder;
        TextView statusPembayaran;
        ImageView img;
        TextView durasi;
        TextView totalHarga;


        public MyViewHolder(View itemView) {

            super(itemView);
            context = itemView.getContext();
            judul = (TextView) itemView.findViewById(R.id.tv_permintaan2_judul);
            tglpengajuan = (TextView) itemView.findViewById(R.id.tv_permintaan2_tanggalpengajuan);
            tanggalawal = (TextView) itemView.findViewById(R.id.tv_permintaan2_tanggalawal);
            tanggalakhir = (TextView) itemView.findViewById(R.id.tv_permintaan2_tanggalakhir);
            penyewa = (TextView) itemView.findViewById(R.id.tv_permintaan2_penyewa);
            noorder = (TextView) itemView.findViewById(R.id.tv_permintaan2_noorder);
            statusPembayaran = (TextView) itemView.findViewById(R.id.tv_permintaan2_status);
            durasi = (TextView) itemView.findViewById(R.id.tv_permintaan2_durasi);
            totalHarga = (TextView) itemView.findViewById(R.id.tv_permintaan2_totalharga);

            img = (ImageView) itemView.findViewById(R.id.img_picture_penyewa2);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InformationPermintaanSewa2 c = data.get(getPosition());
                    String iduser = c.idUser;
                    Intent i = new Intent(context, DetailUser.class);
                    i.putExtra("iduser", iduser);
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

    public String currencyRupiah(double harga) {
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
