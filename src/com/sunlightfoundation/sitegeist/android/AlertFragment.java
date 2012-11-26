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
		
		Spanned aboutText = Html.fromHtml(	 
				getResources().getString(R.string.about_text)
		);
		
		Spanned aboutMoreInfo = Html.fromHtml(
				getResources().getString(R.string.about_more_info)
				);
		
		TextView aboutTextView = (TextView) aboutView.findViewById(R.id.about_text);
		TextView aboutMoreInfoView = (TextView) aboutView.findViewById(R.id.about_more_info);
		
		aboutTextView.setText(aboutText);
		aboutMoreInfoView.setText(aboutMoreInfo);
		
		aboutTextView.setMovementMethod(LinkMovementMethod.getInstance());
		aboutMoreInfoView.setMovementMethod(LinkMovementMethod.getInstance());

		return new AlertDialog.Builder(getActivity()).setIcon(R.drawable.icon)
			.setView(aboutView)
			.setPositiveButton(R.string.about_button, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			})
			.create();
	}
	
	
}
