package com.apartment.swamedia.sewaapartemenbandung.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Nurul Akbar on 16/11/2015.
 */
public class FragmentMenuEditTab2 extends Fragment {
    View v;
    EditText et_current_pass, et_new_pass, et_re_pass;

    TextInputLayout till_current_pass, till_newpass, till_repass;

    AppCompatButton btnSave;

    SharedPreferences sharedPreferences;

    JSONObject jobj;

    String status;

    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.tab2_edit_profil, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ctx = v.getContext();
        sharedPreferences = getActivity().getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);
        inisialisasi();

        et_current_pass.addTextChangedListener(new MyTextWatcher(et_current_pass));
        et_new_pass.addTextChangedListener(new MyTextWatcher(et_new_pass));
        et_re_pass.addTextChangedListener(new MyTextWatcher(et_re_pass));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        return v;
    }

    public void inisialisasi() {
        et_current_pass = (EditText) v.findViewById(R.id.et_edit_current_pass);
        et_new_pass = (EditText) v.findViewById(R.id.et_edit_newpassword);
        et_re_pass = (EditText) v.findViewById(R.id.et_edit_repassword);

        btnSave = (AppCompatButton) v.findViewById(R.id.btn_edit_password);

        till_current_pass = (TextInputLayout) v.findViewById(R.id.input_layout_edit_current_pass);
        till_newpass = (TextInputLayout) v.findViewById(R.id.input_layout_edit_newpassword);
        till_repass = (TextInputLayout) v.findViewById(R.id.input_layout_edit_repassword);
    }


    public void submitForm() {
        int x = 0;
        if (!validateCurrentPassword()) {
            x = x + 1;
            return;
        }
        if (!validateNewPassword()) {
            x = x + 1;
            return;
        }
        if (!validateRePassword()) {
            x = x + 1;
            return;
        }

        if (x == 0) {
            simpanPost();
        }

    }

    private boolean validateCurrentPassword() {
        if (et_current_pass.getText().toString().trim().isEmpty()) {

            till_current_pass.setError(getString(R.string.err_msg_password));
            requestFocus(et_current_pass);
            return false;
        } else {
            till_current_pass.setErrorEnabled(false);
        }
        return true;

    }

    private boolean validateNewPassword() {
        if (et_new_pass.getText().toString().trim().isEmpty()) {
            till_newpass.setError(getString(R.string.err_msg_newpassword));
            requestFocus(et_new_pass);
            return false;
        } else {
            till_newpass.setErrorEnabled(false);

        }
        return true;
    }

    private boolean validateRePassword() {
        if (et_re_pass.getText().toString().trim().isEmpty()) {
            till_repass.setError("Enter your new re-password");
            requestFocus(et_re_pass);
            return false;
        } else if (!et_re_pass.getText().toString().equals(et_new_pass.getText().toString())) {
            till_repass.setError("Password tidak sama");
            requestFocus(et_re_pass);
            return false;
        } else {
            till_repass.setErrorEnabled(false);

        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void simpanPost() {
        String tag_json_obj = "json_obj_update_password";
        String url = ConstantUtil.WEB_SERVICE.URL_UPDATE_PASSWORD;
        final String id_user = sharedPreferences.getString("iduser", "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Update Data...");
        pDialog.show();

        Log.i("url", "url :" + url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("List Bank", response.toString());

                        try {
                            jobj = new JSONObject(response);
                            if (jobj.has("status")) {
                                status = jobj.getString("status");


                                if (status.equals("T")) {
                                    et_current_pass.setText("");
                                    et_new_pass.setText("");
                                    et_re_pass.setText("");
                                    Toast.makeText(getActivity(),"Password Berhasil Diganti",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getActivity(),jobj.getString("message"),Toast.LENGTH_LONG).show();
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
                params.put("current_password", et_current_pass.getText().toString());
                params.put("new_password", et_new_pass.getText().toString());
                params.put("re_password", et_re_pass.getText().toString());


                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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
                case R.id.et_edit_current_pass:
                    validateCurrentPassword();
                    break;
                case R.id.et_edit_newpassword:
                    validateNewPassword();
                    break;
                case R.id.et_edit_repassword:
                    validateRePassword();
                    break;

            }
        }
    }
}
