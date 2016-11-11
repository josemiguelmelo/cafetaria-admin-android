package feup.cmov.cafeteriaadmin.buttons;


import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feup.cmov.cafeteriaadmin.MainActivity;
import feup.cmov.cafeteriaadmin.R;
import feup.cmov.cafeteriaadmin.fragments.OrderFragment;
import feup.cmov.cafeteriaadmin.fragments.QRCodeFragment;
import feup.cmov.cafeteriaadmin.http.Http;
import feup.cmov.cafeteriaadmin.http.RequestCb;
import feup.cmov.cafeteriaadmin.models.Blacklist;
import feup.cmov.cafeteriaadmin.models.Item;
import feup.cmov.cafeteriaadmin.models.Order;
import feup.cmov.cafeteriaadmin.models.PendingOrder;
import feup.cmov.cafeteriaadmin.models.voucher.DiscountVoucher;
import feup.cmov.cafeteriaadmin.models.voucher.ItemOfferVoucher;
import feup.cmov.cafeteriaadmin.models.voucher.Voucher;

public class ConfirmOrderButton extends Button{

    public ConfirmOrderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void activateListener(MainActivity activity, OrderFragment fragment, Order order)
    {
        this.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder(activity.getHttp(),  order, fragment, activity);
            }
        });
    }

    public void placeOrder(Http http, Order order, OrderFragment fragment, MainActivity activity){

        RequestCb requestCb = new RequestCb() {
            @Override
            public void onResponse(JSONObject response) {
                String vouchers, error;
                try {
                    error = response.getString("error");
                    vouchers = response.getString("vouchers");

                    Log.d("OnResponseOrder", "chegou");
                    Toast.makeText(activity, "Order placed successfully.", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

                try {
                    if (Blacklist.find(Blacklist.class, "uuid = ?", order.getUuid()).size() > 0) {
                        Toast.makeText(activity, "User on blacklist.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }catch(Exception e){}

                List<ItemOfferVoucher> itemOfferVouchersListFetchedFromDB = ItemOfferVoucher.listAll(ItemOfferVoucher.class);
                List<DiscountVoucher> discountVouchersListFetchedFromDB = DiscountVoucher.listAll(DiscountVoucher.class);

                ArrayList<Voucher> vouchersList = new ArrayList<>(itemOfferVouchersListFetchedFromDB);
                vouchersList.addAll(discountVouchersListFetchedFromDB);

                int vouchersValidCount = 0;

                Log.d("Vouchers Applied", order.getVouchersApplied().size() + "");

                for(Voucher voucher : order.getVouchersApplied()) {
                    Log.d("Voucher",voucher.serialNumber + "");
                    // if the voucher is invalid
                    for(Voucher voucher1 : vouchersList)
                    {
                        Log.d("Voucher1",voucher1.serialNumber + "");
                        if(voucher1.serialNumber.equals(voucher.serialNumber)) {
                            Log.d("Vouchers equal",  "true");
                            if(voucher1.uuid.equals(order.getUuid())) {
                                Log.d("Vouchers equal",  "increment");
                                vouchersValidCount ++;
                            }

                        }
                    }
                }
                if(vouchersValidCount != order.getVouchersApplied().size())
                {
                    Blacklist blacklist = new Blacklist(order.getUuid());
                    blacklist.save();

                    Toast.makeText(activity, "Voucher not valid.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(! order.validCcDate()) {
                    Blacklist blacklist = new Blacklist(order.getUuid());
                    blacklist.save();
                    Toast.makeText(activity, "Invalid CC.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(activity, "Terminal offline. Order saved.", Toast.LENGTH_SHORT).show();

                String vouchersString = "";
                for(Voucher voucher : order.getVouchersApplied())
                {
                    vouchersString += voucher.serialNumber + ",";
                }
                String itemsString = "";
                for(Item item : order.getCart().getCartList())
                {
                    itemsString += item.itemId + ",";
                }

                PendingOrder pendingOrder = new PendingOrder(vouchersString, itemsString, order.totalPrice() * 100, order.getUuid());
                pendingOrder.save();
            }
        };

        JSONObject postObject = order.postOrderJSON();

        http.post("/orders/store", postObject, requestCb);
    }
}
