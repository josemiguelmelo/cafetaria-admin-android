package feup.cmov.cafeteriaadmin.models;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

public class Item extends SugarRecord{
    public Long itemId;
    public String name;
    public int price;

    public Item() {
    }

    public String showPrice() {
        double decimal = price/100.0;

        return Double.toString(decimal) + " €";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Item))
            return false;
        if (obj == this)
            return true;

        Item item = (Item) obj;
        return this.name.equals(item.name);
    }

    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("name", this.name);
            jsonObject.put("price", showPrice());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}
