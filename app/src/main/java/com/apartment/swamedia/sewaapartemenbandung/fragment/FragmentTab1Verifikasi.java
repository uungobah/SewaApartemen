package com.apartment.swamedia.sewaapartemenbandung.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.MainActivity;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.RoundedImageView;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swa on 12/15/2015.
 */
public class FragmentTab1Verifikasi extends Fragment {

    View v;
    Context ctx;

    JSONObject jobj;
    String status;

    String iduser;

    TextView tv_status_ver;

    SharedPreferences sharedPreferences;

    ImageView img_ktp;

    CardView cv;

    private static final int REQUEST_CODE_KTP = 2;
    private static final int REQUEST_CODECAMERA = 3;

    AppCompatButton btnVer;

    private Bitmap bitmap;

    TextView selectKtp;

    ImageView iv1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab1_verifikasi, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ctx = v.getContext();
        inisialisasi();

        sharedPreferences = ctx.getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        cekVerifikasi();

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekKtp();
            }
        });

        selectKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        return v;


    }

    public void inisialisasi() {
        tv_status_ver = (TextView) v.findViewById(R.id.tv_status_verktp);

        cv = (CardView) v.findViewById(R.id.cv_verifikasi1);
        img_ktp = (ImageView) v.findViewById(R.id.img_select_ktp_verifikasi);
        btnVer = (AppCompatButton) v.findViewById(R.id.btn_verifikasi_ktp);
        selectKtp = (TextView) v.findViewById(R.id.tv_select_ktp);

        iv1 = (ImageView) v.findViewById(R.id.img_verifikasi1);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
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

    public void cekKtp() {
        String tag_json_obj = "json_obj_cek_kode";
        String url = ConstantUtil.WEB_SERVICE.URL_POST_CEK_KTP;
        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        // pDialog.setMessage("Searching Supervisor's Data...");
        // pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Cek Kode", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("status")) {
                                status = jobj.getString("status");

                                if (status.equals("T")) {

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
//                params.put("remember_me","false");
                params.put("act", "2");
                params.put("ktp", getStringBase64Bitmap(getKtp()));
                Log.d("param", params.toString());
                return params;
            }
        };


        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void cekVerifikasi() {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_POST_CEK_VERIFIKASI;
        iduser = sharedPreferences.getString("iduser", "");
        Log.v("URL", url);
        final ProgressDialog pDialog = new ProgressDialog(ctx);
//        pDialog.setMessage("Loading...");
//        pDialog.show();

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

                            }

                            if (status.equals("true")) {
                                JSONArray jarr = jobj.getJSONArray("data");
                                for (int i = 0; i < jarr.length(); i++) {
                                    JSONObject c = jarr.getJSONObject(i);
                                    if (c.getString("type").toString().equals("KTP")) {
                                        tv_status_ver.setText("KTP (" + c.getString("status") + ")");
                                    }

                                    if (c.get("codeStatus").toString().equals("1") && c.getString("type").toString().equals("KTP")) {
                                        cv.setVisibility(View.GONE);
                                        iv1.setVisibility(View.VISIBLE);
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


                return params;
            }

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public static String getStringBase64Bitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[] bitmapBytes = bos.toByteArray();
        String encodedImage = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        Log.i("getStringBase64Bitmap", encodedImage);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;


        if (requestCode == REQUEST_CODE_KTP && resultCode == Activity.RESULT_OK) {
            try {
                // recyle unused bitmaps

                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = ctx.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);

                Log.v("sab", filePath);
                cursor.close();

                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap = null;
                }

                bitmap = BitmapFactory.decodeFile(filePath);

                img_ktp.setBackgroundResource(0);


                img_ktp.setImageBitmap(bitmap);


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
            img_ktp.setImageBitmap(thumbnail);


        }


    }

    public Bitmap getKtp() {
        Bitmap bitmap = ((BitmapDrawable) img_ktp.getDrawable()).getBitmap();
        return bitmap;
    }

}
