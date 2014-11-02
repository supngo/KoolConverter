package com.naturecode.koolconverter;

import java.text.DecimalFormat;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class AreaCalc extends SherlockActivity {
	private EditText area_from;
	private EditText area_to;
	private Spinner type_from;
	private Spinner type_to;
	private double result = 0;	
//	private static final String TAG = "debug";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.unit_calc_800);
//		int width = ((GlobalVariables) this.getApplication()).getScreen_width();
//		if (width>=800){
//			setContentView(R.layout.unit_calc_800);
//		}
//		else
//			setContentView(R.layout.unit_calc_720);
		
		initControls();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case android.R.id.home:
		    	Intent intent = new Intent(this, UnitGrid.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
		    	return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void initControls(){
		area_from = (EditText)findViewById(R.id.unit_from);
		area_to = (EditText)findViewById(R.id.unit_to);
		
		type_from = (Spinner)findViewById(R.id.unit_spinner1);
		type_to = (Spinner)findViewById(R.id.unit_spinner2);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.unit_area, R.layout.my_spinner_text);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    type_from.setAdapter(adapter);
	    type_from.setSelection(4);
	    type_to.setAdapter(adapter);
	    type_to.setSelection(7);   
	    
	    type_from.setOnItemSelectedListener(new MyOnItemSelectedListener(){
	    	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    		calculate(); 
	    	}
	    });
	    
	    type_to.setOnItemSelectedListener(new MyOnItemSelectedListener(){
	    	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    		calculate(); 
	    	}
	    });
	    
	    area_from.addTextChangedListener(new TextWatcher(){ 
	    	 public void afterTextChanged(Editable s) {
	    		calculate(); 
	    	 }
	    	 public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	    	 public void onTextChanged(CharSequence s, int start, int before, int count){}
	    });
	}
	
	private void calculate(){
		try{
			result = Double.parseDouble(area_from.getText().toString().trim().length()<1?"0":area_from.getText().toString().trim());
			if(result==0){
//				CharSequence error = "Input must be greater than 0";
//				Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
//				toast.show();
				area_to.setText("");
			}
			else{
				String selected_type_from = (String)type_from.getSelectedItem();
				String selected_type_to = (String)type_to.getSelectedItem();
				if(selected_type_from.equals(selected_type_to)){
					result=1*result;
				}
				else{
					double square_inch=0;
					HashMap<String, Double> table = new HashMap<String, Double>();
					for(int i=0;i<type_from.getCount();i++){
						String check = (String)type_from.getItemAtPosition(i);
						if (check.equalsIgnoreCase("acre")){
							square_inch = (double)4840 * 1296;
							table.put(check, Double.valueOf(square_inch));
						}
						if (check.equalsIgnoreCase("are")){
							square_inch = (double)100/(0.0254 * 0.0254);
							table.put(check, Double.valueOf(square_inch));
						}
						if (check.equalsIgnoreCase("hectare")){
							square_inch = (double)10000/(0.0254 * 0.0254);
							table.put(check, Double.valueOf(square_inch));
						}
						if (check.equalsIgnoreCase("square centimeter")){
							square_inch = (double)1/(2.54 * 2.54);
							table.put(check, Double.valueOf(square_inch));
						}
						if (check.equalsIgnoreCase("square foot")){
							table.put(check, Double.valueOf(144));
						}
						if (check.equalsIgnoreCase("square inch")){
							table.put(check, Double.valueOf(1));
						}
						if (check.equalsIgnoreCase("square kilometer")){
							square_inch = (double)1000000/(0.0254 * 0.0254);
							table.put(check, Double.valueOf(square_inch));
						}
						if (check.equalsIgnoreCase("square meter")){
							square_inch = (double)1/(0.0254 * 0.0254);
							table.put(check, Double.valueOf(square_inch));
						}
						if (check.equalsIgnoreCase("square mile")){
							square_inch = (double)63360*63360;
							table.put(check, Double.valueOf(square_inch));
//							Log.v(TAG, "square mile " + square_inch);
						}
						if (check.equalsIgnoreCase("square yard")){
							table.put(check, Double.valueOf(1296));
						}
					}
					double from = table.get(selected_type_from);
					double to = table.get(selected_type_to);
					result = (double)result*from/to;
				}
				DecimalFormat frmt = new DecimalFormat("#,###,###,###,###.######");
				String formatted = frmt.format(result);
				area_to.setText(formatted);
			}
		}
		catch(NumberFormatException nfe){
			area_to.setText("");
			CharSequence error = "Number Format Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch (ArithmeticException ae){
			area_to.setText("");
			CharSequence error = "Arithmetic Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();

		}
		catch(Exception e){
			e.printStackTrace();
			CharSequence error = "Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
}