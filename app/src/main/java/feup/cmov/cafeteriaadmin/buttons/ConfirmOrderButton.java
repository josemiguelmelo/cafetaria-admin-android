package feup.cmov.cafeteriaadmin.buttons;


import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import feup.cmov.cafeteriaadmin.MainActivity;
import feup.cmov.cafeteriaadmin.R;
import feup.cmov.cafeteriaadmin.fragments.OrderFragment;
import feup.cmov.cafeteriaadmin.fragments.QRCodeFragment;
import feup.cmov.cafeteriaadmin.http.Http;
import feup.cmov.cafeteriaadmin.http.RequestCb;
import feup.cmov.cafeteriaadmin.models.Order;

public class ConfirmOrderButton extends Button{

    public ConfirmOrderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void activateListener(MainActivity activity, OrderFragment fragment, Order order)
    {
        this.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog pinDialog = new Dialog(activity);
                pinDialog.setContentView(R.layout.dialog_ask_pin);
                pinDialog.setTitle("Pin:");

                Button confirmButton = (Button) pinDialog.findViewById(R.id.confirm_pin);

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText pinText = (EditText) pinDialog.findViewById(R.id.dialog_pin);

                        placeOrder(activity.getHttp(),  order, fragment);
                        Toast.makeText(activity, "Order placed successfully.", Toast.LENGTH_SHORT).show();

                        pinDialog.dismiss();
                    }
                });

                pinDialog.show();
            }
        });
    }

    public void placeOrder(Http http, Order order, OrderFragment fragment){
        RequestCb requestCb = new RequestCb() {
            @Override
            public void onResponse(JSONObject response) {
                String vouchers, error;
                try {
                    error = response.getString("error");
                    vouchers = response.getString("vouchers");

                    // TODO: parse vouchers received from server and add it to app available vouchers
                    // TODO: remove used vouchers on order

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JSONObject postObject = order.postOrderJSON();

        http.post("/orders/store", postObject, requestCb);
    }
}
