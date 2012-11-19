package com.sunlightfoundation.sitegeist.android.utils;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;

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
	
	public static Location lastKnownLocation(Context context) {
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
		
		return location;
	}
}