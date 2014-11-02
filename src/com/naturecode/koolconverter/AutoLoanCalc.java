package com.naturecode.koolconverter;

import java.text.DecimalFormat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.naturecode.koolconverter.util.MathUtil;

public class AutoLoanCalc extends SherlockActivity {
	private EditText loan_amount_txt;
	private EditText trade_in_txt;
	private EditText rate_txt;
	private EditText duration_txt;
	private EditText put_down_txt;
	private EditText tax_txt;
//	private EditText monthly_payment_txt;
//	private EditText total_interest_txt;
//	private EditText total_payment_txt;
	
	private TextView monthly_payment_txt;
	private TextView total_interest_txt;
	private TextView total_payment_txt;
	
	private Button btncalculate;
	private RadioButton year;
	
	private double loan_amount = 0;
	private double trade_in = 0;
	private double rate = 0;
	private double duration = 0;
	private double put_down = 0;
	private double tax = 0;
	private double monthly_payment = 0;
	private double total_interest = 0;
	private double total_payment = 0;
	private static final String TAG = "AutoLoanCalc";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		int width = ((GlobalVariables) this.getApplication()).getScreen_width();
		Log.v(TAG, "width: "+width);
		setContentView(R.layout.auto_calc_1080);
//		if(width<800){
//			Log.v(TAG, "got 720");
//			setContentView(R.layout.auto_calc_720);
//		}
//		else if (width>=800 && width<1080){
//			Log.v(TAG, "got 800");
//			setContentView(R.layout.auto_calc_800);
//		}
//		else{
//			Log.v(TAG, "got 1080");
//			setContentView(R.layout.auto_calc_1080);
//		}
		
//		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
//		actionBar.setTitle(R.string.autoloan_calc);
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
		loan_amount_txt = (EditText)findViewById(R.id.auto_loan);
		trade_in_txt = (EditText)findViewById(R.id.auto_tradein);
		rate_txt = (EditText)findViewById(R.id.auto_rate);
		tax_txt = (EditText)findViewById(R.id.auto_tax);
		
		put_down_txt = (EditText)findViewById(R.id.auto_putdown);
		duration_txt = (EditText)findViewById(R.id.auto_duration);
		
		monthly_payment_txt = (TextView)findViewById(R.id.auto_monthly);
		total_interest_txt = (TextView)findViewById(R.id.auto_interest);
		total_payment_txt = (TextView)findViewById(R.id.auto_total);
		
		btncalculate = (Button)findViewById(R.id.auto_calc);
		btncalculate.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ calculate(); }});	
		
		year=(RadioButton)findViewById(R.id.auto_year);
		loan_amount_txt.setFocusable(true);
	}

	private void calculate(){
		try{
			loan_amount = Double.parseDouble(loan_amount_txt.getText().toString().trim().length()<1?"0":loan_amount_txt.getText().toString().trim());
			trade_in = Double.parseDouble(trade_in_txt.getText().toString().trim().length()<1?"0":trade_in_txt.getText().toString().trim());
			rate = Double.parseDouble(rate_txt.getText().toString().trim().length()<1?"0":rate_txt.getText().toString().trim());
			put_down = Double.parseDouble(put_down_txt.getText().toString().trim().length()<1?"0":put_down_txt.getText().toString().trim());
			tax = Double.parseDouble(tax_txt.getText().toString().trim().length()<1?"0":tax_txt.getText().toString().trim());
			duration = Double.parseDouble(duration_txt.getText().toString().trim().length()<1?"0":duration_txt.getText().toString().trim());

			if(year.isChecked() == true){
				duration = duration * 12;
			}
			
			if(loan_amount==0){
				CharSequence error = "Loan amount must be greater than 0";
				Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
				toast.show();
				monthly_payment_txt.setText("0");
				total_interest_txt.setText("0");
				total_payment_txt.setText("0");
			}
			else if(duration==0){
				CharSequence error = "Duration must be greater than 0";
				Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
				toast.show();
				monthly_payment_txt.setText("0");
				total_interest_txt.setText("0");
				total_payment_txt.setText("0");
			}
			else{
				double principle = loan_amount*tax/100 + loan_amount;
				principle = principle-trade_in-put_down;
				if(rate==0){
					monthly_payment = principle/duration;
					total_payment = principle;
					total_interest = 0;
				}
				else{
					monthly_payment = (double)((principle)*(rate/1200))/(1-Math.pow((1+rate/1200), -1*(duration)));
					total_payment = (double)MathUtil.getDRound(monthly_payment, 2) * duration;
					total_interest = (double)total_payment - principle;
				}		
				
				DecimalFormat frmt = new DecimalFormat("#,###,###,###,###.##");
				String formatted="";
				
				formatted = frmt.format(MathUtil.getDRound(total_interest, 2));
				total_interest_txt.setText(formatted);
						
				formatted = frmt.format(MathUtil.getDRound(total_payment, 2));
				total_payment_txt.setText(formatted);
						
				formatted = frmt.format(MathUtil.getDRound(monthly_payment, 2));
				monthly_payment_txt.setText(formatted);
				
				
//				total_interest_txt.setText(Double.toString(MathUtil.getDRound(total_interest, 2)));
//				total_payment_txt.setText(Double.toString(MathUtil.getDRound(total_payment, 2)));
//				monthly_payment_txt.setText(Double.toString(MathUtil.getDRound(monthly_payment, 2)));
			}
		}
		catch(NumberFormatException nfe){
			monthly_payment_txt.setText("0");
			total_interest_txt.setText("0");
			total_payment_txt.setText("0");
			CharSequence error = "Number Format Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch (ArithmeticException ae){
			monthly_payment_txt.setText("0");
			total_interest_txt.setText("0");
			total_payment_txt.setText("0");
			CharSequence error = "Arithmetic Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch(Exception e){
			e.printStackTrace();
			monthly_payment_txt.setText("0");
			total_interest_txt.setText("0");
			total_payment_txt.setText("0");
			CharSequence error = "Error";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
}