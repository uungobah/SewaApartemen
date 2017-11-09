package com.apartment.swamedia.sewaapartemenbandung.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.apartment.swamedia.sewaapartemenbandung.MainActivity;
import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.controller.AppController;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentNewProperty;
import com.apartment.swamedia.sewaapartemenbandung.util.ConstantUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class NotifInboxServices extends Service {


    int mStartMode;
    SharedPreferences pref;
    NotificationManager manager;
    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;

    String user_id;

    public NotifInboxServices() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("static-access")
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        pref = getApplicationContext().getSharedPreferences(ConstantUtil.SHAREDPREFERENCE.LOGIN, getApplicationContext().MODE_PRIVATE);
        user_id = pref.getString("iduser", "");
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        if (!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }

        return mStartMode;
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        //wakeLock.release();

    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            if (isConnectingToInternet(context)) {

                if (checkLogin() == true){
                    getNotifPemilik();
                    getNotifPenyewa();
                    getNotifKotakMasuk();

                }else{
                }

                stopSelf();
            }

        }
    };

    public void getNotifPemilik() {
        //final String data_pot_bencana = data_potensi_bencana;
        String tag_json_obj = "json_obj";


        String url = ConstantUtil.WEB_SERVICE.URL_LIST_NOTIF_PEMILIK;
        Log.v("URL", url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("List Notif", response.toString());

                JSONObject jobj = null;
                String title = "";
                try {
                    jobj = new JSONObject(response);
                    title = jobj.getString("template_notifikasi");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response != null) {

                    //response = "[{\"iduser\":\"2\",\"idinbox\":\"1\",\"timesend\":\"2015-10-28 00:00:00\",\"name\":\"Lucky Indrawan\"},{\"iduser\":\"3\",\"idinbox\":\"12\",\"timesend\":\"2015-11-10 09:20:37\",\"name\":\"Moch Pakih Kamaludin\"}]";
                    JSONArray notif_array = null;
                    try {
                        notif_array = jobj.getJSONArray("data");
                        if (notif_array.length() != 0) {
                            for (int i = 0; i < notif_array.length(); i++) {
                                JSONObject notif_obj = notif_array.getJSONObject(i);

                                if (notif_obj.get("statusNotifMobileForce").equals("UNOPENED")) {

                                    PendingIntent ip = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class).putExtra("notifpemilik", true), 0);
                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(context)
                                                    .setSmallIcon(R.drawable.logohijau)
                                                    .setContentTitle(title)
                                                    .setContentText(notif_obj.getString("postJudul"))
                                                    .setContentIntent(ip);
                                    mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
                                    mBuilder.setAutoCancel(true);
//
//                                Notification note = new Notification(R.drawable.logohijau, "Android Example Status message!", System.currentTimeMillis());
//
//                                note.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
//
//                                PendingIntent ip = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class).putExtra("notif",true), 0);
//
//
//                                note.setLatestEventInfo(getApplicationContext(), "Inbox Notification", notif_obj.getString("name") + " kirim pesan baru di Inbox", ip);

                                    manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.notify(i, mBuilder.build());
                                } else {

                                }

                            }
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
                params.put("user_id", user_id);
                params.put("type", "1");

                return params;
            }
        };

        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public void getNotifPenyewa() {
        //final String data_pot_bencana = data_potensi_bencana;
        String tag_json_obj = "json_obj";


        String url = ConstantUtil.WEB_SERVICE.URL_LIST_NOTIF_PENYEWA;
        Log.v("URL", url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("List Notif", response.toString());

                JSONObject jobj = null;
                String title = "";
                try {
                    jobj = new JSONObject(response);
                    title = jobj.getString("template_notifikasi");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response != null) {

                    //response = "[{\"iduser\":\"2\",\"idinbox\":\"1\",\"timesend\":\"2015-10-28 00:00:00\",\"name\":\"Lucky Indrawan\"},{\"iduser\":\"3\",\"idinbox\":\"12\",\"timesend\":\"2015-11-10 09:20:37\",\"name\":\"Moch Pakih Kamaludin\"}]";
                    JSONArray notif_array = null;
                    try {
                        notif_array = jobj.getJSONArray("data");
                        if (notif_array.length() != 0) {
                            for (int i = 0; i < notif_array.length(); i++) {
                                JSONObject notif_obj = notif_array.getJSONObject(i);

                                if (notif_obj.get("statusNotifMobileForce").equals("UNOPENED")) {
                                    PendingIntent ip = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class).putExtra("notifpenyewa", true), 1);

                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(context)
                                                    .setSmallIcon(R.drawable.logohijau)
                                                    .setContentTitle(title)
                                                    .setContentText(notif_obj.getString("postJudul"))
                                                    .setContentIntent(ip);
                                    mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
                                    mBuilder.setAutoCancel(true);
//
//                                Notification note = new Notification(R.drawable.logohijau, "Android Example Status message!", System.currentTimeMillis());
//
//                                note.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
//
//                                PendingIntent ip = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class).putExtra("notif",true), 0);
//
//
//                                note.setLatestEventInfo(getApplicationContext(), "Inbox Notification", notif_obj.getString("name") + " kirim pesan baru di Inbox", ip);

                                    manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.notify(i, mBuilder.build());
                                } else {

                                }

                            }
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
                params.put("user_id", user_id);
                params.put("type", "2");
                return params;
            }
        };
        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getNotifKotakMasuk() {
        //final String data_pot_bencana = data_potensi_bencana;
        String tag_json_obj = "json_obj";
        String url = ConstantUtil.WEB_SERVICE.URL_NOTIF_PESAN;
        Log.v("URL", url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("List Notif", response.toString());

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


                            //response = "[{\"iduser\":\"2\",\"idinbox\":\"1\",\"timesend\":\"2015-10-28 00:00:00\",\"name\":\"Lucky Indrawan\"},{\"iduser\":\"3\",\"idinbox\":\"12\",\"timesend\":\"2015-11-10 09:20:37\",\"name\":\"Moch Pakih Kamaludin\"}]";
                            JSONArray notif_array = null;
                            try {
                                notif_array = jobj.getJSONArray("data");

                                if (notif_array.length() != 0) {
                                    for (int i = 0; i < notif_array.length(); i++) {
                                        JSONObject notif_obj = notif_array.getJSONObject(i);
                                        title = notif_obj.getString("sender");
                                        if (notif_obj.get("statusNotification").equals("UNOPENED")) {
                                            PendingIntent ip = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class).putExtra("notifmessage", true), 2);

                                            NotificationCompat.Builder mBuilder =
                                                    new NotificationCompat.Builder(context)
                                                            .setSmallIcon(R.drawable.logohijau)
                                                            .setContentTitle(title)
                                                            .setContentText(notif_obj.getString("content"))
                                                            .setContentIntent(ip);
                                            mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
                                            mBuilder.setAutoCancel(true);
    //
    //                                Notification note = new Notification(R.drawable.logohijau, "Android Example Status message!", System.currentTimeMillis());
    //
    //                                note.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
    //
    //                                PendingIntent ip = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class).putExtra("notif",true), 0);
    //
    //
    //                                note.setLatestEventInfo(getApplicationContext(), "Inbox Notification", notif_obj.getString("name") + " kirim pesan baru di Inbox", ip);

                                            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            manager.notify(i, mBuilder.build());
                                        } else {

                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                params.put("user_id", user_id);
                params.put("typeNotif", "PUSH");
                return params;
            }
        };
        // AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public boolean checkLogin() {
        boolean status = false;
        if (user_id.equals("")) {
            status = false;
        } else {
            status = true;
        }

        return status;
    }
}
