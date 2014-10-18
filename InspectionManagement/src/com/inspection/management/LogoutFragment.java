package com.inspection.management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * This is a bottom bar fragment having Cancel and Logout button. Where do your
 * need this (R.layout.bottom_action_bar) control. Just define a container
 * layout and you are ready to use this in your activity or fragment !! :-)<br/>
 * For Activity, use <Code>addOnActivity(final ActionBarActivity activity,
			final int containerId)</code> <br/>
 * For Fragment, use
 * <code>addAsChild(final Fragment parentFragment, final int containerId)</code><br/>
 * <b>Note : </b> Designed in support with support-v4, v7 libs
 */

// TODO: Implement logout module, callback mechanism can be used. just a
// thought!
public class LogoutFragment extends Fragment implements OnClickListener {
	private static final String TAG = LogoutFragment.class.getSimpleName();
	private Button mCancelButton;
	private Button mLogoutButton;
	private static boolean isChild;

	public static void addAsChild(final Fragment parentFragment,
			final int containerId) {
		isChild = true;
		final String tag = containerId + TAG;
		FragmentManager childFrgMngr = parentFragment.getChildFragmentManager();
		FragmentTransaction transation = childFrgMngr.beginTransaction();

		Fragment fragment = childFrgMngr.findFragmentByTag(tag);
		if (fragment == null) {
			fragment = new LogoutFragment();
			transation.add(containerId, fragment, tag);
		} else {
			transation.show(fragment);
		}
		transation.commitAllowingStateLoss();
	}

	public static void addOnActivity(final ActionBarActivity activity,
			final int containerId) {
		final String tag = containerId + TAG;
		FragmentManager childFrgMngr = activity.getSupportFragmentManager();
		FragmentTransaction transation = childFrgMngr.beginTransaction();

		Fragment fragment = childFrgMngr.findFragmentByTag(tag);
		if (fragment == null) {
			fragment = new LogoutFragment();
			transation.add(containerId, fragment, tag);
		} else {
			isChild();
			transation.show(fragment);
		}
		transation.commitAllowingStateLoss();
	}

	private static void isChild() {
		if (isChild) {
			throw new IllegalStateException(
					"Was previously added as child in this parent, remove first ");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.bottom_action_bar, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mCancelButton = (Button) view.findViewById(R.id.cancel_button);
		mCancelButton.setOnClickListener(this);

		mLogoutButton = (Button) view.findViewById(R.id.logout_button);
		mLogoutButton.setOnClickListener(this);

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_button:
			removeSelf();
			break;

		case R.id.logout_button:
			// TODO: Implement logout module
			break;
		default:
			break;
		}
	}

	private void removeSelf() {
		if (isChild) {
			getActivity().getSupportFragmentManager().beginTransaction()
					.remove(getParentFragment()).commitAllowingStateLoss();
		} else {
			getActivity().getSupportFragmentManager().beginTransaction()
					.remove(this).commitAllowingStateLoss();
		}
	}
}
