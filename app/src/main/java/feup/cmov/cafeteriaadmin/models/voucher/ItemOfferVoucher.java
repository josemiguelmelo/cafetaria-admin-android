package feup.cmov.cafeteriaadmin.models.voucher;


import feup.cmov.cafeteriaadmin.models.Cart;
import feup.cmov.cafeteriaadmin.models.Item;
import feup.cmov.cafeteriaadmin.models.Order;

public class ItemOfferVoucher extends Voucher{
    private Item item;

    public static int TYPE = 1;

    public ItemOfferVoucher(String signature, int serialNumber, Item item)
    {
        super(signature, serialNumber, ItemOfferVoucher.TYPE);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public void apply(Order order) {
        Cart cart = order.getCart();
        int numVoucherItemsInCart = 0;

        for(Item item : cart.getCartList())
        {
            if(item.equals(this.item))
            {
                numVoucherItemsInCart++;
            }
        }

        float discountInPrice = numVoucherItemsInCart * this.item.price;
        float orderPrice = order.totalPrice();

        order.setFinalPrice(orderPrice - discountInPrice);
    }


    @Override
    public String toString(){
        return "Product " + this.item.name + " for free.";
    }
}
