package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.MainActivity;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swa on 1/8/2016.
 */
public class KonfirmasiPembayaran extends ActionBarActivity {

    AppCompatSpinner spin_dari_bank, spin_ke_bank;

    List<String> list_bank_dari;
    ArrayList<HashMap<String, String>> data_nama_bank_dari;
    List<String> list_bank_ke;
    ArrayList<HashMap<String, String>> data_nama_bank_ke;

    JSONObject jobj;
    String status;

    String id_sewa;

    EditText et_order_id,et_norek, et_nama_pemilik, et_jumlah_trf, et_tgl_trf, et_jam_trf;

    ImageView img_bukti_trf;

    TextView tv_select;

    private int hour;
    private int minute;

    static final int TIME_DIALOG_ID = 999;

    int mYear, mMonth, mDay;

    static final int DATE_DIALOG_ID = 1;

    private String[] arrMonth = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    private static final int REQUEST_CODE_KTP = 2;
    private static final int REQUEST_CODECAMERA = 3;

    Bitmap bitmap;

    String idSewa, orderId;

    private TextInputLayout til_nama,til_norek, til_jumlah, til_tanggal, til_jam;

    AppCompatButton btnKonfirmasi;

    TextView tv_err_daribank,tv_err_kebank,tv_err_imgbukti;

    boolean uploadImage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_konfirmasi_pembayaran);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        inisialisai();
        getParameter();
        getBankDari();
        getBankKe();
        setCurrentTimeOnView();
        setCurrentDateOnView();


        et_jam_trf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        et_tgl_trf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(KonfirmasiPembayaran.this, mDateSetListener1, mYear, mMonth, mDay).show();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        spin_dari_bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tv_err_daribank.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_ke_bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tv_err_kebank.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void inisialisai() {
        spin_dari_bank = (AppCompatSpinner) findViewById(R.id.spin_dari_bank);
        spin_ke_bank = (AppCompatSpinner) findViewById(R.id.spin_ke_bank);

        et_order_id = (EditText) findViewById(R.id.et_konfirmasi_orderid);
        et_norek = (EditText) findViewById(R.id.et_konfirmasi_no_rek);
        et_nama_pemilik = (EditText) findViewById(R.id.et_konfirmasi_nama_pemilik);
        et_jumlah_trf = (EditText) findViewById(R.id.et_konfirmasi_jumlah);
        et_tgl_trf = (EditText) findViewById(R.id.et_konfirmasi_tanggal);
        et_jam_trf = (EditText) findViewById(R.id.et_konfirmasi_jam);

        img_bukti_trf = (ImageView) findViewById(R.id.img_bukti_trf);

        tv_select = (TextView) findViewById(R.id.tv_select_buktitrf);

        til_nama = (TextInputLayout) findViewById(R.id.input_layout_konfirmasi_nama_pemilik);
        til_norek = (TextInputLayout) findViewById(R.id.input_layout_konfirmasi_norek);
        til_jumlah = (TextInputLayout) findViewById(R.id.input_layout_konfirmasi_jumlah);
        til_tanggal = (TextInputLayout) findViewById(R.id.input_layout_konfirmasi_tanggal);
        til_jam = (TextInputLayout) findViewById(R.id.input_layout_konfirmasi_jam);

        btnKonfirmasi = (AppCompatButton) findViewById(R.id.btn_send_konfirmasi);

        tv_err_daribank = (TextView) findViewById(R.id.tv_erorr_daribank);
        tv_err_kebank = (TextView) findViewById(R.id.tv_erorr_kebank);
        tv_err_imgbukti = (TextView) findViewById(R.id.tv_erorr_imgbukti);

        et_order_id.setText(orderId);
        et_order_id.setEnabled(false);
    }


    public void setCurrentTimeOnView() {

        // set current time into textview
        et_jam_trf.setText(
                new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)) + ":00");
        // set current time into timepicker
    }


    public void setCurrentDateOnView() {



        // set current time into textview
        if (mMonth < 11){
            mMonth = mMonth + 1;
            et_tgl_trf.setText(
                    new StringBuilder().append(pad(mYear))
                            .append("-").append(pad(mMonth)).append("-").append(pad(mDay)));
        }else{
            mMonth = mMonth +1;
            et_tgl_trf.setText(
                    new StringBuilder().append(pad(mYear))
                            .append("-").append(pad(mMonth)).append("-").append(pad(mDay)));
        }

        // set current time into timepicker
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            String sdate =mYear+"-"+arrMonth[mMonth]+"-"+LPad(mDay + "", "0", 2) ;

            et_tgl_trf.setText(sdate);
        }
    };

    public String LPad(String schar, String spad, int len) {
        String sret = schar;

        for (int i = sret.length(); i < len; i++) {
            sret = spad + sret;
        }

        return new String(sret);
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener, hour, minute, false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;

                    // set current time into textview
                    et_jam_trf.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)) + ":00");


                }
            };

    public void getBankDari() {
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
                        Log.d("List Bank", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");
                                JSONArray jarr = jobj.getJSONArray("data");

                                if (status.equals("true")) {


                                    list_bank_dari = new ArrayList<String>();

                                    list_bank_dari.add("Pilih Bank Asal");

                                    data_nama_bank_dari = new ArrayList<HashMap<String, String>>();
                                    HashMap<String, String> map_bank = new HashMap<String, String>();
                                    map_bank.put("nama", "Pilih Bank");
                                    map_bank.put("id", "0");
                                    data_nama_bank_dari.add(map_bank);

                                    for (int i = 0; i < jarr.length(); i++) {
                                        JSONObject c = jarr.getJSONObject(i);
                                        HashMap<String, String> map_namabank = new HashMap<String, String>();

                                        String name = c.getString("nama");
                                        String id = c.getString("id");

                                        map_namabank.put("nama", name);
                                        map_namabank.put("id", id);

                                        data_nama_bank_dari.add(map_namabank);

                                        list_bank_dari.add(name);

                                    }

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                                            KonfirmasiPembayaran.this,
                                            android.R.layout.simple_spinner_item,
                                            list_bank_dari);
                                    dataAdapter
                                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spin_dari_bank.setAdapter(dataAdapter);


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

    public void getBankKe() {
        String tag_json_obj = "json_obj_nama_bank";
        String url = ConstantUtil.WEB_SERVICE.URL_LIST_BANK_SWA;
        final ProgressDialog pDialog = new ProgressDialog(this);
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("List Bank", response.toString());

                        try {
                            jobj = new JSONObject(response);

                            JSONArray jarr = jobj.getJSONArray("data");

                            list_bank_ke = new ArrayList<String>();

                            list_bank_ke.add("Pilih Bank Tujuan");

                            data_nama_bank_ke = new ArrayList<HashMap<String, String>>();
                            HashMap<String, String> map_bank = new HashMap<String, String>();
                            map_bank.put("nama", "Pilih Bank");
                            map_bank.put("id", "0");
                            data_nama_bank_ke.add(map_bank);

                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject c = jarr.getJSONObject(i);
                                HashMap<String, String> map_namabank = new HashMap<String, String>();

                                String name = c.getString("nameBank");
                                String id = c.getString("idBank");

                                map_namabank.put("nama", name);
                                map_namabank.put("id", id);

                                data_nama_bank_ke.add(map_namabank);

                                list_bank_ke.add(name);

                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                                    KonfirmasiPembayaran.this,
                                    android.R.layout.simple_spinner_item,
                                    list_bank_ke);
                            dataAdapter
                                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spin_ke_bank.setAdapter(dataAdapter);


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

    public void submitKonfirmasiPost() {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_KONFIRMASI_PEMBAYARAN;

        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(this);
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

                            if (jobj.has("message")) {
                                status = jobj.getString("message");
                                Log.v("message", jobj.getString("message"));
                                Toast.makeText(getApplication(), "message " + jobj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            if (status.equals("Success")) {
                                Intent i = new Intent(KonfirmasiPembayaran.this, KonfirmasiPembayaran.class);
                                i.putExtra("idsewa",idSewa);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplication(), "Konfirmasi Gagal", Toast.LENGTH_LONG).show();
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
                params.put("sewaIdModal",idSewa);
                params.put("sewaNoOrderModal", orderId);
                params.put("nomor_rek_pemilik_input", et_norek.getText().toString());
                params.put("nama_pemilik_input", et_nama_pemilik.getText().toString());
                params.put("jumlah_uang_input", et_jumlah_trf.getText().toString());
                params.put("bank_penyewa_input", data_nama_bank_dari.get(spin_dari_bank.getSelectedItemPosition()).get("id"));
                params.put("bank_swamedia_input", data_nama_bank_ke.get(spin_ke_bank.getSelectedItemPosition()).get("id"));
                params.put("tanggal_transfer_input", et_tgl_trf.getText().toString()+" "+et_jam_trf.getText().toString());
                params.put("bukti_transfer_input", getStringBase64Bitmap(getImgBukti()));

                Log.d("Konfirmasi",params.toString());
                return params;
            }

        };

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    Intent i = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(i, REQUEST_CODECAMERA);

                } else if (items[item].equals("Choose from Library")) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, REQUEST_CODE_KTP);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;


        if (requestCode == REQUEST_CODE_KTP && resultCode == Activity.RESULT_OK) {
            try {
                // recyle unused bitmaps

                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);

                Log.v("sab", filePath);
                cursor.close();

                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap = null;
                }

                bitmap = BitmapFactory.decodeFile(filePath);
                img_bukti_trf.setBackgroundResource(0);
                img_bukti_trf.setImageBitmap(bitmap);
                uploadImage = true;


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } else if (requestCode == REQUEST_CODECAMERA && resultCode == Activity.RESULT_OK) {


            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            img_bukti_trf.setImageBitmap(thumbnail);

            uploadImage = true;
        }

    }

    public Bitmap getImgBukti() {
        Bitmap bitmap = ((BitmapDrawable) img_bukti_trf.getDrawable()).getBitmap();
        return bitmap;
    }

    public static String getStringBase64Bitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[] bitmapBytes = bos.toByteArray();
        String encodedImage = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        Log.i("getStringBase64Bitmap", encodedImage);
        return encodedImage;
    }

    private void getParameter() {
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            idSewa = extra.getString("idsewa");
            orderId = extra.getString("orderid");
            Log.d("idSewa",idSewa);
            et_order_id.setText(orderId);
        }


    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateNamaPemilik() {
        if (et_nama_pemilik.getText().toString().trim().isEmpty()) {
            til_nama.setError("Nama pemilik tidak boleh kosong");
            requestFocus(et_nama_pemilik);
            return false;
        } else {
            til_nama.setErrorEnabled(false);

        }
        return true;

    }

    private boolean validateNoRek() {
        if (et_norek.getText().toString().trim().isEmpty()) {
            til_norek.setError("No rekening tidak boleh kosong");
            requestFocus(et_norek);
            return false;
        } else {
            til_norek.setErrorEnabled(false);

        }
        return true;

    }

    private boolean validateJumlah() {
        if (et_jumlah_trf.getText().toString().trim().isEmpty()) {
            til_jumlah.setError("Jumlah transfer tidak boleh kosong");
            requestFocus(et_jumlah_trf);
            return false;
        } else {
            til_jumlah.setErrorEnabled(false);

        }
        return true;

    }

    private boolean validateDariBank() {
        if (spin_dari_bank.getSelectedItemPosition() == 0) {
            tv_err_daribank.setVisibility(View.VISIBLE);
            return false;
        } else {

        }
        return true;

    }

    private boolean validateKeBank() {
        if (spin_ke_bank.getSelectedItemPosition() == 0) {
            tv_err_kebank.setVisibility(View.VISIBLE);
            return false;
        } else {

        }
        return true;

    }

    private boolean validateImgBukti() {
        if (uploadImage == false) {
            tv_err_imgbukti.setVisibility(View.VISIBLE);
            return false;
        } else {

        }
        return true;

    }

    public void submitForm() {
        int x = 0;
        if (!validateNoRek()) {
            x = x + 1;
            return;
        }if (!validateNamaPemilik()) {
            x = x + 1;
            return;
        }
        if (!validateJumlah()) {
            x = x + 1;
            return;
        }
        if (!validateDariBank()) {
            x = x + 1;
            return;
        }
        if (!validateKeBank()) {
            x = x + 1;
            return;
        } if (!validateImgBukti()) {
            x = x + 1;
            return;
        }

        if (x == 0) {

            submitKonfirmasiPost();

        }

    }
}
