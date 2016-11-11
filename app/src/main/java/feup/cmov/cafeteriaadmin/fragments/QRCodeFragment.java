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
import feup.cmov.cafeteriaadmin.adapters.ItemAdapter;
import feup.cmov.cafeteriaadmin.R;
import feup.cmov.cafeteriaadmin.buttons.ConfirmOrderButton;
import feup.cmov.cafeteriaadmin.buttons.ReadQRCodeButton;
import feup.cmov.cafeteriaadmin.models.Cart;
import feup.cmov.cafeteriaadmin.models.Order;
import feup.cmov.cafeteriaadmin.models.voucher.Voucher;

public class QRCodeFragment extends Fragment{
    public static String QRCODE_FRAGMENT = "Read QR Code";

    private ReadQRCodeButton qrCodeButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_order, container,false);

        Log.d("QRCodeFragment", "Fragment onCreateView");


        setupQRCodeButton(v);

        hideNotNeededElements(v);

        ((MainActivity) getActivity()).qrcodeRead = false;

        return v;
    }

    private void hideNotNeededElements(View v){

        TextView orderTotalPrice = (TextView) v.findViewById(R.id.order_total_price);
        orderTotalPrice.setVisibility(View.INVISIBLE);

        ConfirmOrderButton confirmOrderButton = (ConfirmOrderButton) v.findViewById(R.id.confirm_order);
        confirmOrderButton.setVisibility(View.INVISIBLE);
    }
    private void setupQRCodeButton(View rootView)
    {
        qrCodeButton = (ReadQRCodeButton) rootView.findViewById(R.id.qr_code_button);
        qrCodeButton.activateListener((MainActivity)getActivity());
    }

}
