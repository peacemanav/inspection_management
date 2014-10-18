package com.inspection.management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class QCDetailsFragment extends Fragment {

	private static final String TAG = QCDetailsFragment.class.getSimpleName();

	private Spinner mProductTypeSpinner;
	private Spinner mRejectionCategorySpinner;
	private Spinner mRpcSpinner;
	private Spinner mBrandSpinner;
	private Spinner mPrivateLabelSpinner;
	private Spinner mClaimFiledSpinner;

	public QCDetailsFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.qc_details_layout, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		mProductTypeSpinner = (Spinner) view.findViewById(R.id.product_type_spinner);
		setupProductTypeSpinner();
		
		mRejectionCategorySpinner = (Spinner) view.findViewById(R.id.rejection_category_spinner);
		setupRejectionCategorySpinner();
		
		mRpcSpinner = (Spinner) view.findViewById(R.id.rpc_category);
		setupRpcSpinner();
		
		mBrandSpinner = (Spinner) view.findViewById(R.id.brand_category);
		setupBrandSpinner();
		
		mPrivateLabelSpinner = (Spinner) view.findViewById(R.id.private_label_category);
		setupPrivateLabelSpinner();
		
		mClaimFiledSpinner = (Spinner) view.findViewById(R.id.claim_filed_category_spinner);
		setupClaimFiledSpinner();
	}

	private void setupProductTypeSpinner() {
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.product_type_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mProductTypeSpinner.setAdapter(adapter);
	}
	
	private void setupRejectionCategorySpinner() {
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.rejection_category_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mRejectionCategorySpinner.setAdapter(adapter);
	}
	
	private void setupRpcSpinner() {
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.rpc_category_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mRpcSpinner.setAdapter(adapter);
	}
	
	private void setupBrandSpinner() {
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.brand_category_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mBrandSpinner.setAdapter(adapter);
	}
	
	private void setupPrivateLabelSpinner() {
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.private_label_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mPrivateLabelSpinner.setAdapter(adapter);
	}
	
	private void setupClaimFiledSpinner() {
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.claim_filed_category_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mClaimFiledSpinner.setAdapter(adapter);
	}
}
