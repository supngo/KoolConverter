package com.naturecode.koolconverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DecimalFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.naturecode.koolconverter.util.MathUtil;

public class CurrencyCalc extends SherlockActivity {
	private EditText curreny_from;
	private EditText curreny_to;
	private TextView curreny_from_txt;
	private TextView curreny_to_txt;
	
	private TextView curreny_detail_1;
	private TextView curreny_detail_2;
	private TextView curreny_detail_3;
	
	private ImageView flag_from;
	private ImageView flag_to;
	
	private double curreny_from_amt = 0;
	private double curreny_to_amt = 0;
	private Button btncalculate;
	private Button currency_from_button;
	private Button currency_to_button;
	private Button currency_switch;
	private static final String TAG = "CurrencyCalc";
	
//	static final String SOAP_ACTION="http://www.webserviceX.NET/ConversionRate";
//	static final String METHOD_NAME="ConversionRate";
//	static final String NAME_SPACE="http://www.webserviceX.NET/";
//	static final String URL="http://www.webservicex.net/currencyconvertor.asmx";
	
	private Integer[] imgid = {R.drawable.usd, R.drawable.eur, R.drawable.gbp, R.drawable.aud, R.drawable.cad, 
		R.drawable.cny, R.drawable.hkd, R.drawable.inr, R.drawable.jpy, R.drawable.krw, R.drawable.nzd, 
		R.drawable.rub, R.drawable.sgd, R.drawable.afn, R.drawable.all, R.drawable.dzd, R.drawable.ars, 
		R.drawable.bsd, R.drawable.bhd, R.drawable.bdt, R.drawable.bob, R.drawable.brl, R.drawable.bnd,
		R.drawable.khr, R.drawable.clp, R.drawable.cop, R.drawable.crc, R.drawable.hrk, R.drawable.cup, 
		R.drawable.czk, R.drawable.dkk, R.drawable.dop, R.drawable.egp, R.drawable.svc,  
		R.drawable.etb, R.drawable.gel, R.drawable.gtq, R.drawable.gnf, R.drawable.htg, R.drawable.hnl, 
		R.drawable.huf, R.drawable.isk, R.drawable.idr, R.drawable.irr, R.drawable.iqd, R.drawable.ils,
		R.drawable.jmd, R.drawable.jod, R.drawable.kes, R.drawable.kwd, R.drawable.lak, R.drawable.lvl, R.drawable.lbp,
		R.drawable.ltl, R.drawable.mop, R.drawable.mkd, R.drawable.mwk, R.drawable.myr, R.drawable.mxn, 
		R.drawable.mnt, R.drawable.mad, R.drawable.mzm, R.drawable.mmk, R.drawable.nad, R.drawable.npr,
		R.drawable.nio, R.drawable.ngn, R.drawable.nok, R.drawable.pkr, R.drawable.pab, R.drawable.pyg,
		R.drawable.pen, R.drawable.php, R.drawable.pln, R.drawable.qar, 
		R.drawable.rol, R.drawable.sar, R.drawable.rsd, R.drawable.skk,
		R.drawable.sit, R.drawable.zar, R.drawable.lkr, R.drawable.sdd, R.drawable.sek, R.drawable.chf,
		R.drawable.syp, R.drawable.twd, R.drawable.thb, R.drawable.trl, R.drawable.ugx, R.drawable.uah,
		R.drawable.uyu, R.drawable.veb, R.drawable.vnd, R.drawable.yer, R.drawable.zmk, R.drawable.zwd
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.currency_calc_1080);
//		int width = ((GlobalVariables) this.getApplication()).getScreen_width();
//		if(width<800){
//			setContentView(R.layout.currency_calc_720);
//		}
//		else if (width>=800 && width<1080){
//			setContentView(R.layout.currency_calc_800);
//		}
//		else
//			setContentView(R.layout.currency_calc_1080);
		
		initControls();
		Intent data = getIntent();
		getValues(data);
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
		curreny_from = (EditText)findViewById(R.id.currency_from);
		curreny_to = (EditText)findViewById(R.id.currency_to);
		curreny_from_txt = (TextView)findViewById(R.id.currency_from_txt);
		curreny_to_txt = (TextView)findViewById(R.id.currency_to_txt);
		curreny_detail_1 = (TextView)findViewById(R.id.currency_detail_1);
		curreny_detail_2 = (TextView)findViewById(R.id.currency_detail_2);
		curreny_detail_3 = (TextView)findViewById(R.id.currency_detail_3);
		flag_from = (ImageView)findViewById(R.id.currency_from_flag);
		flag_to = (ImageView)findViewById(R.id.currency_to_flag);

		btncalculate = (Button)findViewById(R.id.currency_sumbit);
		btncalculate.setOnClickListener(new Button.OnClickListener() { 
			public void onClick (View v){ 
				if(isOnline()) //Has network, call web service to get currency rate
					readWebpage(); 
				else{  //not network, trying to get currency rate from local db 
					CharSequence error = "No Network Available!";
					Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 10, 0);
					toast.show();
				}
			}
		});	
		currency_from_button = (Button)findViewById(R.id.currency_select1);
		currency_from_button.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ select_currency("from"); }});		
		currency_to_button = (Button)findViewById(R.id.currency_select2);
		currency_to_button.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ select_currency("to"); }});
		currency_switch = (Button)findViewById(R.id.currency_switch);
		currency_switch.setOnClickListener(new Button.OnClickListener() { public void onClick (View v){ switch_currency(); }});
	}
	
	public boolean isOnline(){
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] ni = cm.getAllNetworkInfo();
		boolean result = false;
		if(ni != null){
			for (int i = 0; i < ni.length; i++)
			if(ni[i].getState() == NetworkInfo.State.CONNECTED){
				result = true;
			}
		}
		return result;
	} 
	
	private void getValues(Intent data){
		if(data!=null){
			Bundle extras = data.getExtras();
			if (extras != null) {
				String selected_currency_code = extras.getString("selected_currency_code");
				String selected_currency_desc = extras.getString("selected_currency_desc");
				int selected_flag = Integer.parseInt(extras.getString("selected_flag"));
				
//				Determine whether receiving Currency Code is FROM or TO *** 
				String from_to = ((GlobalVariables) this.getApplication()).getIsCurrency_from_to();
				if(from_to!=null && from_to.equalsIgnoreCase("from")){
//					Setting data from another Activity to populate FROM currency information ***
					currency_from_button.setText(selected_currency_code);
					curreny_from_txt.setText(selected_currency_desc);
					flag_from.setImageDrawable(getResources().getDrawable(imgid[selected_flag]));
					
//					Retrieving data from global variables to populate TO currency information ***
					String currency_to = ((GlobalVariables) this.getApplication()).getCurrency_to();
					String[] currency_desc_array = getResources().getStringArray(R.array.currency_desc);
					String[] currency_codes_array = getResources().getStringArray(R.array.currency_codes);
					String currency_to_desc="";
					int index=0;
					for(int i=0;i<currency_codes_array.length;i++){
						if (currency_to.equalsIgnoreCase(currency_codes_array[i])){
							currency_to_desc = currency_desc_array[i];
							index = i;
							break;
						}
					}
					String from_text = ((GlobalVariables) this.getApplication()).getCurrency_from_text();
					curreny_from.setText(from_text);
					currency_to_button.setText(currency_to);
					curreny_to_txt.setText(currency_to_desc);
					flag_to.setImageDrawable(getResources().getDrawable(imgid[index]));
					
				}
				else{
//					Setting data from another Activity to populate TO currency information ***
					currency_to_button.setText(selected_currency_code);
					curreny_to_txt.setText(selected_currency_desc);
					flag_to.setImageDrawable(getResources().getDrawable(imgid[selected_flag]));
					
//					Retrieving data from global variables to populate FROM currency information ***
					String currency_from = ((GlobalVariables) this.getApplication()).getCurrency_from();
					String[] currency_desc_array = getResources().getStringArray(R.array.currency_desc);
					String[] currency_codes_array = getResources().getStringArray(R.array.currency_codes);
					String currency_from_desc="";
					int index=0;
					for(int i=0;i<currency_codes_array.length;i++){
						if (currency_from.equalsIgnoreCase(currency_codes_array[i])){
							currency_from_desc = currency_desc_array[i];
							index = i;
							break;
						}
					}
					String from_text = ((GlobalVariables) this.getApplication()).getCurrency_from_text();
					curreny_from.setText(from_text);
					currency_from_button.setText(currency_from);
					curreny_from_txt.setText(currency_from_desc);
					flag_from.setImageDrawable(getResources().getDrawable(imgid[index]));
				}
				curreny_to.setText("");
				curreny_detail_1.setText("");
				curreny_detail_2.setText("");
				curreny_detail_3.setText("");
			}
		}
	}
	
	private void select_currency(String type){
//		Setting Global Variables for TO and FROM Currency Codes ***
	    ((GlobalVariables)getApplication()).setIsCurrency_from_to(type);
	    ((GlobalVariables)getApplication()).setCurrency_from(""+currency_from_button.getText());
	    ((GlobalVariables)getApplication()).setCurrency_to(""+currency_to_button.getText());
	    ((GlobalVariables)getApplication()).setCurrency_from_text(""+curreny_from.getText());
	    
		Intent myIntent = new Intent(CurrencyCalc.this, CurrencyType.class);
		startActivity(myIntent);
	}
	
	private void switch_currency(){
		String temp_currency_code="";
		String temp_currency_desc="";
		int flag_from_id=0;
		int flag_to_id=0;
		
		temp_currency_code = ""+currency_from_button.getText();
		temp_currency_desc = ""+curreny_from_txt.getText();
		
		currency_from_button.setText(currency_to_button.getText());
		curreny_from_txt.setText(curreny_to_txt.getText());	
		currency_to_button.setText(temp_currency_code);
		curreny_to_txt.setText(temp_currency_desc);

		String[] currency_codes_array = getResources().getStringArray(R.array.currency_codes);
		for(int i=0;i<currency_codes_array.length;i++){
			if (currency_from_button.getText().toString().equalsIgnoreCase(currency_codes_array[i])){
				flag_from_id = i;
			}
			if (currency_to_button.getText().toString().equalsIgnoreCase(currency_codes_array[i])){
				flag_to_id = i;
			}
		}
		
		flag_from.setImageDrawable(getResources().getDrawable(imgid[flag_from_id]));
		flag_to.setImageDrawable(getResources().getDrawable(imgid[flag_to_id]));
		
		curreny_from.setText("1");
		curreny_to.setText("");
		
//		Setting Global Variables for TO and FROM Currency Codes ***
	    ((GlobalVariables)getApplication()).setCurrency_from(""+currency_to_button.getText());
	    ((GlobalVariables)getApplication()).setCurrency_to(""+currency_from_button.getText());
	}
	
	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog = new ProgressDialog(CurrencyCalc.this);
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Connecting to server. please wait...");
			this.dialog.show();
		}
		
		@Override
		protected String doInBackground(String... urls) {
			String response="";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			String time="";
			String date="";
			String rate="";
			double rate_db;
//			Web Service - Soap implementation ********************************************************
//			SoapObject Request = new SoapObject(NAME_SPACE, METHOD_NAME);
//	        Request.addProperty("FromCurrency", currency_from_button_txt);
//	        Request.addProperty("ToCurrency", currency_to_button_txt);
//	        SoapSerializationEnvelope soapEnvelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
//	        soapEnvelope.dotNet=true;
//	        soapEnvelope.setOutputSoapObject(Request); 
//	        AndroidHttpTransport aht = new AndroidHttpTransport(URL);
//	        aht.call(SOAP_ACTION, soapEnvelope);
//        	SoapPrimitive result = (SoapPrimitive) soapEnvelope.getResponse();
//	        rate = Double.parseDouble(result.toString());
			try{
//				Log.v(TAG, "result: "+result);
//				String[] results = result.trim().split(",");
				
				InputSource res = new InputSource();
				res.setCharacterStream(new StringReader(result));	
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(res);
				
//				Yahoo Finance *********************************************************************
				Node node1 = doc.getElementsByTagName("row").item(0);
				Node node2 = doc.getElementsByTagName("row").item(1);
				if(node1==null || node2==null){
					CharSequence error = "Sorry, Rate is currently not available, please try again later!";
					Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
					toast.show();
				}
				else{
					NodeList node1list = node1.getChildNodes();
					NodeList node2list = node2.getChildNodes();
	
					rate = node1list.item(1).getTextContent();
	
					if(rate!=null && !rate.equalsIgnoreCase("0.00")){
						date = node1list.item(2).getTextContent();
						time = node1list.item(3).getTextContent();
						rate_db = Double.parseDouble(rate);
					}
					else{
						rate = node2list.item(1).getTextContent();
						rate_db = 1/Double.parseDouble(rate);
						date = node2list.item(2).getTextContent();
						time = node2list.item(3).getTextContent();
					}
//					***********************************************************************************

//					Log.v(TAG, "curreny_to_amt: "+curreny_to_amt);
//					Log.v(TAG, "rate_db: "+rate_db);
					curreny_to_amt = MathUtil.getDRound(curreny_from_amt * rate_db, 6);
					DecimalFormat frmt = new DecimalFormat("#,###,###,###,###.######");
					String formatted = frmt.format(curreny_to_amt);
					curreny_to.setText(""+formatted);
					curreny_detail_1.setText("Rate as of:");
					curreny_detail_2.setText(""+date);
					curreny_detail_3.setText(""+time);
					doc = null;
				}
//				closing waiting spinner
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void readWebpage() {
		curreny_from_amt = Double.parseDouble(curreny_from.getText().toString().trim().length()<1?"0":curreny_from.getText().toString().trim());
		if(curreny_from_amt==0){
			CharSequence error = "Amount must be greater than 0";
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
			toast.show();
		}
		else{
			String currency_from_button_txt = currency_from_button.getText().toString();
			String currency_to_button_txt = currency_to_button.getText().toString();
			
//			Web Service Rest implementation **********************************************************		
//			Switching to Webservicex.net *************************************************************
//			String URL1 = "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=";
//			String URL2 = "&ToCurrency=";
//			String URL = URL1 + currency_from_button_txt + URL2 + currency_to_button_txt;
//			String URL3 = URL1 + currency_to_button_txt + URL2 + currency_from_button_txt;
			
			String URL1="http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D'http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3D";
			String URL2 = "%3DX%2B";
			String URL3 = "%3DX%26f%3Dsl1d1t1%26e%3D.csv'";
			String URL = URL1 + currency_from_button_txt + currency_to_button_txt + URL2 + currency_to_button_txt + currency_from_button_txt + URL3;
//			Log.v(TAG, "url: "+URL);
			if(currency_from_button_txt.equalsIgnoreCase(currency_to_button_txt)){
				curreny_to.setText(""+curreny_from_amt);
			}
			else{
				DownloadWebPageTask task = new DownloadWebPageTask();
				task.execute(new String[] {URL});
			}
		}
		
	}
}