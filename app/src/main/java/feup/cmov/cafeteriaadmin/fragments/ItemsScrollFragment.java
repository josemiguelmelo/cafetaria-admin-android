package feup.cmov.cafeteriaadmin.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import feup.cmov.cafeteriaadmin.MainActivity;
import feup.cmov.cafeteriaadmin.adapters.ItemAdapter;
import feup.cmov.cafeteriaadmin.R;
import feup.cmov.cafeteriaadmin.models.Cart;
import feup.cmov.cafeteriaadmin.models.Item;

public class ItemsScrollFragment extends Fragment {
    public static String ITEMS_FRAGMENT = "items_fragment";

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Item> items;
    private Cart cart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        this.items = ((MainActivity) getActivity()).getItems();

        this.cart = ((MainActivity) getActivity()).getCart();

        View v = inflater.inflate(R.layout.content_items_scroll,container,false);

        Log.d("ItemsScrollFragment", "Fragment onCreateView");

        this.setupRecyclerView(v);

        return v;
    }


    private void setupRecyclerView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.items_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ItemAdapter(this.items);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.getPositionClicks().subscribe(item -> {
            if(this.cart.contains(item)) {
                Snackbar.make(rootView.findViewById(R.id.items_recycler_view), "Duplicate item", Snackbar.LENGTH_LONG)
                        .setAction("Add Anyway", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ItemsScrollFragment.this.cart.addItem(item);
                            }
                        })
                        .show();
            } else {
                this.cart.addItem(item);
            }
            Log.w("Clicked", "Item added to cart. Cart #items" + this.cart.getCartSize());
        });
    }
}
