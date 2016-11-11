package feup.cmov.cafeteriaadmin.models;


import android.util.Log;

import com.google.gson.Gson;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import feup.cmov.cafeteriaadmin.models.voucher.Voucher;

public class PendingOrder extends SugarRecord{
    private String vouchers;
    private String cart;
    private float finalPrice;
    private String uuid;


    public PendingOrder() {
    }

    public PendingOrder(String vouchers, String cart, float finalPrice, String uuid) {
        this.vouchers = vouchers;
        this.cart = cart;
        this.uuid = uuid;
        this.finalPrice = finalPrice;
    }


    public JSONObject postOrderJSON()
    {
        JSONObject postObject = new JSONObject();

        try {
            String cartString = "[]";
            if(cart.length() > 0)
                cartString = "[" + cart.substring(0, cart.length()-1) + "]";

            String vouchersString = "[]";
            if(vouchers.length() > 0)
            {
                String[] vouchersStringArray = vouchers.substring(0, vouchers.length()-1).split(",");
                vouchersString = "[";
                for (int j = 0; j < vouchersStringArray.length; j++)
                {
                    if(j > 0)
                        vouchersString += ",";

                    vouchersString += "\"" + vouchersStringArray[j] + "\"";
                }
                vouchersString += "]";
            }


            postObject.put("items", cartString);

            postObject.put("uuid", this.uuid);

            postObject.put("vouchers", vouchersString);

            postObject.put("total_price", this.finalPrice);

            Log.d("Vouchers Array", vouchersString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postObject;
    }
}
