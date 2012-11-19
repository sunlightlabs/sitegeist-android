package com.sunlightfoundation.sitegeist.android;

import java.util.ArrayList;
import java.util.List;

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

import com.sunlightfoundation.sitegeist.android.utils.ActionBarUtils;
import com.sunlightfoundation.sitegeist.android.utils.Utils;
import com.sunlightfoundation.sitegeist.android.utils.FragmentUtils;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity implements ActionBarUtils.HasActionMenu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setupControls();
        setupPager();
    }
    
    public void setupControls() {
    	ActionBarUtils.setTitle(this, R.string.app_name, null);
		
		ActionBarUtils.setActionButton(this, R.id.action_1, R.drawable.feedback, new View.OnClickListener() {
			public void onClick(View v) { 
				Utils.doFeedback(MainActivity.this);
			}
		});
		
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
		
		TabPageIndicator titleIndicator = (TabPageIndicator)findViewById(R.id.titles);
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
		case R.id.review:
			Utils.goReview(this);
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
			return "http://ec2-23-22-182-132.compute-1.amazonaws.com/api/" + tab + "/?header=0";
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