package feup.cmov.cafeteriaadmin.models;


import android.util.Log;

import com.google.gson.Gson;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import feup.cmov.cafeteriaadmin.models.voucher.Voucher;

public class Order extends SugarRecord {
    private ArrayList<Voucher> vouchers;
    private Cart cart;
    private float finalPrice;
    public int cardExpirationMonth;
    public int cardExpirationYear;

    private String uuid;

    public Order(Cart cart, ArrayList<Voucher> vouchers) {
        this.cart = cart;
        this.vouchers = vouchers;
        this.finalPrice = this.cart.totalPrice();
    }

    public float totalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Cart getCart() { return this.cart; }
    public ArrayList<Voucher> getVouchersApplied() { return this.vouchers; }

    public void applyVouchers(){
        for(Voucher voucher : this.vouchers)
        {
            voucher.apply(this);
        }
    }

    public boolean validCcDate() {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if(this.cardExpirationYear > currentYear) {
            return true;
        }

        if(this.cardExpirationYear == currentYear && this.cardExpirationMonth >= currentMonth) {
            return true;
        }

        return false;
    }

    /** Place order to server */

    public void emptyCart(){
        ArrayList<Item> cartItemsList = this.cart.getCartList();
        cartItemsList.clear();
        this.setFinalPrice(this.cart.totalPrice());
    }

    public JSONObject postOrderJSON()
    {
        JSONObject postObject = new JSONObject();

        Gson gson = new Gson();
        try {
            postObject.put("items", gson.toJson(this.cart.getListOfItemsIds()));

            postObject.put("uuid", this.uuid);

            ArrayList<String> vouchersCodes = new ArrayList<>();
            for(Voucher voucher : this.vouchers)
            {
                vouchersCodes.add(voucher.signature);
            }
            postObject.put("vouchers", gson.toJson(vouchersCodes));

            postObject.put("total_price", this.totalPrice() * 100);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postObject;
    }

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public static Order parseFromJSON(String jsonObject)
    {
        Gson gson = new Gson();
        Log.d("JSON Order" , jsonObject);
        return gson.fromJson(jsonObject, Order.class);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
