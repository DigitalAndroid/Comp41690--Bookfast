package ie.bookfast.bookfast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button toMapBtn, toScannerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toMapBtn = (Button) findViewById(R.id.main_to_map_btn);
        toScannerBtn = (Button) findViewById(R.id.main_to_barcode_scanner_btn);

        toMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Finding current location...", Toast.LENGTH_SHORT).show();
                toMap();
            }
        });

        toScannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toScanner();
            }
        });
    }

    private void toMap(){
        Intent mapIntent = new Intent(this, MapActivity.class);
        startActivity(mapIntent);
    }

    private void toScanner(){
        Intent scannerIntent = new Intent(this, BarcodeScanActivity.class);
        startActivity(scannerIntent);
    }
}
