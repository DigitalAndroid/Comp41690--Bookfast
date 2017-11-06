package ie.bookfast.bookfast;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.Context;

/**
 * Created by Aaron on 02/11/2017.
 */

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class BarcodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String ISBN = "";
    static final int REQUEST_CODE_PERMISSIONS = 1000;
    private ZXingScannerView zXingScannerView;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);
        scan();
    }

    public void scan(){
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
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(ISBN, result.getText());
        editor.commit();

        Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        zXingScannerView.resumeCameraPreview(this);

        // open book detail view
        Intent mIntent = new Intent(this, BookDetail.class);
        mIntent.putExtra("ISBN",result.getText());
        startActivity(mIntent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE_PERMISSIONS);
    }




}
