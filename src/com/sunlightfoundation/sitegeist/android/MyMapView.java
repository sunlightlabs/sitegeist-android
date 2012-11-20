package com.sunlightfoundation.sitegeist.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class MyMapView extends MapView {
	public GestureDetector detector;
	public MapTapListener listener;
	
	public MyMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	    GestureDetector.OnDoubleTapListener gestures = new GestureDetector.OnDoubleTapListener() {
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				if (listener != null)
					listener.onMapTap(getProjection().fromPixels((int) e.getX(), (int) e.getY()));
				return true;
			}
			
			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				return false;
			}
			
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				return false;
			}
		};
		
		detector = new GestureDetector(this.getContext(), new GestureDetector.SimpleOnGestureListener());
		detector.setOnDoubleTapListener(gestures);
    }
	
	public void setMapTapListener(MapTapListener listener) {
		this.listener = listener;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		detector.onTouchEvent(ev);
		return super.onTouchEvent(ev);
	}
	
	interface MapTapListener {
		public void onMapTap(GeoPoint point);
	}
}