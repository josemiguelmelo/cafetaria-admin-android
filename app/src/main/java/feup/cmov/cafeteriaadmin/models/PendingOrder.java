package feup.cmov.cafeteriaadmin.models;


import com.orm.SugarRecord;

public class PendingOrder extends SugarRecord{
    private String vouchers;
    private String cart;
    private float finalPrice;
    private String uuid;


    public PendingOrder() {
    }

    public PendingOrder(String vouchers, String cart, float finalPrice, String uuid) {
        this.vouchers = vouchers;
        this.cart = cart;
        this.uuid = uuid;
        this.finalPrice = finalPrice;
    }
}
