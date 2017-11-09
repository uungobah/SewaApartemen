package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.MainActivity;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.DetailMessage;
import com.apartment.swamedia.sewaapartemenbandung.activity.EditProperty;
import com.apartment.swamedia.sewaapartemenbandung.activity.FormLogin;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.maps.GPSTracker;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nurul Akbar on 05/11/2015.
 */
public class AdapterDetailMessaging extends RecyclerView.Adapter<AdapterDetailMessaging.MyViewHolder> {

    JSONObject jobj;
    String status;

    private LayoutInflater inflater;
    List<InformationDetailMessaging> data = Collections.emptyList();
    private Context context;

    SharedPreferences sharedPreferences;

    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    public AdapterDetailMessaging(Context context, List<InformationDetailMessaging> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        sharedPreferences = context.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

    }


    @Override
    public AdapterDetailMessaging.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_detail_messaging, viewGroup, false);
        MyViewHolder hoder = new MyViewHolder(view);
        return hoder;
    }

    @Override
    public void onBindViewHolder(AdapterDetailMessaging.MyViewHolder holder, int i) {
        InformationDetailMessaging current = data.get(i);

        holder.nama.setText(current.nama);
        holder.isi.setText(current.isi);
        holder.tanggal.setText(current.tanggal);
        Picasso.with(context)
                .load(current.img)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView nama;
        TextView isi;
        TextView tanggal;
        ImageView img;


        public MyViewHolder(View itemView) {

            super(itemView);
            context = itemView.getContext();

            isi = (TextView) itemView.findViewById(R.id.tv_messaging_isi_detail);
            tanggal = (TextView) itemView.findViewById(R.id.tv_messaging_tgl_detail);
            nama = (TextView) itemView.findViewById(R.id.tv_messaging_nama_detail);
            img = (ImageView) itemView.findViewById(R.id.img_picture_messaging_detail);

        }

        @Override
        public void onClick(View v) {

        }
    }


}
