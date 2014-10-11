package com.inspection.management.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * This activity is just a communicator between bar code scanner and our scan
 * request fragment <br/>
 * This will be responsible to parsing the bar code scan result and provide
 * required data to caller fragment
 * 
 */
public class BarCodeScannerActivity extends ActionBarActivity {

	public static final String EXTRA_SCAN_DATA = "scan_data";
	public static final String EXTRA_PO_NUMBER = "purchase_order_number";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scan();
	}

	private void scan() {
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
				resultCode, intent);
		if (result != null) {
			setResult(resultCode, parseScanResult(result));
		}
		finish();
	}

	private Intent parseScanResult(IntentResult intentResult) {
		Intent data = new Intent();
		String contents = null;
		if (intentResult != null) {
			contents = intentResult.getContents();
			if (!TextUtils.isEmpty(contents)) {
				data.putExtra(EXTRA_SCAN_DATA, contents);
				// TODO: write parse PO number logic
				// data.putExtra(EXTRA_PO_NUMBER, poNumber);
			}
		}
		Toast.makeText(this, "Result : " + contents, Toast.LENGTH_SHORT).show();
		return data;

		// if (contents != null) {
		// showDialog("Scanned", contents.toString());
		// } else {
		// showDialog("Scanning failed", "Error");
		// }

	}
}
