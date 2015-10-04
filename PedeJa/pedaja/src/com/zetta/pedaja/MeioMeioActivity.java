package com.zetta.pedaja;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class MeioMeioActivity extends Activity {

	private Context context;
	private ListView lstCategoria;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meio_meio);		
		lstCategoria = (ListView) findViewById(R.id.lstCategoria);	
		lstCategoria.setOnItemClickListener(lstCategoriaOnClickListener);
		
		context = this;
		
		CarregaCategoria();
	}
	
	private void CarregaCategoria(){
		try {
			
			DALCategoria dbCategoria = new DALCategoria(context);
			ArrayList<ModelCategoria> listaModel = dbCategoria.selecionaCategoriaMeioMeio(Global.empresa.getEmpresaId());			
			AdapterMeioMeio adp = new AdapterMeioMeio(context, R.layout.item_categoria, listaModel);			
			lstCategoria.setAdapter(adp);
			dbCategoria = null;
		} catch (Exception ex){		
			ex.printStackTrace();
		}
	}
	
	
	OnItemClickListener lstCategoriaOnClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
			 Global.categoriaMeioMeio = Integer.parseInt(((TextView)v.findViewById(R.id.txtCatId)).getText().toString());
			 Intent intent = new Intent(getApplicationContext(),  ConfiguracaoMeioActivity.class); 
	   		 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 startActivity(intent);
		}
		
	};

	

}
