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
import android.widget.TextView;

import java.util.ArrayList;

import feup.cmov.cafeteriaadmin.MainActivity;
import feup.cmov.cafeteriaadmin.R;
import feup.cmov.cafeteriaadmin.adapters.ItemAdapter;
import feup.cmov.cafeteriaadmin.buttons.ConfirmOrderButton;
import feup.cmov.cafeteriaadmin.buttons.ReadQRCodeButton;
import feup.cmov.cafeteriaadmin.models.Cart;
import feup.cmov.cafeteriaadmin.models.Order;
import feup.cmov.cafeteriaadmin.models.voucher.Voucher;

public class OrderFragment extends Fragment {
    public static String ORDER_FRAGMENT = "order_fragment";

    private ConfirmOrderButton confirmOrderButton;

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView orderTotalPrice;

    private Cart cart;
    private Order order;
    private ArrayList<Voucher> vouchers;

    private ReadQRCodeButton qrCodeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.cart = ((MainActivity) getActivity()).getCart();

        this.vouchers = ((MainActivity) getActivity()).getVouchersApplied();

        order = new Order(this.cart, vouchers);
        order.applyVouchers();

        View v = inflater.inflate(R.layout.activity_order, container,false);

        Log.d("OrderFragment", "Fragment onCreateView");

        setupRecyclerView(v);

        setupConfirmOrderButton(v);

        orderTotalPrice = (TextView) v.findViewById(R.id.order_total_price);
        orderTotalPrice.append("" + this.order.totalPrice() + " €");

        qrCodeButton = (ReadQRCodeButton)  v.findViewById(R.id.qr_code_button);

        hideElements();

        ((MainActivity) getActivity()).qrcodeRead = false;

        return v;
    }

    public void hideElements(){
        if(((MainActivity) getActivity()).qrcodeRead)
        {
            qrCodeButton.setVisibility(View.INVISIBLE);
        }
    }


    private void setupConfirmOrderButton(View rootView)
    {
        confirmOrderButton = (ConfirmOrderButton) rootView.findViewById(R.id.confirm_order);
        confirmOrderButton.activateListener((MainActivity)getActivity(), this, this.order);
    }

    private void setupRecyclerView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.items_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ItemAdapter(this.cart.getCartList());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.getPositionClicks().subscribe(item -> {
            if(this.cart.contains(item)) {
                Snackbar.make(rootView.findViewById(R.id.items_recycler_view), "Remove Item?", Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                OrderFragment.this.cart.removeItem(item);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            } else {
                this.cart.addItem(item);
            }
            Log.w("Clicked", "Item removed from cart. Cart #items" + this.cart.getCartSize());
        });
    }

}
