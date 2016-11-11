package feup.cmov.cafeteriaadmin.models;


import android.util.Log;

import com.google.gson.Gson;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import feup.cmov.cafeteriaadmin.models.voucher.DiscountVoucher;
import feup.cmov.cafeteriaadmin.models.voucher.ItemOfferVoucher;
import feup.cmov.cafeteriaadmin.models.voucher.Voucher;

public class Order  {
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

    public Order() {
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
                vouchersCodes.add(voucher.serialNumber);
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


    public static Order parseFromJSON(String jsonObject) throws JSONException {
        JSONObject jsonOrder = new JSONObject(jsonObject);

        JSONObject cartObject = jsonOrder.getJSONObject("cart");
        JSONArray cartArray = cartObject.getJSONArray("cart");

        Cart cart = new Cart();
        for(int i = 0 ; i < cartArray.length(); i++)
        {
            JSONObject itemObject = cartArray.getJSONObject(i);
            Item item = new Item();
            item.itemId = itemObject.getLong("itemId");
            item.name = itemObject.getString("name");
            item.price = itemObject.getInt("price");
            cart.addItem(item);
        }

        JSONArray vouchersArray = jsonOrder.getJSONArray("vouchers");
        ArrayList<Voucher> vouchers = new ArrayList<>();

        for(int i = 0 ; i < vouchersArray.length(); i++)
        {
            JSONObject voucherObject = vouchersArray.getJSONObject(i);
            Voucher voucher;
            String serialNumber = voucherObject.getString("serialNumber");
            String signature = voucherObject.getString("signature");
            if(voucherObject.getInt("type") == ItemOfferVoucher.TYPE) {
                voucher = new ItemOfferVoucher(signature, serialNumber, voucherObject.getLong("item"), "");
            }
            else {
                voucher = new DiscountVoucher(signature, serialNumber, voucherObject.getInt("discountPercentage"), "");
            }
            vouchers.add(voucher);
        }

        Order order = new Order(cart, vouchers);
        order.cardExpirationMonth = jsonOrder.getInt("cardExpirationMonth");
        order.cardExpirationYear = jsonOrder.getInt("cardExpirationYear");
        order.uuid = jsonOrder.getString("uuid");

        order.applyVouchers();

        return order;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
