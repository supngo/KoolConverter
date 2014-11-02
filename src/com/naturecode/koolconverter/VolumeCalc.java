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

public class VolumeCalc extends SherlockActivity {
	private EditText volume_from;
	private EditText volume_to;
	private Spinner type_from;
	private Spinner type_to;
	private double result = 0;	
//	private static final String TAG = "test";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.unit_calc_800);
//		int width = ((GlobalVariables) this.getApplication()).getScreen_width();
//		if (width>=800 && width<1080){
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
		volume_from = (EditText)findViewById(R.id.unit_from);
		volume_to = (EditText)findViewById(R.id.unit_to);
		
		type_from = (Spinner)findViewById(R.id.unit_spinner1);
		type_to = (Spinner)findViewById(R.id.unit_spinner2);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.unit_volume, R.layout.my_spinner_text);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    type_from.setAdapter(adapter);
	    type_from.setSelection(8);
	    type_to.setAdapter(adapter);
	    type_to.setSelection(9);
	    
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
	    
	    volume_from.addTextChangedListener(new TextWatcher(){ 
	    	 public void afterTextChanged(Editable s) {
	    		calculate(); 
	    	 }
	    	 public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	    	 public void onTextChanged(CharSequence s, int start, int before, int count){}
	    });
	}
	
	private void calculate(){
		try{
			result = Double.parseDouble(volume_from.getText().toString().trim().length()<1?"0":volume_from.getText().toString().trim());
			if(result==0){
//				CharSequence error = "Input must be greater than 0";
//				Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
//				toast.show();
				volume_to.setText("");
			}
			else{
				String selected_type_from = (String)type_from.getSelectedItem();
				String selected_type_to = (String)type_to.getSelectedItem();
				if(selected_type_from.equals(selected_type_to)){
					result=1*result;
				}
				else{
//					Log.v(TAG, "input not same output");
					double cubic_inch=0;
					HashMap<String, Double> table = new HashMap<String, Double>();
					for(int i=0;i<type_from.getCount();i++){
						String check = (String)type_from.getItemAtPosition(i);
						if (check.equalsIgnoreCase("barrel(oil)")){
							table.put(check, Double.valueOf(9702));
						}
						if (check.equalsIgnoreCase("bushel")){
							cubic_inch = (double)35239.072 * Math.pow((1/2.54), 3);
							table.put(check, Double.valueOf(cubic_inch));
						}
						if (check.equalsIgnoreCase("cubic centimeter")){
							cubic_inch = (double)Math.pow((1/2.54), 3);
							table.put(check, Double.valueOf(cubic_inch));
						}
						if (check.equalsIgnoreCase("cubic foot")){
							cubic_inch = (double)Math.pow(12, 3);
							table.put(check, Double.valueOf(cubic_inch));
						}
						if (check.equalsIgnoreCase("cubic inch")){
							table.put(check, Double.valueOf(1));
						}
						if (check.equalsIgnoreCase("cubic meter")){
							cubic_inch = (double)Math.pow((100/2.54), 3);
							table.put(check, Double.valueOf(cubic_inch));
						}
						if (check.equalsIgnoreCase("cubic yard")){
							cubic_inch = (double)Math.pow(36, 3);
							table.put(check, Double.valueOf(cubic_inch));
						}
						if (check.equalsIgnoreCase("ounce (US)")){
							cubic_inch = (double)3.785412/128 * Math.pow((10/2.54), 3);
							table.put(check, Double.valueOf(cubic_inch));
						}
						if (check.equalsIgnoreCase("gallon (US)")){
							cubic_inch = (double)3.785412 * Math.pow((10/2.54), 3);
							table.put(check, Double.valueOf(cubic_inch));
						}
						if (check.equalsIgnoreCase("liter")){
							cubic_inch = (double)Math.pow((10/2.54), 3);
							table.put(check, Double.valueOf(cubic_inch));
						}
						if (check.equalsIgnoreCase("pint (US)")){
							cubic_inch = (double)3.785412/8 * Math.pow((10/2.54), 3);
							table.put(check, Double.valueOf(cubic_inch));
						}
						if (check.equalsIgnoreCase("quart (US)")){
							cubic_inch = (double)3.785412/4 * Math.pow((10/2.54), 3);
							table.put(check, Double.valueOf(cubic_inch));
						}
						if (check.equalsIgnoreCase("table spoon")){
							table.put(check, Double.valueOf(0.902344));
						}
						if (check.equalsIgnoreCase("tea spoon")){
							table.put(check, Double.valueOf(0.300781));
						}
					}
					double from = table.get(selected_type_from);
					double to = table.get(selected_type_to);
					result = (double)result*from/to;
				}
				DecimalFormat frmt = new DecimalFormat("#,###,###,###,###.######");
				String formatted = frmt.format(result);
				volume_to.setText(formatted);
			}
		}
		catch(NumberFormatException nfe){
			volume_to.setText("");
			CharSequence error = "Number Format Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch (ArithmeticException ae){
			volume_to.setText("");
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