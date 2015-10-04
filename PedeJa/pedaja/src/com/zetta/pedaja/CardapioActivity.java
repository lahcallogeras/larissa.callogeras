package com.zetta.pedaja;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

@SuppressLint("ResourceAsColor")
public class CardapioActivity extends Activity {
	ExpandableListView exlCardapio;
	Context context = this;
	AdapterCategoria exAdpt;
	ArrayList<ModelCategoria> lstCategorias;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardapio);
		exlCardapio = (ExpandableListView) findViewById(R.id.exlCardapio);  
		exlCardapio.setOnChildClickListener(exlCardapioItemClicked);
		
		DALDestaque dbDestaque = new DALDestaque(context);
		int qtd = dbDestaque.visualizoesDestaque(Global.empresa.getEmpresaId(), Global.usuario.getUsuarioId());
		if(qtd > 0 )
			carregarCategorias();
		else {
			Global.visuDestaqueCarpadio = true;
			Intent intent = new Intent(getApplicationContext(), DestaqueActivity.class); 
	   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
		}
		
		
		
	}
	
	
	private OnChildClickListener exlCardapioItemClicked =  new OnChildClickListener() {	 
		  public boolean onChildClick(ExpandableListView parent, View v,   int groupPosition, int childPosition, long id) {
			    try {
				   
				   DALProduto dbProduto = new DALProduto(context);
				   Global.produto = dbProduto.selecionaProdutoId(lstCategorias.get(groupPosition).getLstProduto().get(childPosition).getProdutoId());  
				   Intent intent = new Intent(getApplicationContext(), ProdutoActivity.class); 
			   	   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				   startActivity(intent);
			    }
			    catch (Exception ex) {
			    	 Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
			    	ex.printStackTrace();
			    }
		   return true;
		  }
	 };
	 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.cardapio, menu);
		return true;
	}
	
	private void carregarCategorias(){
		try {
			DALCategoria dbCategoria = new DALCategoria(context);
			lstCategorias = dbCategoria.selecionaCategoria(Global.empresa.getEmpresaId());		
			exAdpt = new AdapterCategoria(context,   lstCategorias);	
		    exlCardapio.setIndicatorBounds(0, 20);		
			exlCardapio.setAdapter(exAdpt);
			//expandAll();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	//method to expand all groups
	/*
	 private void expandAll() {
		  int count = exAdpt.getGroupCount();
		  for (int i = 0; i < count; i++){
			  exlCardapio.expandGroup(i);
		  }'
	 } */
	
	 @Override 
	    public void onBackPressed(){
	    	finish();
	    }
		
	
	

}

