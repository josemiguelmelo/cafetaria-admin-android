package feup.cmov.cafeteriaadmin.buttons;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import feup.cmov.cafeteriaadmin.MainActivity;

import feup.cmov.cafeteriaadmin.QRCodeScannerActivity;
import feup.cmov.cafeteriaadmin.models.Order;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ReadQRCodeButton extends Button{
    public ReadQRCodeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void activateListener(MainActivity activity){
        this.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent intent = new Intent(activity, QRCodeScannerActivity.class);
                    activity.startActivityForResult(intent, 0);
                } catch (Exception e) {
                    showDialog(activity , "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                }

            }
        });
    }


    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                }
                catch (ActivityNotFoundException anfe) {
                    Log.e("DOWNLOAD", "Could not download the intent");
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }
}
