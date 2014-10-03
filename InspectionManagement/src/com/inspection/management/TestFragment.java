package com.inspection.management;

import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.inspection.management.db.InspectionMetadata.PartnerTable;

public class TestFragment extends Fragment {

	private ListView mListView;
	private PartnerSearchAdapter mPartnerSearchAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		insertDummyData();
	}
	
	private void insertDummyData() {

		// clean old data.
		getActivity().getContentResolver().delete(PartnerTable.CONTENT_URI,
				null, null);

		final ContentValues[] values = new ContentValues[52];
		int asciiCode = 65;
		for (int i = 0; i < values.length; i++) {
			final ContentValues value = new ContentValues();
			value.put(PartnerTable.PARTNER_NAME, (char) asciiCode
					+ " some random text");
			values[i] = value;
			asciiCode++;
			if (asciiCode == 91) {
				asciiCode = 65;
			}
		}
		getActivity().getContentResolver().bulkInsert(PartnerTable.CONTENT_URI,
				values);
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

		final Cursor cursor = getActivity().getContentResolver().query(
				PartnerTable.CONTENT_URI, PartnerTableQuery.PROJECTION, null,
				null, PartnerTableQuery.SORT_ORDER);

		mPartnerSearchAdapter = new PartnerSearchAdapter(getActivity(), cursor);
		mListView.setAdapter(mPartnerSearchAdapter);
	}

	private static class PartnerInfoViewHolder {
		public TextView separator;
		public TextView titleView;
		public CharArrayBuffer titleBuffer = new CharArrayBuffer(128);
	}

	private static class PartnerSearchAdapter extends CursorAdapter {

		/**
		 * State of ListView item that has never been determined.
		 */
		private static final int STATE_UNKNOWN = 0;

		/**
		 * State of a ListView item that is sectioned. A sectioned item must
		 * display the separator.
		 */
		private static final int STATE_SECTIONED_CELL = 1;

		/**
		 * State of a ListView item that is not sectioned and therefore does not
		 * display the separator.
		 */
		private static final int STATE_REGULAR_CELL = 2;

		private final CharArrayBuffer mBuffer = new CharArrayBuffer(128);
		private int[] mCellStates;

		public PartnerSearchAdapter(Context context, Cursor cursor) {
			super(context, cursor, false);
			mCellStates = cursor == null ? null : new int[cursor.getCount()];
		}

		@Override
		public void changeCursor(Cursor cursor) {
			super.changeCursor(cursor);
			mCellStates = cursor == null ? null : new int[cursor.getCount()];
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {

			final PartnerInfoViewHolder holder = (PartnerInfoViewHolder) view
					.getTag();

			/*
			 * Separator
			 */
			boolean needSeparator = false;

			final int position = cursor.getPosition();
			cursor.copyStringToBuffer(PartnerTableQuery.TITLE,
					holder.titleBuffer);

			switch (mCellStates[position]) {
			case STATE_SECTIONED_CELL:
				needSeparator = true;
				break;

			case STATE_REGULAR_CELL:
				needSeparator = false;
				break;

			case STATE_UNKNOWN:
			default:
				// A separator is needed if it's the first itemview of the
				// ListView or if the group of the current cell is different
				// from the previous itemview.
				if (position == 0) {
					needSeparator = true;
				} else {
					cursor.moveToPosition(position - 1);

					cursor.copyStringToBuffer(PartnerTableQuery.TITLE, mBuffer);
					if (mBuffer.sizeCopied > 0
							&& holder.titleBuffer.sizeCopied > 0
							&& mBuffer.data[0] != holder.titleBuffer.data[0]) {
						needSeparator = true;
					}

					cursor.moveToPosition(position);
				}

				// Cache the result
				mCellStates[position] = needSeparator ? STATE_SECTIONED_CELL
						: STATE_REGULAR_CELL;
				break;
			}

			if (needSeparator) {
				holder.separator.setText(holder.titleBuffer.data, 0, 1);
				holder.separator.setVisibility(View.VISIBLE);
			} else {
				holder.separator.setVisibility(View.GONE);
			}

			/*
			 * Title
			 */
			holder.titleView.setText(holder.titleBuffer.data, 0,
					holder.titleBuffer.sizeCopied);

			// /*
			// * Subtitle
			// */
			// holder.subtitleBuffer.setLength(0);
			// final String album = cursor.getString(PartnerTableQuery.ALBUM);
			// if (!TextUtils.isEmpty(album)) {
			// holder.subtitleBuffer.append(album);
			// final String artist = cursor.getString(PartnerTableQuery.ARTIST);
			// if (!TextUtils.isEmpty(artist)) {
			// holder.subtitleBuffer.append(" - ");
			// holder.subtitleBuffer.append(artist);
			// }
			// }
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {

			View v = LayoutInflater.from(context).inflate(R.layout.list_item,
					parent, false);

			// The following code allows us to keep a reference on the child
			// views of the item. It prevents us from calling findViewById at
			// each getView/bindView and boosts the rendering code.
			PartnerInfoViewHolder holder = new PartnerInfoViewHolder();
			holder.separator = (TextView) v.findViewById(R.id.separator);
			holder.titleView = (TextView) v.findViewById(R.id.title);

			v.setTag(holder);

			return v;
		}

	}

	/**
	 * Keep query data in one place
	 * 
	 * @author Cyril Mottier
	 */
	private interface PartnerTableQuery {
		String[] PROJECTION = { PartnerTable._ID, PartnerTable.PARTNER_NAME,
				PartnerTable.ACCEPTED, PartnerTable.REJECTED,
				PartnerTable.TENTATIVE };

		int TITLE = 1;
		int ALBUM = 2;

		String SORT_ORDER = PartnerTable.PARTNER_NAME + " ASC";
	}
}
