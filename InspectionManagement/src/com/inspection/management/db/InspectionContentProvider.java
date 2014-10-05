package com.inspection.management.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.inspection.management.db.InspectionMetadata.CarrierTable;
import com.inspection.management.db.InspectionMetadata.PartnerTable;
import com.inspection.management.db.InspectionMetadata.PurchaseTable;

public class InspectionContentProvider extends ContentProvider {

	/** TAG */
	private static final String TAG = InspectionContentProvider.class
			.getSimpleName();

	private DatabaseHelper mDatabaseHelper;

	private static UriMatcher sUriMatcher;
	private static final int PARTNER 		= 1;
	private static final int PARTNER_ID 	= 2;
	private static final int CARRIER 		= 3;
	private static final int CARRIER_ID 	= 4;
	private static final int PURCHASE		= 5;
	private static final int PURCHASE_ID	= 6;
	
	{
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(InspectionMetadata.AUTHORITY,
				PartnerTable.TABLE_NAME, PARTNER);
		sUriMatcher.addURI(InspectionMetadata.AUTHORITY,
				PartnerTable.TABLE_NAME + "/#", PARTNER_ID);
		
		sUriMatcher.addURI(InspectionMetadata.AUTHORITY,
				CarrierTable.TABLE_NAME, CARRIER);
		sUriMatcher.addURI(InspectionMetadata.AUTHORITY,
				CarrierTable.TABLE_NAME + "/#", CARRIER_ID);
		
		sUriMatcher.addURI(InspectionMetadata.AUTHORITY,
				PurchaseTable.TABLE_NAME, PURCHASE);
		sUriMatcher.addURI(InspectionMetadata.AUTHORITY,
				PurchaseTable.TABLE_NAME + "/#", PURCHASE_ID);
	}

	@Override
	public boolean onCreate() {

		mDatabaseHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String tableName = null;
		switch (sUriMatcher.match(uri)) {
		case PARTNER:
		case PARTNER_ID:
			tableName = PartnerTable.TABLE_NAME;
			break;
		case CARRIER:
		case CARRIER_ID:
			tableName = CarrierTable.TABLE_NAME;
			break;
		case PURCHASE:
		case PURCHASE_ID:
			tableName = PurchaseTable.TABLE_NAME;
			break;
			

		default:
			throw new IllegalArgumentException("Not a valid URI");
		}

		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

		return db.query(tableName, projection, selection, selectionArgs, null,
				null, sortOrder);
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String tableName = null;
		switch (sUriMatcher.match(uri)) {
		case PARTNER:
		case PARTNER_ID:
			tableName = PartnerTable.TABLE_NAME;
			break;
		case CARRIER:
		case CARRIER_ID:
			tableName = CarrierTable.TABLE_NAME;
			break;
		case PURCHASE:
		case PURCHASE_ID:
			tableName = PurchaseTable.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException("Not a valid URI");
		}

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		long id = db.insert(tableName, null, values);

		if (id > 0) {
			Uri insertUri = ContentUris.withAppendedId(uri, id);
			getContext().getContentResolver().notifyChange(insertUri, null);
			return insertUri;
		}

		throw new IllegalStateException("Could not insert!");
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		int rowsInserted = super.bulkInsert(uri, values);
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsInserted;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		String tableName = null;
		switch (sUriMatcher.match(uri)) {
		case PARTNER:
		case PARTNER_ID:
			tableName = PartnerTable.TABLE_NAME;
			break;
		case CARRIER:
		case CARRIER_ID:
			tableName = CarrierTable.TABLE_NAME;
			break;
		case PURCHASE:
		case PURCHASE_ID:
			tableName = PurchaseTable.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException("Not a valid URI");
		}

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		return db.delete(tableName, selection, selectionArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		String tableName = null;
		switch (sUriMatcher.match(uri)) {
		case PARTNER:
		case PARTNER_ID:
			tableName = PartnerTable.TABLE_NAME;
			break;
		case CARRIER:
		case CARRIER_ID:
			tableName = CarrierTable.TABLE_NAME;
			break;
		case PURCHASE:
		case PURCHASE_ID:
			tableName = PurchaseTable.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException("Not a valid URI");
		}

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		int rowsAffected = db.update(tableName, values, selection,
				selectionArgs);

		if (rowsAffected > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
			return rowsAffected;
		}

		return 0;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, InspectionMetadata.DB_NAME, null,
					InspectionMetadata.DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(getPartnerTableQuery());
				db.execSQL(getCarrierTableQuery());
				db.execSQL(getPurchaseOrderTableQuery());
			} catch (SQLException e) {
				Log.e(TAG, "onCreate : failed to create parts table");
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}

		private static String getPartnerTableQuery() {
			final StringBuilder builder = new StringBuilder();

			builder.append("CREATE TABLE IF NOT EXISTS "
					+ PartnerTable.TABLE_NAME + " (");
			builder.append(PartnerTable._ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, ");
			builder.append(PartnerTable.PARTNER_NAME + " TEXT, ");
			builder.append(PartnerTable.ACCEPTED + " INTEGER, ");
			builder.append(PartnerTable.REJECTED + " INTEGER,");
			builder.append(PartnerTable.TENTATIVE + " INTEGER);");

			return builder.toString();
		}

		private static String getCarrierTableQuery() {
			final StringBuilder builder = new StringBuilder();

			builder.append("CREATE TABLE IF NOT EXISTS "
					+ CarrierTable.TABLE_NAME + " (");
			builder.append(PartnerTable._ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, ");
			builder.append(CarrierTable.CARRIER_NAME + " TEXT, ");
			builder.append(CarrierTable.ACCEPTED + " INTEGER, ");
			builder.append(CarrierTable.REJECTED + " INTEGER,");
			builder.append(CarrierTable.TENTATIVE + " INTEGER);");

			return builder.toString();
		}
		
		private static String getPurchaseOrderTableQuery() {
			final StringBuilder builder = new StringBuilder();

			builder.append("CREATE TABLE IF NOT EXISTS "
					+ PurchaseTable.TABLE_NAME + " (");
			builder.append(PurchaseTable._ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, ");
			builder.append(PurchaseTable.ORDER_NO + " TEXT, ");
			builder.append(PurchaseTable.ETA + " TEXT);");

			return builder.toString();
		}
	}
}
