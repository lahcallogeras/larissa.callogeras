package com.zetta.pedaja;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class AdapterCategoria  extends BaseExpandableListAdapter{
	
	Context context; 
    List<ModelCategoria> lstCategoria = null;
    
    public AdapterCategoria(Context context,List<ModelCategoria> lstCategoria) {    	
    	super();
        this.context = context;
        this.lstCategoria = lstCategoria; 
    } 
   
    public Object getChild(int groupPosition, int childPosition) {
    	List<ModelProduto> lstProduto = lstCategoria.get(groupPosition).getLstProduto();
    	return lstProduto.get(childPosition);
    }

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) 
	{
		View v = convertView;
	    if (v == null) {
	        LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
	        v = inflater.inflate(R.layout.item_produto, parent, false);
	    }
	    
	    TextView txtProdId = (TextView) v.findViewById(R.id.txtProdId);
		TextView txtProdDesc = (TextView) v.findViewById(R.id.txtProdDesc);
		TextView txtProdDetalhe = (TextView) v.findViewById(R.id.txtProdDetalhe);				
	     
	    ModelProduto produto =   lstCategoria.get(groupPosition).getLstProduto().get(childPosition);

	    txtProdId.setText(produto.getProdutoId() + "");
	    txtProdDesc.setText(produto.getDescricao());
	    
	    if(produto.getDetalhe().equals("")) {
	    	txtProdDetalhe.setVisibility(View.GONE);
	    }
	    else {
	    	txtProdDetalhe.setText(produto.getDetalhe());
	    	txtProdDetalhe.setVisibility(View.VISIBLE);
	    }
	    return v;

	}

	public int getChildrenCount(int groupPosition) {
		List<ModelProduto> lstProduto = lstCategoria.get(groupPosition).getLstProduto();
		return lstProduto.size() ;
	}

	public Object getGroup(int groupPosition) {
		return lstCategoria.get(groupPosition);
	}

	public int getGroupCount() {
		return lstCategoria.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View v = convertView;		
		    if (v == null) { 
		    	LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		        v = inflater.inflate(R.layout.item_categoria, parent, false);
		    }
		    
		    TextView txtCatId = (TextView) v.findViewById(R.id.txtCatId);
			TextView txtCatDesc = (TextView) v.findViewById(R.id.txtCatDesc);
		    ModelCategoria categoria =   lstCategoria.get(groupPosition);
		    txtCatId.setText(categoria.getCategoriaId() + "");
		    txtCatDesc.setText(categoria.getDescricao());
		
		  return v;		
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


}
