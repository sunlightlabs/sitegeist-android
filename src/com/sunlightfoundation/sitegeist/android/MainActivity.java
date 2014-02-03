package com.sunlightfoundation.sitegeist.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.maps.GeoPoint;
import com.sunlightfoundation.sitegeist.android.utils.ActionBarUtils;
import com.sunlightfoundation.sitegeist.android.utils.FragmentUtils;
import com.sunlightfoundation.sitegeist.android.utils.Utils;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity implements ActionBarUtils.HasActionMenu {
	public GeoPoint location;
	
	private static int RESPONSE_LOCATION = 1;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if (hasLocation())
        	haveLocation();
        else
        	findLocation();
    }
    
    
    private boolean hasLocation() {
    	GeoPoint savedLocation = Utils.savedLocation(this);
    	if (savedLocation != null) {
    		this.location = savedLocation;
    		return true;
    	}
    	
    	return false;
    }
    
    private void findLocation() {
    	this.startActivityForResult(new Intent(this, FindLocation.class), RESPONSE_LOCATION);
    }
    
    private void onFindLocation() {
    	if (hasLocation())
    		haveLocation();
    	else
    		finish(); // just leave
    }
    
    // set up the pager and load the web views
    private void haveLocation() {
    	setupControls();
    	setupPager();
    }
    
    // will only be run if we have a location, and a source
    public void setupControls() {
    	ActionBarUtils.setTitle(this, getResources().getString(R.string.app_name), null);
		
    	ActionBarUtils.setActionButton(this, R.id.action_1, R.drawable.location, new View.OnClickListener() {
			public void onClick(View v) {
				findLocation();
			}
		});
    	
    	ActionBarUtils.setActionButton(this, R.id.action_2, R.drawable.share, new View.OnClickListener() {
			public void onClick(View v) {
				sharePane();
			}
		});
		
		ActionBarUtils.setActionMenu(this, R.menu.main);
    }
    
    public void showAbout() {
		FragmentUtils.alertDialog(this, AlertFragment.ABOUT);		
	}
    
    public void sharePane() {
    	String url = "https://sitegeist.sunlightfoundation.com/share/";
    	ViewPager pager = (ViewPager) findViewById(R.id.pager);
    	BasicAdapter adapter = (BasicAdapter) pager.getAdapter();
    	String title = (String) adapter.getPageTitle(pager.getCurrentItem());
    	title = title.toLowerCase();
		if (this.location != null)
			url += "?cll=" + Utils.geoToLoc(this.location.getLatitudeE6()) + "," + Utils.geoToLoc(this.location.getLongitudeE6()) + "&pane=" + title;
		Intent intent = new Intent(Intent.ACTION_SEND).setType("text/plain")
				.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_text) + " " + url)
				.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_email_subject));
		startActivity(Intent.createChooser(intent, "Share this:"));
    }

	private void setupPager() {
    	BasicAdapter adapter = new BasicAdapter(getSupportFragmentManager());
		adapter.add("PEOPLE", WebFragment.newInstance("people"));
		adapter.add("HOUSING", WebFragment.newInstance("housing"));
		adapter.add("FUN", WebFragment.newInstance("fun"));
		adapter.add("WEATHER", WebFragment.newInstance("environment"));
		adapter.add("HISTORY", WebFragment.newInstance("history"));
		
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(5);
		pager.setAdapter(adapter);
		
		TabPageIndicator titleIndicator = (TabPageIndicator) findViewById(R.id.titles);
    	titleIndicator.setViewPager(pager);
    	if (pager != null) {
    		titleIndicator.setCurrentItem(0);
    	}
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode != RESPONSE_LOCATION) return;
    	onFindLocation();
    }
    
    @Override 
	public boolean onCreateOptionsMenu(Menu menu) { 
		super.onCreateOptionsMenu(menu); 
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		menuSelected(item);
		return true;
	}
	
	@Override
	public void menuSelected(MenuItem item) {
		switch(item.getItemId()) { 
		case R.id.feedback:
			Utils.doFeedback(this);
			break;
		case R.id.review:
			Utils.goReview(this);
			break;
		case R.id.about:
			showAbout();
			break;
		}
	}
	
	public static class WebFragment extends Fragment {
		
		private String tab;
		
		public static WebFragment newInstance(String tab) {
			WebFragment frag = new WebFragment();
			Bundle args = new Bundle();
			args.putString("tab", tab);
			frag.setArguments(args);
			frag.setRetainInstance(true);
			return frag;
		}
		
		public WebFragment() {}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			tab = getArguments().getString("tab");
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.webview_fragment, container, false);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			loadSite();
		}
		
		public void loadSite() {
			Utils.loadUrl(Utils.webViewFor(getView()), url());
		}
		
		public String url() {
			String url = "https://sitegeist.sunlightfoundation.com/api/" + tab + "/?header=0";
			MainActivity activity = (MainActivity) getActivity();
			if (activity.location != null)
				url += "&cll=" + Utils.geoToLoc(activity.location.getLatitudeE6()) + "," + Utils.geoToLoc(activity.location.getLongitudeE6());
			return url;
		}
	}
	
	public class BasicAdapter extends FragmentPagerAdapter {
		private List<WebFragment> fragments = new ArrayList<WebFragment>();
		private List<String> names = new ArrayList<String>();
		
		public BasicAdapter(FragmentManager manager) {
	        super(manager);
	    }
		
		public void add(String title, WebFragment fragment) {
			fragments.add(fragment);
			names.add(title);
		}
		
		public void updateAll() {
			for (int i=0; i<fragments.size(); i++)
				fragments.get(i).loadSite();
		}

	    @Override
	    public int getCount() {
	        return fragments.size();
	    }

	    @Override
	    public Fragment getItem(int position) {
	        return fragments.get(position);
	    }
	    
	    @Override
        public CharSequence getPageTitle(int position) {
            return names.get(position);
        }
	}
}