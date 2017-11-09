package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.DetailProperty;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nurul Akbar on 08/09/2015.
 */

public class AdapterProperty extends RecyclerView.Adapter {

    private LayoutInflater inflater;
    List<InformationProperty> data = Collections.emptyList();
    private Context context;
    int x = 0;
    public int com1 = 0, com2 = 0;

    private static AdapterProperty instance = null;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public AdapterProperty(Context context, List<InformationProperty> data, RecyclerView recyclerView) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

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

    public AdapterProperty() {

    }


    public synchronized static AdapterProperty getInstance() {
        if (instance == null) {
            instance = new AdapterProperty();
        }
        return instance;
    }

    public int getCom1() {
        return com1;
    }

    public int getCom2() {
        return com2;
    }

    public void setCom1(int com1) {
        this.com1 = com1;
    }

    public void setCom2(int com2) {
        this.com2 = com2;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getX(){
        return x;
    }


    @Override
    public int getItemViewType(int position) {
        return data.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder hoder;
        if (i == VIEW_ITEM) {
            View view = inflater.inflate(R.layout.custom_list_property, viewGroup, false);
            hoder = new MyViewHolder(view);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progress_item, viewGroup , false);
            hoder = new ProgressViewHolder(v);
        }

        return hoder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof MyViewHolder) {
            InformationProperty current = data.get(i);
            MyViewHolder viewHolder = (MyViewHolder) holder;

            viewHolder.nama.setText(current.nama_apartment);

            viewHolder.lokasi.setText(current.lokasi);
            viewHolder.tipesewaandproperty.setText(current.tipe_property + " - " + current.tipe_sewa);


            //Ubah ke currency rupiah
            double harga = Double.parseDouble(current.harga);
            viewHolder.harga.setText(currencyRupiah(harga));

//        viewHolder.img_apartment.setImageBitmap(current.img_apartment);
            Picasso.with(context)
                    .load(current.img_apartment)
                    .into(viewHolder.img_apartment);
            viewHolder.cb_compare.setId(Integer.parseInt(current.id));
        }else{
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
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

        TextView nama;
        TextView lokasi;
        TextView desc;
        TextView tipesewaandproperty;
        TextView tipe_properti;
        TextView jumlah_kamar_tidur;
        TextView jumlah_kamar_mandi;
        TextView harga;
        ImageView img_apartment;
        AppCompatButton btn_detail;
        CheckBox cb_compare;
        LinearLayout ll_list;

        public MyViewHolder(View itemView) {

            super(itemView);
            context = itemView.getContext();
            nama = (TextView) itemView.findViewById(R.id.tv_nama_property);
            lokasi = (TextView) itemView.findViewById(R.id.tv_lokasi);
            tipesewaandproperty = (TextView) itemView.findViewById(R.id.tv_propertysewa);
            tipe_properti = (TextView) itemView.findViewById(R.id.tv_tipe_properti);
            jumlah_kamar_tidur = (TextView) itemView.findViewById(R.id.tv_jumlah_kamar_tidur);
            jumlah_kamar_mandi = (TextView) itemView.findViewById(R.id.tv_jumlah_kamar_mandi);
            harga = (TextView) itemView.findViewById(R.id.tv_harga);
            img_apartment = (ImageView) itemView.findViewById(R.id.img_property);
            cb_compare = (CheckBox) itemView.findViewById(R.id.cb_compare);
            btn_detail = (AppCompatButton) itemView.findViewById(R.id.btn_detail);
            ll_list = (LinearLayout) itemView.findViewById(R.id.ll_list_property);



            ll_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailProperty.class);
                    InformationProperty current = data.get(getPosition());
                    String id = current.id;
                    i.putExtra("id", id);
                    i.putExtra("action", "1");
                    context.startActivity(i);
//                    ((Activity)context).finish();
                }
            });

            cb_compare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (x == 2) {
                            Toast.makeText(context, "Maksimal Check 2", Toast.LENGTH_LONG).show();
                            cb_compare.setChecked(false);
                        } else {

                            if (x == 0) {
                                com1 = cb_compare.getId();
                                AdapterProperty.getInstance().setCom1(com1);


                            } else if (x == 1 && com1 != 0 && com2 == 0) {
                                com2 = cb_compare.getId();
                                AdapterProperty.getInstance().setCom2(com2);

                            } else if (x == 1 && com1 == 0 && com2 != 0) {
                                com1 = cb_compare.getId();
                                AdapterProperty.getInstance().setCom1(com1);
                            }
                            x = x + 1;
                            AdapterProperty.getInstance().setX(x);
//                            Toast.makeText(context, "X = " + x + " com1 = " + com1 + "com 2 = " + com2, Toast.LENGTH_LONG).show();
                        }
                    }

                    if (!isChecked) {
                        if (com1 == cb_compare.getId()) {
                            com1 = 0;
                            AdapterProperty.getInstance().setCom1(com1);
                        } else if (com2 == cb_compare.getId()) {
                            com2 = 0;
                            AdapterProperty.getInstance().setCom2(com2);

                        }
                        x = x - 1;
                        AdapterProperty.getInstance().setX(x);
//                        Toast.makeText(context, "X = " + x + " com1 = " + com1 + "com 2 = " + com2, Toast.LENGTH_LONG).show();
                    }
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

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
