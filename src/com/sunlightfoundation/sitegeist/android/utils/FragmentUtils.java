package com.sunlightfoundation.sitegeist.android.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.sunlightfoundation.sitegeist.android.R;
import com.sunlightfoundation.sitegeist.android.AlertFragment;

public class FragmentUtils {
	
	
	public static void alertDialog(FragmentActivity activity, int type) {
		AlertFragment.create(type).show(activity.getSupportFragmentManager(), "dialog");
	}
	

}
