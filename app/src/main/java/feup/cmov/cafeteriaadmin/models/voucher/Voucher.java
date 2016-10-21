package feup.cmov.cafeteriaadmin.models.voucher;


import feup.cmov.cafeteriaadmin.models.Order;

public abstract class Voucher {

    public String code;
    public Voucher(String code)
    {
        this.code = code;
    }
    public abstract void apply(Order cart);
}
