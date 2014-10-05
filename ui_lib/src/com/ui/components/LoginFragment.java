package com.ui.components;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ui.components.LoginFragment.LoginScreenErrorListener.LoginFailedErrorType;
import com.ui.components.utils.NetworkUtils;

/**
 * From UI point of view, LoginFrament is divided into three different parts. 1.
 * Top placeholder 2. UI for Login functionality ( edit text for username and
 * password, button to click login ) 3. Bottom placeholder
 * 
 * All 3 UI components occupy equal weight on the screen (That means screen is
 * divided into 3 equal parts occupying above 3 components).
 * 
 * Placeholder are meant to be used if we want to have UI added to basic login
 * screen. Like, using top placeholder you can add title.
 * 
 * add <<<<builder information>>>>> ----------------------------------------
 * When this fragment is extended, it is mandatory for you to call
 * super.onCreateView and super.onViewCreated. This is because we get callback
 * to inflate and set required UI values respectively.
 * 
 * @author sushil
 * 
 */
public abstract class LoginFragment extends Fragment implements OnClickListener {

	private int mThemeRes = R.style.LoginTheme;

	protected EditText mUserNameEditText;
	protected EditText mPasswordditText;
	protected Button mLogInButton;

	protected RelativeLayout mTopPlaceHolder;
	protected RelativeLayout mBottomPlaceHolder;

	private LoginScreenErrorListener mLoginScreenErrorListener;

	private LoginNetworkEssentials mLoginNetworkEssentials;

	private LoginAsyncTask mLoginAsyncTask;

	private ProgressDialog mProgressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mThemeRes != -1) {
			// create ContextThemeWrapper from the original Activity Context
			// with the custom theme
			final Context contextThemeWrapper = new ContextThemeWrapper(
					getActivity(), mThemeRes);

			// clone the inflater using the ContextThemeWrapper
			inflater = inflater.cloneInContext(contextThemeWrapper);
		}

		return inflater.inflate(R.layout.login_ui_form, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mUserNameEditText = (EditText) view
				.findViewById(R.id.user_name_edittext);
		mPasswordditText = (EditText) view.findViewById(R.id.password_edittext);
		mLogInButton = (Button) view.findViewById(R.id.login_button);
		mLogInButton.setOnClickListener(this);

		mTopPlaceHolder = (RelativeLayout) view
				.findViewById(R.id.login_top_placeholder);
		mBottomPlaceHolder = (RelativeLayout) view
				.findViewById(R.id.login_bottom_placeholder);

		initDefaultValues();
	}

	public void initDefaultValues() {
		setLoginScreenErrorListener(new DefalutErrorScreenListener());
	}

	@Override
	public void onClick(View v) {

		if (R.id.login_button == v.getId()) {
			handleLogin();
		}
	}

	private void handleLogin() {
		if (TextUtils.isEmpty(mUserNameEditText.getText())) {
			reportError(LoginFailedErrorType.USER_NAME_MISSING);
			return;
		}

		if (TextUtils.isEmpty(mPasswordditText.getText())) {
			reportError(LoginFailedErrorType.PASSWORD_MISSING);
			return;
		}
		mLoginAsyncTask = new LoginAsyncTask();
		mLoginAsyncTask.execute();
	}

	@Override
	public void onDetach() {

		if (mLoginAsyncTask != null
				&& mLoginAsyncTask.getStatus() == Status.RUNNING) {
			mLoginAsyncTask.cancel(true);
		}
		super.onDetach();
	}

	public interface LoginScreenErrorListener {

		public enum LoginFailedErrorType {
			USER_NAME_MISSING, PASSWORD_MISSING, LOGIN_FAILED, POOR_INTERNET_CONNECTION, LOGIN_CANCELLED
		}

		public void onLoginFailed(LoginFailedErrorType errorType);
	}

	public abstract void onLoginSuccess(HttpResponse httpResponse);

	// TODO this method should be removed, it has been to enter dummy.
	abstract public void testMethod();

	public abstract class LoginNetworkEssentials {

		abstract public String getNetworkUri();

		abstract public StringEntity getDataToPost();
	}

	public class DefalutErrorScreenListener implements LoginScreenErrorListener {

		@Override
		public void onLoginFailed(LoginFailedErrorType errorType) {

			if (LoginFailedErrorType.USER_NAME_MISSING == errorType) {
				showDialog(R.string.error_user_name_missing);
			} else if (LoginFailedErrorType.PASSWORD_MISSING == errorType) {
				showDialog(R.string.error_password_missing);
			} else if (LoginFailedErrorType.POOR_INTERNET_CONNECTION == errorType) {
				showDialog(R.string.error_bad_internet);
			} else if (LoginFailedErrorType.LOGIN_FAILED == errorType) {
				showDialog(R.string.error_login_failed);
			} else if (LoginFailedErrorType.LOGIN_CANCELLED == errorType) {
				showDialog(R.string.error_login_cancelled);
			}
		}

		private void showDialog(final int stringRes) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity());
			builder.setTitle(R.string.error_title).setMessage(stringRes);
			builder.setPositiveButton(R.string.button_ok, null);

			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	/**
	 * This should be called inside onCreate otherwise it will late to apply
	 * theme.
	 * 
	 * @param themeRes
	 */
	public void setTheme(int themeRes) {
		mThemeRes = themeRes;
	}

	public void setLoginScreenErrorListener(
			LoginScreenErrorListener errorListener) {
		mLoginScreenErrorListener = errorListener;
	}

	public class LoginAsyncTask extends AsyncTask<Void, Void, HttpResponse> {

		private boolean loginSuccess = false;
		private LoginFailedErrorType mErrorType;

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setTitle(R.string.progress_text_title_login);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(false);

			mProgressDialog.show();
		}

		@Override
		protected HttpResponse doInBackground(Void... params) {

			// TODO dummy call, following method, should be removed
			testMethod();
			
			if (mLoginNetworkEssentials == null) {
				loginSuccess = true;
				return null;
			}

			if (!NetworkUtils.isNetworkAvailable(getActivity())) {
				mErrorType = LoginFailedErrorType.POOR_INTERNET_CONNECTION;
				return null;
			}

			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			final int timeoutConnection = 20 * 1000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			final int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			final HttpClient httpClient = new DefaultHttpClient(httpParameters);

			final String loginUrl = mLoginNetworkEssentials.getNetworkUri();

			final HttpPost httpPost = new HttpPost(loginUrl);
			httpPost.setHeader("Content-type", "application/json");

			httpPost.setEntity(mLoginNetworkEssentials.getDataToPost());

			// Execute HTTP Post Request
			HttpResponse response = null;
			try {
				response = httpClient.execute(httpPost);
				loginSuccess = true;
			} catch (ClientProtocolException e) {
				mErrorType = LoginFailedErrorType.LOGIN_FAILED;
				e.printStackTrace();
			} catch (IOException e) {
				mErrorType = LoginFailedErrorType.LOGIN_FAILED;
				e.printStackTrace();
			}

			return response;
		}

		@Override
		protected void onPostExecute(HttpResponse result) {

			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}

			if (isCancelled()) {
				reportError(LoginFailedErrorType.LOGIN_CANCELLED);
				return;
			}

			if (loginSuccess) {
				onLoginSuccess(result);
			} else {
				reportError(mErrorType);
			}
		}
	}

	public void reportError(LoginFailedErrorType errorType) {
		if (mLoginScreenErrorListener != null) {
			mLoginScreenErrorListener.onLoginFailed(errorType);
		}
	}
}
