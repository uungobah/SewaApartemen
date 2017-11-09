package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nurul Akbar on 05/11/2015.
 */
public class AdapterKotakMasuk extends RecyclerView.Adapter<AdapterKotakMasuk.MyViewHolder> {

    JSONObject jobj;
    String status;

    private LayoutInflater inflater;
    List<InformationMessaging> data = Collections.emptyList();
    private Context context;

    SharedPreferences sharedPreferences;

    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    public AdapterKotakMasuk(Context context, List<InformationMessaging> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

        sharedPreferences = context.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

    }


    @Override
    public AdapterKotakMasuk.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_messaging, viewGroup, false);
        MyViewHolder hoder = new MyViewHolder(view);
        return hoder;
    }

    @Override
    public void onBindViewHolder(AdapterKotakMasuk.MyViewHolder holder, int i) {
        InformationMessaging current = data.get(i);

        holder.nama.setText(current.nama);
        holder.subject.setText(current.subject);
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
        LinearLayout ll;

        public MyViewHolder(View itemView) {

            super(itemView);
            context = itemView.getContext();

            isi = (TextView) itemView.findViewById(R.id.tv_messaging_isi);
            tanggal = (TextView) itemView.findViewById(R.id.tv_messaging_tgl);
            subject = (TextView) itemView.findViewById(R.id.tv_messaging_subject);
            nama = (TextView) itemView.findViewById(R.id.tv_messaging_nama);
            img = (ImageView) itemView.findViewById(R.id.img_picture_messaging);
            ll = (LinearLayout) itemView.findViewById(R.id.ll_messaging);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InformationMessaging im = data.get(getPosition());
                    String id = im.id;
                    Intent i = new Intent(context, DetailMessage.class);
                    i.putExtra("id_message",id);
                    context.startActivity(i);
                }
            });

            ll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dialogHapus(getPosition());
                    return false;
                }
            });
//            spinPilihan = (AppCompatSpinner) itemView.findViewById(R.id.spin_manage_action);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void deleteMessage(final String idSubject,final int position) {


        String tag_json_obj = "json_obj_list_apartment";

        String url = ConstantUtil.WEB_SERVICE.URL_DELETE_MESSAGE;
        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(context);

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
                                    delete(position);
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
                params.put("idSubject", idSubject);

                return params;
            }
        };


        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }


    private void dialogHapus(final int position) {
        final CharSequence[] items = {"Ya", "Tidak"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Apakah yakin untuk hapus?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Ya")) {
                    InformationMessaging i = data.get(position);
                    String id_Subject= i.id;
                    deleteMessage(id_Subject,position);
                    dialog.dismiss();
                } else if (items[item].equals("Tidak")) {
                    dialog.dismiss();

                }
            }
        });
        builder.show();
    }
}
