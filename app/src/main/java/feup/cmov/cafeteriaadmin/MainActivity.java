package feup.cmov.cafeteriaadmin;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import feup.cmov.cafeteriaadmin.fragments.ItemsScrollFragment;
import feup.cmov.cafeteriaadmin.fragments.OrderFragment;
import feup.cmov.cafeteriaadmin.fragments.QRCodeFragment;
import feup.cmov.cafeteriaadmin.fragments.VouchersFragment;
import feup.cmov.cafeteriaadmin.http.Http;
import feup.cmov.cafeteriaadmin.http.RequestCb;
import feup.cmov.cafeteriaadmin.models.Cart;
import feup.cmov.cafeteriaadmin.models.Item;
import feup.cmov.cafeteriaadmin.models.Order;
import feup.cmov.cafeteriaadmin.models.voucher.DiscountVoucher;
import feup.cmov.cafeteriaadmin.models.voucher.Voucher;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private FragmentRegistry fragmentRegistry;

    private Http http;
    private ArrayList<Item> items;
    private ArrayList<Voucher> vouchersList;
    private ArrayList<Voucher> vouchersApplied;
    private Cart cart;
    private Order order;

    public boolean qrcodeRead = false;
    private boolean reloadFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.cart = new Cart();
        this.vouchersList = new ArrayList<>();
        this.vouchersApplied = new ArrayList<>();

        this.vouchersList.add(new DiscountVoucher("20discount", 20));

        this.getItemsFromServer();

        this.initFragmentsRegistry();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.initNavigationView();

        this.initDrawerLayout();
    }


    @Override
    /** Open fragment with order details after reading QR Code*/
    public void onResume() {
        super.onResume();
        if(reloadFragment)
        {
            reloadFragment = false;
            openFragment(OrderFragment.ORDER_FRAGMENT);
        }
    }

    /** Receives the QR Code result **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String qrCodeOrderJson = intent.getStringExtra("SCAN_RESULT");

                parseOrderFromQRCodeResult(qrCodeOrderJson);

                this.qrcodeRead = true;
                this.reloadFragment = true;
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }


    private void parseOrderFromQRCodeResult(String qrCodeResult){
        this.order = Order.parseFromJSON(qrCodeResult);
        this.cart = this.order.getCart();
        this.vouchersApplied = this.order.getVouchersApplied();

        Log.d("Order uuid", "#" + this.order.getUuid());
    }

    private void getItemsFromServer()
    {
        this.http = new Http(this);
        this.items = new ArrayList<>();

        RequestCb requestCb = new RequestCb() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                     Log.d("NUMBER ITEMS" , "#" + items.length());
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject jsonItem = items.getJSONObject(i);
                        Item item = new Item();
                        item.id = jsonItem.getInt("id");
                        item.name = jsonItem.getString("name");
                        item.price = jsonItem.getInt("price");
                        MainActivity.this.items.add(item);
                    }

                    MainActivity.this.openFragment(ItemsScrollFragment.ITEMS_FRAGMENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        this.http.get("/items", requestCb);
    }

    private void initFragmentsRegistry()
    {
        HashMap<String, Fragment> registry = new HashMap<>();

        registry.put(ItemsScrollFragment.ITEMS_FRAGMENT, new ItemsScrollFragment());
        registry.put(QRCodeFragment.QRCODE_FRAGMENT, new QRCodeFragment());
        registry.put(VouchersFragment.VOUCHER_FRAGMENT, new VouchersFragment());
        registry.put(OrderFragment.ORDER_FRAGMENT, new OrderFragment());

        fragmentRegistry = new FragmentRegistry(getSupportFragmentManager(), registry);
    }

    private void openFragment(String key)
    {
        fragmentRegistry.openFragment(key);
    }


    private void initNavigationView()
    {
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){
                    case R.id.menu_cart:
                        MainActivity.this.openFragment(QRCodeFragment.QRCODE_FRAGMENT);
                        return true;

                    case R.id.menu_items:
                        MainActivity.this.openFragment(ItemsScrollFragment.ITEMS_FRAGMENT);
                        return true;

                    case R.id.menu_vouchers:
                        MainActivity.this.openFragment(VouchersFragment.VOUCHER_FRAGMENT);
                        return true;
                    default:
                        return false;

                }
            }
        });
    }



    private void initDrawerLayout() {

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** GETs and SETs **/

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }


    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Http getHttp() {
        return http;
    }

    public void setHttp(Http http) {
        this.http = http;
    }

    public ArrayList<Voucher> getVouchersList() {
        return this.vouchersList;
    }
    public ArrayList<Voucher> getVouchersApplied() {
        return this.vouchersApplied;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
