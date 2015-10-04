package com.zetta.pedaja;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class AdapterTelefoneEmpresa extends ArrayAdapter<ModelTelefoneEmpresa>{
 	Context context; 
    int layoutResourceId;    
    RadioButton mCurrentlyCheckedRB;
    int posicao;    
    List<ModelTelefoneEmpresa> lstTelefone = null;
	Telefoneolder holder;
    
    public AdapterTelefoneEmpresa(Context context, int layoutResourceId,  List<ModelTelefoneEmpresa> lstTelefone) {
    	super(context, layoutResourceId, lstTelefone);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.lstTelefone = lstTelefone; 
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
	            
	            holder = new Telefoneolder();
	            holder.txtDescricaoTelefone = (TextView)row.findViewById(R.id.txtDescricaoTelefone);
	            holder.txtNumeroTelefone = (TextView)row.findViewById(R.id.txtNumeroTelefone);
	            
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (Telefoneolder)row.getTag();
	        }
	        
	        
	        ModelTelefoneEmpresa telefone =   lstTelefone.get(position);
	        holder.txtNumeroTelefone.setText("(" + telefone.getDDD() + ") " + telefone.getTelefone());
	        holder.txtDescricaoTelefone.setText(telefone.getDescricao());
	     
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        return row;
    }
    	    
    static class Telefoneolder
    {
        TextView txtNumeroTelefone;
        TextView txtDescricaoTelefone;
        
    }
}
