package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
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
import com.apartment.swamedia.sewaapartemenbandung.activity.EditRekening;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

/**
 * Created by swa on 12/17/2015.
 */
public class AdapterRekening extends RecyclerView.Adapter<AdapterRekening.MyViewHolder> {

    private LayoutInflater inflater;
    List<InformationRekening> data = Collections.emptyList();
    private Context context;
    JSONObject jobj;
    String status;

    SharedPreferences sharedPreferences;

    public AdapterRekening(Context context, List<InformationRekening> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

        sharedPreferences = context.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);
    }

    @Override
    public AdapterRekening.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_rekening, viewGroup, false);
        MyViewHolder hoder = new MyViewHolder(view);
        return hoder;
    }

    @Override
    public void onBindViewHolder(final AdapterRekening.MyViewHolder holder, int i) {
        InformationRekening current = data.get(i);

        holder.namabank.setText(current.namabank);
        holder.namapemilik.setText(current.nama_pemilik);
        holder.norek.setText(current.norek);
        holder.cabang.setText(current.cabang);


        // Transformation Image Mengikuti Size ImageView
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = holder.logo.getWidth();

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };

        Picasso.with(this.context)
                .load(current.logo)
                .error(android.R.drawable.stat_notify_error)
                .transform(transformation)
                .into(holder.logo);

//        Picasso.with(context).load(current.logo).into(holder.logo);

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


        TextView namabank;
        TextView namapemilik;
        TextView norek;
        TextView cabang;

        ImageView logo;

        AppCompatButton btn_hapus;
        AppCompatButton btn_edit;

        public MyViewHolder(View itemView) {

            super(itemView);
            context = itemView.getContext();
            namabank = (TextView) itemView.findViewById(R.id.tv_nama_bank);
            namapemilik = (TextView) itemView.findViewById(R.id.tv_nama_pemilik);
            norek = (TextView) itemView.findViewById(R.id.tv_no_rek);
            cabang = (TextView) itemView.findViewById(R.id.tv_cabang);
            logo = (ImageView) itemView.findViewById(R.id.img_logo_bank);
            btn_hapus = (AppCompatButton) itemView.findViewById(R.id.btn_rekening_hapus);
            btn_edit = (AppCompatButton) itemView.findViewById(R.id.btn_rekening_edit);

            btn_hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    konfirmasiHapus(data.get(getPosition()).idrek, getPosition());

                }
            });

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EditRekening.class);
                    i.putExtra("id_rek", data.get(getPosition()).idrek);
                    i.putExtra("aksi", "Ubah");
                    context.startActivity(i);

                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }


    public void deleteRekening(final String idRek, final int position) {
        String tag_json_obj = "json_obj_delete_rekening";
        String url = ConstantUtil.WEB_SERVICE.URL_DELETE_REKENING;
        final List<InformationRekening> data = new ArrayList<>();

        final String idUser = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(context);


        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("List Bank", response.toString());

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
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("paper", "Error: " + error.getMessage());
                pDialog.hide();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id_rek", idRek);
                params.put("user_id", idUser);
                params.put("act", "2");
                Log.d("params", params.toString());
                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void konfirmasiHapus(final String idRek, final int position) {
        final CharSequence[] items = {"Ya", "Tidak"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Konfirmasi Hapus Rekening!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Ya")) {
                    deleteRekening(idRek, position);
                    dialog.dismiss();
                } else if (items[item].equals("Tidak")) {
                    dialog.dismiss();

                }
            }
        });
        builder.show();
    }



}
