package ie.bookfast.bookfast;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import static ie.bookfast.bookfast.R.id.map;

public class MapActivity extends AppCompatActivity {

    private MyLocationNewOverlay mLocationOverlay;

    private LocationManager locationManager;
    private Location location;
    private double latitude;
    private double longitude;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //determineUserLocation();

        //configuring an instance to call osm servers
        Context thisCtx = getApplicationContext();
        Configuration.getInstance().load(thisCtx, PreferenceManager.getDefaultSharedPreferences(thisCtx));
        setContentView(R.layout.activity_map);

        //getting the map Variable
        MapView bookMap = (MapView) findViewById(map);

        //adding in zoom controls + setting initial zoom and location
        bookMap.setBuiltInZoomControls(true);
        bookMap.setMultiTouchControls(true);
        IMapController mapController = bookMap.getController();
        mapController.setZoom(12);
        GeoPoint startPoint = new GeoPoint(53.305344, -6.220654);
        //startPoint.setLatitude(latitude);
        //startPoint.setLongitude(longitude);
        mapController.setCenter(startPoint);

        //adding in 'my location'
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(thisCtx), bookMap);
        this.mLocationOverlay.enableMyLocation();
        bookMap.getOverlays().add(this.mLocationOverlay);

        //filling tile on screen with map
        bookMap.setTileSource(TileSourceFactory.MAPNIK);
    }

    @Override
    public void onResume() {
        super.onResume();
        //saving state of map on resume of application
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    /*
    private void determineUserLocation() {
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //location can also .getaltitude and .getspeed

        if (location != null) {
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            Log.d("old","lat :  "+latitude);
            Log.d("old","long :  "+longitude);
            System.out.println(latitude + " " + longitude);
            //this.onLocationChanged(location);
        }
    }
    */
}
