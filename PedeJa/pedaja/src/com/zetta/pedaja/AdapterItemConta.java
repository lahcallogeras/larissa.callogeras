package com.zetta.pedaja;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;


public class AdapterItemConta extends ArrayAdapter<ModelVisualizarPedido>{
 	Context context; 
    int layoutResourceId;    
    RadioButton mCurrentlyCheckedRB;
    int posicao;    
    List<ModelVisualizarPedido> lstItemConta = null;
	TipoProdutoHolder holder;
    
    public AdapterItemConta(Context context, int layoutResourceId,  List<ModelVisualizarPedido> lstItemConta) {
    	super(context, layoutResourceId, lstItemConta);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.lstItemConta = lstItemConta; 
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
	            holder.txtContaDescricao = (TextView)row.findViewById(R.id.txtContaDescricao);
	            holder.txtContaValor = (TextView)row.findViewById(R.id.txtContaValor);
	            holder.txtContaTipoProduto = (TextView)row.findViewById(R.id.txtContaTipoProduto);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (TipoProdutoHolder)row.getTag();
	        }
	        
	        
	        ModelVisualizarPedido model =   lstItemConta.get(position);
	        
	        
	        double Valor = model.getValor() + model.getValorAcomp() + model.getValorMeioMeio();
	        
	        String Descricao = model.getDescCategoria() + " " + model.getProduto();
	        if(model.getDescricaoMeioMeio() != null && !model.getDescricaoMeioMeio().equals("")){	        	
	        	Descricao = "Meio a Meio - " + Descricao + "/" + model.getDescricaoMeioMeio(); 	        	
	        }
	        
	        Descricao +=  "<body  style=\"text-align:justify\">" + " - " + model.getDescricao() + "</body >";
	        
	        holder.txtContaDescricao.setText(Html.fromHtml(Descricao));
	        
	        if(model.getDescricaoAcomp() != null && !model.getDescricaoAcomp().equals("")){
	        	holder.txtContaTipoProduto.setText("Acomp.: " + model.getDescricaoAcomp());
	        	holder.txtContaTipoProduto.setVisibility(View.VISIBLE);
	        } else 
	        	holder.txtContaTipoProduto.setVisibility(View.GONE);
	        
	        holder.txtContaValor.setText(String.format("R$ %.2f", Valor));
	        
	        
	        
	     
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        return row;
    }
    	    
    static class TipoProdutoHolder
    {
        TextView txtContaDescricao;
        TextView txtContaValor;
        TextView txtContaTipoProduto;
        
        
        
    }
}
