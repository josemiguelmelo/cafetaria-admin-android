package feup.cmov.cafeteriaadmin.models.voucher;


import feup.cmov.cafeteriaadmin.models.Order;

public class DiscountVoucher extends Voucher{
    private int discountPercentage;
    public static int TYPE = 0;

    public DiscountVoucher() {}

    public DiscountVoucher(String signature, String serialNumber, int discountPercentage, String uuid)
    {
        super(signature, serialNumber, DiscountVoucher.TYPE, uuid);
        this.discountPercentage = discountPercentage;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    private float getPercentage() { return this.discountPercentage / 100f; }

    @Override
    public void apply(Order order) {
        float discount = order.totalPrice() * this.getPercentage();
        order.setFinalPrice(order.totalPrice() - discount);
    }

    @Override
    public String toString(){
        return "Discount of " + this.discountPercentage + "%";
    }
}
