package com.naturecode.koolconverter;

import java.text.DecimalFormat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.naturecode.koolconverter.util.MathUtil;

public class ShoppingDiscountCalc extends SherlockActivity {
	private EditText ori_price_txt;
	private EditText discount_txt;
	private EditText tax_txt;
	
	private TextView dis;
	private TextView dis_price;
	private TextView tax_price;
	private TextView total;

	private Button btn_discount_up;
	private Button btn_discount_dn;
	private Button btn_tax_up;
	private Button btn_tax_dn;
	
	private double ori_price = 0;
	private int discount = 0;
	private double tax=0;

	private double discount_amt = 0;
	private double tax_amt=0;
	private double sale_amt=0;
	private double total_amt=0;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.shopping_calc_1080);
//		int width = ((GlobalVariables) this.getApplication()).getScreen_width();
//		if(width==720){
//			setContentView(R.layout.shopping_calc_720);
//		}
//		else if (width>=800){
//			setContentView(R.layout.shopping_calc_800);
//		}
//		else
//			setContentView(R.layout.shopping_calc_720);
		
//		if(width<800){
//			setContentView(R.layout.shopping_calc_720);
//		}
//		else if (width>=800 && width<1080){
//			setContentView(R.layout.shopping_calc_800);
//		}
//		else{
//			setContentView(R.layout.shopping_calc_1080);
//		}
		
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
		ori_price_txt = (EditText)findViewById(R.id.shopping_ori_price);
		discount_txt = (EditText)findViewById(R.id.shopping_discount);
		tax_txt = (EditText)findViewById(R.id.shopping_tax);
		
		dis=(TextView)findViewById(R.id.shopping_dis_field);
		dis_price=(TextView)findViewById(R.id.shopping_dis_price_field);
		total=(TextView)findViewById(R.id.shopping_total_price_field);
		tax_price=(TextView)findViewById(R.id.shopping_tax_price_field);
		
		btn_discount_up = (Button)findViewById(R.id.shopping_discount_up);
		btn_discount_up.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ changeValue("discount", "up"); }});
		btn_discount_dn = (Button)findViewById(R.id.shopping_discount_dn);
		btn_discount_dn.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ changeValue("discount", "down"); }});
		btn_tax_up = (Button)findViewById(R.id.shopping_tax_up);
		btn_tax_up.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ changeValue("tax", "up"); }});
		btn_tax_dn = (Button)findViewById(R.id.shopping_tax_dn);
		btn_tax_dn.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ changeValue("tax", "down"); }});
		
		ori_price_txt.addTextChangedListener(new MyTextWatcher());
		discount_txt.addTextChangedListener(new MyTextWatcher());
		tax_txt.addTextChangedListener(new MyTextWatcher());
	}
	
	private void changeValue(String type, String direction){
		if (type.equalsIgnoreCase("discount")){
			discount = Integer.parseInt(discount_txt.getText().toString().trim().length()<1?"0":discount_txt.getText().toString().trim());
			if (direction.equalsIgnoreCase("up")){
				if(discount<100){
					discount++;
					discount_txt.setText(""+discount);
				}
			}
			else{
				if(discount>0){
					discount--;
					discount_txt.setText(""+discount);
				}
			}
		}
		else{
			tax = Double.parseDouble(tax_txt.getText().toString().trim().length()<1?"0":tax_txt.getText().toString().trim());
			if (direction.equalsIgnoreCase("up")){
				if(tax<100){
					tax = tax +0.25;
					tax_txt.setText(""+tax);
				}
			}
			else{
				if(tax>0.25){
					tax = tax - 0.25;
					tax_txt.setText(""+tax);
				}
			}
		}
		if(ori_price_txt.getText().toString().trim().length()==0){
			dis.setText("0");
			dis_price.setText("0");
			tax_price.setText("0");
			total.setText("0");
		}
	}

	private void calculate(){
		try{
			ori_price = Double.parseDouble(ori_price_txt.getText().toString().trim().length()<1?"0":ori_price_txt.getText().toString().trim());
			discount = Integer.parseInt(discount_txt.getText().toString().trim().length()<1?"0":discount_txt.getText().toString().trim());
			tax = Double.parseDouble(tax_txt.getText().toString().trim().length()<1?"0":tax_txt.getText().toString().trim());

			if(ori_price==0){
				CharSequence error = "Original Price must be greater than 0";
				Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
				toast.show();
				dis.setText("0");
				dis_price.setText("0");
				tax_price.setText("0");
				total.setText("0");
			}
			else{
				discount_amt =  ori_price*discount/100;
				sale_amt = ori_price-discount_amt;
				tax_amt = sale_amt * tax/100;
				total_amt = sale_amt + tax_amt;
				
				DecimalFormat frmt = new DecimalFormat("#,###,###,###,###.##");
				String formatted="";
				
				formatted = frmt.format(MathUtil.getDRound(discount_amt, 2));
				dis.setText(formatted);
				
				formatted = frmt.format(MathUtil.getDRound(sale_amt, 2));
				dis_price.setText(formatted);
				
				formatted = frmt.format(MathUtil.getDRound(tax_amt, 2));
				tax_price.setText(formatted);
				
				formatted = frmt.format(MathUtil.getDRound(total_amt, 2));
				total.setText(formatted);
			}
		}
		catch(NumberFormatException nfe){
			dis.setText("0");
			dis_price.setText("0");
			tax_price.setText("0");
			total.setText("0");
			CharSequence error = "Number Format Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch (ArithmeticException ae){
			dis.setText("0");
			dis_price.setText("0");
			tax_price.setText("0");
			total.setText("0");
			CharSequence error = "Arithmetic Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch(Exception e){
			dis.setText("0");
			dis_price.setText("0");
			tax_price.setText("0");
			total.setText("0");
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