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

public class LengthCalc extends SherlockActivity {
	private EditText length_from;
	private EditText length_to;
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
		length_from = (EditText)findViewById(R.id.unit_from);
		length_to = (EditText)findViewById(R.id.unit_to);
		
		type_from = (Spinner)findViewById(R.id.unit_spinner1);
		type_to = (Spinner)findViewById(R.id.unit_spinner2);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.unit_length, R.layout.my_spinner_text);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    type_from.setAdapter(adapter);
	    type_from.setSelection(7);
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
	    
	    length_from.addTextChangedListener(new TextWatcher(){ 
	    	 public void afterTextChanged(Editable s) {
	    		calculate(); 
	    	 }
	    	 public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	    	 public void onTextChanged(CharSequence s, int start, int before, int count){}
	    });
	}
	
	private void calculate(){
		try{
			result = Double.parseDouble(length_from.getText().toString().trim().length()<1?"0":length_from.getText().toString().trim());
			if(result==0){
//				CharSequence error = "Input must be greater than 0";
//				Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
//				toast.show();
				length_to.setText("");
			}
			else{
				String selected_type_from = (String)type_from.getSelectedItem();
				String selected_type_to = (String)type_to.getSelectedItem();
				if(selected_type_from.equals(selected_type_to)){
					result=1*result;
				}
				else{
					double inch=0;
					HashMap<String, Double> table = new HashMap<String, Double>();
					for(int i=0;i<type_from.getCount();i++){
						String check = (String)type_from.getItemAtPosition(i);
						if (check.equalsIgnoreCase("millimeter")){
							inch = (double)0.1/2.54;
							table.put(check, Double.valueOf(inch));
						}
						if (check.equalsIgnoreCase("centimeter")){
							inch = (double)1/2.54;
							table.put(check, Double.valueOf(inch));
						}
						if (check.equalsIgnoreCase("decimeter")){
							inch = (double)10/2.54;
							table.put(check, Double.valueOf(inch));
						}
						if (check.equalsIgnoreCase("fathom")){
							table.put(check, Double.valueOf(72));
						}
						if (check.equalsIgnoreCase("foot")){
							table.put(check, Double.valueOf(12));
						}
						if (check.equalsIgnoreCase("inch")){
							table.put(check, Double.valueOf(1));
						}
						if (check.equalsIgnoreCase("kilometer")){
							inch = (double)100000/2.54;
							table.put(check, Double.valueOf(inch));
						}
						if (check.equalsIgnoreCase("meter")){
							inch = (double)100/2.54;
							table.put(check, Double.valueOf(inch));
						}
						if (check.equalsIgnoreCase("mile")){
							table.put(check, Double.valueOf(63360));
						}
						if (check.equalsIgnoreCase("league")){
							inch = (double)5.55600 * 100000/2.54;
							table.put(check, Double.valueOf(inch));
						}
						if (check.equalsIgnoreCase("yard")){
							table.put(check, Double.valueOf(36));
						}
					}
					double from = table.get(selected_type_from);
					double to = table.get(selected_type_to);
					result = (double)result*from/to;
				}
				DecimalFormat frmt = new DecimalFormat("#,###,###,###,###.######");
				String formatted = frmt.format(result);
				length_to.setText(formatted);
			}
		}
		catch(NumberFormatException nfe){
			length_to.setText("");
			CharSequence error = "Number Format Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch (ArithmeticException ae){
			length_to.setText("");
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