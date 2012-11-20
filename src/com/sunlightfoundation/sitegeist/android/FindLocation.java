package com.sunlightfoundation.sitegeist.android;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.view.GestureDetector;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.sunlightfoundation.sitegeist.android.utils.Utils;

public class FindLocation extends MapActivity implements MyMapView.MapTapListener {

	private MyMapView map;
	private MapController controller;
	private TextView debugChosen, debugCurrent;
	
	private MyLocationOverlay location;
	
	private SelectedOverlay selected;
	private GeoPoint selectedPoint;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_location);
        
        setupControls();
        setupMap();
    }
	
	private void setupControls() {
		debugChosen = (TextView) findViewById(R.id.debug_chosen);
	    debugCurrent = (TextView) findViewById(R.id.debug_current);
	}
	
	private void setupMap() {
		map = (MyMapView) findViewById(R.id.map);
		map.setMapTapListener(this);
		
		controller = map.getController();
	    map.setBuiltInZoomControls(true);
	    
	    initialCenter();
	    location = new MyLocationOverlay(this, map);
	    selected = new SelectedOverlay(this);
	    
	    List<Overlay> overlays = map.getOverlays();
	    overlays.add(location);
	    overlays.add(selected);
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		if (location != null)
			location.enableMyLocation();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (location != null)
			location.disableMyLocation();
	}
	
	@Override
	public void onMapTap(GeoPoint point) {
		selected(point);
	}
	
	public void selected(GeoPoint point) {
		debugChosen.setText(point.toString());
		selectedPoint = point;
	}
	
	private void initialCenter() {
		GeoPoint center = quickGetLocation();
	    if (center != null) {
	    	controller.setZoom(15);
			controller.setCenter(center);
			debugCurrent.setText(center.toString());
	    } else
	    	controller.setZoom(4);
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
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	static class SelectedOverlay extends Overlay {
		private FindLocation context;
		
		public SelectedOverlay(FindLocation context) {
			this.context = context;
		}
		
		@Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
            
            if (context.selectedPoint != null) {
	            Point screenPts = new Point();
	            mapView.getProjection().toPixels(context.selectedPoint, screenPts);
 
	            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.person);            
	            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
            }
            return true;
        }
	}

}