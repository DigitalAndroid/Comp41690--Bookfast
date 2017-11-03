package ie.bookfast.bookfast;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.location.OverpassAPIProvider;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import static ie.bookfast.bookfast.R.id.map;

public class MapActivity extends AppCompatActivity{

    private MyLocationNewOverlay mLocationOverlay;
    private ScaleBarOverlay scaleBarOverlay;
    private MapEventsOverlay mapEventsOverlay;

    private LocationManager locationManager;
    private Location currentLocation;
    private double latitude;
    private double longitude;

    GeoPoint startPoint;

    MapView bookMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //configuring an instance to call osm servers
        Context thisCtx = getApplicationContext();
        Configuration.getInstance().load(thisCtx, PreferenceManager.getDefaultSharedPreferences(thisCtx));
        setContentView(R.layout.activity_map);

        getCurrentLocation();

        //getting the map Variable
        bookMap = (MapView) findViewById(map);

        //adding in zoom controls + setting initial zoom and location
        bookMap.setBuiltInZoomControls(true);
        bookMap.setMultiTouchControls(true);
        IMapController mapController = bookMap.getController();
        mapController.setZoom(10);
        startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);


        //adding in 'my location'
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(thisCtx), bookMap);
        this.mLocationOverlay.enableMyLocation();
        bookMap.getOverlays().add(this.mLocationOverlay);

        scaleBarOverlay = new ScaleBarOverlay(bookMap);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(getScreenWidth()/2, 10);
        bookMap.getOverlays().add(this.scaleBarOverlay);

        new overpassKML().execute("amenity=library");

        //filling tile on screen with map
        bookMap.setTileSource(TileSourceFactory.MAPNIK);

    }

    @Override
    public void onResume() {
        super.onResume();
        //saving state of map on resume of application
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    //gets map information from overpass query
    private class overpassKML extends AsyncTask<String, Void, FolderOverlay> {
        @Override
        protected FolderOverlay doInBackground(String... tag) {
            OverpassAPIProvider overpassProvider = new OverpassAPIProvider();
            BoundingBox oBB = new BoundingBox(startPoint.getLatitude() + 0.25, startPoint.getLongitude() + 0.25,
                    startPoint.getLatitude() - 0.25, startPoint.getLongitude() - 0.25);
            String url = overpassProvider.urlForTagSearchKml(tag[0], oBB, 500, 30);
            KmlDocument kmlDocument = new KmlDocument();
            boolean ok = overpassProvider.addInKmlFolder(kmlDocument.mKmlRoot, url);
            FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(bookMap, null, null, kmlDocument);

            return kmlOverlay;
        }

        @Override
        protected void onPostExecute(FolderOverlay kmlOverlay) {
            bookMap.getOverlays().add(kmlOverlay);

        }
    }

    //returns screen width in pixels
    private int getScreenWidth(){
        //getting screen width
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        return displaymetrics.widthPixels;
    }

    private void getCurrentLocation(){
        //check permissions to access GPS
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MainActivity.requestPermission(this);
        }

        //getting and setting last known location
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


            if(LocationManager.NETWORK_PROVIDER == null){
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            else{
                currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }

        latitude = currentLocation.getLatitude();
        longitude = currentLocation.getLongitude();

        //TODO: what if user declines permission
    }
}
