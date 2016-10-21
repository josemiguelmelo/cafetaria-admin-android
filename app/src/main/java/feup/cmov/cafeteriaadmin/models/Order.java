package feup.cmov.cafeteriaadmin.models;


import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import feup.cmov.cafeteriaadmin.models.voucher.Voucher;

public class Order {
    private ArrayList<Voucher> vouchers;
    private Cart cart;
    private float finalPrice;

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

    public void applyVouchers(){
        for(Voucher voucher : this.vouchers)
        {
            voucher.apply(this);
        }
    }

    /** Place order to server */

    public void emptyCart(){
        ArrayList<Item> cartItemsList = this.cart.getCartList();
        cartItemsList.clear();
        this.setFinalPrice(this.cart.totalPrice());
    }

    public JSONObject postOrderJSON(String uuid)
    {
        JSONObject postObject = new JSONObject();

        Gson gson = new Gson();
        try {
            postObject.put("items", gson.toJson(this.cart.getListOfItemsIds()));

            postObject.put("uuid", uuid);

            ArrayList<String> vouchersCodes = new ArrayList<>();
            for(Voucher voucher : this.vouchers)
            {
                vouchersCodes.add(voucher.code);
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

}
