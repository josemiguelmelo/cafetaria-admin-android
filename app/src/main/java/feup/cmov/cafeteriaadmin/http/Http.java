package feup.cmov.cafeteriaadmin.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.InetAddress;

public class Http {

    public static String server = "http://f7ea5888.ngrok.io";

    private Context context;

    public boolean internetAvailable;

    public Http(Context context, RequestCb callback) {
        this.internetAvailable = false;

        this.context = context;

        this.get("/vouchers", callback);
    }

    public Http(Context context) {
        this.internetAvailable = false;

        this.context = context;

        this.get("/vouchers", new RequestCb() {
            @Override
            public void onResponse(JSONObject response) {
                internetAvailable = true;
            }

            @Override
            public void onError(VolleyError error) {
                internetAvailable = false;
            }
        });
    }

    public void get(String url, final RequestCb requestCb) {
        this.request(url, null, Request.Method.GET, requestCb);
    }

    public void post(String url, JSONObject data, final RequestCb requestCb) {
        this.request(url, data, Request.Method.POST, requestCb);
    }

    public void request(String url, JSONObject data, int method, final RequestCb requestCb) {
        RequestQueue queue = Volley.newRequestQueue(this.context);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (method, server + url, data, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Http.this.internetAvailable = true;
                        requestCb.onResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Http.this.internetAvailable = false;
                        requestCb.onError(error);
                        Log.w("HTTP ERROR", error.toString());
                    }
                });

        queue.add(jsObjRequest);
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
