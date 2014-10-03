package com.ui.components;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public abstract class AlphabeticalListFragment extends Fragment {

	public static final String TAG = AlphabeticalListFragment.class
			.getSimpleName();

	private ListView mListView;
	
//	private static final String PROVIDER_URI = "uri";
//
//	public static AlphabeticalListFragment newInstance(Uri uri) {
//		AlphabeticalListFragment fragment = new AlphabeticalListFragment();
//
//		// Supply index input as an argument.
//		Bundle args = new Bundle();
//		args.putParcelable(PROVIDER_URI, uri);
//		fragment.setArguments(args);
//
//		return fragment;
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.alphabetical_list_view, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
//		mListView = (ListView) view.findViewById(R.id.list_view);
		mListView.setAdapter(getAdapter());
	}
	
	abstract public CursorAdapter getAdapter();
}
