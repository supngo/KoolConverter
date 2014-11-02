package com.naturecode.koolconverter;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.naturecode.koolconverter.util.CustomAdapter;
import com.naturecode.koolconverter.util.RowData;

public class CurrencyType extends SherlockListActivity {
	private LayoutInflater mInflater;
	private Vector<RowData> data = new Vector<RowData>();
	EditText currency_search;
	RowData rd;
	CustomAdapter adapter;
//	private static final String TAG = "debug";
	private Integer[] atrributes = {R.layout.currency_list_720, R.id.currency_title, R.id.currency_detail, R.id.currency_flag};
	
	private Integer[] images = {R.drawable.usd, R.drawable.eur, R.drawable.gbp, R.drawable.aud, R.drawable.cad, 
			R.drawable.cny, R.drawable.hkd, R.drawable.inr, R.drawable.jpy, R.drawable.krw, R.drawable.nzd, 
			R.drawable.rub, R.drawable.sgd, R.drawable.afn, R.drawable.all, R.drawable.dzd, R.drawable.ars, 
			R.drawable.bsd, R.drawable.bhd, R.drawable.bdt, R.drawable.bob, R.drawable.brl, R.drawable.bnd,
			R.drawable.khr, R.drawable.clp, R.drawable.cop, R.drawable.crc, R.drawable.hrk, R.drawable.cup, 
			R.drawable.czk, R.drawable.dkk, R.drawable.dop, R.drawable.egp, R.drawable.svc, 
			R.drawable.etb, 
			R.drawable.gel, 
			R.drawable.gtq, R.drawable.gnf, R.drawable.htg, R.drawable.hnl, 
			R.drawable.huf, R.drawable.isk, R.drawable.idr, 
			R.drawable.irr, 
			R.drawable.iqd, R.drawable.ils,
			R.drawable.jmd, R.drawable.jod, R.drawable.kes, R.drawable.kwd, R.drawable.lak, R.drawable.lvl, R.drawable.lbp,
			R.drawable.ltl, R.drawable.mop, R.drawable.mkd, R.drawable.mwk, R.drawable.myr, R.drawable.mxn, 
			R.drawable.mnt, R.drawable.mad, R.drawable.mzm, R.drawable.mmk, R.drawable.nad, R.drawable.npr,
			R.drawable.nio, R.drawable.ngn, R.drawable.nok, R.drawable.pkr, R.drawable.pab, R.drawable.pyg,
			R.drawable.pen, R.drawable.php, R.drawable.pln, R.drawable.qar, 
			R.drawable.rol,
			R.drawable.sar, 
			R.drawable.rsd, 
			R.drawable.skk,
			R.drawable.sit, R.drawable.zar, R.drawable.lkr, R.drawable.sdd, R.drawable.sek, R.drawable.chf,
			R.drawable.syp, R.drawable.twd, R.drawable.thb, R.drawable.trl, R.drawable.ugx, R.drawable.uah,
			R.drawable.uyu, R.drawable.veb, R.drawable.vnd, R.drawable.yer, R.drawable.zmk, R.drawable.zwd};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		showCurrencyList();

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
	}
	
	private void showCurrencyList(){
		int width = ((GlobalVariables) this.getApplication()).getScreen_width();
		if(width<800){
			atrributes[0] = R.layout.currency_list_720;
		}
		else if (width>=800 && width<1080){
			atrributes[0] = R.layout.currency_list_800;
		}
		else
			atrributes[0] = R.layout.currency_list_1080;
		
		
		String[] currency_codes = getResources().getStringArray(R.array.currency_codes);
		String[] currency_desc = getResources().getStringArray(R.array.currency_desc);
		data = new Vector<RowData>();
		for(int i=0;i<currency_codes.length;i++){
			rd = new RowData(i, currency_desc[i], currency_codes[i]);
			data.add(rd);
		}
		adapter = new CustomAdapter(this, data, mInflater, atrributes, images);
		
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select_currency_from_search(position);
			}
		});
	}
	
	private void doMySearch(String query) {
		String[] currency_codes = getResources().getStringArray(R.array.currency_codes);
		String[] currency_desc = getResources().getStringArray(R.array.currency_desc);
		String cursor1, cursor2;
		data = new Vector<RowData>();
		Vector<Integer> search_image = new Vector<Integer>();
		for(int i=0;i<currency_codes.length;i++){
			cursor1 = currency_desc[i].toLowerCase();
			cursor2 = currency_codes[i].toLowerCase();	
			if(cursor1.contains(query.toLowerCase()) || cursor2.contains(query.toLowerCase())){
				search_image.add(images[i]);
				RowData rd = new RowData(i, currency_desc[i], currency_codes[i]);
				data.add(rd);
			}
		}
		if (data.size() < 1) {
			ListView listView = (ListView) findViewById( android.R.id.list);
			listView.setEmptyView(findViewById( android.R.id.empty));
			listView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, new ArrayList<String>() ) );
		} 
		else {
			adapter = new CustomAdapter(this, data, mInflater, atrributes, images);
			getListView().setAdapter(adapter); 
			getListView().setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					select_currency_from_search(position);
				}
			});
		}
	}
	
	private void select_currency_from_search(int position){
		if(position >= 0){
			String selected_currency_code = data.elementAt(position).getmDetail();
			String selected_currency_desc = data.elementAt(position).getmTitle();
			String selected_flag = ""+data.elementAt(position).getmId()+"";
			Intent intent = new Intent(CurrencyType.this, CurrencyCalc.class);
			intent.putExtra("selected_currency_code", selected_currency_code);
			intent.putExtra("selected_currency_desc", selected_currency_desc);
			intent.putExtra("selected_flag", selected_flag);
			startActivity(intent);
			finish();
		}
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.search_record:
			onSearchRequested();
			return true;
			
		case R.id.playlist:
			showCurrencyList();
			return true;
		
		case android.R.id.home:
	    	Intent intent = new Intent(this, CurrencyCalc.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
	    	return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}