package com.sunlightfoundation.sitegeist.android.utils;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.sunlightfoundation.sitegeist.android.R;


public class Utils {
	public static final String TAG = "Sitegeist";
	public static final String USER_AGENT = "com.sunlightfoundation.sitegeist.android";
	
	@SuppressLint("SetJavaScriptEnabled")
	public static WebView webViewFor(final View view) {
		WebView results = (WebView) view.findViewById(R.id.content);
		results.getSettings().setJavaScriptEnabled(true);
		return results;
	}
	
	public static void loadUrl(WebView webview, String url) {
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("User-Agent", USER_AGENT);
		webview.loadUrl(url, headers);
	}
	
	public static void doFeedback(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", activity.getResources().getString(R.string.contact_email), null));
		intent.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.contact_subject));
		activity.startActivity(intent);
	}
	
	public static void goReview(Activity activity) {
		String packageName = activity.getResources().getString(R.string.package_name);
		String uri = "market://details?id=" + packageName;
		try {	
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
		} catch(ActivityNotFoundException e) {
			// swallow
		}
	}
	
	public static boolean locationEnabled(Context context) {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); 
	}
	
	public static GeoPoint lastKnownLocation(Context context) {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = null;
		
		String provider = LocationManager.GPS_PROVIDER;
		if (manager.isProviderEnabled(provider))
			location = manager.getLastKnownLocation(provider);

		if (location == null) {
			provider = LocationManager.NETWORK_PROVIDER;
			if (manager.isProviderEnabled(provider))
				location = manager.getLastKnownLocation(provider);
		}
		
		if (location != null)
			return new GeoPoint(locToGeo(location.getLatitude()), locToGeo(location.getLongitude()));
		else
			return null;
	}
	
	public static boolean saveLocation(Context context, GeoPoint point) {
		return PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("savedLocation", true)
				.putInt("lat", point.getLatitudeE6())
				.putInt("lng", point.getLongitudeE6())
				.commit();
	}
	
	public static GeoPoint savedLocation(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (prefs.getBoolean("savedLocation", false))
			return new GeoPoint(prefs.getInt("lat", 0), prefs.getInt("lng", 0));
		else
			return null;
	}
	
	public static int locToGeo(double degrees) {
		return (int) (degrees * 1E6);
	}
	
	public static double geoToLoc(int degreesE6) {
		return (double) (((double) degreesE6) / 1E6);
	}
	
	public static void alert(Context context, int msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}