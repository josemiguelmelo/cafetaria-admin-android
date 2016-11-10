package feup.cmov.cafeteriaadmin.http;


import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RequestCb {
    public void onResponse(JSONObject response);
    public void onError(VolleyError error);
}
