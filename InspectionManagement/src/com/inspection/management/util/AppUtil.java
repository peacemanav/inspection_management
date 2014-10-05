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
		activity.getContentResolver().delete(PartnerTable.CONTENT_URI, null,
				null);

		final ContentValues[] values = new ContentValues[sPartner.length];
		for (int i = 0; i < values.length; i++) {
			final ContentValues value = new ContentValues();
			value.put(PartnerTable.PARTNER_NAME, sPartner[i]);
			value.put(PartnerTable.ACCEPTED, randInt(10, 100));
			value.put(PartnerTable.REJECTED, randInt(10, 100));
			value.put(PartnerTable.TENTATIVE, randInt(10, 100));
			values[i] = value;
			// getActivity().getContentResolver().insert(PartnerTable.CONTENT_URI,
			// value);
		}
		activity.getContentResolver().bulkInsert(PartnerTable.CONTENT_URI,
				values);
	}

	// TODO test methods, should be removed.
	public static void insertCarrierDummyData(Activity activity) {

		// clean old data.
		activity.getContentResolver().delete(CarrierTable.CONTENT_URI, null,
				null);

		final ContentValues[] values = new ContentValues[sCarrier.length];
		for (int i = 0; i < values.length; i++) {
			final ContentValues value = new ContentValues();
			value.put(CarrierTable.CARRIER_NAME, sCarrier[i]);
			value.put(CarrierTable.ACCEPTED, randInt(10, 100));
			value.put(CarrierTable.REJECTED, randInt(10, 100));
			value.put(CarrierTable.TENTATIVE, randInt(10, 100));

			values[i] = value;
			// activity.getContentResolver().insert(CarrierTable.CONTENT_URI,
			// value);
		}
		activity.getContentResolver().bulkInsert(CarrierTable.CONTENT_URI,
				values);
	}

	public static void insertPurchanseOrderDummyData(Activity activity) {

		// clean old data.
		activity.getContentResolver().delete(PurchaseTable.CONTENT_URI, null,
				null);

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
		activity.getContentResolver().bulkInsert(PurchaseTable.CONTENT_URI,
				values);
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

	private static String[] sPartner = { "A & Z LLC", "All Fresh GPS LLC",
			"Allstate Apple Exchange", "Alsum Frams & Produce Inc",
			"Borton Sons Ins", "BellHarvest Sales Inc", "Cristina Foods Inc",
			"Caito Foods Service Inc", "CMI LLC", "D & Z LLC",
			"DAll Fresh GPS LLC", "DAllstate Apple Exchange",
			"Dlsum Frams & Produce Inc", "Eorton Sons Ins",
			"EBellHarvest Sales Inc", "Fristina Foods Inc",
			"Faito Foods Service Inc", "FMI LLC", "FA & Z LLC",
			"GAll Fresh GPS LLC", "GAllstate Apple Exchange",
			"GAlsum Frams & Produce Inc", "Horton Sons Ins",
			"HellHarvest Sales Inc", "Iristina Foods Inc",
			"Iaito Foods Service Inc", "IMI LLC", "J & Z LLC",
			"JAll Fresh GPS LLC", "Jllstate Apple Exchange",
			"Klsum Frams & Produce Inc", "Korton Sons Ins",
			"KellHarvest Sales Inc", "Lristina Foods Inc",
			"Laito Foods Service Inc", "MMI LLC", "N & Z LLC",
			"NAll Fresh GPS LLC", "Ollstate Apple Exchange",
			"Olsum Frams & Produce Inc", "Porton Sons Ins",
			"QellHarvest Sales Inc", "Rristina Foods Inc",
			"Raito Foods Service Inc", "SMI LLC", "S & Z LLC",
			"TAll Fresh GPS LLC", "TAllstate Apple Exchange",
			"Ualsum Frams & Produce Inc", "Uorton Sons Ins",
			"VellHarvest Sales Inc", "Vristina Foods Inc",
			"Vaito Foods Service Inc", "WCMI LLC", "WA & Z LLC",
			"XAll Fresh GPS LLC", "Xllstate Apple Exchange",
			"Xlsum Frams & Produce Inc", "Yorton Sons Ins",
			"YellHarvest Sales Inc", "Zristina Foods Inc",
			"Zaito Foods Service Inc", "ZCMI LLC" };
	
	private static String[] sCarrier = {
		"ABC Trucking", "Acme Trucking", "Allover Transport", "Americal Freight",
		"BCD Trucking", "Beeline Freight",
		"CDE Trucking", "Countryide Freight", "Crosscountry Transport",
		"DBC Trucking", "DAcme Trucking", "DAllover Transport", "DAmerical Freight",
		"EBCD Trucking", "EBeeline Freight",
		"FDE Trucking", "Fountryide Freight", "Frosscountry Transport",
		"GABC Trucking", "GAcme Trucking", "GAllover Transport", "GAmerical Freight",
		"HBCD Trucking", "HBeeline Freight",
		"ICDE Trucking", "ICountryide Freight", "ICrosscountry Transport",
		"JABC Trucking", "JAcme Trucking", "JAllover Transport", "JAmerical Freight",
		"KBCD Trucking", "KBeeline Freight",
		"LCDE Trucking", "LCountryide Freight", "LCrosscountry Transport",
		"MABC Trucking", "MAcme Trucking", "MAllover Transport", "MAmerical Freight",
		"NBCD Trucking", "NBeeline Freight",
		"OCDE Trucking", "OCountryide Freight", "OCrosscountry Transport",
		"PABC Trucking", "PAcme Trucking", "PAllover Transport", "PAmerical Freight",
		"QBCD Trucking", "QBeeline Freight",
		"RCDE Trucking", "RCountryide Freight", "RCrosscountry Transport",
		"SABC Trucking", "SAcme Trucking", "SAllover Transport", "SAmerical Freight",
		"TBCD Trucking", "TBeeline Freight",
		"UCDE Trucking", "UCountryide Freight", "VCrosscountry Transport",
		"VABC Trucking", "VAcme Trucking", "WAllover Transport", "WAmerical Freight",
		"WBCD Trucking", "XBeeline Freight",
		"XCDE Trucking", "XCountryide Freight", "XCrosscountry Transport",
		"YABC Trucking", "YAcme Trucking", "ZAllover Transport", "ZAmerical Freight",
		"ZBCD Trucking", "Zeeline Freight"
	};
}
