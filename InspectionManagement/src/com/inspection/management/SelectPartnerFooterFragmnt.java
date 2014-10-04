package com.inspection.management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SelectPartnerFooterFragmnt extends Fragment {

	public SelectPartnerFooterFragmnt() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TextView textView = new TextView(getActivity());
		textView.setText("Footer");
//		return super.onCreateView(inflater, container, savedInstanceState);
		return textView;
	}
	
}
