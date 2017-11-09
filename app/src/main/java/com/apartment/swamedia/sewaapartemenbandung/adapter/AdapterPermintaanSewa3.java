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
public class AdapterPermintaanSewa3 extends RecyclerView.Adapter<AdapterPermintaanSewa3.MyViewHolder> {

    private LayoutInflater inflater;
    List<InformationPermintaanSewa3> data = Collections.emptyList();
    private Context context;
    SharedPreferences sharedPreferences;
    JSONObject jobj;
    String status;

    public AdapterPermintaanSewa3(Context context, List<InformationPermintaanSewa3> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        sharedPreferences = context.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

    }


    @Override
    public AdapterPermintaanSewa3.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_permintaan_sewa3, viewGroup, false);
        MyViewHolder hoder = new MyViewHolder(view);
        return hoder;
    }

    @Override
    public void onBindViewHolder(AdapterPermintaanSewa3.MyViewHolder holder, int i) {
        InformationPermintaanSewa3 current = data.get(i);


        holder.judul.setText("Nama properti : " + current.judul);
        holder.penyewa.setText(current.penyewa);
        holder.tanggalawal.setText("Check In : " + current.tanggalawal);
        holder.tanggalakhir.setText("Check Out : " + current.tanggalakhir);
        holder.tglpengajuan.setText("Pengajuan : " + current.tanggalpengajuan);
        holder.noorder.setText("No order : " + current.no_order);
        holder.statusPembayaran.setText("Status : " + current.status);
        holder.durasi.setText("Durasi : " + current.durasi);
        Double harga = Double.parseDouble(current.total_harga);
        holder.totalHarga.setText("Biaya : "+ currencyRupiah(harga));

        Picasso.with(context).load(current.img).into(holder.img);

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
            judul = (TextView) itemView.findViewById(R.id.tv_permintaan3_judul);
            tglpengajuan = (TextView) itemView.findViewById(R.id.tv_permintaan3_tanggalpengajuan);
            tanggalawal = (TextView) itemView.findViewById(R.id.tv_permintaan3_tanggalawal);
            tanggalakhir = (TextView) itemView.findViewById(R.id.tv_permintaan3_tanggalakhir);
            penyewa = (TextView) itemView.findViewById(R.id.tv_permintaan3_penyewa);
            noorder = (TextView) itemView.findViewById(R.id.tv_permintaan3_noorder);
            statusPembayaran = (TextView) itemView.findViewById(R.id.tv_permintaan3_status);
            durasi = (TextView) itemView.findViewById(R.id.tv_permintaan3_durasi);
            totalHarga = (TextView) itemView.findViewById(R.id.tv_permintaan3_totalharga);

            img = (ImageView) itemView.findViewById(R.id.img_picture_penyewa3);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InformationPermintaanSewa3 c = data.get(getPosition());
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
