package com.naturecode.koolconverter;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.naturecode.koolconverter.util.MathUtil;

public class Mortgage_MonPay_Fragment extends SherlockFragment{
//public class Mortgage_MonPay_Fragment extends Fragment{
	private EditText loan_amount_txt;
	private EditText rate_txt;
	private EditText duration_txt;
	private TextView monthly_payment_txt;
	private EditText putDown_txt;
	private Button btncalculate;
	private RadioButton mortgage_year;
	
	private double loan_amount = 0;
	private double rate = 0;
	private double duration = 0;
	private double putDown = 0;
	private double monthlyPayment = 0;
//	private static final String TAG = "debug";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view;
		view = inflater.inflate(R.layout.mortgage_monthly_payment_1080, container, false);
//    	int width = ((GlobalVariables) getActivity().getApplication()).getScreen_width();
//    	if (width<800 ){
//    		view = inflater.inflate(R.layout.mortgage_monthly_payment_720, container, false);
//    	}
//    	else if (width>=800 && width<1080){
//			view = inflater.inflate(R.layout.mortgage_monthly_payment_800, container, false);
//		}
//		else
//			view = inflater.inflate(R.layout.mortgage_monthly_payment_1080, container, false);
		
		loan_amount_txt = (EditText)view.findViewById(R.id.mortgage_loan);
		rate_txt = (EditText)view.findViewById(R.id.mortgage_rate);
		duration_txt = (EditText)view.findViewById(R.id.mortgage_duration);	
		putDown_txt = (EditText)view.findViewById(R.id.mortgage_putdown);	
		monthly_payment_txt = (TextView)view.findViewById(R.id.mortgage_monpay);
		mortgage_year=(RadioButton)view.findViewById(R.id.mortgage_year);
		btncalculate = (Button)view.findViewById(R.id.mortgage_calc);
		btncalculate.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ calculate(); }});	
		
		return view;
	}
	
	private void calculate(){
		try{
			loan_amount = Double.parseDouble(loan_amount_txt.getText().toString().trim().length()<1?"0":loan_amount_txt.getText().toString().trim());
			rate = Double.parseDouble(rate_txt.getText().toString().trim().length()<1?"0":rate_txt.getText().toString().trim());
			duration = Double.parseDouble(duration_txt.getText().toString().trim().length()<1?"0":duration_txt.getText().toString().trim());
			putDown = Double.parseDouble(putDown_txt.getText().toString().trim().length()<1?"0":putDown_txt.getText().toString().trim());

			if(mortgage_year.isChecked() == true){
				duration = duration * 12;
			}
			
			if(loan_amount==0){
				CharSequence error = "Loan amount must be greater than 0";
				Toast toast = Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
				toast.show();
				monthly_payment_txt.setText("0");
			}
			else if(duration==0){
				CharSequence error = "Duration must be greater than 0";
				Toast toast = Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
				toast.show();
				monthly_payment_txt.setText("0");
			}
			else{
				loan_amount = loan_amount - putDown;
				rate = (double) (rate/1200);	
				double rate_plus_1 = (double) (rate+1);
				monthlyPayment = (double) ( (loan_amount) * rate * Math.pow(rate_plus_1, duration) / (Math.pow(rate_plus_1, duration)-1) );
				
				DecimalFormat frmt = new DecimalFormat("#,###,###,###,###.##");
				String formatted = frmt.format(MathUtil.getDRound(monthlyPayment, 2));
				monthly_payment_txt.setText(formatted);
			}
		}
		catch(NumberFormatException nfe){
			monthly_payment_txt.setText("0");
			CharSequence error = "Number Format Error";
			Toast toast = Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch (ArithmeticException ae){
			monthly_payment_txt.setText("0");
			CharSequence error = "Arithmetic Error";
			Toast toast = Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		catch(Exception e){
			monthly_payment_txt.setText("0");
			CharSequence error = "Error";
			Toast toast = Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
}