package com.inspection.management;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inspection.management.util.AppUtil;
import com.ui.components.LoginFragment;
import com.ui.components.ManageActionBarTitle;

public class InspectionManagementActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getSupportFragmentManager().addOnBackStackChangedListener(
				mOnBackStackChangeListener);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new InspectionLoginFragment())
					.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private OnBackStackChangedListener mOnBackStackChangeListener = new OnBackStackChangedListener() {

		@Override
		public void onBackStackChanged() {
			Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
			
			if (fragment instanceof ManageActionBarTitle) {
				((ManageActionBarTitle) fragment).updateActionBarTitle();
			}
		}
	};

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class InspectionLoginFragment extends LoginFragment {

		public InspectionLoginFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getActivity().getActionBar().hide();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			return super.onCreateView(inflater, container, savedInstanceState);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);

			mUserNameEditText.setBackgroundResource(R.drawable.top_semi_curved);
			mPasswordditText
					.setBackgroundResource(R.drawable.bottom_semi_curved);

			addTitle();
		}

		private void addTitle() {

			final LayoutInflater inflator = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final TextView titleTextView = (TextView) inflator.inflate(
					R.layout.login_title_text_view, null, false);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					RelativeLayout.TRUE);
			mTopPlaceHolder.addView(titleTextView, params);
		}

		@Override
		public void onLoginSuccess(HttpResponse httpResponse) {
			Toast.makeText(getActivity(), "login success", Toast.LENGTH_SHORT)
					.show();

			AppUtil.hideKeyboard(getActivity(), mUserNameEditText);

			// getActivity().getSupportFragmentManager().beginTransaction()
			// .add(R.id.container, new PartnerFragment()).commit();
			getActivity().getSupportFragmentManager().beginTransaction()
					.remove(this).add(R.id.container, new SelectPartnerCarrierFragment())
					.commit();
		}

		// TODO this method should be removed from superclass
		@Override
		public void testMethod() {
			AppUtil.insertPartnerDummyData(getActivity());
			AppUtil.insertCarrierDummyData(getActivity());
			AppUtil.insertPurchanseOrderDummyData(getActivity());
		}
	}
}
