package feup.cmov.cafeteriaadmin.http;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import feup.cmov.cafeteriaadmin.MainActivity;
import feup.cmov.cafeteriaadmin.fragments.ItemsScrollFragment;
import feup.cmov.cafeteriaadmin.models.Item;

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
                            item = Item.findById(Item.class, jsonItem.getInt("id"));
                        } catch(Exception e)
                        {
                            item = null;
                        }
                        if(item == null)
                            item = new Item();
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
        };

        Log.d("Get Items", "GET SENT");

        activity.getHttp().get("/items", requestCb);
    }

    public void getVouchers()
    {
        // TODO : get available vouchers to user from server and save to local db
    }
}
