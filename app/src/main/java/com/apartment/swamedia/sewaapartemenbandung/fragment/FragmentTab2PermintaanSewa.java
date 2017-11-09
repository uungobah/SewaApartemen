package com.apartment.swamedia.sewaapartemenbandung.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterPermintaanSewa1;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterPermintaanSewa1Paging;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterPermintaanSewa2;
import com.apartment.swamedia.sewaapartemenbandung.adapter.AdapterPermintaanSewa2Paging;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationPermintaanSewa1;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationPermintaanSewa2;
import com.apartment.swamedia.sewaapartemenbandung.adapter.InformationPermintaanSewa3;
import com.apartment.swamedia.sewaapartemenbandung.adapter.OnLoadMoreListener;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swa on 1/4/2016.
 */
public class FragmentTab2PermintaanSewa extends Fragment {
    private RecyclerView recyclerView;
    private AdapterPermintaanSewa2Paging adapter;
    JSONObject jobj;
    SharedPreferences sharedPreferences;
    String iduser;
    String status;

    View v;
    Context ctx;

    int page = 1;

    List<InformationPermintaanSewa2> data = new ArrayList<>();

    protected Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab2_permintaan_sewa, container, false);
        ctx = getContext();
        iduser="";

        sharedPreferences = ctx.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_status_pembayaran);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        handler = new Handler();

        getStatusPembayaran();

        return v;
    }


    public void getStatusPembayaran() {
        String tag_json_obj = "json_obj_req";
        iduser = sharedPreferences.getString("iduser", "");
        String url = ConstantUtil.WEB_SERVICE.URL_PERMINTAAN_SEWA;

        Log.v("URL", url);
        Log.v("IdUser", iduser);

        final ProgressDialog pDialog = new ProgressDialog(ctx);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response.toString());
                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");

                                if (status.equals("true")) {

                                    JSONArray jarr = jobj
                                            .getJSONArray("data");
                                    Log.v("Masuk sukses", "Masuk");
//                                    int[] img_apartment = {R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment, R.drawable.photo_apartment};
                                  if (jarr.length() > 0) {
                                      for (int i = 0; i < jarr.length(); i++) {
                                          JSONObject c = jarr.getJSONObject(i);
                                          Log.v("Masuk looping", "Masuk");

                                          InformationPermintaanSewa2 current = new InformationPermintaanSewa2();

                                          current.penyewa = c.getString("kustomerName");
                                          current.judul = c.getString("postJudul");
                                          current.tanggalawal = c.getString("sewaTglMasuk");
                                          current.tanggalakhir = c.getString("sewaTglKeluar");
                                          current.tanggalpengajuan = c.getString("sewaTglPengajuan");
                                          current.img = c.getString("kustomerImage");
                                          current.no_order = c.getString("sewaKodeTrk");
                                          current.id = c.getString("sewaId");
                                          current.status = c.getString("sewaStatus");
                                          current.idUser = c.getString("kustomerId");
                                          current.durasi = c.getString("sewaDurasi");
                                          current.total_harga = c.getString("sewaTotalHarga");
                                          data.add(current);

                                      }

                                      if (page < 2) {
                                          recyclerView.setHasFixedSize(true);
                                          adapter = new AdapterPermintaanSewa2Paging(ctx, data, recyclerView);
                                          recyclerView.setAdapter(adapter);
                                      } else {
                                          adapter.setLoaded();
                                          onItemsLoadComplete();
                                      }

                                      adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                                          @Override
                                          public void onLoadMore() {
                                              data.add(null);
                                              adapter.notifyItemInserted(data.size() - 1);

                                              handler.postDelayed(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      try {
                                                          data.remove(data.size() - 1);
                                                          adapter.notifyItemRemoved(data.size());
                                                          refreshItems();
                                                      } catch (JSONException e) {
                                                          e.printStackTrace();
                                                      }
                                                  }
                                              }, 5000);
                                          }
                                      });
                                      page++;
                                  }
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
                VolleyLog.d("Erorr", "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("act", "2");
                params.put("user_id", iduser);
                params.put("type", "2");
                params.put("page", String.valueOf(page));
                return params;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    void refreshItems() throws JSONException {
        // Load items
        // ...

        getStatusPembayaran();

    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
    }
}
