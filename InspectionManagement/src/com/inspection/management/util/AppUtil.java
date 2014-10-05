package com.inspection.management.util;

import java.util.Random;

import com.inspection.management.db.InspectionMetadata.CarrierTable;
import com.inspection.management.db.InspectionMetadata.PartnerTable;
import com.inspection.management.db.InspectionMetadata.PurchaseTable;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class AppUtil {

	public static void hideKeyboard(Context context, EditText editText) {

		if (context == null || editText == null) {
			throw new IllegalArgumentException(
					"Context or EditText can not be NULL");
		}

		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	// TODO test methods, should be removed.
	public static void insertPartnerDummyData(Activity activity) {

		// clean old data.
		activity.getContentResolver().delete(PartnerTable.CONTENT_URI,
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
			// getActivity().getContentResolver().insert(PartnerTable.CONTENT_URI,
			// value);
		}
		activity.getContentResolver().bulkInsert(PartnerTable.CONTENT_URI,
				values);
	}

	// TODO test methods, should be removed.
	public static void insertCarrierDummyData(Activity activity) {

		// clean old data.
		activity.getContentResolver().delete(CarrierTable.CONTENT_URI,
				null, null);

		final ContentValues[] values = new ContentValues[52];
		int asciiCode = 65;
		for (int i = 0; i < values.length; i++) {
			final ContentValues value = new ContentValues();
			value.put(CarrierTable.CARRIER_NAME, (char) asciiCode
					+ " some random text");
			values[i] = value;
			asciiCode++;
			if (asciiCode == 91) {
				asciiCode = 65;
			}
			activity.getContentResolver().insert(CarrierTable.CONTENT_URI,
					value);
		}
		// getActivity().getContentResolver().bulkInsert(PartnerTable.CONTENT_URI,
		// values);
	}
	
	public static void insertPurchanseOrderDummyData(Activity activity) {

		// clean old data.
		activity.getContentResolver().delete(PurchaseTable.CONTENT_URI,
				null, null);

		final ContentValues[] values = new ContentValues[5];
		for (int i = 0; i < values.length; i++) {
			final ContentValues value = new ContentValues();
			value.put(PurchaseTable.ORDER_NO,
					String.valueOf(randInt(123456, 999999)));
			value.put(PurchaseTable.ETA, "12.45");
			values[i] = value;
			// getActivity().getContentResolver().insert(PurchaseTable.CONTENT_URI,
			// value);
		}
		activity.getContentResolver().bulkInsert(
				PurchaseTable.CONTENT_URI, values);
	}
	
	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
