package com.zetta.pedaja;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

@SuppressLint("NewApi")
public class MesaActivity extends Activity {
	Spinner spMesa;
	Button btnMesaConfirmar;
	EditText edtMesaEst;
	Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mesa);
		spMesa = (Spinner) findViewById(R.id.spMesa);		
		btnMesaConfirmar = (Button) findViewById(R.id.btnMesaConfirmar);	
		edtMesaEst = (EditText) findViewById(R.id.edtMesaEst);	
		btnMesaConfirmar.setOnClickListener(btnMesaConfirmarOnClick);
		carregaMesa();
	}
	
	
	private OnClickListener btnMesaConfirmarOnClick = new OnClickListener() {			
		public void onClick(View v) {
			try {
				
				if(edtMesaEst.getText().toString().equals("")){
					mostrarMensagem("Informe o código da empresa!", "Aviso");
					return;
				} else {
					
					int Empresa = Integer.parseInt(edtMesaEst.getText().toString());
					if(Empresa != Global.empresa.getEmpresaId()) {
						mostrarMensagem("Esta mesa não pertence ao estabelecimento selecionado.", "Aviso");
						return;
					}
				}
				
				Intent intent = new Intent(getApplicationContext(), ConfirmarMesaActivity.class); 
		   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			
			 } catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};	
	
	public void carregaMesa(){
		DALMesa dbMesa = new DALMesa(context);		
		ArrayList<ModelMesa> lstMesa = dbMesa.selecionarMesas(Global.empresa.getEmpresaId());
		dbMesa = null;
		AdapterMesa  adpMesa = new AdapterMesa(lstMesa, context);
		spMesa.setAdapter(adpMesa);
		spMesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {	
			public void onItemSelected(AdapterView<?> parent, View arg1, int posicao, long id) {

				try{
					ModelMesa mesa = (ModelMesa) parent.getItemAtPosition(posicao);
					Global.mesaSelecionada = mesa.getNumero();
				} catch (Exception ex) {
		        	ex.printStackTrace();
		        }
				 				
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
	 
	       
	    });		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.mesa, menu);
		return true;
	}
	
	
	private void mostrarMensagem(String msg, String titulo) {
		new AlertDialog.Builder(this)
				.setTitle(titulo)
				.setMessage(msg)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("OK", null).show();
	
	}
	
	
	
	

}
