package feup.cmov.cafeteriaadmin.models.voucher;


import feup.cmov.cafeteriaadmin.models.Order;

public class DiscountVoucher extends Voucher{
    private int discountPercentage;

    public DiscountVoucher(String code, int discountPercentage)
    {
        super(code);
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
