package com.inspection.management;

import java.util.List;

import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.inspection.management.db.InspectionMetadata.PurchaseTable;
import com.inspection.management.util.AppUtil;
import com.ui.components.ManageActionBarTitle;

//TODO: Cursor used is of PartenerTable, was only for testing, Need to implement when PurchaseTable is ready
public class PurchaseOrderFragment extends Fragment implements
		LoaderCallbacks<Cursor>, OnItemClickListener, OnClickListener,
		ManageActionBarTitle {

	protected static final int SEARCH_PURCHSE_ORDER_LOADER_ID = 500;
	private ListView mOrderListView;
	private String mSearchText = "";
	private EditText mSearchEditText;
	private Button mCancelButton;

	private PurchaseOrderCursorAdapter mPurchaseOrderCursorAdapter;

	public PurchaseOrderFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		updateActionBarTitle();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_purchese_order, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mOrderListView = (ListView) view.findViewById(R.id.list_view);
		mSearchEditText = (EditText) view.findViewById(R.id.search_text);
		mCancelButton = (Button) view.findViewById(R.id.cancel_button);

		mCancelButton.setOnClickListener(this);
		mSearchEditText.addTextChangedListener(mTextWatcher);
		mOrderListView.setOnItemClickListener(this);
		mOrderListView.setOnScrollListener(mScrollListener);
	}

	private OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			AppUtil.hideKeyboard(getActivity(), mSearchEditText);
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		final Cursor cursor = getActivity().getContentResolver().query(
				PurchaseTable.CONTENT_URI, PurchaseOrderTableQuery.PROJECTION,
				null, null, PurchaseOrderTableQuery.SORT_ORDER);

		mPurchaseOrderCursorAdapter = new PurchaseOrderCursorAdapter(
				getActivity(), cursor);
		mOrderListView.setAdapter(mPurchaseOrderCursorAdapter);

		getActivity().getSupportLoaderManager().initLoader(
				SEARCH_PURCHSE_ORDER_LOADER_ID, null, this);
		// // Dummy list
		// List<String> list = new ArrayList<String>();
		// list.add("");
		// list.add("");
		// list.add("");
		// list.add("");
		// mOrderListView.setAdapter(new DummyListAdapter(getActivity(),
		// android.R.layout.simple_list_item_1, list));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_button:
			removeSelf();
			break;

		default:
			break;
		}
	}

	private void removeSelf() {
		getActivity().getSupportFragmentManager().beginTransaction()
				.remove(this).commit();
	}

	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mSearchText = s.toString();
			getActivity().getSupportLoaderManager().restartLoader(
					SEARCH_PURCHSE_ORDER_LOADER_ID, null,
					PurchaseOrderFragment.this);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	private class DummyListAdapter extends ArrayAdapter<String> {

		public DummyListAdapter(Context context, int resource,
				List<String> items) {
			super(context, resource, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.purchase_order_list_item, null);
			}
			return convertView;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

		String selection = null;
		String[] selectionArgs = null;

		selection = PurchaseTable.ORDER_NO + " LIKE ?";
		selectionArgs = new String[] { "%" + mSearchText + "%" };

		final CursorLoader cursorLoader = new CursorLoader(getActivity(),
				PurchaseTable.CONTENT_URI, PurchaseOrderTableQuery.PROJECTION,
				selection, selectionArgs, PurchaseOrderTableQuery.SORT_ORDER);

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mPurchaseOrderCursorAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mPurchaseOrderCursorAdapter.changeCursor(null);
	}

	// TODO: Create projection for purchase order table
	private interface PurchaseOrderTableQuery {
		final String[] PROJECTION = { PurchaseTable._ID,
				PurchaseTable.ORDER_NO, PurchaseTable.ETA };

		int ORDER_NO = 1;
		int ETA = 2;

		final String SORT_ORDER = PurchaseTable.ORDER_NO + " ASC";
	}

	/**
	 * Purchase order number list adapter
	 * 
	 */
	private class PurchaseOrderCursorAdapter extends CursorAdapter {
		private LayoutInflater mInflater;

		public PurchaseOrderCursorAdapter(Context context, Cursor cursor) {
			super(context, cursor, false);
			mInflater = getActivity().getLayoutInflater();
		}

		private class PurchaseOrderViewHolder {
			TextView mOrderNumberTextView;
			public CharArrayBuffer orderNoBuffer = new CharArrayBuffer(128);
			TextView mOrderTimeTextView;
			public CharArrayBuffer etaBuffer = new CharArrayBuffer(128);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			PurchaseOrderViewHolder holder = (PurchaseOrderViewHolder) view
					.getTag();
			cursor.copyStringToBuffer(PurchaseOrderTableQuery.ORDER_NO,
					holder.orderNoBuffer);
			cursor.copyStringToBuffer(PurchaseOrderTableQuery.ETA,
					holder.etaBuffer);

			holder.mOrderNumberTextView.setText(holder.orderNoBuffer.data, 0,
					holder.orderNoBuffer.sizeCopied);
			holder.mOrderTimeTextView.setText(holder.etaBuffer.data, 0,
					holder.etaBuffer.sizeCopied);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View newView = mInflater.inflate(R.layout.purchase_order_list_item,
					null);
			PurchaseOrderViewHolder holder = new PurchaseOrderViewHolder();
			holder.mOrderNumberTextView = (TextView) newView
					.findViewById(R.id.purchaseOrderTextView);
			holder.mOrderTimeTextView = (TextView) newView
					.findViewById(R.id.purchaseOrderTimeTextView);
			newView.setTag(holder);
			return newView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO: Get the purchase order number pass to PODetailFragment
		AppUtil.hideKeyboard(getActivity(), mSearchEditText);
		getActivity()
				.getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.container,
						PODetailFragment.newInstance("PO #" + position))
				.addToBackStack(null).commit();
	}

	@Override
	public void updateActionBarTitle() {
		getActivity().getActionBar().setTitle("A & Z LLC");
	}

	@Override
	public void onDestroy() {
		try {
			getActivity().getSupportLoaderManager().destroyLoader(
					SEARCH_PURCHSE_ORDER_LOADER_ID);
		} catch (Exception e) {
			// do nothing
		}
		super.onDestroy();
	}
}
