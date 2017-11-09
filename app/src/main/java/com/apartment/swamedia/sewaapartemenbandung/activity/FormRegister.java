package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.apartment.swamedia.sewaapartemenbandung.MainActivity;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nurul Akbar on 12/10/2015.
 */
public class FormRegister extends ActionBarActivity {

    private EditText et_firstname, et_lastname, et_phone_number, et_email, et_username, et_password, et_repassword;

    // Text Input Layout
    private TextInputLayout til_firstname, til_lastname, til_phone_number, til_email, til_username, til_password, til_repassword;

    //AppCompatButton
    private AppCompatButton btnRegister;

    //TextView
    TextView tv_login, tv_msg_error_rg, tv_msg_error_tipe, tv_select_picture, tv_select_ktp,tv_msg_success;

    // ImageView
    CircularImageView img_picture;
    ImageView img_ktp;

    //RadioGroup
    RadioGroup rg_sex;

    //RadioButton
    RadioButton rb_sex1, rb_sex2;

    private AppCompatSpinner spinTipePelanggan;

    private static final int REQUEST_CODE_PROFIL = 1;
    private static final int REQUEST_CODE_KTP = 2;
    private static final int REQUEST_CODECAMERA = 3;
    private Bitmap bitmap;

    String s_tipe, s_img_profil, s_first_name, s_last_name, s_jk, s_email, s_nohp, s_username, s_password, s_repassword, s_ktp;

    JSONObject jobj;
    String status;

    public FormRegister() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        inisialisasi();


        et_password.addTextChangedListener(new MyTextWatcher(et_password));
        et_repassword.addTextChangedListener(new MyTextWatcher(et_repassword));
        et_firstname.addTextChangedListener(new MyTextWatcher(et_firstname));
        et_lastname.addTextChangedListener(new MyTextWatcher(et_lastname));
        et_email.addTextChangedListener(new MyTextWatcher(et_email));
        et_phone_number.addTextChangedListener(new MyTextWatcher(et_phone_number));


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("pencet button","pencet button");
                submitForm();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FormRegister.this, FormLogin.class));
                finish();
            }
        });

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                validateSex();
            }
        });

        tv_select_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, REQUEST_CODE_PROFIL);
            }
        });


    }

    public void inisialisasi() {
        // Button
        btnRegister = (AppCompatButton) findViewById(R.id.btn_register);
        //EditText

        et_firstname = (EditText) findViewById(R.id.et_register_firstname);
        et_lastname = (EditText) findViewById(R.id.et_register_lastname);
        et_email = (EditText) findViewById(R.id.et_register_email);
        et_phone_number = (EditText) findViewById(R.id.et_register_phone_number);
        et_password = (EditText) findViewById(R.id.et_register_password);
        et_repassword = (EditText) findViewById(R.id.et_register_repassword);

        // TextInputLayout
        til_firstname = (TextInputLayout) findViewById(R.id.input_layout_register_firstname);
        til_lastname = (TextInputLayout) findViewById(R.id.input_layout_register_lastname);

        til_phone_number = (TextInputLayout) findViewById(R.id.input_layout_register_phone_number);
        til_email = (TextInputLayout) findViewById(R.id.input_layout_register_email);
        til_password = (TextInputLayout) findViewById(R.id.input_layout_register_password);
        til_repassword = (TextInputLayout) findViewById(R.id.input_layout_register_repassword);

        //TextView
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_msg_error_rg = (TextView) findViewById(R.id.tv_msg_error_rg);
        tv_select_picture = (TextView) findViewById(R.id.tv_select_picture);
        tv_msg_success = (TextView) findViewById(R.id.tv_msg_success_register);

        // ImageView
        img_picture = (CircularImageView) findViewById(R.id.img_select_picture);
        img_ktp = (ImageView) findViewById(R.id.img_select_ktp);


//        img_picture.setBorderColor(Color.GRAY);
////        img_picture.setBorderColor(getResources().getColor(R.color.GrayLight));
//        img_picture.setBorderWidth(10);
//// Add Shadow with default param
//        img_picture.addShadow();
//// or with custom param
//        img_picture.setShadowRadius(15);
//        img_picture.setShadowColor(Color.RED);

        //RadioGroup
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        // RadioButton
        rb_sex1 = (RadioButton) findViewById(R.id.rb_sex1);
        rb_sex2 = (RadioButton) findViewById(R.id.rb_sex2);


        s_email = "";
        s_first_name = "";
        s_img_profil = "";
        s_jk = "";
        s_ktp = "";
        s_last_name = "";
        s_nohp = "";
        s_password = "";
        s_tipe = "";
        s_repassword = "";
        s_username = "";


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



    public void submitForm() {
        Log.d("validasi","validasi");
        int x = 0;

//        if (!validateTipe()) {
//            x = x + 1;
//            return;
//        }

        if (!validateFirstName()) {
            x = x + 1;
            return;
        }

        if (!validateLastName()) {
            x = x + 1;
            return;
        }



        if (!validateEmail()) {
            x = x + 1;
            return;
        }

        if (!validatePhoneNumber()) {
            x = x + 1;
            return;
        }



        if (!validatePassword()) {
            x = x + 1;
            return;
        }

        if (!validateRePassword()) {
            x = x + 1;
            return;
        }

        if (x == 0) {
            getParameter();
            submitRegisterPost();

        }
    }

    private boolean validateFirstName() {
        if (et_firstname.getText().toString().trim().isEmpty()) {
            til_firstname.setError(getString(R.string.err_msg_firstname));
            requestFocus(et_firstname);
            return false;
        } else {
            til_firstname.setErrorEnabled(false);

        }
        return true;


    }

    private boolean validateSex() {


        if (rg_sex.getCheckedRadioButtonId() == -1) {//Grp is your radio group object
            tv_msg_error_rg.setVisibility(View.VISIBLE);
            requestFocus(rb_sex2);
            return false;
        } else {
            tv_msg_error_rg.setVisibility(View.GONE);
        }

        return true;


    }

    private boolean validateTipe() {


        if (spinTipePelanggan.getSelectedItemPosition() == 0) {//Grp is your radio group object
            tv_msg_error_tipe.setVisibility(View.VISIBLE);
            requestFocus(spinTipePelanggan);
            return false;
        } else {
            tv_msg_error_tipe.setVisibility(View.GONE);
        }

        return true;


    }

    private boolean validateLastName() {
        if (et_lastname.getText().toString().trim().isEmpty()) {
            til_lastname.setError(getString(R.string.err_msg_lastname));
            requestFocus(et_lastname);
            return false;
        } else {
            til_lastname.setErrorEnabled(false);

        }
        return true;


    }

    private boolean validatePhoneNumber() {
        if (et_phone_number.getText().toString().trim().isEmpty()) {
            til_phone_number.setError(getString(R.string.err_msg_phone_number));
            requestFocus(et_phone_number);
            return false;
        } else {
            til_phone_number.setErrorEnabled(false);

        }
        return true;


    }

    private boolean validateEmail() {
        String email = et_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            til_email.setError(getString(R.string.err_msg_email));
            requestFocus(et_email);
            return false;
        } else {
            til_email.setErrorEnabled(false);
        }

        return true;
    }



    private boolean validatePassword() {
        if (et_password.getText().toString().trim().isEmpty()) {
            til_password.setError(getString(R.string.err_msg_password));
            requestFocus(et_password);
            return false;
        } else if (et_password.getText().toString().length() <= 5) {
            til_password.setError("Password Harus Lebih Dari 5 Karakter");
            requestFocus(et_password);
            return false;
        } else {
            til_password.setErrorEnabled(false);

        }
        return true;


    }

    private boolean validateRePassword() {
        if (et_repassword.getText().toString().trim().isEmpty()) {
            til_repassword.setError(getString(R.string.err_msg_password));
            requestFocus(et_repassword);
            return false;
        } else if (et_repassword.getText().toString().length() <= 5) {
            til_repassword.setError("Password Harus Lebih Dari 5 Karakter");
            requestFocus(et_repassword);
            return false;
        } else if (!et_repassword.getText().toString().equals(et_password.getText().toString())) {
            til_repassword.setError("Password Harus Sama");
            requestFocus(et_repassword);
            return false;
        } else {
            til_repassword.setErrorEnabled(false);

        }
        return true;


    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                case R.id.et_register_firstname:
                    validateFirstName();
                    break;
                case R.id.et_register_lastname:
                    validateLastName();
                    break;
                case R.id.et_register_password:
                    validatePassword();
                    break;
                case R.id.et_register_email:
                    validateEmail();
                    break;
                case R.id.et_register_phone_number:
                    validatePhoneNumber();
                    break;
                case R.id.et_register_repassword:
                    validateRePassword();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;


        if (requestCode == REQUEST_CODE_PROFIL && resultCode == Activity.RESULT_OK) {
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

                img_picture.setBackgroundResource(0);

                // Create Circle Image
                RoundedImageView r = new RoundedImageView(this);
//                img_picture.setImageBitmap(r.getCroppedBitmap(bitmap, 10));

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

        } else if (requestCode == REQUEST_CODE_KTP && resultCode == Activity.RESULT_OK) {
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

    public void getParameter() {

        s_email = et_email.getText().toString();
//        s_username = et_username.getText().toString();
        s_first_name = et_firstname.getText().toString();
        s_last_name = et_lastname.getText().toString();
        s_nohp = et_phone_number.getText().toString();
        s_password = et_password.getText().toString();
        s_repassword = et_repassword.getText().toString();
//        s_tipe = String.valueOf(spinTipePelanggan.getSelectedItemPosition());
//        s_img_profil = getStringBase64Bitmap(getImgProfile());
//        s_ktp = getStringBase64Bitmap(getKtp());
//
//        int id = rg_sex.getCheckedRadioButtonId();
//        if (R.id.rb_sex1 == id) {
//            s_jk = "PRIA";
//
//        } else if (R.id.rb_sex1 == id) {
//            s_jk = "WANITA";
//        }

    }

    public static String getStringBase64Bitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[] bitmapBytes = bos.toByteArray();
        String encodedImage = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        Log.i("getStringBase64Bitmap", encodedImage);
        return encodedImage;
    }

    public Bitmap getKtp() {
        Bitmap bitmap = ((BitmapDrawable) img_ktp.getDrawable()).getBitmap();
        return bitmap;
    }

    public Bitmap getImgProfile() {
        Bitmap bitmap = ((BitmapDrawable) img_ktp.getDrawable()).getBitmap();
        return bitmap;
    }

    public void submitRegisterPost() {
        String tag_json_obj = "json_obj_req";

        String url = ConstantUtil.WEB_SERVICE.URL_REGISTER;

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
                            if (status.equals("Success")){
//                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                finish();
                                tv_msg_success.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(getApplication(), "Register Gagal", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplication(), "Register Gagal Ada Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("useremail", s_email);
                params.put("first_name", s_first_name);
                params.put("last_name", s_last_name);
                params.put("phone", s_nohp);
                params.put("password", s_password);
                params.put("repassword", s_repassword);
//                params.put("gender", s_jk);
//                params.put("ktp", s_ktp);
                params.put("user_type","2");
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

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
