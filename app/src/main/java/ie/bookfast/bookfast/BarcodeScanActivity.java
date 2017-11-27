package ie.bookfast.bookfast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Aaron on 02/11/2017.
 */

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_barcode_scan);
        startScanner();
    }

    public void startScanner(){
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        zXingScannerView.resumeCameraPreview(this);
        // open book detail view
        Intent mIntent = new Intent(this, BookDetail.class);
        mIntent.putExtra("ISBN",result.getText());
        startActivity(mIntent);
        finish();
    }

}
