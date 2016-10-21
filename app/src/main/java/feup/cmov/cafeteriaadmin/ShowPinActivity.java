package feup.cmov.cafeteriaadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowPinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String pin = getIntent().getStringExtra("pin");

        setContentView(R.layout.activity_show_pin);
        TextView pinView = (TextView) findViewById(R.id.pin);
        pinView.setText(pin);
    }
}
