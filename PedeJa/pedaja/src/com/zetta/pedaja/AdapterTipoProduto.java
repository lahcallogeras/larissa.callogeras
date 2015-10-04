package com.zetta.pedaja;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;


public class AdapterTipoProduto extends ArrayAdapter<ModelTipoProduto>{
 	Context context; 
    int layoutResourceId;    
    RadioButton mCurrentlyCheckedRB;
    int posicao;    
    List<ModelTipoProduto> lstTipoProduto = null;
	TipoProdutoHolder holder;
    
    public AdapterTipoProduto(Context context, int layoutResourceId,  List<ModelTipoProduto> lstTipoProduto) {
    	super(context, layoutResourceId, lstTipoProduto);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.lstTipoProduto = lstTipoProduto; 
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
	            
	            holder = new TipoProdutoHolder();
	            holder.rbTipoProd = (RadioButton)row.findViewById(R.id.rbTipoProd);
	            holder.txtTipoProdDesc = (TextView)row.findViewById(R.id.txtTipoProdDesc);
	            holder.txtTipoProdId = (TextView)row.findViewById(R.id.txtTipoProdId);
	            holder.txtTipoProdValor = (TextView)row.findViewById(R.id.txtTipoProdValor);
	            
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (TipoProdutoHolder)row.getTag();
	        }
	        
	        
	        ModelTipoProduto tipoProduto =   lstTipoProduto.get(position);
	        holder.txtTipoProdDesc.setText(tipoProduto.getDescricao());
	        holder.txtTipoProdValor.setText(String.format("R$ %.2f",  tipoProduto.getValor()));
	        holder.txtTipoProdId.setText("" + tipoProduto.getEmpresaId());	
	        holder.rbTipoProd.setTag(position);
	        
	        if(Global.produtoSelecionado == tipoProduto.getTipoProdutoId()){
	        	 holder.rbTipoProd.setChecked(true);
         	     mCurrentlyCheckedRB = (RadioButton) holder.rbTipoProd;
	        }
	        
	        holder.rbTipoProd.setOnClickListener(new OnClickListener() {

	            public void onClick(View v) {
	                
	            	int i = Integer.parseInt(v.getTag().toString());
	            	Global.produtoSelecionado = lstTipoProduto.get(i).getTipoProdutoId();
	            	
	            	            	
	            	if(mCurrentlyCheckedRB == null) {
	            	    mCurrentlyCheckedRB = (RadioButton) v;
	            	    mCurrentlyCheckedRB.setChecked(true);
	            	}
       
	            	if(mCurrentlyCheckedRB == v)
	            	    return;
	            	mCurrentlyCheckedRB.setChecked(false);
	            	((RadioButton)v).setChecked(true);
	            	mCurrentlyCheckedRB = (RadioButton)v; 
	            	
	            	
	            
	            	
	            }
	        });
	        
	        
	     
	        
	     
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        return row;
    }
    	    
    static class TipoProdutoHolder
    {
        RadioButton rbTipoProd;
        TextView txtTipoProdDesc;
        TextView txtTipoProdId;
        TextView txtTipoProdValor;
        
        
    }
}
