package com.inspection.management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.inspection.management.model.PurchaseOrder;

public class RejectionMenuFragment extends Fragment implements OnClickListener {

	private static final String REJECTED_PURCHASE_ORDER = "rejected_purchase_order";
	private Button mQcDetailButton;
	private Button mTrailerTempButton;
	private Button mDefectButton;

	private TextView mItemNameTextView;
	private TextView mItemDetailTextView;
	private TextView mContactTextView;

	private PurchaseOrder mPurchaseOrder;

	public RejectionMenuFragment() {
	}

	public static RejectionMenuFragment newInstance(
			final PurchaseOrder purchaseOrder) {
		RejectionMenuFragment f = new RejectionMenuFragment();
		Bundle args = new Bundle();
		args.putSerializable(REJECTED_PURCHASE_ORDER, purchaseOrder);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null
				&& savedInstanceState.containsKey(REJECTED_PURCHASE_ORDER)) {
			init(savedInstanceState);
		} else {
			init(getArguments());
		}
		if (mPurchaseOrder == null) {
			throw new IllegalArgumentException(
					"Rejected purchase order is null");
		}
	}

	private void init(final Bundle args) {
		if (args.containsKey(REJECTED_PURCHASE_ORDER)) {
			mPurchaseOrder = (PurchaseOrder) args
					.getSerializable(REJECTED_PURCHASE_ORDER);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_rejection_menu, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		LogoutFragment.addAsChild(this, R.id.logout_panel);

		mItemNameTextView = (TextView) view
				.findViewById(R.id.item_name_textview);
		mItemDetailTextView = (TextView) view
				.findViewById(R.id.item_details_textview);
		mContactTextView = (TextView) view.findViewById(R.id.contact_textview);

		mQcDetailButton = (Button) view
				.findViewById(R.id.quality_control_button);
		mTrailerTempButton = (Button) view.findViewById(R.id.trail_temp_button);
		mDefectButton = (Button) view.findViewById(R.id.defects_button);

		mQcDetailButton.setOnClickListener(this);
		mTrailerTempButton.setOnClickListener(this);
		mDefectButton.setOnClickListener(this);

		mItemNameTextView.setText(mPurchaseOrder.getItemName());
		mItemDetailTextView.setText(mPurchaseOrder.getItemDetails());
		mContactTextView.setText(mPurchaseOrder.getContact());
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(REJECTED_PURCHASE_ORDER, mPurchaseOrder);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quality_control_button:
			Toast.makeText(getActivity(),
					"QcDetailButton : " + mPurchaseOrder.getItemName(),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.trail_temp_button:
			Toast.makeText(getActivity(),
					"TrailerTempButton : " + mPurchaseOrder.getItemName(),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.defects_button:
			Toast.makeText(getActivity(),
					"DefectButton : " + mPurchaseOrder.getItemName(),
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		getActivity().getSupportFragmentManager().beginTransaction()
		.add(R.id.container, new QCDetailsFragment())
		.addToBackStack(null).commit();
	}

}
