package com.inspection.management;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

public class PODetailFragment extends Fragment implements OnClickListener {

	private static final String PO_NUMBER = "po_number";

	private String mPONumber;

	private Spinner mCarrierSpinner;
	private Spinner mWarehouseNumSpinner;
	private Spinner mStatusSpinner;
	private TextView mPOTextView;
	private Button mCancelButton;
	private Button mLogoutButton;

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
		
		getActivity().getActionBar().setTitle("A & Z LLC");
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
			
		default:
			break;
		}
	}

	private void removeSelf() {
		getActivity().getSupportFragmentManager().beginTransaction()
				.remove(this).commit();
	}
	
	private void attachSelectCarrierFragment() {
		getActivity().getSupportFragmentManager().beginTransaction()
		.add(R.id.container, new TestFragment(TestFragment.SCREEN_SELECT_CARRIER))
		.addToBackStack(null).commit();
	}

	// initialize carriers
	private void initCarrierList() {

		List<String> list = new ArrayList<String>();
		list.add("Carrier 1");
		list.add("Carrier 2");
		list.add("Carrier 3");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCarrierSpinner.setAdapter(dataAdapter);
	}

	// initialize carriers
	private void initWarehouseNumList() {

		List<String> list = new ArrayList<String>();
		list.add("Warehouse #1");
		list.add("Warehouwe #2");
		list.add("Warehouse #3");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mWarehouseNumSpinner.setAdapter(dataAdapter);
	}

	// initialize carriers
	private void initStatusList() {

		List<String> list = new ArrayList<String>();
		list.add("Status 1");
		list.add("Status 2");
		list.add("Status 3");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mStatusSpinner.setAdapter(dataAdapter);
	}

	private class OnCarrierSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
//			Toast.makeText(
//					parent.getContext(),
//					"OnCarrierSelectedListener : "
//							+ parent.getItemAtPosition(pos).toString(),
//					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}

	private class OnWareHouseNumSelectedListener implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
//			Toast.makeText(
//					parent.getContext(),
//					"OnWareHouseNumSelectedListener : "
//							+ parent.getItemAtPosition(pos).toString(),
//					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private class OnStatusSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
//			Toast.makeText(
//					parent.getContext(),
//					"OnStatusSelectedListener : "
//							+ parent.getItemAtPosition(pos).toString(),
//					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
