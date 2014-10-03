package com.inspection.management.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class InspectionMetadata {
	
	public static String AUTHORITY = "com.inspection.management";

	public static final String DB_NAME = "inspection";
	public static final int DB_VERSION = 1;

	public static class PartnerTable implements BaseColumns {

		public static final String TABLE_NAME = "partner";;
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + TABLE_NAME);

		public static final String PARTNER_NAME 	= "name";
		public static final String ACCEPTED 		= "accepted";
		public static final String REJECTED 		= "rejected";
		public static final String TENTATIVE 		= "tentative";
	}
	
	public static class CarrierTable implements BaseColumns {

		public static final String TABLE_NAME = "carrier";;
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + TABLE_NAME);

		public static final String CARRIER_NAME 	= "name";
		public static final String ACCEPTED 		= "accepted";
		public static final String REJECTED 		= "rejected";
		public static final String TENTATIVE 		= "tentative";
	}
}
