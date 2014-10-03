package com.ui.components;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;

public abstract class AlphabeticalCursorAdapter extends CursorAdapter implements
		SectionIndexer {

	protected static final int TYPE_HEADER = 1;
	protected static final int TYPE_NORMAL = 0;

	private static final int TYPE_COUNT = 2;

	private AlphabetIndexer indexer;

	private int[] usedSectionNumbers;

	protected Map<Integer, Integer> sectionToOffset;
	private Map<Integer, Integer> sectionToPosition;

	protected Activity mActivity;
	protected String mColumnName;
	
	public AlphabeticalCursorAdapter(Activity context, Cursor cursor, String columnName) {
		super(context, cursor, true);
		mActivity = context;
		mColumnName = columnName;
		updateIndexer();
	}

	private void updateIndexer() {
		Cursor c = getCursor();
		if (c == null || mColumnName == null) {
			return;
		}
		indexer = new AlphabetIndexer(c, c.getColumnIndexOrThrow(mColumnName),
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		sectionToPosition = new TreeMap<Integer, Integer>(); 
		// use a TreeMap because we are going to iterate over its keys in sorted order
		sectionToOffset = new HashMap<Integer, Integer>();

		final int count = super.getCount();

		int i;
		// temporarily have a map alphabet section to first index it appears
		// (this map is going to be doing something else later)
		for (i = count - 1; i >= 0; i--) {
			sectionToPosition.put(indexer.getSectionForPosition(i), i);
		}

		i = 0;
		usedSectionNumbers = new int[sectionToPosition.keySet().size()];

		// note that for each section that appears before a position, we must
		// offset our
		// indices by 1, to make room for an alphabetical header in our list
		for (Integer section : sectionToPosition.keySet()) {
			sectionToOffset.put(section, i);
			usedSectionNumbers[i] = section;
			i++;
		}

		// use offset to map the alphabet sections to their actual indicies in
		// the list
		for (Integer section : sectionToPosition.keySet()) {
			sectionToPosition.put(section, sectionToPosition.get(section)
					+ sectionToOffset.get(section));
		}
	}
	
	@Override
	protected void onContentChanged() {
		super.onContentChanged();
		updateIndexer();
	}

	public String getHeaderForPosition(int position) {
		return (String)getSections()[getSectionForPosition(position)];
	}

	@Override
	public int getCount() {
		if (super.getCount() != 0) {
			// sometimes your data set gets invalidated. In this case getCount()
			// should return 0 and not our adjusted count for the headers.
			// The only way to know if data is invalidated is to check if
			// super.getCount() is 0.
			return super.getCount() + usedSectionNumbers.length;
		}

		return 0;
	}

	@Override
	public Object getItem(int position) {
		
		if (getItemViewType(position) == TYPE_NORMAL) {
			// we define this function in the full code later
			// if the list item is not a header, then we fetch the data set item
			// with the same position
			// off-setted by the number of headers that appear before the item
			// in the list
			return super.getItem(position
					- sectionToOffset.get(getSectionForPosition(position)) - 1);
		}

		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		if (!sectionToOffset.containsKey(section)) {
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

			return indexer.getPositionForSection(usedSectionNumbers[i])
					+ sectionToOffset.get(usedSectionNumbers[i]);
		}

		return indexer.getPositionForSection(section)
				+ sectionToOffset.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		int i = 0;
		int maxLength = usedSectionNumbers.length;

		// linear scan over the used alphabetical sections' positions
		// to find where the given section fits in
		while (i < maxLength
				&& position >= sectionToPosition.get(usedSectionNumbers[i])) {
			i++;
		}
		return usedSectionNumbers[i - 1];
	}
	
	@Override
    public Object[] getSections() {
        return indexer.getSections();
    }

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
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
}
