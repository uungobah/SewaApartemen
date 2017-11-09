package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.EditRekening;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentNewProperty;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

/**
 * Created by swa on 12/17/2015.
 */
public class AdapterUploadGalery extends RecyclerView.Adapter<AdapterUploadGalery.MyViewHolder> {

    private LayoutInflater inflater;
    List<InformationUploadGallery> data = Collections.emptyList();
    private Context context;
    JSONObject jobj;
    String status;
    String newId;

    SharedPreferences sharedPreferences;

    public AdapterUploadGalery(Context context, List<InformationUploadGallery> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

        sharedPreferences = context.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);
    }

    @Override
    public AdapterUploadGalery.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_upload_gallery, viewGroup, false);
        MyViewHolder hoder = new MyViewHolder(view);
        return hoder;
    }

    @Override
    public void onBindViewHolder(final AdapterUploadGalery.MyViewHolder holder, int i) {
        InformationUploadGallery current = data.get(i);
        Log.d("Rv galeryy","Masuk");
        holder.im.setImageBitmap(current.img);
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
        AppCompatButton btnDelete;

        public MyViewHolder(View itemView) {

            super(itemView);
            context = itemView.getContext();
            im = (ImageView) itemView.findViewById(R.id.img_upload_gallery);
            btnDelete = (AppCompatButton) itemView.findViewById(R.id.btn_del_galery);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    konfirmasiHapus(data.get(getPosition()).id, getPosition());
                }
            });



        }

        @Override
        public void onClick(View v) {

        }
    }


    public void deleteImage(final String idImage, final int position) {
        String tag_json_obj = "json_obj_delete_rekening";
        String url = ConstantUtil.WEB_SERVICE.URL_DELETE_UPLOAD_GALERY;
        final List<InformationRekening> data = new ArrayList<>();


        final ProgressDialog pDialog = new ProgressDialog(context);
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("delete Image", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");
////                                JSONArray jarr = jobj.getJSONArray("data");
                                if (status.equals("true")) {
                                    delete(position);
                                    int awal = FragmentNewProperty.getInstance().getJum_upload_foto();
                                    int akhir = awal - 1;
                                    FragmentNewProperty.getInstance().setJum_upload_foto(akhir);
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

                params.put("idImage", idImage);
                params.put("act", "2");
                Log.d("params", params.toString());
                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void konfirmasiHapus(final String idImage, final int position) {
        final CharSequence[] items = {"Ya", "Tidak"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Konfirmasi Hapus Gambar!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Ya")) {
                    deleteImage(idImage, position);
                    dialog.dismiss();
                } else if (items[item].equals("Tidak")) {
                    dialog.dismiss();

                }
            }
        });
        builder.show();
    }




}
