package com.naturecode.koolconverter;

import java.util.Vector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.naturecode.koolconverter.util.CustomAdapter;
import com.naturecode.koolconverter.util.RowData;

public class MainCalc extends ListActivity {
	private LayoutInflater mInflater;
	private Vector<RowData> data = new Vector<RowData>();
	RowData rd;
	private Integer[] atrributes = {R.layout.main_720, R.id.calculator_title, R.id.calculator_detail, R.id.calculator_icon};
	private Integer[] images = {R.drawable.auto, R.drawable.currency, R.drawable.mortgage, R.drawable.shopping, R.drawable.tip, R.drawable.unit};
	private int width=720;
	private int height=0;
	private static final String TAG = "MainCalc";
	/** Called when the activity is first created. */
	@Override
	@TargetApi(13)
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		if(Build.VERSION.SDK_INT>=13){
//			Display display = getWindowManager().getDefaultDisplay();
//			Point size = new Point();
//			display.getSize(size);
//			width = size.x;
//			height = size.y;
//		}
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		height = metrics.heightPixels;
		
//		Log.v(TAG, "width: "+width);
//		Log.v(TAG, "height: "+height);
		((GlobalVariables)getApplication()).setScreen_width(width);
		((GlobalVariables)getApplication()).setScreen_height(height);
		
		setContentView(R.layout.mainlist);
		String[] calculator_types = getResources().getStringArray(R.array.calculator_types);
		String[] calculator_desc = getResources().getStringArray(R.array.calculator_desc);
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		data = new Vector<RowData>();
		for(int i=0;i<calculator_types.length;i++){
			rd = new RowData(i, calculator_types[i], calculator_desc[i]);
			data.add(rd);
		}
		
		if(width<800){
			atrributes[0] = R.layout.main_720;
		}	
		else if (width>=800 && width<1080){
			atrributes[0] = R.layout.main_800;
		}
		else{
			atrributes[0] = R.layout.main_1080;
		}
		
		CustomAdapter adapter = new CustomAdapter(this, data, mInflater, atrributes, images);
		setListAdapter(adapter);
		getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				dispatch(position);
			}
		});
	}
	
	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, MainCalc.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}
	
	private void dispatch(int position){
		String calc_type="start";
		try{
			calc_type = data.elementAt(position).getmTitle();
			if(calc_type!=null && calc_type.equalsIgnoreCase("Car Loan Calculator")){
				Intent myIntent = new Intent(MainCalc.this, AutoLoanCalc.class);
				MainCalc.this.startActivity(myIntent);
			}
			else if (calc_type.equalsIgnoreCase("Tip Calculator")){
				Intent myIntent = new Intent(MainCalc.this, TipCalc.class);
				MainCalc.this.startActivity(myIntent);
			}
			else if (calc_type.equalsIgnoreCase("Shopping Discount")){
				Intent myIntent = new Intent(MainCalc.this, ShoppingDiscountCalc.class);
				MainCalc.this.startActivity(myIntent);
			}
			else if (calc_type.equalsIgnoreCase("Currency Converter")){
				Intent myIntent = new Intent(MainCalc.this, CurrencyCalc.class);
				MainCalc.this.startActivity(myIntent);
			}
			else if (calc_type.equalsIgnoreCase("Mortgage Calculator")){
				Intent myIntent = new Intent(MainCalc.this, MortgageCalc.class);
				MainCalc.this.startActivity(myIntent);
			}
			else {
				Intent myIntent = new Intent(MainCalc.this, UnitGrid.class);
				MainCalc.this.startActivity(myIntent);
			}
		}catch(Exception e){
			CharSequence error = calc_type;
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
}