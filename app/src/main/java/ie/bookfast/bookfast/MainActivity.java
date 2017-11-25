package ie.bookfast.bookfast;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button toMapBtn, toScannerBtn, toBibliotherapyBtn, toFavouritesBtn;
    static final int REQUEST_CODE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //asking for permissions for location access
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //if permission not given, request
            requestPermission(this);
        }


        toMapBtn = (Button) findViewById(R.id.main_to_map_btn);
        toScannerBtn = (Button) findViewById(R.id.main_to_barcode_scanner_btn);
        toBibliotherapyBtn = (Button) findViewById(R.id.main_to_bibliotherapy_btn);
        toFavouritesBtn = (Button) findViewById(R.id.main_to_favourites_btn);

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

        toBibliotherapyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toBibliotherapy();
            }
        });

        toFavouritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toFavourites();
            }
        });
    }

    private void toFavourites(){
        Intent favouritesIntent = new Intent(this, favouritesActivity.class);
        startActivity(favouritesIntent);
    }


    private void toLibraryIreland(){
        Uri libraryIrelandWebpage = Uri.parse("http://www.libraryireland.com/");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, libraryIrelandWebpage);
        startActivity(webIntent);
    }

    private void toMap(){
        Intent mapIntent = new Intent(this, MapActivity.class);
        startActivity(mapIntent);
    }

    private void toScanner(){
        Intent scannerIntent = new Intent(this, BarcodeScanActivity.class);
        startActivity(scannerIntent);
    }

    private void toBibliotherapy(){

        Intent bibliotherapyIntent = new Intent(this, BibliotherapyActivity.class);
        startActivity(bibliotherapyIntent);

    }

    //method to ask for permission at runtime REQUEST_CODE_PERMISSIONS is ID of permission ask
    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA
                        },
                REQUEST_CODE_PERMISSIONS);
    }

    //processing the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // related task you need to do.

                } else {
                    //TODO:Implement functionality for if user denies coarse/fine location permission
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
