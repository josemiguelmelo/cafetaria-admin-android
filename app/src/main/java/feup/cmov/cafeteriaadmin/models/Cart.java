package feup.cmov.cafeteriaadmin.models;

import java.util.ArrayList;

public class Cart {

    private ArrayList<Item> cart;

    public Cart() {
        this.cart = new ArrayList<>();
    }

    public void addItem(Item item)
    {
        this.cart.add(item);
    }

    public void removeItem(Item item)
    {
        this.cart.remove(item);
    }

    public ArrayList<Item> getCartList() {
        return cart;
    }


    public int getCartSize(){
        return this.cart.size();
    }

    public boolean contains(Item item){
        return this.cart.contains(item);
    }

    public float totalPrice() {
        float totalPrice = 0;
        for(Item item : this.cart)
        {
            totalPrice += item.price;
        }
        return totalPrice / 100;
    }

    public ArrayList<Long> getListOfItemsIds() {
        ArrayList<Long> idsList = new ArrayList<>();

        for(Item item : cart)
        {
            idsList.add(item.id);
        }

        return idsList;
    }
}
