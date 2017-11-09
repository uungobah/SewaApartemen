package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.DetailMessage;
import com.apartment.swamedia.sewaapartemenbandung.activity.DetailUser;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nurul Akbar on 20/11/2015.
 */
public class AdapterNotificationPaging extends RecyclerView.Adapter{

    private LayoutInflater inflater;
    List<InformationNotification> data = Collections.emptyList();
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

    Date then;

    public AdapterNotificationPaging(Context context, List<InformationNotification> data,RecyclerView recyclerView) {
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
            View view = inflater.inflate(R.layout.custom_list_notification, viewGroup, false);
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

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hoder, int i) {

        if (hoder instanceof MyViewHolder) {
            MyViewHolder holder = (MyViewHolder) hoder;
            InformationNotification current = data.get(i);

            then = new Date(current.tgl);

            holder.pengirim.setText(current.sender);
            holder.tglnotif.setText(subStract(dateNow(), then));

//            holder.judul.setText("Nama properti : " + current.judul);
//            holder.pemilik.setText(current.pemilik);
//            holder.tanggalawal.setText("Check In : " + current.tanggalawal);
//            holder.tanggalakhir.setText("Check Out : " + current.tanggalakhir);
//            holder.tglpengajuan.setText("Pengajuan : " + current.tanggalpengajuan);
//            holder.noorder.setText("No order : " + current.no_order);
//            holder.durasi.setText("Durasi : " + current.durasi);
//
//            Double harga = Double.parseDouble(current.total_harga);
//            holder.totalHarga.setText("Biaya : " + currencyRupiah(harga));
//            holder.statusPengajuan.setText("Status : " + current.status);
//
//            if (!current.reject.equals("null")) {
//                holder.reject.setText(current.reject);
//                holder.reject.setVisibility(View.VISIBLE);
//            }
//
//            Picasso.with(context).load(current.img).into(holder.img);

        }else{
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
        TextView pengirim;
        TextView tglnotif;
        LinearLayout ll;

        public MyViewHolder(View itemView) {

            super(itemView);
            context = itemView.getContext();
            judul = (TextView) itemView.findViewById(R.id.tv_notif_judul);
            pengirim = (TextView) itemView.findViewById(R.id.tv_notif_pengirim);
            tglnotif = (TextView) itemView.findViewById(R.id.tv_notif_tgl);
            ll = (LinearLayout) itemView.findViewById(R.id.ll_notif);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InformationNotification c = data.get(getPosition());
                    String idmessage = c.parent;
                    String id_notif = c.idNotif;
                    String typeTrx = c.typeTrx;

                    updateNotif(id_notif, typeTrx, idmessage);

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

    public Date dateNow() {
        Date currentDate = new Date(System.currentTimeMillis());
        return currentDate;
    }


    public String subStract(Date now, Date then) {
        long diff = Math.abs(now.getTime() - then.getTime());
        long diffMinute = diff / (60 * 1000);
        long diffHour = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        long diffWeek = diff / (7 * 24 * 60 * 60 * 1000);

        if (diffMinute < 60) {
            return String.valueOf(diffMinute + " minute ago");
        } else if (diffHour < 60) {
            return String.valueOf(diffHour + " hour ago");
        } else if (diffDays < 7) {
            return String.valueOf(diffDays + " day ago");
        } else {
            return String.valueOf(diffWeek + " week ago");
        }

    }

    public void updateNotif(final String id_notif,String typeTrx, final String id_parent) {
        //final String data_pot_bencana = data_potensi_bencana;
        String tag_json_obj = "json_obj";
        String url = ConstantUtil.WEB_SERVICE.URL_UPDATE_NOTIF+"/2/"+typeTrx+"/"+id_notif+"/"+id_parent;
        Log.v("URL", url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Status Update", response.toString());
                JSONObject jobj = null;
                String title = "";
                try {
                    jobj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response != null) {
                    try {
                        if (jobj.getString("success").equals("true")) {

                            Intent i = new Intent(context, DetailMessage.class);
                            i.putExtra("id_message", id_parent);
                            context.startActivity(i);
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("paper", "Error: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("act", "2");
                params.put("id_notif", id_notif);
                params.put("typeNotif", "ALL_NOTIF");
                return params;
            }
        };
        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
