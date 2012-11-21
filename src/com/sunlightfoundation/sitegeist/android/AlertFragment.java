package com.sunlightfoundation.sitegeist.android;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunlightfoundation.sitegeist.android.R;

public class AlertFragment extends DialogFragment {

	public static final int ABOUT = 1;
	public static final int CHANGELOG = 2;
	public static final int FIRST = 3;
	
	public static AlertFragment create(int type) {
		AlertFragment fragment = new AlertFragment();
		Bundle args = new Bundle();
		args.putInt("type", type);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		int type = getArguments().getInt("type");
		
		if (type == ABOUT)
			return about(inflater);
		else
			return null;
	}

	
	public Dialog about(LayoutInflater inflater) {
		View aboutView = inflater.inflate(R.layout.about, null);
		
		Spanned about2 = Html.fromHtml(
				"Sitegeist helps you quickly learn more about your surroundings " +
				"and draws on free information to showcase what's possible with access to data. " +
				
				"The project presents localized information with straightforward infographics so you can " +
				"get back to enjoying the neighborhood. Sitegeist was created by the Sunlight Foundation " +
				
				"with input from <a href=\"http://www.ideo.com/\">IDEO</a> and support from the " + 
				"<a href=\"http://www.knightfoundation.org/\">John S. and James L. Knight Foundation</a>." 
				
		);
		TextView aboutView2 = (TextView) aboutView.findViewById(R.id.about_2);
		aboutView2.setText(about2);
		aboutView2.setMovementMethod(LinkMovementMethod.getInstance());

		Spanned about1 = Html.fromHtml(
				"<div style=\"text-align:center\"><a href=\"http://sunlightfoundation.com\">[more info link]</a>   " +
			    "<a href=\"http://sunlightfoundation.com\">[methodology link]</a></div>"
				);
		TextView aboutView1 = (TextView) aboutView.findViewById(R.id.about_1);
		aboutView1.setText(about1);
		aboutView1.setMovementMethod(LinkMovementMethod.getInstance());
		
		return new AlertDialog.Builder(getActivity()).setIcon(R.drawable.icon)
			.setView(aboutView)
			.setPositiveButton(R.string.about_button, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			})
			.create();
	}
	
	
}
