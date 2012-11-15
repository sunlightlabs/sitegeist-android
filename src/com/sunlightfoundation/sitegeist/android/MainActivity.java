package com.sunlightfoundation.sitegeist.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sunlightfoundation.sitegeist.android.utils.ActionBarUtils;
import com.sunlightfoundation.sitegeist.android.utils.TitlePageAdapter;
import com.sunlightfoundation.sitegeist.android.utils.Utils;

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
		
		ActionBarUtils.setActionMenu(this, R.menu.main);
    }
    
    private void setupPager() {
		TitlePageAdapter adapter = new TitlePageAdapter(this);
		
		adapter.add("people", "People", WebFragment.newInstance("people"));
		adapter.add("environment", "Env", WebFragment.newInstance("environment"));
		adapter.add("fun", "Fun", WebFragment.newInstance("fun"));
		adapter.add("history", "History", WebFragment.newInstance("history"));
		adapter.add("housing", "Housing", WebFragment.newInstance("housing"));
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
		
		String tab;
		
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
			View view = inflater.inflate(R.layout.webview_fragment, container, false);
			Utils.loadUrl(Utils.webViewFor(view), url());
			return view;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
		}
		
		public String url() {
			return "http://ec2-23-22-182-132.compute-1.amazonaws.com:8080/api/" + tab + "/?header=0";
		}
	}
}