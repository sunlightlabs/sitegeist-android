package com.sunlightfoundation.sitegeist.android;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.sunlightfoundation.sitegeist.android.utils.ActionBarUtils;
import com.sunlightfoundation.sitegeist.android.utils.Utils;

public class FindLocation extends MapActivity implements MyMapView.MapTapListener {

	private MyMapView map;
	private MapController controller;
	
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
		ActionBarUtils.setTitle(this, R.string.title_location, null);
	    
	    findViewById(R.id.center).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (location != null && controller != null) {
					GeoPoint myLocation = location.getMyLocation();
					if (myLocation != null) {
						controller.animateTo(myLocation);
						selected(myLocation);
					} else
						Utils.alert(FindLocation.this, R.string.enable_location);
				}
			}
		});
	    
	    findViewById(R.id.use_location).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectedPoint != null) {
					Utils.saveLocation(FindLocation.this, selectedPoint);
					finish();
				}
			}
		});
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
		selectedPoint = point;
		controller.animateTo(point);
	}
	
	private void initialCenter() {
		GeoPoint center = Utils.lastKnownLocation(this);
	    if (center != null) {
	    	controller.setZoom(15);
			controller.setCenter(center);
	    } else
	    	controller.setZoom(4);
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
 
	            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker);            
	            canvas.drawBitmap(bmp, screenPts.x-40, screenPts.y-63, null);         
            }
            return true;
        }
	}

}