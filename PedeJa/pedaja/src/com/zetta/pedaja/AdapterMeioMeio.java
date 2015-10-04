package com.zetta.pedaja;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class AdapterMeioMeio  extends ArrayAdapter<ModelCategoria>{
 	Context context; 
    int layoutResourceId;    
    RadioButton mCurrentlyCheckedRB;
    int posicao;    
    List<ModelCategoria> lista = null;
	Holder holder;
    
    public AdapterMeioMeio(Context context, int layoutResourceId,  List<ModelCategoria> lista) {
    	super(context, layoutResourceId, lista);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.lista = lista; 
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        posicao= position;
        try {
	        
	        if(row == null)
	        {
	        	LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new Holder();
	            holder.txtCatDesc = (TextView)row.findViewById(R.id.txtCatDesc);
	            holder.txtCatId = (TextView)row.findViewById(R.id.txtCatId);
	            
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (Holder)row.getTag();
	        }
	        
	        
	        ModelCategoria model =   lista.get(position);
	        holder.txtCatId.setText(model.getCategoriaId() + "");
	        holder.txtCatDesc.setText(model.getDescricao());
	     
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        return row;
    }
    	    
    static class Holder
    {
        TextView txtCatDesc;
        TextView txtCatId;
        
    }


}
