package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
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
 * Created by swa on 12/17/2015.
 */
public class EditRekening extends ActionBarActivity {

    View v;

    AppCompatSpinner spin_namabank;

    JSONObject jobj;
    String status;

    EditText et_edit_namarek, et_edit_norek, et_edit_cabangbank, et_edit_rek_katasandi;
    AppCompatButton btn_save;

    List<String> list_bank;
    ArrayList<HashMap<String, String>> data_nama_bank;

    SharedPreferences sharedPreferences;

    String id_rek, aksi;

    String rek_id, rek_user, rek_nama_rek, rek_no_rek, rek_bank_id, rek_cabang;

    TextView tv_title;

    private TextInputLayout til_nama, til_norek, til_cabang, til_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_edit_rekening);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedPreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);


        inisialisasi();

        getNamaBank();

        getParameter();


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        et_edit_namarek.addTextChangedListener(new MyTextWatcher(et_edit_namarek));
        et_edit_norek.addTextChangedListener(new MyTextWatcher(et_edit_norek));
        et_edit_cabangbank.addTextChangedListener(new MyTextWatcher(et_edit_cabangbank));
        et_edit_rek_katasandi.addTextChangedListener(new MyTextWatcher(et_edit_rek_katasandi));


    }

    public void inisialisasi() {
        spin_namabank = (AppCompatSpinner) findViewById(R.id.spin_edit_namabank);
        et_edit_namarek = (EditText) findViewById(R.id.et_edit_namarek);
        et_edit_norek = (EditText) findViewById(R.id.et_edit_norek);
        et_edit_cabangbank = (EditText) findViewById(R.id.et_edit_cabangbank);
        et_edit_rek_katasandi = (EditText) findViewById(R.id.et_edit_rekening_kata_sandi);

        tv_title = (TextView) findViewById(R.id.tv_title_editrek);

        til_nama = (TextInputLayout) findViewById(R.id.input_layout_edit_nama_rek);
        til_norek = (TextInputLayout) findViewById(R.id.input_layout_edit_norek);
        til_cabang = (TextInputLayout) findViewById(R.id.input_layout_edit_cabangbank);
        til_password = (TextInputLayout) findViewById(R.id.input_layout_rekening_password);


        btn_save = (AppCompatButton) findViewById(R.id.btn_edit_rek);
        id_rek = "";
        aksi = "";

    }

    private void getParameter() {
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            id_rek = extra.getString("id_rek");
            aksi = extra.getString("aksi");
        }

        if (aksi.equals("Ubah")) {
            getDetailRekening();
            btn_save.setText(aksi);
            tv_title.setText(aksi + " Rekening");
        } else {
            btn_save.setText(aksi);
            tv_title.setText(aksi + " Rekening");
        }

    }


    public void submitForm() {
        int x = 0;
        if (!validateNamaRek()) {
            x = x + 1;
            return;
        }
        if (!validateNoRek()) {
            x = x + 1;
            return;
        }
        if (!validateCabang()) {
            x = x + 1;
            return;
        }
        if (!validatePassword()) {
            x = x + 1;
            return;
        }
        if (!validateBank()) {
            x = x + 1;
            return;
        }

        if (x == 0) {

            if (aksi.equals("Ubah")) {
                editRek();
            } else {
                tambahRek();
            }

        }

    }

    public void getNamaBank() {
        String tag_json_obj = "json_obj_nama_bank";
        String url = ConstantUtil.WEB_SERVICE.URL_LIST_BANK;
        final ProgressDialog pDialog = new ProgressDialog(this);
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Nama Bank", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");
                                JSONArray jarr = jobj.getJSONArray("data");

                                if (status.equals("true")) {


                                    list_bank = new ArrayList<String>();

                                    list_bank.add("Pilih Bank");

                                    data_nama_bank = new ArrayList<HashMap<String, String>>();
                                    HashMap<String, String> map_bank = new HashMap<String, String>();
                                    map_bank.put("nama", "Pilih Bank");
                                    map_bank.put("id", "0");
                                    data_nama_bank.add(map_bank);

                                    for (int i = 0; i < jarr.length(); i++) {
                                        JSONObject c = jarr.getJSONObject(i);
                                        HashMap<String, String> map_namabank = new HashMap<String, String>();

                                        String name = c.getString("nama");
                                        String id = c.getString("id");

                                        map_namabank.put("nama", name);
                                        map_namabank.put("id", id);

                                        data_nama_bank.add(map_namabank);

                                        list_bank.add(name);

                                    }

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                                            EditRekening.this,
                                            android.R.layout.simple_spinner_item,
                                            list_bank);
                                    dataAdapter
                                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spin_namabank.setAdapter(dataAdapter);


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
        });

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void getDetailRekening() {
        final String tag_json_obj = "json_obj_update_rekening";
        String url = ConstantUtil.WEB_SERVICE.URL_POST_DETAIL_REK;
        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Update Data...");
        pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Detail rekening", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");

                                if (status.equals("true")) {
                                    JSONObject obj = jobj.getJSONObject("data");

                                    rek_id = obj.getString("rek_id");
                                    rek_user = obj.getString("rek_user");
                                    rek_nama_rek = obj.getString("rek_nama_rek");
                                    rek_no_rek = obj.getString("rek_no_rek");
                                    rek_bank_id = obj.getString("rek_bankid");
                                    rek_cabang = obj.getString("rek_cabang");

                                    et_edit_namarek.setText(rek_nama_rek);
                                    et_edit_norek.setText(rek_no_rek);
                                    et_edit_cabangbank.setText(rek_cabang);

                                    for (int i = 0; i < data_nama_bank.size(); i++) {
                                        if (rek_bank_id.equals(data_nama_bank.get(i).get("id"))) {
                                            spin_namabank.setSelection(i);
                                        }
                                    }


                                } else {
                                    Toast.makeText(getApplicationContext(), "Data Rekening Gagal Diganti", Toast.LENGTH_LONG).show();
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

                params.put("user_id", id_user);
                params.put("id_rek", id_rek);
                params.put("act", "2");

                Log.v("paramDetailRek", params.toString());

                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void editRek() {
        final String tag_json_obj = "json_obj_update_rekening";
        String url = ConstantUtil.WEB_SERVICE.URL_POST_EDIT_REK;
        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Ubah Rekening...");
        pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Status Ubah", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("status")) {
                                status = jobj.getString("status");
                                if (status.equals("T")){
                                    messageUbah();
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

                params.put("user_id", id_user);
                params.put("id_rek", id_rek);
                params.put("act", "2");
                params.put("nama_rekening", et_edit_namarek.getText().toString());
                params.put("cabang", et_edit_cabangbank.getText().toString());
                params.put("no_rekening", et_edit_norek.getText().toString());
                params.put("current_password", et_edit_rek_katasandi.getText().toString());
                params.put("bank", data_nama_bank.get(spin_namabank.getSelectedItemPosition()).get("id"));

                Log.v("paramEditRek", params.toString());

                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void tambahRek() {
        final String tag_json_obj = "json_obj_update_rekening";
        String url = ConstantUtil.WEB_SERVICE.URL_POST_CREATE_REK;
        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Simpan Rekening...");
        pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Tambah rek", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("status")) {
                                status = jobj.getString("status");
                                if (status.equals("T")){
                                    messageTambah();
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

                params.put("user_id", id_user);
                params.put("act", "2");
                params.put("nama_rekening", et_edit_namarek.getText().toString());
                params.put("cabang", et_edit_cabangbank.getText().toString());
                params.put("no_rekening", et_edit_norek.getText().toString());
                params.put("current_password", et_edit_rek_katasandi.getText().toString());
                params.put("bank", data_nama_bank.get(spin_namabank.getSelectedItemPosition()).get("id"));

                Log.v("paramEditRek", params.toString());

                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateNamaRek() {
        if (et_edit_namarek.getText().toString().trim().isEmpty()) {
            til_nama.setError("Nama rekening tidak boleh kosong");
            requestFocus(et_edit_namarek);
            return false;
        } else {
            til_nama.setErrorEnabled(false);

        }
        return true;

    }

    private boolean validateNoRek() {
        if (et_edit_norek.getText().toString().trim().isEmpty()) {
            til_norek.setError("Nomor rekening tidak boleh kosong");
            requestFocus(et_edit_norek);
            return false;
        } else {
            til_norek.setErrorEnabled(false);

        }
        return true;

    }

    private boolean validateCabang() {
        if (et_edit_cabangbank.getText().toString().trim().isEmpty()) {
            til_cabang.setError("Cabang tidak boleh kosong");
            requestFocus(et_edit_cabangbank);
            return false;
        } else {
            til_cabang.setErrorEnabled(false);

        }
        return true;

    }

    private boolean validateBank() {
        if (spin_namabank.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Bank harus dipilih", Toast.LENGTH_LONG).show();
            return false;
        } else {

        }
        return true;

    }

    private boolean validatePassword() {
        if (et_edit_rek_katasandi.getText().toString().trim().isEmpty()) {
            til_password.setError("Kata sandi tidak boleh kosong");
            requestFocus(et_edit_rek_katasandi);
            return false;
        } else {
            til_password.setErrorEnabled(false);

        }
        return true;
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_edit_namarek:
                    validateNamaRek();
                    break;
                case R.id.et_edit_norek:
                    validateNoRek();
                    break;
                case R.id.et_edit_cabangbank:
                    validateCabang();
                    break;
                case R.id.et_edit_rekening_kata_sandi:
                    validatePassword();
                    break;


            }
        }
    }
    public void messageTambah() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Informasi")
                .setMessage("Rekening berhasil ditambah")
                .setCancelable(false)
                .setPositiveButton("Tutup",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                resetForm();
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public void messageUbah() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Informasi")
                .setMessage("Rekening berhasil diubah")
                .setCancelable(false)
                .setPositiveButton("Tutup",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                resetForm();
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void resetForm(){
        et_edit_namarek.setText("");
        et_edit_rek_katasandi.setText("");
        et_edit_cabangbank.setText("");
        et_edit_norek.setText("");
        spin_namabank.setSelection(0);
    }
}
