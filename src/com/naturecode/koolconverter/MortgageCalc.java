package com.naturecode.koolconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class MortgageCalc extends SherlockFragmentActivity {
//public class MortgageCalc extends FragmentActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mortgage_main);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		ActionBar actionbar = getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.Tab RemBalTab = actionbar.newTab().setText("Remaining Balance");
		ActionBar.Tab MonPayTab = actionbar.newTab().setText("Monthly Payment");
		
		Fragment RemBal = new Mortgage_RemBal_Fragment();
		Fragment MonPay = new Mortgage_MonPay_Fragment();
		RemBalTab.setTabListener(new MyTabsListener(RemBal));
		MonPayTab.setTabListener(new MyTabsListener(MonPay));
		actionbar.addTab(RemBalTab);
		actionbar.addTab(MonPayTab);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case android.R.id.home:
		    	Intent intent = new Intent(this, MainCalc.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
		    	return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;
		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			FragmentManager fragMgr = getSupportFragmentManager();
			FragmentTransaction xaction = fragMgr.beginTransaction();
			xaction.replace(R.id.fragment_container, fragment);
			xaction.commit();
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			FragmentManager fragMgr = getSupportFragmentManager();
			FragmentTransaction xaction = fragMgr.beginTransaction();			
			xaction.remove(fragment);
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stubs
		}
	}
}