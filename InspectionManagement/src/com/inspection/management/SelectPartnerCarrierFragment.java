package com.inspection.management;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inspection.management.adapter.AlphabetListAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.inspection.management.db.InspectionMetadata.CarrierTable;
import com.inspection.management.db.InspectionMetadata.PartnerTable;
import com.inspection.management.scanner.BarCodeScannerActivity;
import com.inspection.management.util.AppUtil;
import com.inspection.management.view.MultiFiniteProgressView;
import com.inspection.management.view.OverLineTextView;
import com.ui.components.ManageActionBarTitle;

public class SelectPartnerCarrierFragment extends Fragment implements
		LoaderCallbacks<Cursor>, OnItemClickListener, ManageActionBarTitle {

	/** TAG. */
	private static final String TAG = SelectPartnerCarrierFragment.class
			.getSimpleName();

	private ListView mListView;
	private ImageView mScanCodeButton;

	private AlphabetListAdapter mAlphabetListAdapter;

	private EditText mSearchEditText;

	private LinearLayout mSideIndexLayout;

	private int SEARCH_PARTNER_LOADER_ID = 1000;
	private int SEARCH_CARRIER_LOADER_ID = 1001;

	private int mLoaderId;

	private String mSearchText = "";

	public static int SCREEN_SELECT_PARTNER = 1;
	public static int SCREEN_SELECT_CARRIER = 2;

	private int mCurrentScreen = SCREEN_SELECT_PARTNER;

	private int sideIndexHeight;
	private static float sideIndexX;
	private static float sideIndexY;

	private int indexListSize;

	public SelectPartnerCarrierFragment() {

	}

	public SelectPartnerCarrierFragment(int screen) {
		mCurrentScreen = screen;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		updateActionBarTitle();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.alphabetical_list_view, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mListView = (ListView) view.findViewById(R.id.list_view);
		mSearchEditText = (EditText) view.findViewById(R.id.search_text);
		mSearchEditText.addTextChangedListener(mTextWatcher);
		mSideIndexLayout = (LinearLayout) view.findViewById(R.id.side_index);
		mScanCodeButton = (ImageView) view.findViewById(R.id.code_scan_button);
		mScanCodeButton.setOnClickListener(mClickListener);

		doSearchTextSettings();

		Cursor cursor = null;
		String columnName;
		if (SCREEN_SELECT_PARTNER == mCurrentScreen) {
			cursor = getActivity().getContentResolver().query(
					PartnerTable.CONTENT_URI, PartnerTableQuery.PROJECTION,
					null, null, PartnerTableQuery.SORT_ORDER);
			columnName = PartnerTable.PARTNER_NAME;
		} else {
			cursor = getActivity().getContentResolver().query(
					CarrierTable.CONTENT_URI, CarrierTableQuery.PROJECTION,
					null, null, CarrierTableQuery.SORT_ORDER);
			columnName = CarrierTable.CARRIER_NAME;
		}

		mAlphabetListAdapter = new AlphabetListAdapter(getActivity(), cursor,
				columnName);
		mListView.setAdapter(mAlphabetListAdapter);
		mListView.setFastScrollEnabled(true);
		updateSideAlphabetIndex();

		// don't ever forget to do this, either here or in your ListView layout
		// getListView().setFastScrollEnabled(true);

		// mPartnerSearchAdapter = new PartnerSearchAdapter(getActivity(),
		// cursor,
		// mCurrentScreen);
		// mListView.setAdapter(mPartnerSearchAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(mScrollListener);
		Log.d("test", "onViewCreated");
	}

	
	private TextView tmpTV;
	
	private void updateSideAlphabetIndex() {

		String[] sections = (String[]) mAlphabetListAdapter.getSections();
		mSideIndexLayout.removeAllViews();
		for (String section : sections) {
			tmpTV = new OverLineTextView(getActivity());
			tmpTV.setText(section);
			tmpTV.setGravity(Gravity.CENTER);
			tmpTV.setTextSize(12);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			tmpTV.setLayoutParams(params);
			mSideIndexLayout.addView(tmpTV);
		}
		sideIndexHeight = mSideIndexLayout.getHeight();

		mSideIndexLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// now you know coordinates of touch
				sideIndexX = event.getX();
				float sideIndexY = event.getY();

				// and can display a proper item it country list
				displayListItem(sideIndexY);

				return true;
			}
		});
	}

	public void displayListItem(float sideIndexY) {
		
		indexListSize = tmpTV.getMeasuredHeight();
		int position = (int) (sideIndexY/indexListSize);

		// compute the item index for given event position belongs to
		int itemPosition = mAlphabetListAdapter.getPositionForSection(position);

		if (itemPosition < mAlphabetListAdapter.getCount()) {
			// Object[] indexItem = sections.get(itemPosition);
			// int subitemPosition = sections.get(indexItem[0]);

			// ListView listView = (ListView) findViewById(android.R.id.list);
			mListView.setSelection(itemPosition);
		}
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

	private void doSearchTextSettings() {
		if (SCREEN_SELECT_CARRIER == mCurrentScreen) {
			mSearchEditText.setHint(R.string.hint_search_carrier);
		}
	}

	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mSearchText = s.toString();
			getActivity().getSupportLoaderManager().restartLoader(mLoaderId,
					null, SelectPartnerCarrierFragment.this);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d("test", "onAttach");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("test", "onActivityCreated");
		getActivity().getSupportLoaderManager().initLoader(mLoaderId, null,
				this);
		// new DummyAskTask().execute();

	}

	private static class PartnerInfoViewHolder {
		public TextView separator;
		public TextView titleView;
		public CharArrayBuffer titleBuffer = new CharArrayBuffer(128);
		public MultiFiniteProgressView progressView;
	}

//	private static class PartnerSearchAdapter extends CursorAdapter {
//
//		/**
//		 * State of ListView item that has never been determined.
//		 */
//		private static final int STATE_UNKNOWN = 0;
//
//		/**
//		 * State of a ListView item that is sectioned. A sectioned item must
//		 * display the separator.
//		 */
//		private static final int STATE_SECTIONED_CELL = 1;
//
//		/**
//		 * State of a ListView item that is not sectioned and therefore does not
//		 * display the separator.
//		 */
//		private static final int STATE_REGULAR_CELL = 2;
//
//		private final CharArrayBuffer mBuffer = new CharArrayBuffer(128);
//		private int[] mCellStates;
//
//		private int mCurrentScreen;
//
//		int mHighlightColor = Color.CYAN;
//
//		public PartnerSearchAdapter(Context context, Cursor cursor,
//				int currentScreen) {
//			super(context, cursor, false);
//			mCellStates = cursor == null ? null : new int[cursor.getCount()];
//			mCurrentScreen = currentScreen;
//			mHighlightColor = context.getResources().getColor(
//					R.color.search_partner_highlight_color);
//		}
//
//		@Override
//		public void changeCursor(Cursor cursor) {
//			super.changeCursor(cursor);
//			mCellStates = cursor == null ? null : new int[cursor.getCount()];
//		}
//
//		@Override
//		public void bindView(View view, Context context, Cursor cursor) {
//
//			final PartnerInfoViewHolder holder = (PartnerInfoViewHolder) view
//					.getTag();
//
//			/*
//			 * Separator
//			 */
//			boolean needSeparator = false;
//
//			final int position = cursor.getPosition();
//			cursor.copyStringToBuffer(PartnerTableQuery.TITLE,
//					holder.titleBuffer);
//
//			switch (mCellStates[position]) {
//			case STATE_SECTIONED_CELL:
//				needSeparator = true;
//				break;
//
//			case STATE_REGULAR_CELL:
//				needSeparator = false;
//				break;
//
//			case STATE_UNKNOWN:
//			default:
//				// A separator is needed if it's the first itemview of the
//				// ListView or if the group of the current cell is different
//				// from the previous itemview.
//				if (position == 0) {
//					needSeparator = true;
//				} else {
//					cursor.moveToPosition(position - 1);
//
//					if (SCREEN_SELECT_PARTNER == mCurrentScreen) {
//						cursor.copyStringToBuffer(PartnerTableQuery.TITLE,
//								mBuffer);
//					} else {
//						cursor.copyStringToBuffer(CarrierTableQuery.TITLE,
//								mBuffer);
//					}
//					if (mBuffer.sizeCopied > 0
//							&& holder.titleBuffer.sizeCopied > 0
//							&& mBuffer.data[0] != holder.titleBuffer.data[0]) {
//						needSeparator = true;
//					}
//
//					cursor.moveToPosition(position);
//				}
//
//				// Cache the result
//				mCellStates[position] = needSeparator ? STATE_SECTIONED_CELL
//						: STATE_REGULAR_CELL;
//				break;
//			}
//
//			if (needSeparator) {
//				holder.separator.setText(holder.titleBuffer.data, 0, 1);
//				holder.separator.setVisibility(View.VISIBLE);
//			} else {
//				holder.separator.setVisibility(View.GONE);
//			}
//
//			/*
//			 * Title
//			 */
//			holder.titleView.setText(holder.titleBuffer.data, 0,
//					holder.titleBuffer.sizeCopied);
//
//			int accepted = 0;
//			int rejected = 0;
//			int tentative = 0;
//
//			if (SCREEN_SELECT_PARTNER == mCurrentScreen) {
//				accepted = cursor.getInt(PartnerTableQuery.ACCEPTED);
//				rejected = cursor.getInt(PartnerTableQuery.REJECTED);
//				tentative = cursor.getInt(PartnerTableQuery.TENTATIVE);
//			} else {
//				accepted = cursor.getInt(CarrierTableQuery.ACCEPTED);
//				rejected = cursor.getInt(CarrierTableQuery.REJECTED);
//				tentative = cursor.getInt(CarrierTableQuery.TENTATIVE);
//			}
//
//			if (rejected > 60) {
//				view.setBackgroundColor(mHighlightColor);
//				holder.separator.setBackgroundColor(Color.WHITE);
//			} else {
//				view.setBackgroundColor(Color.WHITE);
//			}
//
//			holder.progressView.setIndicatorValues(accepted, rejected,
//					tentative);
//		}
//
//		@Override
//		public View newView(Context context, Cursor cursor, ViewGroup parent) {
//
//			View v = LayoutInflater.from(context).inflate(R.layout.list_item,
//					parent, false);
//
//			// The following code allows us to keep a reference on the child
//			// views of the item. It prevents us from calling findViewById at
//			// each getView/bindView and boosts the rendering code.
//			PartnerInfoViewHolder holder = new PartnerInfoViewHolder();
//			holder.separator = (TextView) v.findViewById(R.id.separator);
//			holder.titleView = (TextView) v.findViewById(R.id.title);
//			holder.progressView = (MultiFiniteProgressView) v
//					.findViewById(R.id.progress_view);
//
//			v.setTag(holder);
//
//			return v;
//		}
//
//		public View mItemContainer;
//	}

	// private static class PartnerSearchAdapter extends CursorAdapter {
	//
	// /**
	// * State of ListView item that has never been determined.
	// */
	// private static final int STATE_UNKNOWN = 0;
	//
	// /**
	// * State of a ListView item that is sectioned. A sectioned item must
	// * display the separator.
	// */
	// private static final int STATE_SECTIONED_CELL = 1;
	//
	// /**
	// * State of a ListView item that is not sectioned and therefore does not
	// * display the separator.
	// */
	// private static final int STATE_REGULAR_CELL = 2;
	//
	// private final CharArrayBuffer mBuffer = new CharArrayBuffer(128);
	// private int[] mCellStates;
	//
	// private int mCurrentScreen;
	//
	// int mHighlightColor = Color.CYAN;
	//
	// private AlphabetIndexer mAlphabetIndexer;
	//
	// public PartnerSearchAdapter(Context context, Cursor cursor,
	// int currentScreen) {
	// super(context, cursor, false);
	// mCellStates = cursor == null ? null : new int[cursor.getCount()];
	// mCurrentScreen = currentScreen;
	// mHighlightColor = context.getResources().getColor(
	// R.color.search_partner_highlight_color);
	// }
	//
	// @Override
	// public void changeCursor(Cursor cursor) {
	// super.changeCursor(cursor);
	// mCellStates = cursor == null ? null : new int[cursor.getCount()];
	// }
	//
	// @Override
	// public void bindView(View view, Context context, Cursor cursor) {
	//
	// final PartnerInfoViewHolder holder = (PartnerInfoViewHolder) view
	// .getTag();
	//
	// /*
	// * Separator
	// */
	// boolean needSeparator = false;
	//
	// final int position = cursor.getPosition();
	// cursor.copyStringToBuffer(PartnerTableQuery.TITLE,
	// holder.titleBuffer);
	//
	// switch (mCellStates[position]) {
	// case STATE_SECTIONED_CELL:
	// needSeparator = true;
	// break;
	//
	// case STATE_REGULAR_CELL:
	// needSeparator = false;
	// break;
	//
	// case STATE_UNKNOWN:
	// default:
	// // A separator is needed if it's the first itemview of the
	// // ListView or if the group of the current cell is different
	// // from the previous itemview.
	// if (position == 0) {
	// needSeparator = true;
	// } else {
	// cursor.moveToPosition(position - 1);
	//
	// if (SCREEN_SELECT_PARTNER == mCurrentScreen) {
	// cursor.copyStringToBuffer(PartnerTableQuery.TITLE,
	// mBuffer);
	// } else {
	// cursor.copyStringToBuffer(CarrierTableQuery.TITLE,
	// mBuffer);
	// }
	// if (mBuffer.sizeCopied > 0
	// && holder.titleBuffer.sizeCopied > 0
	// && mBuffer.data[0] != holder.titleBuffer.data[0]) {
	// needSeparator = true;
	// }
	//
	// cursor.moveToPosition(position);
	// }
	//
	// // Cache the result
	// mCellStates[position] = needSeparator ? STATE_SECTIONED_CELL
	// : STATE_REGULAR_CELL;
	// break;
	// }
	//
	// if (needSeparator) {
	// holder.separator.setText(holder.titleBuffer.data, 0, 1);
	// holder.separator.setVisibility(View.VISIBLE);
	// } else {
	// holder.separator.setVisibility(View.GONE);
	// }
	//
	// /*
	// * Title
	// */
	// holder.titleView.setText(holder.titleBuffer.data, 0,
	// holder.titleBuffer.sizeCopied);
	//
	// int accepted = 0;
	// int rejected = 0;
	// int tentative = 0;
	//
	// if (SCREEN_SELECT_PARTNER == mCurrentScreen) {
	// accepted = cursor.getInt(PartnerTableQuery.ACCEPTED);
	// rejected = cursor.getInt(PartnerTableQuery.REJECTED);
	// tentative = cursor.getInt(PartnerTableQuery.TENTATIVE);
	// } else {
	// accepted = cursor.getInt(CarrierTableQuery.ACCEPTED);
	// rejected = cursor.getInt(CarrierTableQuery.REJECTED);
	// tentative = cursor.getInt(CarrierTableQuery.TENTATIVE);
	// }
	//
	// if (rejected > 60) {
	// view.setBackgroundColor(mHighlightColor);
	// holder.separator.setBackgroundColor(Color.WHITE);
	// } else {
	// view.setBackgroundColor(Color.WHITE);
	// }
	//
	// holder.progressView.setIndicatorValues(accepted, rejected,
	// tentative);
	// }
	//
	// @Override
	// public View newView(Context context, Cursor cursor, ViewGroup parent) {
	//
	// View v = LayoutInflater.from(context).inflate(R.layout.list_item,
	// parent, false);
	//
	// // The following code allows us to keep a reference on the child
	// // views of the item. It prevents us from calling findViewById at
	// // each getView/bindView and boosts the rendering code.
	// PartnerInfoViewHolder holder = new PartnerInfoViewHolder();
	// holder.separator = (TextView) v.findViewById(R.id.separator);
	// holder.titleView = (TextView) v.findViewById(R.id.title);
	// holder.progressView = (MultiFiniteProgressView) v
	// .findViewById(R.id.progress_view);
	// holder.mItemContainer = v.findViewById(R.id.item_container);
	//
	// v.setTag(holder);
	//
	// return v;
	// }
	//
	// }

	/**
	 * Keep query data in one place
	 */
	private interface PartnerTableQuery {
		String[] PROJECTION = { PartnerTable._ID, PartnerTable.PARTNER_NAME,
				PartnerTable.ACCEPTED, PartnerTable.REJECTED,
				PartnerTable.TENTATIVE };

		int TITLE = 1;
		int ACCEPTED = 2;
		int REJECTED = 3;
		int TENTATIVE = 4;

		String SORT_ORDER = PartnerTable.PARTNER_NAME + " ASC";
	}

	private interface CarrierTableQuery {
		String[] PROJECTION = { CarrierTable._ID, CarrierTable.CARRIER_NAME,
				CarrierTable.ACCEPTED, CarrierTable.REJECTED,
				CarrierTable.TENTATIVE };

		int TITLE = 1;
		int ACCEPTED = 2;
		int REJECTED = 3;
		int TENTATIVE = 4;

		String SORT_ORDER = CarrierTable.CARRIER_NAME + " ASC";
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

		Log.d("test", "onCreateLoader");

		String selection = null;
		String[] selectionArgs = null;
		String[] projection = null;
		String sortOrder = null;
		Uri uri = null;

		if (SCREEN_SELECT_PARTNER == mCurrentScreen) {
			selection = PartnerTable.PARTNER_NAME + " LIKE ?";
			selectionArgs = new String[] { "%" + mSearchText + "%" };
			projection = PartnerTableQuery.PROJECTION;
			sortOrder = PartnerTableQuery.SORT_ORDER;
			uri = PartnerTable.CONTENT_URI;
		} else {
			selection = CarrierTable.CARRIER_NAME + " LIKE ?";
			selectionArgs = new String[] { "%" + mSearchText + "%" };
			projection = CarrierTableQuery.PROJECTION;
			sortOrder = CarrierTableQuery.SORT_ORDER;
			uri = CarrierTable.CONTENT_URI;
		}

		final CursorLoader cursorLoader = new CursorLoader(getActivity(), uri,
				projection, selection, selectionArgs, sortOrder);

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.d("test", "onLoadFinished");
		mAlphabetListAdapter.changeCursor(cursor);
		updateSideAlphabetIndex();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d("test", "onLoaderReset");
		mAlphabetListAdapter.changeCursor(null);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(TAG, "onItemClick : " + position);
		AppUtil.hideKeyboard(getActivity(), mSearchEditText);
		if (SCREEN_SELECT_PARTNER == mCurrentScreen) {
			getActivity().getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PurchaseOrderFragment())
			.addToBackStack(null).commit();
		} else {
			getActivity().getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new POAcceptFragment())
			.addToBackStack(null).commit();

		}
	}

	@Override
	public void onDestroy() {
		try {
			getActivity().getSupportLoaderManager().destroyLoader(mLoaderId);
		} catch (Exception e) {
			// do nothing
		}
		super.onDestroy();
	}

	@Override
	public void updateActionBarTitle() {
		getActivity().getActionBar().show();
		if (SCREEN_SELECT_PARTNER == mCurrentScreen) {
			mLoaderId = SEARCH_PARTNER_LOADER_ID;
			getActivity().getActionBar()
					.setTitle(R.string.select_partner_title);
		} else {
			mLoaderId = SEARCH_CARRIER_LOADER_ID;
			getActivity().getActionBar()
					.setTitle(R.string.select_carrier_title);
		}
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.code_scan_button:
				// start Scanner activity
				scanCode();
				break;

			default:
				break;
			}
		}
	};

	private void scanCode() {
		Intent scannerIntent = new Intent(getActivity(),
				BarCodeScannerActivity.class);
		setTargetFragment(SelectPartnerCarrierFragment.this, 1111);
		startActivityForResult(scannerIntent, 1111);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// IntentResult result =
		// IntentIntegrator.parseActivityResult(requestCode,
		// resultCode, intent);

		String contents = null;
		String poNumber = null;
		if (intent != null) {
			contents = intent
					.getStringExtra(BarCodeScannerActivity.EXTRA_SCAN_DATA);
			poNumber = intent
					.getStringExtra(BarCodeScannerActivity.EXTRA_PO_NUMBER);
		}

		handleResult(poNumber);

		if (contents != null) {
			showDialog("Scanned", contents);
		} else {
			showDialog(getString(R.string.scan_code_error_title), "Error");
		}
	}

	private void handleResult(final String orderNumber) {
		// TODO:
		if (!TextUtils.isEmpty(orderNumber)) {
			// Close this activity and go to next screen
			getActivity()
					.getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.container,
							PODetailFragment.newInstance(orderNumber))
					.addToBackStack(null).commit();
		} else {
			showDialog(getString(R.string.scan_code_error_title),
					getString(R.string.scan_code_not_found));
			// show error
		}
	}

	private void showDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(getString(R.string.scan_code),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						scanCode();
					}
				});

		builder.show();
	}
}
