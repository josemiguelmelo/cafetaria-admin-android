package feup.cmov.cafeteriaadmin.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {
    public int id;
    public String name;
    public int price;

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
