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

public class AdapterEstado extends BaseAdapter implements SpinnerAdapter {

	Context context; 
	
    private  ArrayList<ModelEstado> data;
    //int layoutResourceId;    
    
    public AdapterEstado(ArrayList<ModelEstado> data, Context context){
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
	        text.setText(data.get(position).getNome());
         
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return text;
    	/*
    	View row = convertView;
    	EstadoHolder holder = null;   	
        
		if(row == null)
        {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new EstadoHolder();
            holder.txtEstEndereco = (TextView)row.findViewById(R.id.txtItemEstado);
            row.setTag(holder);
			
        }
		else
        {
            holder = (EstadoHolder)row.getTag();
        }
        
		ModelEstado model =   data.get(position);
		holder.txtEstEndereco.setText(model.getNome()); */
    		
    }
    
    static class EstadoHolder
    {
        TextView txtEstEndereco;
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

