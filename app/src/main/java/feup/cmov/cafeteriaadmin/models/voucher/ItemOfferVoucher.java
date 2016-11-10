package feup.cmov.cafeteriaadmin.models.voucher;


import android.util.Log;

import feup.cmov.cafeteriaadmin.models.Cart;
import feup.cmov.cafeteriaadmin.models.Item;
import feup.cmov.cafeteriaadmin.models.Order;

public class ItemOfferVoucher extends Voucher{
    public Long item;

    public static int TYPE = 1;

    public ItemOfferVoucher() {}

    public ItemOfferVoucher(String signature, String serialNumber, Long item, String uuid)
    {
        super(signature, serialNumber, ItemOfferVoucher.TYPE, uuid);
        this.item = item;
    }

    public Long getItem() {
        return item;
    }

    public void setItem(Long item) {
        this.item = item;
    }

    @Override
    public void apply(Order order) {
        Cart cart = order.getCart();
        int price = 0;

        for(Item item : cart.getCartList())
        {
            if(item.itemId.equals(this.item))
            {
                price = item.price;
            }
        }

        float orderPrice = order.totalPrice();
        Log.d("Order Price", orderPrice + "");
        Log.d("Discount Price", (price/100.0f) + "");

        order.setFinalPrice(orderPrice - (price/100.0f));
    }


    @Override
    public String toString(){

        Item itemObj = Item.find(Item.class, "item_id = ?", Long.toString(this.item)).get(0);
        return "Product " + itemObj.name + " for free.";
    }
}
