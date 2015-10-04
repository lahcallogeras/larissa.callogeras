package com.zetta.pedaja;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
public class ConfiguracaoMeioActivity extends Activity {

	private Context context;
	
	private RadioButton rbGrande;
	private RadioButton rbMedio;
	private RadioButton rbPequeno;
	private RadioButton rbMetade01;
	private RadioButton rbMetade02;
	private Button btnConfirmar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configuracao_meio);
		
		rbGrande = (RadioButton)findViewById(R.id.rbGrande);
		rbMedio = (RadioButton)findViewById(R.id.rbMedio);
		rbPequeno = (RadioButton)findViewById(R.id.rbPequeno);
		rbMetade01 = (RadioButton)findViewById(R.id.rbMetade01);
		rbMetade02 = (RadioButton)findViewById(R.id.rbMetade02);
		btnConfirmar = (Button)findViewById(R.id.btnConfirmar);		
		btnConfirmar.setOnClickListener(btnConfirmarOnClik);
		
		context = this;
		CarregaInformacao();
		
		
	}
	
	private void CarregaInformacao(){
		try {
			
			DALCategoria dbCategoria = new DALCategoria(context);
			ModelCategoria categoria = dbCategoria.selecionaCategoriaId(Global.categoriaMeioMeio);
			
			if(categoria.getMixProduto() > 2)
				rbMetade02.setVisibility(View.VISIBLE);
			else
				rbMetade02.setVisibility(View.GONE);
			
			dbCategoria = null;
			
		} catch (Exception ex){
			ex.printStackTrace();
		}		
	}
	
	OnClickListener btnConfirmarOnClik = new OnClickListener() {
		
		public void onClick(View v) {
			
			try {
				
				Global.Tamanho = "";
				if(rbGrande.isChecked())
					Global.Tamanho = "G";
				else  if(rbMedio.isChecked())
					Global.Tamanho = "M";
				else  if(rbPequeno.isChecked())
					Global.Tamanho = "P";
				

				Global.modelMetade01 = new ModelMetade();
				Global.modelMetade02 = new ModelMetade();
				Global.modelMetade03 = new ModelMetade();
				
				
				if(rbMetade01.isChecked()){					
					Intent intent = new Intent(getApplicationContext(), DuasMetadesActivity.class); 
			   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				} 
				else if(rbMetade02.isChecked()){
					Intent intent = new Intent(getApplicationContext(), TresMetadesActivity.class); 
			   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);					
				}
				
				
			} catch (Exception ex){
				ex.printStackTrace();				
			}
			
			
		}
	};

	

}
