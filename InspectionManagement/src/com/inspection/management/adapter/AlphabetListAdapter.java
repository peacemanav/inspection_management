package com.inspection.management.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.inspection.management.R;
import com.inspection.management.view.MultiFiniteProgressView;

public class AlphabetListAdapter extends CursorAdapter implements
		SectionIndexer {

	private AlphabetIndexer mIndexer;

	private String mColumnName;

	// this array is for fast lookup later and will contain the just the
	// alphabet sections that actually appear in the data set
	private int[] usedSectionNumbers;

	// map from alphabet section to the index it ought
	// to appear in
	private Map<Integer, Integer> mSectionToPosition;

	// map from alphabet section to the number of other sections
	// that appear before it
	private Map<Integer, Integer> mSectionToOffset;

	private static final int TYPE_HEADER = 1;
	private static final int TYPE_NORMAL = 0;

	private static final int TYPE_COUNT = 2;

	private LayoutInflater mLayoutInflator;

	public AlphabetListAdapter(Context context, Cursor c, String columnName) {
		super(context, c, false);

		mColumnName = columnName;
		mLayoutInflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initIndexer(c);

//		mHighlightColor = context.getResources().getColor(
//				R.color.search_partner_highlight_color);
	}

	private void initIndexer(Cursor cursor) {
		final int columnIndex = cursor.getColumnIndexOrThrow(mColumnName);
		mIndexer = new AlphabetIndexer(cursor, columnIndex,
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ");

		// use a TreeMap because we are going to iterate over its keys in sorted
		// order
		mSectionToPosition = new TreeMap<Integer, Integer>();
		mSectionToOffset = new HashMap<Integer, Integer>();

		final int count = super.getCount();

		int i;
		// temporarily have a map alphabet section to first index it appears
		// (this map is going to be doing something else later)
		for (i = count - 1; i >= 0; i--) {
			mSectionToPosition.put(mIndexer.getSectionForPosition(i), i);
		}

		i = 0;
		usedSectionNumbers = new int[mSectionToPosition.keySet().size()];

		// note that for each section that appears before a position, we must
		// offset our
		// indices by 1, to make room for an alphabetical header in our list
		for (Integer section : mSectionToPosition.keySet()) {
			mSectionToOffset.put(section, i);
			usedSectionNumbers[i] = section;
			i++;
		}

		// use offset to map the alphabet sections to their actual indicies in
		// the list
		for (Integer section : mSectionToPosition.keySet()) {
			mSectionToPosition.put(section, mSectionToPosition.get(section)
					+ mSectionToOffset.get(section));
		}
	}

	@Override
	public int getCount() {
		if (super.getCount() != 0) {
			return super.getCount() + usedSectionNumbers.length;
		}

		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == getPositionForSection(getSectionForPosition(position))) {
			return TYPE_HEADER;
		}
		return TYPE_NORMAL;
	}

	@Override
	public Object getItem(int position) {
		if (getItemViewType(position) == TYPE_NORMAL) {
			// we define this function in the full code later if the list item
			// is not a
			// header, then we fetch the data set item with the same position
			// off-settled
			// by the number of headers that appear before the item in the list
			return super
					.getItem(position
							- mSectionToOffset
									.get(getSectionForPosition(position)) - 1);
		}

		return null;
	}

	// these two methods just disable the headers
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		if (getItemViewType(position) == TYPE_HEADER) {
			return false;
		}
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int type = getItemViewType(position);
		if (type == TYPE_HEADER) {
			if (convertView == null) {
				convertView = mLayoutInflator.inflate(
						R.layout.separator, parent, false);
			}
			((TextView) convertView.findViewById(R.id.separator))
					.setText((String) getSections()[getSectionForPosition(position)]);
			return convertView;
		}
		return super.getView(
				position
						- mSectionToOffset.get(getSectionForPosition(position))
						- 1, convertView, parent);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();
		cursor.copyStringToBuffer(holder.TITLE, holder.buffer);

		holder.title.setText(holder.buffer.data, 0, holder.buffer.sizeCopied);
		
		int accepted = 0;
		int rejected = 0;
		int tentative = 0;

		accepted = cursor.getInt(ViewHolder.ACCEPTED);
		rejected = cursor.getInt(ViewHolder.REJECTED);
		tentative = cursor.getInt(ViewHolder.TENTATIVE);

		holder.progressView.setIndicatorValues(accepted, rejected,
				tentative);

		if (rejected > 60) {
			view.setBackgroundResource(R.drawable.list_bg_pink);
		} else {
			view.setBackgroundResource(R.drawable.list_bg_white);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = mLayoutInflator.inflate(R.layout.list_item, parent, false);

		final ViewHolder viewHolder = new ViewHolder();
		viewHolder.title = (TextView) view.findViewById(R.id.title);
		viewHolder.progressView = (MultiFiniteProgressView) view
				.findViewById(R.id.progress_view);
		view.setTag(viewHolder);

		return view;
	}

	private class ViewHolder {
		TextView title;
		MultiFiniteProgressView progressView;
		CharArrayBuffer buffer = new CharArrayBuffer(128);

		int TITLE = 1;

		final static int ACCEPTED = 2;
		final static int REJECTED = 3;
		final static int TENTATIVE = 4;
	}

	@Override
	public int getPositionForSection(int section) {
		if (!mSectionToOffset.containsKey(section)) {
			// This is only the case when the FastScroller is scrolling,
			// and so this section doesn't appear in our data set. The
			// implementation
			// of Fastscroller requires that missing sections have the same
			// index as the
			// beginning of the next non-missing section (or the end of the the
			// list if
			// if the rest of the sections are missing).
			// So, in pictorial example, the sections D and E would appear at
			// position 9
			// and G to Z appear in position 11.
			int i = 0;
			int maxLength = usedSectionNumbers.length;

			// linear scan over the sections (constant number of these) that
			// appear in the
			// data set to find the first used section that is greater than the
			// given section, so in the
			// example D and E correspond to F
			while (i < maxLength && section > usedSectionNumbers[i]) {
				i++;
			}
			if (i == maxLength)
				return getCount(); // the given section is past all our data

			return mIndexer.getPositionForSection(usedSectionNumbers[i])
					+ mSectionToOffset.get(usedSectionNumbers[i]);
		}

		return mIndexer.getPositionForSection(section)
				+ mSectionToOffset.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		int i = 0;
		int maxLength = usedSectionNumbers.length;

		// linear scan over the used alphabetical sections' positions
		// to find where the given section fits in
		while (i < maxLength
				&& position >= mSectionToPosition.get(usedSectionNumbers[i])) {
			i++;
		}
		return usedSectionNumbers[i - 1];
	}

	@Override
	public Object[] getSections() {
		return mIndexer.getSections();
	}

	@Override
	public void changeCursor(Cursor cursor) {
		super.changeCursor(cursor);
		initIndexer(cursor);
	}
}
