package com.naturecode.koolconverter.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.naturecode.koolconverter.GlobalVariables;
import com.naturecode.koolconverter.R;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] unit_lables;

//	Keep all Images in array
	public Integer[] unit_images = {
			R.drawable.angle, R.drawable.area, R.drawable.bitbyte, 
			R.drawable.density, R.drawable.electric, R.drawable.energy,
			R.drawable.force, R.drawable.fuel, R.drawable.length, 
			R.drawable.power, R.drawable.pressure, R.drawable.speed,
			R.drawable.temperature, R.drawable.volume, R.drawable.weight
	};

//	Constructor
	public ImageAdapter(Context context, String[] unit_lables) {
		this.context = context;
		this.unit_lables = unit_lables;
	}

	@Override
	public int getCount() {
		return unit_images.length;
	}

	@Override
	public Object getItem(int position) {
		return unit_images[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		if (convertView == null) {
			gridView = new View(context);	
			
			int width = ((GlobalVariables) this.context.getApplicationContext()).getScreen_width();
			
			if(width<800){
				gridView = inflater.inflate(R.layout.unit_grid_720, null);
			}
			else if (width>=800 && width<1080){
				gridView = inflater.inflate(R.layout.unit_grid_800, null);
			}
			else{
				gridView = inflater.inflate(R.layout.unit_grid_1080, null);
			}

			
//			get layout from mobile.xml
//			gridView = inflater.inflate(R.layout.unit_grid_800, null);

//			set value into textview
			TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
			textView.setText(unit_lables[position]);

//			set image based on selected text
			ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
			
			for(int i=0; i<unit_lables.length; i++){
				imageView.setImageResource(unit_images[i]);
			}
			String label = unit_lables[position];
			if (label.equals("Angle")) {
				imageView.setImageResource(R.drawable.angle);
			} 
			else if (label.equals("Area")) {
				imageView.setImageResource(R.drawable.area);
			}
			else if (label.equals("Bit & Byte")) {
				imageView.setImageResource(R.drawable.bitbyte);
			}
			else if (label.equals("Density")) {
				imageView.setImageResource(R.drawable.density);
			}
			else if (label.equals("Electric")) {
				imageView.setImageResource(R.drawable.electric);
			}
			else if (label.equals("Energy")) {
				imageView.setImageResource(R.drawable.energy);
			}
			else if (label.equals("Force")) {
				imageView.setImageResource(R.drawable.force);
			}
			else if (label.equals("Fuel")) {
				imageView.setImageResource(R.drawable.fuel);
			}
			else if (label.equals("Length")) {
				imageView.setImageResource(R.drawable.length);
			}
			else if (label.equals("Power")) {
				imageView.setImageResource(R.drawable.power);
			}
			else if (label.equals("Pressure")) {
				imageView.setImageResource(R.drawable.pressure);
			}
			else if (label.equals("Speed")) {
				imageView.setImageResource(R.drawable.speed);
			}
			else if (label.equals("Temperature")) {
				imageView.setImageResource(R.drawable.temperature);
			}
			else if (label.equals("Volume")) {
				imageView.setImageResource(R.drawable.volume);
			}
			else if (label.equals("Weight")) {
				imageView.setImageResource(R.drawable.weight);
			}
	
//			ImageView imageView = new ImageView(context);
//			imageView.setImageResource(unit_images[position]);
//			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//			imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
//			return imageView;
		}
		else {
			gridView = (View) convertView;
		}
		return gridView;
	}
}