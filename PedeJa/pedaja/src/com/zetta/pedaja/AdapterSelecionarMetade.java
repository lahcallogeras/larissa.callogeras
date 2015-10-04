package com.zetta.pedaja;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterSelecionarMetade  extends ArrayAdapter<ModelMetade>{
 	Context context; 
    int layoutResourceId;    
    int posicao;    
    List<ModelMetade> lista = null;
	Holder holder;
    
    public AdapterSelecionarMetade(Context context, int layoutResourceId,  List<ModelMetade> lista) {
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
	            holder.txtDescricao = (TextView)row.findViewById(R.id.txtDescricao);
	            holder.txtValor = (TextView)row.findViewById(R.id.txtValor);
	            holder.txtDetalhe = (TextView)row.findViewById(R.id.txtDetalhe);
	            holder.txtTipoProdutoId = (TextView)row.findViewById(R.id.txtTipoProdutoId);
	            
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (Holder)row.getTag();
	        }
	        
	        
	        ModelMetade model =   lista.get(position);
	        holder.txtTipoProdutoId.setText(model.getTipoProdutoId() + "");
	        holder.txtDescricao.setText(model.getDescricao());
	        holder.txtValor.setText(String.format("R$ %.2f",  model.getValor()));
	        
	        if(model.getDetalhe() != null && !model.getDetalhe().equals("")){
	        	holder.txtDetalhe.setText(model.getDetalhe());
	        	holder.txtDetalhe.setVisibility(View.VISIBLE);
	        } else 
	        	holder.txtDetalhe.setVisibility(View.GONE);
	     
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        return row;
    }
    	    
    static class Holder
    {
        TextView txtDescricao;
        TextView txtValor;
        TextView txtDetalhe;
        TextView txtTipoProdutoId;
        
    }


}
