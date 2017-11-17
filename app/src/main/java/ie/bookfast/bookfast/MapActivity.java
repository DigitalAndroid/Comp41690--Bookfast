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
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.location.OverpassAPIProvider;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

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

        //navigateToMarkers();

        //filling tile on screen with map
        bookMap.setTileSource(TileSourceFactory.MAPNIK);

    }

    //method to set onclicks for markers - when you click one, you navigate to it.
    private void navigateToMarkers(){
        for(int i=0; i<bookMap.getOverlays().size(); i++){
            if(bookMap.getOverlays().get(i) instanceof Marker){
                ((Marker) bookMap.getOverlays().get(i)).setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        DrawRoad drawRoad = new DrawRoad(getCurrentLocation(), marker.getPosition());
                        drawRoad.execute();

                        Toast.makeText(MapActivity.this, "Marker clicked!", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                });
            }
        }
    }

    //we do this in a different thread as it takes some amount of processing power to get route to ucd.
    private class DrawRoad extends AsyncTask<Void, Void, Road> {
        GeoPoint startPoint;
        GeoPoint endPoint;

        public DrawRoad(GeoPoint startPoint, GeoPoint endPoint){
            this.startPoint = startPoint;
            this.endPoint = endPoint;
        }

        @Override
        protected Road doInBackground(Void... voids) {
            RoadManager roadManager = new OSRMRoadManager(MapActivity.this);

            ArrayList<GeoPoint> waypoints = new ArrayList<>();
            waypoints.add(startPoint);
            waypoints.add(endPoint);

            Road road = roadManager.getRoad(waypoints);

            return road;
        }

        protected void onPostExecute(Road road){
            updateUIWithRoad(road);
        }
    }

    public void updateUIWithRoad(Road road){
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

        bookMap.getOverlays().add(roadOverlay);

        bookMap.invalidate();

        //adding bubble at every new road/turn
        for (int i=0; i<road.mNodes.size(); i++){
            RoadNode node = road.mNodes.get(i);
            Marker nodeMarker = new Marker(bookMap);
            nodeMarker.setSnippet(node.mInstructions);
            nodeMarker.setSubDescription(Road.getLengthDurationText(this, node.mLength, node.mDuration));
            nodeMarker.setIcon(getResources().getDrawable(R.mipmap.direction_marker));
            nodeMarker.setPosition(node.mLocation);
            nodeMarker.setTitle("Step "+i);
            determineDirectonImage(node, nodeMarker);
            bookMap.getOverlays().add(nodeMarker);
        }

    }

    //setting the direction icons
    private void determineDirectonImage(RoadNode node, Marker nodeMarker){
        switch (node.mManeuverType){
            case 1: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_continue));
                break;
            case 3: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_slight_left));
                break;
            case 4: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_turn_left));
                break;
            case 5: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_sharp_left));
                break;
            case 6: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_slight_right));
                break;
            case 7: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_turn_right));
                break;
            case 8: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_sharp_right));
                break;
            case 12: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_u_turn));
                break;
            case 13: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_u_turn));
                break;
            case 14: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_u_turn));
                break;
            case 24: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_arrived));
                break;
            case 27: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_roundabout));
                break;
            case 28: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_roundabout));
                break;
            case 29: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_roundabout));
                break;
            case 30: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_roundabout));
                break;
            case 31: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_roundabout));
                break;
            case 32: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_roundabout));
                break;
            case 33: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_roundabout));
                break;
            case 34: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_roundabout));
                break;
            default: nodeMarker.setImage(getResources().getDrawable(R.mipmap.ic_continue));
                break;
        }
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
            List<Overlay> overlayList = kmlOverlay.getItems();

            for(int i=0; i<overlayList.size(); i++){
                if(overlayList.get(i) instanceof Marker){
                    Marker marker = (Marker) overlayList.get(i);
                    marker.setIcon(getResources().getDrawable(R.mipmap.ic_library));

                    bookMap.getOverlays().add(marker);
                }

            }

            //after markers placed, set the onclick listeners for them
            navigateToMarkers();
        }
    }

    //returns screen width in pixels
    private int getScreenWidth(){
        //getting screen width
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        return displaymetrics.widthPixels;
    }

    private GeoPoint getCurrentLocation(){
        GeoPoint currentGeoPointLocation;

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

        return currentGeoPointLocation = new GeoPoint(latitude, longitude);
        //TODO: what if user declines permission
    }
}
