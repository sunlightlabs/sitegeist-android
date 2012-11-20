package com.sunlightfoundation.sitegeist.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import com.sunlightfoundation.sitegeist.android.utils.ActionBarUtils;
import com.sunlightfoundation.sitegeist.android.utils.Utils;
import com.sunlightfoundation.sitegeist.android.utils.FragmentUtils;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity implements ActionBarUtils.HasActionMenu {
	public double lat = 0, lng = 0;
	
	public static int RESPONSE_LOCATION = 1;
	public static int LOCATION_YES = 1;
	public static int LOCATION_NO = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setupControls();
        setupPager();
        getLocation();
    }
    
    private void getLocation() {
    	if (!quickGetLocation())
    		findLocation();
    }
    
    private boolean quickGetLocation() {
    	Location location = Utils.lastKnownLocation(this);
    	if (location != null) {
    		lat = location.getLatitude();
    		lng = location.getLongitude();
    		return true;
    	} else
    		return false;
    }
    
    private void findLocation() {
    	this.startActivityForResult(new Intent(this, FindLocation.class), RESPONSE_LOCATION);
    }
    
    private void onFindLocation(double lat, double lng) {
    	
    }
    
    private void onFindLocation() {
    	
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode != RESPONSE_LOCATION) return;
    }
    
    public void setupControls() {
    	ActionBarUtils.setTitle(this, R.string.app_name, null);
		
		ActionBarUtils.setActionButton(this, R.id.action_1, R.drawable.about, new View.OnClickListener() {
			public void onClick(View v) {
				showAbout();
			}
			
		});
		
		ActionBarUtils.setActionMenu(this, R.menu.main);
    }
    
    public void showAbout() {
		FragmentUtils.alertDialog(this, AlertFragment.ABOUT);		
	}

	private void setupPager() {
    	BasicAdapter adapter = new BasicAdapter(getSupportFragmentManager());
		adapter.add("PEOPLE", WebFragment.newInstance("people"));
		adapter.add("ENVIRONMENT", WebFragment.newInstance("environment"));
		adapter.add("FUN", WebFragment.newInstance("fun"));
		adapter.add("HISTORY", WebFragment.newInstance("history"));
		adapter.add("HOUSING", WebFragment.newInstance("housing"));
		
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(5);
		pager.setAdapter(adapter);
		
		TabPageIndicator titleIndicator = (TabPageIndicator) findViewById(R.id.titles);
    	titleIndicator.setViewPager(pager);
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
		case R.id.location:
			findLocation();
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
			String url = "http://sitegeist.sunlightfoundation.com/api/" + tab + "/?header=0";
			MainActivity activity = (MainActivity) getActivity();
			if (activity.lat != 0 || activity.lng != 0)
				url += "&cll=" + activity.lat + "," + activity.lng;
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
	
	static class LocationAlertFragment extends DialogFragment {
		
		public static LocationAlertFragment create(int type) {
			LocationAlertFragment fragment = new LocationAlertFragment();
			fragment.setRetainInstance(true);
			return fragment;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			
//			View aboutView = inflater.inflate(R.layout.about, null);
//			
//			return new AlertDialog.Builder(getActivity()).setIcon(R.drawable.icon)
//					.setView(aboutView)
//					.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							getActivity().finish();
//						}
//					})
//					.setPositiveButton("Go to Location Settings", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							getActivity().startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
//						}
//					})
//					.create();
			return null;
		}
	}
}