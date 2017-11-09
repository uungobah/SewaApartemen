package com.apartment.swamedia.sewaapartemenbandung;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.activity.FormLogin;
import com.apartment.swamedia.sewaapartemenbandung.activity.ListNotificationAct;
import com.apartment.swamedia.sewaapartemenbandung.activity.ManagementProperty;
import com.apartment.swamedia.sewaapartemenbandung.activity.PermintaanSewaAct;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentEditProfile;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentHome;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentMessaging;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentNewProperty;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentPengajuanSewa;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentPermintaanSewa;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab1Messaging;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentVerifikasi;
import com.apartment.swamedia.sewaapartemenbandung.fragment.SearchByMaps2;
import com.apartment.swamedia.sewaapartemenbandung.maps.GPSTracker;
import com.apartment.swamedia.sewaapartemenbandung.service.AlarmInboxReceiver;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private Toolbar toolbar;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private DrawerLayout drawer;
    CircularImageView imgProfile;
    SharedPreferences sharedpreferences;

    TextView tv_nama, tv_email;

    JSONObject jobj;
    String status;
    GPSTracker gpsTracker;


    private int hot_number = 0;
    private TextView ui_hot = null;

    String iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Kalau mau pakai Icon Pada ActionBar
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
//        getSupportActionBar().setIcon(R.drawable.logohijau2);


        sharedpreferences = getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, Context.MODE_PRIVATE);
        iduser = sharedpreferences.getString("iduser", "");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);

//        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);

        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);

//        navigationView.addHeaderView(header);

        imgProfile = (CircularImageView) header.findViewById(R.id.img_profile);
        tv_nama = (TextView) header.findViewById(R.id.tv_nama);
        tv_email = (TextView) header.findViewById(R.id.tv_email);

        navigationView.setNavigationItemSelectedListener(this);

        getProfile();
        checkLogin();
        getNotifKotakMasuk();


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                FragmentEditProfile fragment = new FragmentEditProfile();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        boolean notifPemilik = getIntent().getBooleanExtra("notifpemilik", false);
        boolean notifPenyewa = getIntent().getBooleanExtra("notifpenyewa", false);
        boolean notifMessage = getIntent().getBooleanExtra("notifmessage", false);

        if (notifPemilik == true) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentPermintaanSewa fragment = new FragmentPermintaanSewa();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        } else if (notifPenyewa == true) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentPengajuanSewa fragment = new FragmentPengajuanSewa();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        } else if (notifMessage == true) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentMessaging fragment = new FragmentMessaging();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentHome fragment = new FragmentHome();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        }


        if (!isNetworkConnected()) {
            Snackbar snackbar = Snackbar
                    .make(navigationView, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            snackbar.show();
        }


    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");

            //You can replace it with your name
            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void checkLogin() {

        if (iduser.equals("")) {
            navigationView.getMenu().findItem(R.id.nav_list_apartment).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_new_apartment).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_edit_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_permintaan).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_verifikasi).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_pengajuan).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_message).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_inorout).setTitle("Login");
        } else {
            navigationView.getMenu().findItem(R.id.nav_list_apartment).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_new_apartment).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_edit_profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_permintaan).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_verifikasi).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_pengajuan).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_message).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_inorout).setTitle("Logout");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
//         getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        // Create your menu...
        if (iduser.equals("")) {
            MenuItem item = menu.findItem(R.id.menu_hotlist);
            item.setVisible(false);
            this.invalidateOptionsMenu();
        } else {
            final View menu_hotlist = menu.findItem(R.id.menu_hotlist).getActionView();
            ui_hot = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
            new MyMenuItemStuffListener(menu_hotlist, "Show hot message") {
                @Override
                public void onClick(View v) {
//                onHotlistSelected();
                    startActivity(new Intent(MainActivity.this, ListNotificationAct.class));
                }
            };
        }
        return true;
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        // Check current message count
//        //boolean haveMessages = mMessageCount != 0;
//
//        // Set 'delete' menu item state depending on count
//        MenuItem loginorout = menu.findItem(R.id.nav_inorout);
//        loginorout.setTitle("Login");
//        //deleteItem.setEnabled(haveMessages);
//
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit_profile) {
            // Handle the camera action
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentEditProfile fragment = new FragmentEditProfile();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_home) {
            // Handle the camera action

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentHome fragment = new FragmentHome();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_message) {
            // Handle the camera action

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentMessaging fragment = new FragmentMessaging();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_location) {
            if (checkLocation() == true) {
                startActivity(new Intent(MainActivity.this, SearchByMaps2
                        .class));
            }
        } else if (id == R.id.nav_list_apartment) {

            startActivity(new Intent(MainActivity.this, ManagementProperty
                    .class));

        } else if (id == R.id.nav_new_apartment) {
            if (checkLocation() == true) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                FragmentNewProperty fragment = new FragmentNewProperty();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
            }

        } else if (id == R.id.nav_verifikasi) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentVerifikasi fragment = new FragmentVerifikasi();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_inorout) {

            startActivity(new Intent(MainActivity.this, FormLogin.class));
            SharedPreferences.Editor edit = sharedpreferences.edit();
            edit.clear();
            edit.commit();
            imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_account));
            tv_nama.setText("Name");
            tv_email.setText("Email");


        } else if (id == R.id.nav_permintaan) {
//            startActivity(new Intent(MainActivity.this, PermintaanSewaAct.class));
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentPermintaanSewa fragment = new FragmentPermintaanSewa();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_pengajuan) {
//            startActivity(new Intent(MainActivity.this, PermintaanSewaAct.class));
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentPengajuanSewa fragment = new FragmentPengajuanSewa();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        iduser = sharedpreferences.getString("iduser", "");
        checkLogin();
        getNotifKotakMasuk();
        invalidateOptionsMenu();
    }

    public void getProfile() {
        String tag_json_obj = "json_obj_profile";
        String url = ConstantUtil.WEB_SERVICE.URL_PROFIL;
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
                                JSONObject obj = jobj.getJSONObject("data");

                                if (status.equals("true")) {


                                    tv_nama.setText(obj.getString("first_name") + " " + obj.getString("last_name"));
                                    tv_email.setText(obj.getString("email"));


                                    Picasso.with(getApplicationContext())
                                            .load(obj.getString("foto"))
                                            .into(imgProfile);

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

                params.put("user_id", iduser);
//                params.put("remember_me","false");
                params.put("act", "2");

                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public boolean checkLocation() {

        boolean status = false;

        gpsTracker = new GPSTracker(this);

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


    // call the updating code on the main thread,
// so we can call this asynchronously
    public void updateHotCount(final int new_hot_number) {
        hot_number = new_hot_number;
        if (ui_hot == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_hot_number == 0)
                    ui_hot.setVisibility(View.INVISIBLE);
                else {
                    ui_hot.setVisibility(View.VISIBLE);
                    ui_hot.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }

    static abstract class MyMenuItemStuffListener implements View.OnClickListener, View.OnLongClickListener {
        private String hint;
        private View view;

        MyMenuItemStuffListener(View view, String hint) {
            this.view = view;
            this.hint = hint;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        abstract public void onClick(View v);

        @Override
        public boolean onLongClick(View v) {
            final int[] screenPos = new int[2];
            final Rect displayFrame = new Rect();
            view.getLocationOnScreen(screenPos);
            view.getWindowVisibleDisplayFrame(displayFrame);
            final Context context = view.getContext();
            final int width = view.getWidth();
            final int height = view.getHeight();
            final int midy = screenPos[1] + height / 2;
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            Toast cheatSheet = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
            if (midy < displayFrame.height()) {
                cheatSheet.setGravity(Gravity.TOP | Gravity.RIGHT,
                        screenWidth - screenPos[0] - width / 2, height);
            } else {
                cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
            }
            cheatSheet.show();
            return true;
        }
    }

    public void getNotifKotakMasuk() {
        //final String data_pot_bencana = data_potensi_bencana;
        String tag_json_obj = "json_obj";
        String url = ConstantUtil.WEB_SERVICE.URL_NOTIF_PESAN;
        Log.v("URL", url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("List Notif Kotak Masuk", response.toString());

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
                            JSONObject obj = jobj.getJSONObject("data");
                            try {
                                hot_number = Integer.parseInt(obj.getString("total"));
                                updateHotCount(hot_number);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            updateHotCount(hot_number);
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
                params.put("user_id", iduser);
                params.put("typeNotif", "ALL_NOTIF");
                return params;
            }
        };
        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}
