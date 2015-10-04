package com.zetta.pedaja;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConfirmarMesaActivity extends Activity {

	EditText edtNumeroMesa;
	Button btnExMesaConf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmar_mesa);
		edtNumeroMesa = (EditText) findViewById(R.id.edtNumeroMesa);	
		btnExMesaConf = (Button) findViewById(R.id.btnExMesaConf);
		btnExMesaConf.setOnClickListener(btnExMesaConfOnClick);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.confirmar_mesa, menu);
		return true;
	}
	
	private OnClickListener btnExMesaConfOnClick = new OnClickListener() {			
		public void onClick(View v) {
			
			String mesaInvertida = edtNumeroMesa.getText().toString();
			String mesa = "";
			for(int i =  mesaInvertida.length() - 1; i >= 0 ; i-- ){
				mesa = mesa + mesaInvertida.substring(i, i+1);
			}
			
			if(!mesa.equals(Global.mesaSelecionada)) 
				mostrarMensagem("O número informado não corresponde ao número da mesa selecionado anteriormente. Verifique!", "Aviso");
			else {
				Global.abrirConta = true;
				finish();
			}
			
		}
	};	
	
	
	private void mostrarMensagem(String msg, String titulo) {
		new AlertDialog.Builder(this)
				.setTitle(titulo)
				.setMessage(msg)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("OK", null).show();
	
	}
	
	
	
	@Override 
	 public void onBackPressed(){
		Intent intent = new Intent(getApplicationContext(), MesaActivity.class); 
   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
	 }
	

}
