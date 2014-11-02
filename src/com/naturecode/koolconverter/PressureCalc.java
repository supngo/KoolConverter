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

public class PressureCalc extends SherlockActivity {
	private EditText input;
	private EditText output;
	private Spinner type_from;
	private Spinner type_to;
	private double result = 0;	

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
		input = (EditText)findViewById(R.id.unit_from);
		output = (EditText)findViewById(R.id.unit_to);
		
		type_from = (Spinner)findViewById(R.id.unit_spinner1);
		type_to = (Spinner)findViewById(R.id.unit_spinner2);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.unit_pressure, R.layout.my_spinner_text);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    type_from.setAdapter(adapter);
	    type_from.setSelection(12);
	    type_to.setAdapter(adapter);
	    type_to.setSelection(5);   
	    
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
	    
	    input.addTextChangedListener(new TextWatcher(){ 
	    	 public void afterTextChanged(Editable s) {
	    		calculate(); 
	    	 }
	    	 public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	    	 public void onTextChanged(CharSequence s, int start, int before, int count){}
	    });
	}
	
	private void calculate(){
		try{
			result = Double.parseDouble(input.getText().toString().trim().length()<1?"0":input.getText().toString().trim());
			if(result==0){
				output.setText("");
			}
			else{
				String selected_type_from = (String)type_from.getSelectedItem();
				String selected_type_to = (String)type_to.getSelectedItem();
				if(!selected_type_from.equals(selected_type_to)){
					double psi=0;
					HashMap<String, Double> table = new HashMap<String, Double>();
					for(int i=0;i<type_from.getCount();i++){
						String check = (String)type_from.getItemAtPosition(i);
						if (check.equalsIgnoreCase("bar")){
							psi = (double) 100000/(0.453592 * 9.80665) * 1/(Math.pow((100/2.54), 2));
							table.put(check, Double.valueOf(psi));
						}
						if (check.equalsIgnoreCase("centimeter mercury")){
							psi = (double)0.491154/2.54;
							table.put(check, Double.valueOf(psi));
						}
						if (check.equalsIgnoreCase("centimeter water")){
							psi = (double)0.036127/2.54;
							table.put(check, Double.valueOf(psi));
						}
						if (check.equalsIgnoreCase("inch of mercury")){
							table.put(check, Double.valueOf(0.491154));
						}
						if (check.equalsIgnoreCase("inch of water")){
							table.put(check, Double.valueOf(0.036127));
						}
						if (check.equalsIgnoreCase("kilogram-force/square centimeter")){
							psi = (double)1/0.453592   * 1/(Math.pow((1/2.54), 2));
							table.put(check, Double.valueOf(psi));
						}
						if (check.equalsIgnoreCase("kilogram-force/square meter")){
							psi = (double)1/0.453592  * 1/(Math.pow((100/2.54), 2));
							table.put(check, Double.valueOf(psi));
						}
						if (check.equalsIgnoreCase("kilonewton/square meter")){
							psi = (double) 1000/(0.453592 * 9.80665) * 1/(Math.pow((100/2.54), 2));
							table.put(check, Double.valueOf(psi));
						}
						if (check.equalsIgnoreCase("kilopascal")){
							psi = (double) 1000/(0.453592 * 9.80665) * 1/(Math.pow((100/2.54), 2));
							table.put(check, Double.valueOf(psi));
						}
						if (check.equalsIgnoreCase("newton/square meter")){
							psi = (double) 1/(0.453592 * 9.80665) * 1/(Math.pow((100/2.54), 2));
							table.put(check, Double.valueOf(psi));
						}
						if (check.equalsIgnoreCase("pascal")){
							psi = (double) 1/(0.453592 * 9.80665) * 1/(Math.pow((100/2.54), 2));
							table.put(check, Double.valueOf(psi));
						}
						if (check.equalsIgnoreCase("pound-force/square foot")){
							psi = (double)1/144;
							table.put(check, Double.valueOf(psi));
						}
						if (check.equalsIgnoreCase("pound-force/square inch (psi)")){
							table.put(check, Double.valueOf(1));
						}
						if (check.equalsIgnoreCase("ton-force/square foot (US)")){
							psi = (double)2000/144;
							table.put(check, Double.valueOf(psi));
						}
						if (check.equalsIgnoreCase("ton-force/square inch (US)")){
							table.put(check, Double.valueOf(2000));
						}
					}
					double from = table.get(selected_type_from);
					double to = table.get(selected_type_to);
					result = (double)result*from/to;
				}
				DecimalFormat frmt = new DecimalFormat("#,###,###,###,###.########");
				String formatted = frmt.format(result);
				output.setText(formatted);
			}
		}
		catch(NumberFormatException nfe){
			output.setText("");
			CharSequence error = "Number Format Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch (ArithmeticException ae){
			output.setText("");
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