package com.sunlightfoundation.sitegeist.android;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.sunlightfoundation.sitegeist.android.utils.Utils;

public class FindLocation extends MapActivity implements LocationListener {

	private MapView map;
	private MapController controller;
	private TextView debug;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_location);
        
        setupControls();
    }
	
	private void setupControls() {
		map = (MapView) findViewById(R.id.map);
		controller = map.getController();
	    map.setBuiltInZoomControls(true);
	    
	    debug = (TextView) findViewById(R.id.debug);
	    
	    GeoPoint center = quickGetLocation();
	    if (center != null) {
	    	centerTo(center);
	    	controller.setZoom(15);
	    } else {
	    	controller.setZoom(2);
	    }
	    	
	}
	
	private void centerTo(GeoPoint center) {
		controller.setCenter(center);
		debug.setText("" + center.toString());
	}
	
	private GeoPoint quickGetLocation() {
    	Location location = Utils.lastKnownLocation(this);
    	if (location != null) {
    		int lat = (int) (location.getLatitude() * 1000000);
    		int lng = (int) (location.getLongitude() * 1000000);
    		return new GeoPoint(lat, lng);
    	}
    	return null;
    }
	
	
	
	@Override
	public void onLocationChanged(Location location) {
		
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}