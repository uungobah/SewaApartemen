package com.apartment.swamedia.sewaapartemenbandung.fragment;


import android.app.Activity;
import android.app.Dialog;
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
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.activity.EditProperty;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.mikhaellopez.circularimageview.CircularImageView;
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

import static android.R.style.Theme_DeviceDefault;
import static android.R.style.Theme_DeviceDefault_Light;
import static android.R.style.Theme_DeviceDefault_NoActionBar_TranslucentDecor;
import static android.R.style.Theme_Translucent;

/**
 * Created by Nurul Akbar on 16/11/2015.
 */


public class FragmentMenuEditTab1 extends Fragment {

    TextView tv_change_picture;

    // ImageView
    CircularImageView img_picture;

    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private String selectedImagePath;

    EditText et_edit_first_name, et_edit_last_name, et_edit_email, et_edit_phone, et_edit_about, et_fb, et_twitter, et_google, et_linkedln;

    AppCompatButton btn_save, btn_popup;

    RadioGroup rg_gender;
    RadioButton rb_male, rb_female;

    View v;

    Dialog dialog;
    JSONObject jobj;
    String status;

    SharedPreferences sharedPreferences;

    String s_tipe, s_img_profil, s_first_name, s_last_name, s_jk, s_email, s_nohp, s_username, s_password, s_repassword, s_ktp;

    String s_about, s_fb, s_twitter, s_google, s_linkedin;

    String kode;

    Context ctx;

    EditText et_kode, et_nohp;

    TextView tv_status_ver_ktp, tv_status_ver_hp;

    AppCompatButton verifikasi, kirimKode;

    private static final int REQUEST_CODE_KTP = 2;
    private static final int REQUEST_CODECAMERA = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab1_edit_profil, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedPreferences = getActivity().getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);

        ctx = v.getContext();
        inisialisasi();

        getProfile();
        cekVerifikasi();
        tv_change_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(
//                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                startActivityForResult(i, REQUEST_CODE);

                selectImage();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParameter();
                simpanPost();
            }
        });

        btn_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });

        return v;


    }

    public void popup() {
        dialog = new Dialog(ctx, Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.popup_verifikasi_nohp);

        verifikasi = (AppCompatButton) dialog.findViewById(R.id.btn_verifikasi);
        kirimKode = (AppCompatButton) dialog.findViewById(R.id.btn_send_code);
        et_kode = (EditText) dialog.findViewById(R.id.et_verifikasi_kode);
        et_nohp = (EditText) dialog.findViewById(R.id.et_verifikasi_nohp);

        et_nohp.setText(et_edit_phone.getText().toString());
        et_nohp.setEnabled(false);
        // if button is clicked, close the custom dialog
        kirimKode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimKode();
            }
        });

        verifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kode = et_kode.getText().toString();
                cekKode();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void inisialisasi() {
        tv_change_picture = (TextView) v.findViewById(R.id.tv_change_picture);

        et_edit_first_name = (EditText) v.findViewById(R.id.et_edit_firstname);
        et_edit_last_name = (EditText) v.findViewById(R.id.et_edit_lastname);
        et_edit_email = (EditText) v.findViewById(R.id.et_edit_email);
        et_edit_phone = (EditText) v.findViewById(R.id.et_edit_phone_number);
        et_edit_about = (EditText) v.findViewById(R.id.et_edit_about_me);
        et_fb = (EditText) v.findViewById(R.id.et_edit_fb);
        et_twitter = (EditText) v.findViewById(R.id.et_edit_twitter);
        et_google = (EditText) v.findViewById(R.id.et_edit_google);
        et_linkedln = (EditText) v.findViewById(R.id.et_edit_linked);

        rg_gender = (RadioGroup) v.findViewById(R.id.rg_edit_sex);
        rb_male = (RadioButton) v.findViewById(R.id.rb_edit_sex1);
        rb_female = (RadioButton) v.findViewById(R.id.rb_edit_sex2);

        btn_save = (AppCompatButton) v.findViewById(R.id.btn_edit_profil);
        btn_popup = (AppCompatButton) v.findViewById(R.id.btn_popup_verifikasi);

        // ImageView
        img_picture = (CircularImageView) v.findViewById(R.id.img_change_picture);

        s_email = "";
        s_first_name = "";
        s_img_profil = "";
        s_jk = "";
        s_last_name = "";
        s_nohp = "";
        s_about = "";
        s_fb = "";
        s_twitter = "";
        s_google = "";
        s_linkedin = "";

        kode = "";

        tv_status_ver_hp = (TextView) v.findViewById(R.id.tv_status_verfikasi_hp);
        tv_status_ver_ktp = (TextView) v.findViewById(R.id.tv_status_verfikasi_ktp);

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

                img_picture.setBackgroundResource(0);


                img_picture.setImageBitmap(bitmap);


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
            img_picture.setImageBitmap(thumbnail);


        }

    }

    public void getParameter() {

        s_email = et_edit_email.getText().toString();
//        s_username = et_ed.getText().toString();
        s_first_name = et_edit_first_name.getText().toString();
        s_last_name = et_edit_last_name.getText().toString();
        s_nohp = et_edit_phone.getText().toString();
        s_img_profil = getStringBase64Bitmap(getImgProfile());

        int id = rg_gender.getCheckedRadioButtonId();
        if (R.id.rb_edit_sex1 == id) {
            s_jk = "PRIA";

        } else if (R.id.rb_edit_sex2 == id) {
            s_jk = "WANITA";
        }

        s_about = et_edit_about.getText().toString();
        s_fb = et_fb.getText().toString();
        s_twitter = et_twitter.getText().toString();
        s_google = et_google.getText().toString();
        s_linkedin = et_linkedln.getText().toString();

    }

    public static String getStringBase64Bitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[] bitmapBytes = bos.toByteArray();
        String encodedImage = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        Log.i("getStringBase64Bitmap", encodedImage);
        return encodedImage;
    }


    public void getProfile() {
        String tag_json_obj = "json_obj_profile";
        String url = ConstantUtil.WEB_SERVICE.URL_PROFIL;
        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
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
                                JSONObject obj = jobj.getJSONObject("data");

                                if (status.equals("true")) {

//                                    http://172.17.1.31:89/sab/uploads/profile/

                                    et_edit_first_name.setText(obj.getString("first_name"));
                                    et_edit_last_name.setText(obj.getString("last_name"));
                                    et_edit_email.setText(obj.getString("email"));
                                    et_edit_phone.setText(obj.getString("hp"));
                                    et_edit_about.setText(obj.getString("about"));
                                    et_fb.setText(obj.getString("facebook"));
                                    et_twitter.setText(obj.getString("twitter"));
                                    et_google.setText(obj.getString("google"));
                                    et_linkedln.setText(obj.getString("linkedin"));


                                    String gender = obj.getString("gender");

                                    if (gender.equals("WANITA")) {
                                        rb_female.setChecked(true);
                                    } else if (gender.equals("PRIA")) {
                                        rb_male.setChecked(true);
                                    }


                                    Picasso.with(getActivity())
                                            .load(obj.getString("foto"))
                                            .into(img_picture);

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

                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void cekVerifikasi() {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_POST_CEK_VERIFIKASI;
        final String iduser = sharedPreferences.getString("iduser", "");
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
                                        tv_status_ver_ktp.setText("KTP (" + c.getString("status") + ")");
                                    } else {
                                        tv_status_ver_hp.setText("HP   (" + c.getString("status") + ")");
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

    public void simpanPost() {
        String tag_json_obj = "json_obj_update_profil";
        String url = ConstantUtil.WEB_SERVICE.URL_UPDATE_PROFIL;
        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Update Data...");
        pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Update Profil", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");
                                if (status.equals("true")) {

//                                    getProfile();
                                    message();
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
                params.put("first_name", s_first_name);
                params.put("last_name", s_last_name);
                params.put("gender", s_jk);
                params.put("hp", s_nohp);
                params.put("about_me", s_about);
                params.put("fb_profile", s_fb);
                params.put("twitter_profile", s_twitter);
                params.put("google_profile", s_google);
                params.put("linkedin_profile", s_linkedin);
                params.put("p_profile", s_img_profil);

                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public Bitmap getImgProfile() {
        Bitmap bitmap = ((BitmapDrawable) img_picture.getDrawable()).getBitmap();
        return bitmap;
    }


    public void kirimKode() {
        String tag_json_obj = "json_obj_kirim_kode";
        String url = ConstantUtil.WEB_SERVICE.URL_KIRIM_KODE;
        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Kirim Kode", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("success")) {
                                status = jobj.getString("success");
                                if (status.equals("true")) {
                                    Toast.makeText(ctx, "Permintaan terkirim", Toast.LENGTH_SHORT).show();
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

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void cekKode() {
        String tag_json_obj = "json_obj_cek_kode";
        String url = ConstantUtil.WEB_SERVICE.URL_CEK_KODE;
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
                            if (jobj.has("success")) {
                                status = jobj.getString("success");

                                if (status.equals("true")) {
                                    getProfile();
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
                params.put("kode_sms", kode);
                params.put("phone1", et_nohp.getText().toString());

                Log.d("param", params.toString());
                return params;
            }
        };


        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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

    public void message() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);

        alertDialogBuilder
                .setTitle("Informasi")
                .setMessage("Ubah profil berhasil!")
                .setCancelable(false)
                .setPositiveButton("Tutup",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getProfile();
                                dialog.dismiss();

                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
