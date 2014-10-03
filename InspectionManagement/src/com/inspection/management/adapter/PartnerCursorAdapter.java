package com.inspection.management.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ui.components.AlphabeticalCursorAdapter;

public class PartnerCursorAdapter extends AlphabeticalCursorAdapter {

	private LayoutInflater mInflator;

	public PartnerCursorAdapter(Activity context, Cursor cursor,
			String columnName) {
		super(context, cursor, columnName);
		mInflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// mPosition = position;
		// return super.getView(position -
		// sectionToOffset.get(getSectionForPosition(position)) - 1,
		// convertView, parent);

		final int type = getItemViewType(position);

		if (convertView == null) {
			convertView = new TextView(mActivity);
		}

		if (type == TYPE_HEADER) {
			TextView textView = (TextView) convertView;
			textView.setText(getHeaderForPosition(position));
		} else {
			getCursor().moveToPosition(
					position
							- sectionToOffset
									.get(getSectionForPosition(position)) - 1);
			TextView textView = (TextView) convertView;
			Cursor cursorUpdated = (Cursor) getItem(position);
			textView.setText(cursorUpdated.getString(cursorUpdated
					.getColumnIndex(mColumnName)));
		}

		return convertView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// final int type = getItemViewType(mPosition);
		// if (type == TYPE_HEADER) {
		// TextView textView = (TextView) view;
		// textView.setText(getHeaderForPosition(mPosition));
		// } else {
		// TextView textView = (TextView) view;
		// Cursor cursorUpdated = (Cursor) getItem(mPosition);
		// textView.setText(cursorUpdated.getString(cursorUpdated
		// .getColumnIndex(mColumnName)));
		// }
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// final int type = getItemViewType(mPosition);
		// if (type == TYPE_HEADER) {
		// // return mInflator.inflate(R.layout.header,parent, false);
		// return new TextView(context);
		// }
		// // return mInflator.inflate(R.layout.partner_item, parent, false);
		// return new TextView(context);
		return null;
	}
}
