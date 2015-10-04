package com.zetta.pedaja;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SelecionarMetadeActivity extends Activity {

	private static List<ModelMetade> lista;
	private Context context;
	private ListView lstMetade;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selecionar_metade);
		lstMetade = (ListView) findViewById(R.id.lstMetade);	
		lstMetade.setOnItemClickListener(lstMetadeOnClickListener);
		
		context = this;		
		CarregaMetade();
		
	}
	
	private void CarregaMetade(){
		try {
			
			DALTipoProduto dbTipoProduto = new DALTipoProduto(context);
			lista = dbTipoProduto.selecionarSaboresMetade();		
			AdapterSelecionarMetade adp = new AdapterSelecionarMetade(context, R.layout.item_selecao_metade, lista);			
			lstMetade.setAdapter(adp);
			dbTipoProduto = null;
		} catch (Exception ex){		
			ex.printStackTrace();
		}
	}
	

	OnItemClickListener lstMetadeOnClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
			
			if(Global.MetadeSelecionada == 1)
				Global.modelMetade01 = lista.get(position);
			else if(Global.MetadeSelecionada == 2)
				Global.modelMetade02 = lista.get(position);
			else if(Global.MetadeSelecionada == 3)
				Global.modelMetade03 = lista.get(position);
					
			onBackPressed();
		}
		
	};

	


}
