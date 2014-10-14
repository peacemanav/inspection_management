package com.inspection.management;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ui.components.ManageActionBarTitle;

public class PODetailFragment extends Fragment implements OnClickListener,
		ManageActionBarTitle {

	private static final String PO_NUMBER = "po_number";

	private String mPONumber;

	private Spinner mCarrierSpinner;
	private Spinner mWarehouseNumSpinner;
	private Spinner mStatusSpinner;
	private TextView mPOTextView;
	private Button mCancelButton;
	private Button mLogoutButton;
	private Button mSaveNContinueButton;
	private View mSavenConitnueView;

	private String mCarrier;
	private String mWareHouseNumber;
	private String mStatus;

	public PODetailFragment() {
	}

	public static PODetailFragment newInstance(String poNumber) {
		PODetailFragment instance = new PODetailFragment();
		Bundle args = new Bundle();
		args.putString(PO_NUMBER, poNumber);
		instance.setArguments(args);
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateActionBarTitle();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_po_detail_layout, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mCarrierSpinner = (Spinner) view.findViewById(R.id.carrier_dropdown);
		mWarehouseNumSpinner = (Spinner) view
				.findViewById(R.id.warehouse_no_dropdown);
		mStatusSpinner = (Spinner) view.findViewById(R.id.status_dropdown);
		mPOTextView = (TextView) view.findViewById(R.id.title_po_number);

		mCancelButton = (Button) view.findViewById(R.id.cancel_button);
		mCancelButton.setOnClickListener(this);

		mLogoutButton = (Button) view.findViewById(R.id.logout_button);
		mLogoutButton.setOnClickListener(this);

		mSavenConitnueView = view.findViewById(R.id.save_panel);
		mSaveNContinueButton = (Button) view
				.findViewById(R.id.save_n_continue_button);
		mSaveNContinueButton.setOnClickListener(this);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			init(savedInstanceState);
		} else {
			init(getArguments());
		}
		// get the details of po and init dropdown list
		initCarrierList();
		initWarehouseNumList();
		initStatusList();

		mPOTextView.setText("PO # 12345");

		mCarrierSpinner
				.setOnItemSelectedListener(new OnCarrierSelectedListener());
		mWarehouseNumSpinner
				.setOnItemSelectedListener(new OnWareHouseNumSelectedListener());
		mStatusSpinner
				.setOnItemSelectedListener(new OnStatusSelectedListener());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(PO_NUMBER, mPONumber);
	}

	private void init(Bundle bundle) {
		if (bundle != null && bundle.containsKey(PO_NUMBER)) {
			mPONumber = bundle.getString(PO_NUMBER);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_button:
			removeSelf();
			break;

		case R.id.logout_button:
			attachSelectCarrierFragment();
			break;
		case R.id.save_n_continue_button:
			// TODO: Save data to database
			break;

		default:
			break;
		}
	}

	private void removeSelf() {
		getActivity().getSupportFragmentManager().beginTransaction()
				.remove(this).commit();
	}

	private void attachSelectCarrierFragment() {
		getActivity()
				.getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.container,
						new SelectPartnerCarrierFragment(
								SelectPartnerCarrierFragment.SCREEN_SELECT_CARRIER))
				.addToBackStack(null).commit();
	}

	// initialize carriers
	private void initCarrierList() {

		List<String> list = new ArrayList<String>();
		list.add((String) mCarrierSpinner.getPrompt());
		list.add("Carrier 1");
		list.add("Carrier 2");
		list.add("Carrier 3");
		ArrayAdapter<String> dataAdapter = new PromptSpinnerAdapter(
				getActivity(), android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCarrierSpinner.setAdapter(dataAdapter);
	}

	// initialize carriers
	private void initWarehouseNumList() {

		List<String> list = new ArrayList<String>();
		list.add((String) mWarehouseNumSpinner.getPrompt());
		list.add("Warehouse #1");
		list.add("Warehouwe #2");
		list.add("Warehouse #3");
		ArrayAdapter<String> dataAdapter = new PromptSpinnerAdapter(
				getActivity(), android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mWarehouseNumSpinner.setAdapter(dataAdapter);
	}

	// initialize carriers
	private void initStatusList() {

		List<String> list = new ArrayList<String>();
		list.add((String) mStatusSpinner.getPrompt());
		list.add("Status 1");
		list.add("Status 2");
		list.add("Status 3");
		ArrayAdapter<String> dataAdapter = new PromptSpinnerAdapter(
				getActivity(), android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mStatusSpinner.setAdapter(dataAdapter);
	}

	private class OnCarrierSelectedListener implements OnItemSelectedListener {
		private boolean isInitilizing = true;

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if (isInitilizing) {
				isInitilizing = false;
				return;
			}
			mCarrier = (String) parent.getAdapter().getItem(pos);
			onItemSelectionChanged();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}

	private class OnWareHouseNumSelectedListener implements
			OnItemSelectedListener {
		private boolean isInitilizing = true;

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if (isInitilizing) {
				isInitilizing = false;
				return;
			}
			mWareHouseNumber = (String) parent.getAdapter().getItem(pos);
			onItemSelectionChanged();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private class OnStatusSelectedListener implements OnItemSelectedListener {
		private boolean isInitilizing = true;

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if (isInitilizing) {
				isInitilizing = false;
				return;
			}
			mStatus = (String) parent.getAdapter().getItem(pos);
			onItemSelectionChanged();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	@Override
	public void updateActionBarTitle() {
		getActivity().getActionBar().setTitle("A & Z LLC");
	}

	private void onItemSelectionChanged() {
		if (TextUtils.isEmpty(mCarrier)
				|| mCarrierSpinner.getPrompt().equals(mCarrier)) {
			enableView(false);
		} else if (TextUtils.isEmpty(mWareHouseNumber)
				|| mWarehouseNumSpinner.getPrompt().equals(mWareHouseNumber)) {
			enableView(false);
		} else if (TextUtils.isEmpty(mStatus)
				|| mStatusSpinner.getPrompt().equals(mStatus)) {
			enableView(false);
		} else {
			enableView(true);
		}
	}

	private void enableView(boolean enable) {
		// TODO: background color need to be extracted from images and set
		if (enable) {
			mSavenConitnueView.setVisibility(View.VISIBLE);
		} else {
			mSavenConitnueView.setVisibility(View.GONE);
		}
	}

	private class PromptSpinnerAdapter extends ArrayAdapter<String> {

		public PromptSpinnerAdapter(Context context, int resource,
				List<String> list) {
			super(context, resource, list);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			View v = null;
			if (position == 0) {
				TextView tv = new TextView(getContext());
				tv.setHeight(0);
				tv.setVisibility(View.GONE);
				tv.setTag(R.id.prompt_tag_key, "prompt");
				v = tv;
			} else if (convertView != null
					&& "prompt".equalsIgnoreCase((String) convertView
							.getTag(R.id.prompt_tag_key))) {
				// don't reuse prompt view
				v = super.getDropDownView(position, null, parent);
			} else {
				v = super.getDropDownView(position, convertView, parent);
			}
			return v;
		}
	}
}
