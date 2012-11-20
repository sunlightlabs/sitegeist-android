package com.sunlightfoundation.sitegeist.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class MyMapView extends MapView {
	public GestureDetector detector;
	
	public MyMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
	public void addGestureDetector(GestureDetector detector) {
		this.detector = detector;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		detector.onTouchEvent(ev);
		return super.onTouchEvent(ev);
	}
}