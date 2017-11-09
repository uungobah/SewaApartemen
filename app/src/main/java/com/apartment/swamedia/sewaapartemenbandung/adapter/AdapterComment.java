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
public class AdapterComment extends RecyclerView.Adapter<AdapterComment.MyViewHolder> {

    JSONObject jobj;
    String status;

    private LayoutInflater inflater;
    List<InformationComment> data = Collections.emptyList();
    private Context context;

    SharedPreferences sharedPreferences;

    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    public AdapterComment(Context context, List<InformationComment> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

        sharedPreferences = context.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

    }


    @Override
    public AdapterComment.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_comment, viewGroup, false);
        MyViewHolder hoder = new MyViewHolder(view);
        return hoder;
    }

    @Override
    public void onBindViewHolder(AdapterComment.MyViewHolder holder, int i) {
        InformationComment current = data.get(i);

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
        TextView subject;
        TextView isi;
        TextView tanggal;
        ImageView img;

        public MyViewHolder(View itemView) {

            super(itemView);
            context = itemView.getContext();

            isi = (TextView) itemView.findViewById(R.id.tv_comment_isi);
            tanggal = (TextView) itemView.findViewById(R.id.tv_comment_tgl);
            nama = (TextView) itemView.findViewById(R.id.tv_comment_nama);
            img = (ImageView) itemView.findViewById(R.id.img_picture_comment);

//            spinPilihan = (AppCompatSpinner) itemView.findViewById(R.id.spin_manage_action);
        }

        @Override
        public void onClick(View v) {

        }
    }

//    public void nonAktifApartment(String idProperti, final String statusAktif, final int position) {
//
//
//        String tag_json_obj = "json_obj_list_apartment";
//        String user_id = sharedPreferences.getString("iduser", "");
//        String role = sharedPreferences.getString("role", "");
//
//        String url = ConstantUtil.WEB_SERVICE.URL_NON_AKTIF_APARTEMEN + "/" + idProperti + "/" + statusAktif + "/2/" + user_id;
//        Log.v("URL", url);
//        final ProgressDialog pDialog = new ProgressDialog(context);
//
//
//        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("data", response.toString());
//
//                        try {
//                            jobj = new JSONObject(response);
//                            if (jobj.has("status")) {
//                                status = jobj.getString("status");
//
//
//                                if (status.equals("T")) {
//                                    InformationComment i = new InformationComment();
//                                    i.id = data.get(position).id;
//                                    if (statusAktif.equals("ACTIVE")) {
//                                        i.status = "Active";
//                                    } else {
//                                        i.status = "Non Active";
//                                    }
//                                    i.harga = data.get(position).harga;
//                                    i.img = data.get(position).img;
//                                    i.judul = data.get(position).judul;
//                                    i.tipesewa = data.get(position).tipesewa;
//
//                                    data.set(position, i);
//                                    notifyDataSetChanged();
//
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        pDialog.hide();
//
//                    }
//                }, new Response.ErrorListener() {
//            int i = 0;
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("paper", "Error: " + error.getMessage());
//                pDialog.hide();
//                i = i + 1;
//
//                Log.v("Jumlah Erorr", "Erorr " + i);
//            }
//        });
//
//
//        // AppController.getInstance().getRequestQueue().getCache().remove(url);
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//
//
//    }


}
