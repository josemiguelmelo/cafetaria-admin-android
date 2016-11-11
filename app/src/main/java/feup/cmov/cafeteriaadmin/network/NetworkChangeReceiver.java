package feup.cmov.cafeteriaadmin.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.List;

import feup.cmov.cafeteriaadmin.http.Http;
import feup.cmov.cafeteriaadmin.http.RequestCb;
import feup.cmov.cafeteriaadmin.models.PendingOrder;

public class NetworkChangeReceiver extends BroadcastReceiver {
    Http http;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d("Internet state", "Internet state changed");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        http = new Http(context, new RequestCb() {
            @Override
            public void onResponse(JSONObject response) {
                List<PendingOrder> pendingOrderList = PendingOrder.listAll(PendingOrder.class);
                Log.d("Pending orders", "# " + pendingOrderList.size());
                for(int i = 0; i < pendingOrderList.size() ; i++)
                {
                    PendingOrder pendingOrder = pendingOrderList.get(i);

                    Log.d("Pending order", "Send to server " + pendingOrder.postOrderJSON().toString());
                    http.post("/orders/store", pendingOrder.postOrderJSON(), new RequestCb() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Pending order delete", pendingOrder.postOrderJSON().toString());
                            pendingOrder.delete();
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.d("Pending order", "Could not send it.");
                        }
                    });
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.d("Network state" , "Internet not available");
            }
        });

    }
}