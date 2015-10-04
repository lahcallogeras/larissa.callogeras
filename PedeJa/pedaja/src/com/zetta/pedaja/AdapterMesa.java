package com.zetta.pedaja;


import java.util.ArrayList;


import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class AdapterMesa extends BaseAdapter implements SpinnerAdapter {

	Context context; 
	
    private  ArrayList<ModelMesa> data;
    //int layoutResourceId;    
    
    public AdapterMesa(ArrayList<ModelMesa> data, Context context){
        this.data = data;
        this.context = context;
        //this.layoutResourceId = layoutResourceId;
    }

  
    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int i) {
        return i;
    }
    
    public View getCustomView(int position, View convertView, ViewGroup parent) {
    	TextView text =  null;
        try {
	        LayoutInflater vi =  (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	        if (convertView != null){
	            text = (TextView) convertView;
	        } else {
	            text = (TextView) vi.inflate( android.R.layout.simple_dropdown_item_1line, parent, false);
	        }
	        text.setTextColor(Color.BLACK);
	        text.setText(data.get(position).getNumero());
         
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return text;
        		
    }
    
   

	public int getItemViewType(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub
		
	}

	public void unregisterDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub
		
	}

	public View getDropDownView(int position, View convertView, ViewGroup parent)  {
		return getCustomView(position, convertView, parent);
	}
	

	public View getView(int position, View convertView, ViewGroup parent) {
	    return getCustomView(position, convertView, parent);
	}

}

