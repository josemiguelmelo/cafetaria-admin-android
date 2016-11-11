package feup.cmov.cafeteriaadmin.models;


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
                vouchersString = "[" + vouchers.substring(0, vouchers.length()-1) + "]";


            postObject.put("items", cartString);

            postObject.put("uuid", this.uuid);

            postObject.put("vouchers", vouchersString);

            postObject.put("total_price", this.finalPrice);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postObject;
    }
}
