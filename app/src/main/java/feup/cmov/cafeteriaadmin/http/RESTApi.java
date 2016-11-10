package feup.cmov.cafeteriaadmin.http;


import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import feup.cmov.cafeteriaadmin.MainActivity;
import feup.cmov.cafeteriaadmin.fragments.ItemsScrollFragment;
import feup.cmov.cafeteriaadmin.models.Item;
import feup.cmov.cafeteriaadmin.models.voucher.DiscountVoucher;
import feup.cmov.cafeteriaadmin.models.voucher.ItemOfferVoucher;
import feup.cmov.cafeteriaadmin.models.voucher.Voucher;

public class RESTApi {

    MainActivity activity;
    public RESTApi(MainActivity activity)
    {
        this.activity = activity;
    }

    public void getItems(){
        RequestCb requestCb = new RequestCb() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    Log.d("D/Number items" , "#" + items.length());
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject jsonItem = items.getJSONObject(i);

                        Item item ;
                        try {
                            item = Item.find(Item.class, "item_id = ?", "" + jsonItem.getLong("id") ).get(0);
                        } catch(Exception e)
                        {
                            item = null;
                        }

                        if(item == null) {
                            Log.d("ITEM NOT FOunD", "item was not found locally");
                            item = new Item();
                        }

                        item.itemId = jsonItem.getLong("id");
                        item.name = jsonItem.getString("name");
                        item.price = jsonItem.getInt("price");

                        item.save();

                        activity.getItems().add(item);
                    }

                    activity.openFragment(ItemsScrollFragment.ITEMS_FRAGMENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        };

        Log.d("Get Items", "GET SENT");

        activity.getHttp().get("/items", requestCb);
    }

    public void getVouchers()
    {
        RequestCb requestCb = new RequestCb() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    activity.getVouchersList().clear();


                    JSONArray vouchers = response.getJSONArray("vouchers");
                    Log.d("D/Number vouchers" , "#" + vouchers.length());
                    for (int i = 0; i < vouchers.length(); i++) {
                        JSONObject jsonItem = vouchers.getJSONObject(i);

                        String type = jsonItem.getString("type");

                        Voucher voucher;

                        String serialNumber = jsonItem.getString("serial_number");
                        String signature = jsonItem.getString("signature");
                        String uuid = jsonItem.getString("uuid");

                        boolean voucherExists = false;
                        if(type.equals("discount")) {
                            int discount = jsonItem.getInt("discount");

                            if(DiscountVoucher.find(DiscountVoucher.class, "serial_number = ?", serialNumber).size() > 0)
                                voucherExists = true;

                            voucher = new DiscountVoucher(signature, serialNumber, discount, uuid);
                        } else {
                            Long itemId = jsonItem.getLong("item_id");

                            if(ItemOfferVoucher.find(ItemOfferVoucher.class, "serial_number = ?", serialNumber).size() > 0)
                                voucherExists = true;

                            voucher = new ItemOfferVoucher(signature, serialNumber, itemId, uuid);
                        }

                        activity.getVouchersList().add(voucher);

                        if(voucherExists)
                            continue;

                        voucher.save();
                    }

                    activity.openFragment(ItemsScrollFragment.ITEMS_FRAGMENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        };

        Log.d("Get Vouchers", "Request sent");

        activity.getHttp().get("/vouchers", requestCb);
    }
}
