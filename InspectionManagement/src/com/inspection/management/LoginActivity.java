package com.inspection.management;

import org.apache.http.HttpResponse;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ui.components.LoginFragment;

public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.login, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends LoginFragment {

        public PlaceholderFragment() {
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
            final TextView titleTextView = new TextView(getActivity());
            titleTextView.setText(R.string.login_title);
        }

        @Override
        public void onLoginSuccess(HttpResponse httpResponse) {
            Toast.makeText(getActivity(), "login success", Toast.LENGTH_SHORT)
                    .show();
//            getActivity().getSupportFragmentManager().beginTransaction()
//            .add(R.id.container, new PartnerFragment()).commit();
            getActivity().getSupportFragmentManager().beginTransaction()
            .remove(this).add(R.id.container, new TestFragment()).commit();
        }
    }
}
