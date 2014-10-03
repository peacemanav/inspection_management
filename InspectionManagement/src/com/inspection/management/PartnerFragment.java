package com.inspection.management;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;

import com.inspection.management.adapter.PartnerCursorAdapter;
import com.inspection.management.db.InspectionMetadata.PartnerTable;
import com.ui.components.AlphabeticalListFragment;

public class PartnerFragment extends AlphabeticalListFragment {

	private Cursor mCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		insertDummyData();
		mCursor = getActivity().getContentResolver().query(
				PartnerTable.CONTENT_URI, null, null, null,
				PartnerTable.PARTNER_NAME + " ASC");
	}

	@Override
	public CursorAdapter getAdapter() {
		return new PartnerCursorAdapter(getActivity(), mCursor,
				PartnerTable.PARTNER_NAME);
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
}
