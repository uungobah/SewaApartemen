package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
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
import com.apartment.swamedia.sewaapartemenbandung.MainActivity;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.DetailProperty;
import com.apartment.swamedia.sewaapartemenbandung.activity.EditProperty;
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
public class AdapterManagementPropertyPaging extends RecyclerView.Adapter {

    JSONObject jobj;
    String status;

    private LayoutInflater inflater;
    List<InformationManagementProperty> data = Collections.emptyList();
    private Context context;

    SharedPreferences sharedPreferences;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    public AdapterManagementPropertyPaging(Context context, List<InformationManagementProperty> data, RecyclerView recyclerView) {
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
            View view = inflater.inflate(R.layout.custom_management_list_property, viewGroup, false);
            hoder = new MyViewHolder(view);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progress_item, viewGroup , false);
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


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hoder, int i) {


        if (hoder instanceof MyViewHolder) {
            MyViewHolder holder = (MyViewHolder) hoder;
            InformationManagementProperty current = data.get(i);

            holder.harga.setText("Harga : " + current.harga);
            holder.judul.setText("Nama : " + current.judul);
            holder.tipesewa.setText("Tipe Sewa : " + current.tipesewa);
            holder.status.setText(current.status);
            holder.statusReject.setText(current.statusReject);

            if (current.status.equals("Active")) {
                holder.btnAktif.setVisibility(View.VISIBLE);
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDetail.setVisibility(View.VISIBLE);
                holder.btnAktif.setText("Non Active");
            } else if (current.status.equals("Non Active")) {
                holder.btnAktif.setVisibility(View.VISIBLE);
                holder.btnEdit.setVisibility(View.GONE);
                holder.btnDetail.setVisibility(View.VISIBLE);
                holder.btnAktif.setText("Active");
            } else if (current.status.equals("Pending")) {
                holder.btnAktif.setVisibility(View.GONE);
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDetail.setVisibility(View.VISIBLE);
            } else if (current.status.equals("Corrective")) {
                holder.btnAktif.setVisibility(View.GONE);
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDetail.setVisibility(View.VISIBLE);
            } else if (current.status.equals("Rejected")) {
                holder.btnAktif.setVisibility(View.GONE);
                holder.btnEdit.setVisibility(View.GONE);
                holder.btnDetail.setVisibility(View.VISIBLE);
                holder.statusReject.setVisibility(View.VISIBLE);
            }

            Picasso.with(context)
                    .load(current.img)
                    .into(holder.img);
        }else{

            ((ProgressViewHolder) hoder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView judul;
        TextView tipesewa;
        TextView harga;
        TextView status;
        TextView statusReject;
        ImageView img;
        AppCompatSpinner spinPilihan;
        AppCompatButton btnAktif, btnEdit, btnDetail;

        public MyViewHolder(View itemView) {

            super(itemView);
            context = itemView.getContext();
            judul = (TextView) itemView.findViewById(R.id.tv_manage_judul);
            tipesewa = (TextView) itemView.findViewById(R.id.tv_manage_tipesewa);
            harga = (TextView) itemView.findViewById(R.id.tv_manage_harga);
            status = (TextView) itemView.findViewById(R.id.tv_manage_status);
            statusReject = (TextView) itemView.findViewById(R.id.tv_manage_status_reject);
            img = (ImageView) itemView.findViewById(R.id.img_manage_apartment);
//            spinPilihan = (AppCompatSpinner) itemView.findViewById(R.id.spin_manage_action);
            btnAktif = (AppCompatButton) itemView.findViewById(R.id.btn_manage_aktif);
            btnEdit = (AppCompatButton) itemView.findViewById(R.id.btn_manage_edit);
            btnDetail = (AppCompatButton) itemView.findViewById(R.id.btn_manage_detail);


            btnAktif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InformationManagementProperty current = data.get(getPosition());
                    String id = current.id;
                    String status = "";
                    if (current.status.equals("Active")) {
                        status = "NONACTIVE";
                    } else {
                        status = "ACTIVE";
                    }
                    nonAktifApartment(id, status, getPosition());
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkLocation() == true) {
                        Intent i = new Intent(context, EditProperty.class);
                        String idPost = data.get(getPosition()).id;
                        i.putExtra("idPost", idPost);
                        context.startActivity(i);
                        ((Activity) context).finish();
                    }

                }
            });

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailProperty.class);
                    InformationManagementProperty current = data.get(getPosition());
                    String id = current.id;
                    i.putExtra("id", id);
                    i.putExtra("action", "0");
                    context.startActivity(i);
//                    ((Activity)context).finish();
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void nonAktifApartment(String idProperti, final String statusAktif, final int position) {


        String tag_json_obj = "json_obj_list_apartment";
        String user_id = sharedPreferences.getString("iduser", "");
        String role = sharedPreferences.getString("role", "");

        String url = ConstantUtil.WEB_SERVICE.URL_NON_AKTIF_APARTEMEN + "/" + idProperti + "/" + statusAktif + "/2/" + user_id;
        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(context);


        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("data", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("status")) {
                                status = jobj.getString("status");


                                if (status.equals("T")) {
                                    InformationManagementProperty i = new InformationManagementProperty();
                                    i.id = data.get(position).id;
                                    if (statusAktif.equals("ACTIVE")) {
                                        i.status = "Active";
                                    } else {
                                        i.status = "Non Active";
                                    }
                                    i.harga = data.get(position).harga;
                                    i.img = data.get(position).img;
                                    i.judul = data.get(position).judul;
                                    i.tipesewa = data.get(position).tipesewa;

                                    data.set(position, i);
                                    notifyDataSetChanged();

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
        });


        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    public boolean checkLocation() {

        boolean status = false;

        GPSTracker gpsTracker = new GPSTracker(context);

        if (gpsTracker.canGetLocation()) {
            status = true;
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
            status = false;
        }
        return status;
    }

}
