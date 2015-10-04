package com.zetta.pedaja;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;


public class AdapterAcompanhamento extends ArrayAdapter<ModelTipoProduto>{
 	Context context; 
    int layoutResourceId;    
    RadioButton mCurrentlyCheckedRB;
    int posicao;    
    List<ModelTipoProduto> lstAcompanhamento = null;
	TipoProdutoHolder holder;
    
    public AdapterAcompanhamento(Context context, int layoutResourceId,  List<ModelTipoProduto> lstAcompanhamento) {
    	super(context, layoutResourceId, lstAcompanhamento);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.lstAcompanhamento = lstAcompanhamento; 
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
	            holder.cbAcompanhamento = (CheckBox)row.findViewById(R.id.cbAcompanhamento);
	            holder.txtAcompanhamentoDesc = (TextView)row.findViewById(R.id.txtAcompanhamentoDesc);
	            holder.txtAcompdValor = (TextView)row.findViewById(R.id.txtAcompdValor);
	            holder.txtAcompId = (TextView)row.findViewById(R.id.txtAcompId);

	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (TipoProdutoHolder)row.getTag();
	        }
	        
	        
	        ModelTipoProduto tipoProduto =   lstAcompanhamento.get(position);
	        holder.txtAcompanhamentoDesc.setText(tipoProduto.getDescricao());
	        holder.txtAcompdValor.setText(String.format("R$ %.2f",  tipoProduto.getValor()));
	        holder.txtAcompId.setText("" + tipoProduto.getEmpresaId());	
	        holder.cbAcompanhamento.setTag(position);	        
	        holder.cbAcompanhamento.setChecked(tipoProduto.getSelecionado());
	        
	        holder.cbAcompanhamento.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					lstAcompanhamento.get(Integer.parseInt(buttonView.getTag().toString())).setSelecionado(isChecked);
					
				}
	        	
	        	
	        });
	        
	        
	     
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        return row;
    }
    	    
    static class TipoProdutoHolder
    {
        CheckBox cbAcompanhamento;
        TextView txtAcompanhamentoDesc;
        TextView txtAcompdValor;
        TextView txtAcompId;
        
        
    }
}
