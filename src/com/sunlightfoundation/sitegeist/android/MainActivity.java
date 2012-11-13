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
		
		adapter.add("census", "People", WebFragment.newInstance(R.drawable.census));
		adapter.add("environment", "Env", WebFragment.newInstance(R.drawable.environment));
		adapter.add("fun", "Fun", WebFragment.newInstance(R.drawable.fun));
		adapter.add("history", "History", WebFragment.newInstance(R.drawable.history));
		adapter.add("housing", "Housing", WebFragment.newInstance(R.drawable.housing));
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
		
		int mockId;
		
		public static WebFragment newInstance(int mockId) {
			WebFragment frag = new WebFragment();
			Bundle args = new Bundle();
			args.putInt("mock_id", mockId);
			frag.setArguments(args);
			frag.setRetainInstance(true);
			return frag;
		}
		
		public WebFragment() {}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mockId = getArguments().getInt("mock_id");
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.webview_fragment, container, false);
			ImageView content = (ImageView) view.findViewById(R.id.content);
			content.setImageResource(mockId);
			return view;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
		}
	}
}