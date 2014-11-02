package com.naturecode.koolconverter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.naturecode.koolconverter.util.ImageAdapter;

public class UnitGrid extends SherlockActivity {
	GridView gridView;
	String[] unit_list;
	private static final String TAG = "UnitGrid";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unit_grid_layout);
		
		unit_list = getResources().getStringArray(R.array.units);
		gridView = (GridView) findViewById(R.id.gridView1);
		gridView.setAdapter(new ImageAdapter(this, unit_list));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
//		int height = ((GlobalVariables) this.getApplication()).getScreen_height();
//		gridView.setMinimumHeight(380);
//		Log.v(TAG, "height: "+height);
//		Log.v(TAG, "height: "+height/5);
//		gridView.setVerticalSpacing(height/30);
//		int width = ((GlobalVariables) this.getApplication()).getScreen_width();
//		if (width>=800){
//			gridView.setVerticalSpacing(22);
//		}
//		else
			gridView.setVerticalSpacing(18);
		
		
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (unit_list[position].equalsIgnoreCase("Angle")) {
					Intent myIntent = new Intent(UnitGrid.this, AngleCalc.class);
					UnitGrid.this.startActivity(myIntent);
				} 
				else if (unit_list[position].equalsIgnoreCase("Area")) {
					Intent myIntent = new Intent(UnitGrid.this, AreaCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Bit & Byte")) {
					Intent myIntent = new Intent(UnitGrid.this, ByteCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Density")) {
					Intent myIntent = new Intent(UnitGrid.this, DensityCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Electric")) {
					Intent myIntent = new Intent(UnitGrid.this, ElectricCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Energy")) {
					Intent myIntent = new Intent(UnitGrid.this, EnergyCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Force")) {
					Intent myIntent = new Intent(UnitGrid.this, ForceCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Fuel")) {
					Intent myIntent = new Intent(UnitGrid.this, FuelCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Length")) {
					Intent myIntent = new Intent(UnitGrid.this, LengthCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Power")) {
					Intent myIntent = new Intent(UnitGrid.this, PowerCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Pressure")) {
					Intent myIntent = new Intent(UnitGrid.this, PressureCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Speed")) {
					Intent myIntent = new Intent(UnitGrid.this, SpeedCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Temperature")) {
					Intent myIntent = new Intent(UnitGrid.this, TemperatureCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Volume")) {
					Intent myIntent = new Intent(UnitGrid.this, VolumeCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
				else if (unit_list[position].equalsIgnoreCase("Weight")) {
					Intent myIntent = new Intent(UnitGrid.this, WeightCalc.class);
					UnitGrid.this.startActivity(myIntent);
				}
			}
		});
	}
	
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
}