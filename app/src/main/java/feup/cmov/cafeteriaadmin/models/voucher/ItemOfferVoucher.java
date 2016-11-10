package feup.cmov.cafeteriaadmin.models.voucher;


import feup.cmov.cafeteriaadmin.models.Cart;
import feup.cmov.cafeteriaadmin.models.Item;
import feup.cmov.cafeteriaadmin.models.Order;

public class ItemOfferVoucher extends Voucher{
    private int item;

    public static int TYPE = 1;

    public ItemOfferVoucher() {}

    public ItemOfferVoucher(String signature, String serialNumber, int item, String uuid)
    {
        super(signature, serialNumber, ItemOfferVoucher.TYPE, uuid);
        this.item = item;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
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

        order.setFinalPrice(orderPrice - price);
    }


    @Override
    public String toString(){

        Item itemObj = Item.find(Item.class, "item_id = ?", Integer.toString(this.item)).get(0);
        return "Product " + itemObj.name + " for free.";
    }
}
