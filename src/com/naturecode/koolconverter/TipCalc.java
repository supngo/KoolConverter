package com.naturecode.koolconverter;

import java.text.DecimalFormat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.naturecode.koolconverter.util.MathUtil;

public class TipCalc extends SherlockActivity {
	private EditText txtbillamount;
	private EditText txtpeople;
	private EditText txtpercentage;
	private TextView txtperperson;
	private TextView txttipamount;
	private TextView txttotal;
	private EditText txttax;
	
	private Button btn_tip_up;
	private Button btn_tip_dn;
	private Button btn_split_up;
	private Button btn_split_dn;
//	private CheckBox tax;
	Spinner tax_type;
//	private RadioButton tax_dollar;
//	private RadioButton tax_percent;
	
	private double billamount = 0;
	private int percentage = 0;
	private int numofpeople=0;
	private double tax_amount=0;
	private double tipamount = 0;
	private double totaltopay = 0;
	private double perperson = 0;
	private static final String TAG = "TipCalc";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.tip_calc_1080);
		
//		int width = ((GlobalVariables) this.getApplication()).getScreen_width();
//		if(width<800){
//			setContentView(R.layout.tip_calc_720);
//		}
//		else if (width>=800 && width<1080){
//			setContentView(R.layout.tip_calc_800);
//		}
//		else{
//			setContentView(R.layout.tip_calc_1080);
//		}
		
//		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
//		actionBar.setTitle(R.string.tip_calc);
//		actionBar.setHomeAction(new IntentAction(this, MainCalc.createIntent(this), R.drawable.ic_launcher));
//		actionBar.setDisplayHomeAsUpEnabled(true);
		
		initControls();
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
	
	private void initControls(){
		txtbillamount = (EditText)findViewById(R.id.tip_bill);
		txtpercentage = (EditText)findViewById(R.id.tip_tip);
		txtpeople = (EditText)findViewById(R.id.tip_split);
		txttax = (EditText)findViewById(R.id.tip_tax);
		
		txtperperson=(TextView)findViewById(R.id.tip_person_field);
		txttipamount=(TextView)findViewById(R.id.tip_tip_field);
		txttotal=(TextView)findViewById(R.id.tip_total_field);
		
		tax_type = (Spinner)findViewById(R.id.tax_type);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tax_type, R.layout.my_spinner_text);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tax_type.setAdapter(adapter);
//		tax_dollar = (RadioButton)findViewById(R.id.tax_dollar);
//		tax_percent= (RadioButton)findViewById(R.id.tax_percent);
		
		btn_tip_up = (Button)findViewById(R.id.tip_up);
		btn_tip_up.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ changeValue("tip", "up"); }});
		btn_tip_dn = (Button)findViewById(R.id.tip_dn);
		btn_tip_dn.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ changeValue("tip", "down"); }});
		btn_split_up = (Button)findViewById(R.id.tip_slit_up);
		btn_split_up.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ changeValue("split", "up"); }});
		btn_split_dn = (Button)findViewById(R.id.tip_split_dn);
		btn_split_dn.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ changeValue("split", "down"); }});
		
		tax_type.setOnItemSelectedListener(new OnItemSelectedListener() {
	    	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    		calculate(); 
	    	}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
	    });
		
//		tax_percent.setOnClickListener(new Button.OnClickListener(){
//			public void onClick (View v){ 
//			if(tax_percent.isChecked()){
//				calculate(); 
//			}
//		}});
//		
//		tax_dollar.setOnClickListener(new Button.OnClickListener(){
//			public void onClick (View v){ 
//			if(tax_dollar.isChecked()){
//				calculate(); 
//			}
//		}});
		
		txtbillamount.addTextChangedListener(new MyTextWatcher());
		txtpercentage.addTextChangedListener(new MyTextWatcher());
		txtpeople.addTextChangedListener(new MyTextWatcher());
		txttax.addTextChangedListener(new MyTextWatcher());
	}
	
	private void changeValue(String type, String direction){
		if (type.equalsIgnoreCase("tip")){
			percentage = Integer.parseInt(txtpercentage.getText().toString().trim().length()<1?"0":txtpercentage.getText().toString().trim());
			if (direction.equalsIgnoreCase("up")){
				if(percentage<100){
					percentage++;
					txtpercentage.setText(""+percentage);
				}
			}
			else{
				if(percentage>0){
					percentage--;
					txtpercentage.setText(""+percentage);
				}
			}
		}
		else{
			numofpeople = Integer.parseInt(txtpeople.getText().toString().trim().length()<1?"0":txtpeople.getText().toString().trim());
			if (direction.equalsIgnoreCase("up")){
				if(numofpeople<100){
					numofpeople++;
					txtpeople.setText(""+numofpeople);
				}
			}
			else{
				if(numofpeople>1){
					numofpeople--;
					txtpeople.setText(""+numofpeople);
				}
			}
		}
		if(txtbillamount.getText().toString().trim().length()==0){
			txttipamount.setText("0");
			txttotal.setText("0");
			txtperperson.setText("0");
		}
	}

	private void calculate(){
		try{
			billamount = Double.parseDouble(txtbillamount.getText().toString().trim().length()<1?"0":txtbillamount.getText().toString().trim());
			percentage = Integer.parseInt(txtpercentage.getText().toString().trim().length()<1?"0":txtpercentage.getText().toString().trim());
			numofpeople = Integer.parseInt(txtpeople.getText().toString().trim().length()<1?"0":txtpeople.getText().toString().trim());
			tax_amount = Double.parseDouble(txttax.getText().toString().trim().length()<1?"0":txttax.getText().toString().trim());
//			Log.v(TAG, "tax1: "+tax_amount);
			if(billamount==0){
				CharSequence error = "Bill amount must be greater than 0";
				Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
				toast.show();
				txttipamount.setText("0");
				txttotal.setText("0");
				txtperperson.setText("0");
			}
			
			else if(numofpeople==0){
				CharSequence error = "Split by must be greater than 0";
				Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
				toast.show();
			}
			else{
//				if(!tax_dollar.isChecked()){
//					tax_amount = (billamount*tax_amount)/100;
//				}
				if(tax_type.getSelectedItem().toString().equalsIgnoreCase("%")){
					tax_amount = (billamount*tax_amount)/100;
//					Log.v(TAG, "tax1: "+tax_amount);
				}
//				Log.v(TAG, "tax2: "+tax_amount);
				tipamount = (billamount*percentage)/100;
				totaltopay = billamount+tipamount+tax_amount;
				perperson = totaltopay/numofpeople;
//				Log.v(TAG, "total: "+totaltopay);
				
				DecimalFormat frmt = new DecimalFormat("#,###,###,###,###.##");
				String formatted="";
			
				formatted = frmt.format(MathUtil.getDRound(tipamount, 2));
				txttipamount.setText(formatted);
				formatted = frmt.format(MathUtil.getDRound(totaltopay, 2));
				txttotal.setText(formatted);
				formatted = frmt.format(MathUtil.getDRound(perperson, 2));
				txtperperson.setText(formatted);
			}
		}
		catch(NumberFormatException nfe){
			txttipamount.setText("0");
			txttotal.setText("0");
			txtperperson.setText("0");
			CharSequence error = "Number Format Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch (ArithmeticException ae){
			txttipamount.setText("0");
			txttotal.setText("0");
			txtperperson.setText("0");
			CharSequence error = "Arithmetic Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch(Exception e){
			txttipamount.setText("0");
			txttotal.setText("0");
			txtperperson.setText("0");
			CharSequence error = "Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	
	private class MyTextWatcher implements TextWatcher{
		public void afterTextChanged(Editable s) {
			calculate(); 
		}
		public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		public void onTextChanged(CharSequence s, int start, int before, int count){}
	}
}