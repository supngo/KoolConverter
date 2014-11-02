package com.naturecode.koolconverter;

import java.text.DecimalFormat;

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

public class TemperatureCalc extends SherlockActivity {
	private EditText temperature_from;
	private EditText temperature_to;
	private Spinner type_from;
	private Spinner type_to;
	private double result = 0;	
//	private static final String TAG = "test";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.temp_calc_800);
//		int width = ((GlobalVariables) this.getApplication()).getScreen_width();
//		if (width>=800 && width<1080){
//			setContentView(R.layout.temp_calc_800);
//		}
//		else
//			setContentView(R.layout.temp_calc_720);
		
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
		temperature_from = (EditText)findViewById(R.id.unit_from);
		temperature_to = (EditText)findViewById(R.id.unit_to);
		
		type_from = (Spinner)findViewById(R.id.unit_spinner1);
		type_to = (Spinner)findViewById(R.id.unit_spinner2);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.unit_temperature, R.layout.my_spinner_text);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    type_from.setAdapter(adapter);
	    type_from.setSelection(1);
	    type_to.setAdapter(adapter);
	    type_to.setSelection(0);
	    
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
	    
	    temperature_from.addTextChangedListener(new TextWatcher(){ 
	    	 public void afterTextChanged(Editable s) {
	    		calculate(); 
	    	 }
	    	 public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	    	 public void onTextChanged(CharSequence s, int start, int before, int count){}
	    });
	}
	
	private void calculate(){
		try{
			if(temperature_from.getText().toString().trim().length()<1){
				temperature_to.setText("");
			}
			else{
				result = Double.parseDouble(temperature_from.getText().toString().trim());
				String selected_type_from = (String)type_from.getSelectedItem();
				String selected_type_to = (String)type_to.getSelectedItem();
				if(selected_type_from.equals(selected_type_to)){
					result=1*result;
				}
				else{
					if(selected_type_from.equalsIgnoreCase("Celsius")){
						if(selected_type_to.equalsIgnoreCase("Fahrenheit")){
							result = (double)result *(9.0/5.0)+32;
						}
						else{
							result = (double)result + 273.15;
						}
					}
					else if(selected_type_from.equalsIgnoreCase("Fahrenheit")){
						if(selected_type_to.equalsIgnoreCase("Celsius")){
							result = (double)(result-32)*(5.0/9.0);
						}
						else{
							result = (double)(result-32)*(5.0/9.0)+273.15;
						}
					}
					else{
						if(selected_type_to.equalsIgnoreCase("Celsius")){
							result = (double)result - 273.15;
						}
						else{
							result = (double)(result-273.15)*(9.0/5.0)+32;
						}
					}
				}
				DecimalFormat frmt = new DecimalFormat("#,###,###,###,###.######");
				String formatted = frmt.format(result);
				temperature_to.setText(formatted);
			}
		}
		catch(NumberFormatException nfe){
			temperature_to.setText("");
			CharSequence error = "Number Format Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch (ArithmeticException ae){
			temperature_to.setText("");
			CharSequence error = "Arithmetic Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();

		}
		catch(Exception e){
//			e.printStackTrace();
			CharSequence error = "Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
}