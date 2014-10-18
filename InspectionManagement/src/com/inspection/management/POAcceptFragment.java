package com.inspection.management;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inspection.management.model.PurchaseOrder;
import com.inspection.management.model.PurchaseOrder.AcceptStatus;

public class POAcceptFragment extends Fragment {
	private ListView mListView;
	private List<PurchaseOrder> mItemList;
	private PurchaseOrderAdapter mListAdapter;

	public POAcceptFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_po_accept, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		LogoutFragment.addAsChild(this, R.id.logout_panel);
		mListView = (ListView) view.findViewById(R.id.order_list);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mItemList = new ArrayList<PurchaseOrder>();
		createDummyData();
		createDummyData();
		createDummyData();
		mListAdapter = new PurchaseOrderAdapter(getActivity(),
				android.R.layout.simple_list_item_1, mItemList);
		mListView.setAdapter(mListAdapter);
	}

	private void createDummyData() {

		PurchaseOrder item = new PurchaseOrder();

		item.setItemName("Fuji Apples");
		item.setItemDetails("Washington 8 oz");
		item.setContact("009418423");
		item.setStatus(AcceptStatus.NONE);

		mItemList.add(item);

		item = new PurchaseOrder();
		item.setItemName("Red Delicious Apples");
		item.setItemDetails("Washington extra fancy 8 oz");
		item.setContact("009418423");
		item.setStatus(AcceptStatus.NONE);

		mItemList.add(item);

		item = new PurchaseOrder();
		item.setItemName("Red Delicious Apples");
		item.setItemDetails("Washington extra fancy 8 oz");
		item.setContact("009418423");
		item.setStatus(AcceptStatus.NONE);

		mItemList.add(item);
	}

	private OnClickListener mListButtonClickLintener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			PurchaseOrder item = (PurchaseOrder) v.getTag();
			switch (v.getId()) {
			case R.id.accept_button:
				Toast.makeText(getActivity(), "accept : " + item.getItemName(),
						Toast.LENGTH_SHORT).show();
				item.setStatus(AcceptStatus.ACCEPTED);
				mListAdapter.notifyDataSetChanged();
				break;

			case R.id.reject_button:
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.container,
								RejectionMenuFragment.newInstance(item))
						.addToBackStack(null).commit();
				break;

			case R.id.edit_button:
				Toast.makeText(getActivity(), "edit : " + item.getItemName(),
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	private class PurchaseOrderAdapter extends ArrayAdapter<PurchaseOrder> {
		private LayoutInflater mInflator;

		public PurchaseOrderAdapter(Context context, int resource,
				List<PurchaseOrder> itemList) {
			super(context, resource, itemList);
			mInflator = LayoutInflater.from(context);
		}

		private class OrderViewHolder {
			TextView nameTextview;
			TextView detailsTextView;
			TextView contactTextView;
			Button acceptButton;
			Button rejectButton;
			ImageButton editButton;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			OrderViewHolder holder;
			if (convertView == null) {
				holder = new OrderViewHolder();
				convertView = mInflator.inflate(R.layout.po_list_item, null);

				holder.nameTextview = (TextView) convertView
						.findViewById(R.id.item_name_textview);
				holder.detailsTextView = (TextView) convertView
						.findViewById(R.id.item_details_textview);
				holder.contactTextView = (TextView) convertView
						.findViewById(R.id.contact_textview);

				holder.acceptButton = (Button) convertView
						.findViewById(R.id.accept_button);
				holder.rejectButton = (Button) convertView
						.findViewById(R.id.reject_button);
				holder.editButton = (ImageButton) convertView
						.findViewById(R.id.edit_button);
				convertView.setTag(holder);
			}

			holder = (OrderViewHolder) convertView.getTag();

			PurchaseOrder item = getItem(position);

			holder.nameTextview.setText(item.getItemName());
			holder.detailsTextView.setText(item.getItemDetails());
			holder.contactTextView.setText(item.getContact());

			holder.acceptButton.setTag(item);
			holder.rejectButton.setTag(item);
			holder.editButton.setTag(item);

			holder.acceptButton.setOnClickListener(mListButtonClickLintener);
			holder.rejectButton.setOnClickListener(mListButtonClickLintener);
			holder.editButton.setOnClickListener(mListButtonClickLintener);

			switch (item.getStatus()) {
			case ACCEPTED:
				holder.acceptButton.setText(getString(R.string.accepted));
				holder.rejectButton.setVisibility(View.GONE);
				holder.editButton.setVisibility(View.VISIBLE);
				break;
			case NONE:
				holder.rejectButton.setVisibility(View.VISIBLE);
				holder.acceptButton.setText(getString(R.string.accept));
				holder.editButton.setVisibility(View.GONE);
				break;
			case REJECTED:
			default:
				break;
			}

			return convertView;
		}
	}
}
