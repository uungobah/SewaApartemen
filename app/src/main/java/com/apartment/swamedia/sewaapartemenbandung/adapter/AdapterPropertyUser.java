package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.DetailProperty;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.List;

/**
 * Created by swa on 12/17/2015.
 */
public class AdapterPropertyUser extends RecyclerView.Adapter<AdapterPropertyUser.MyViewHolder> {

    private LayoutInflater inflater;
    List<InformationPropertyUser> data = Collections.emptyList();
    private Context context;
    JSONObject jobj;
    String status;
    String newId;

    SharedPreferences sharedPreferences;

    public AdapterPropertyUser(Context context, List<InformationPropertyUser> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

        sharedPreferences = context.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);
    }

    @Override
    public AdapterPropertyUser.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_property_user, viewGroup, false);
        MyViewHolder hoder = new MyViewHolder(view);
        return hoder;
    }

    @Override
    public void onBindViewHolder(final AdapterPropertyUser.MyViewHolder holder, int i) {
        InformationPropertyUser current = data.get(i);
        Log.d("Rv galeryy", "Masuk");
        Double harga = Double.parseDouble(current.harga);
        holder.harga.setText(currencyRupiah(harga));
        holder.nama.setText(current.nama);
        Picasso.with(context).load(current.img).into(holder.im);
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

        ImageView im;
        TextView harga,nama;
        LinearLayout ll_list;

        public MyViewHolder(View itemView) {

            super(itemView);
            context = itemView.getContext();
            im = (ImageView) itemView.findViewById(R.id.img_property_user);
            nama = (TextView) itemView.findViewById(R.id.tv_nama_property_user);
            harga = (TextView) itemView.findViewById(R.id.tv_harga_user);
            ll_list = (LinearLayout) itemView.findViewById(R.id.ll_property_user);

            ll_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailProperty.class);
                    InformationPropertyUser current = data.get(getPosition());
                    String id = current.id;
                    i.putExtra("id", id);
                    i.putExtra("action", "1");
                    context.startActivity(i);
                }
            });
        }

        @Override
        public void onClick(View v) {

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
