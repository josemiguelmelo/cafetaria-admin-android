package feup.cmov.cafeteriaadmin.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import feup.cmov.cafeteriaadmin.MainActivity;
import feup.cmov.cafeteriaadmin.R;
import feup.cmov.cafeteriaadmin.adapters.VoucherAdapter;
import feup.cmov.cafeteriaadmin.models.voucher.Voucher;

public class VouchersFragment extends Fragment {
    public static String VOUCHER_FRAGMENT = "Vouchers";


    private RecyclerView mRecyclerView;
    private VoucherAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Voucher> vouchersList;
    private ArrayList<Voucher> vouchersApplied;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.vouchersList = ((MainActivity) getActivity()).getVouchersList();
        this.vouchersApplied = ((MainActivity) getActivity()).getVouchersApplied();

        View v = inflater.inflate(R.layout.content_items_scroll, container,false);

        Log.d("VouchersFragment", "Fragment onCreateView");

        setupRecyclerView(v);

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

        mAdapter = new VoucherAdapter(this.vouchersList, this.vouchersApplied);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.getPositionClicks().subscribe(item -> {
            if(this.vouchersList.contains(item)) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((MainActivity) getActivity());

                if(this.vouchersApplied.contains(item))
                {
                    alertDialogBuilder.setTitle("Remove Voucher");
                    alertDialogBuilder.setMessage("Are you sure you want to remove this voucher?");
                }else {

                    alertDialogBuilder.setTitle("Apply Voucher");
                    alertDialogBuilder.setMessage("Are you sure you want to apply this voucher?");
                }

                alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(VouchersFragment.this.vouchersApplied.contains(item))
                            VouchersFragment.this.vouchersApplied.remove(item);
                        else
                            VouchersFragment.this.vouchersApplied.add(item);

                        mAdapter.notifyDataSetChanged();
                    }
                });

                // Setting Negative "NO" Button
                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialogBuilder.show();

            }
            Log.w("Clicked", "Voucher clicked. Voucher list #items" + this.vouchersList.size());
        });
    }
}
