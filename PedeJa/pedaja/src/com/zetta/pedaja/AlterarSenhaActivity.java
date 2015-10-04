package com.zetta.pedaja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AlterarSenhaActivity extends Activity {

	EditText edtAltSenhaAtual;
	EditText edtAltNovaSenha;
	EditText edtAltConfSenha;
	Button btnAlterarSenha;
	
	 private ProgressDialog progressDialog;
	 private Context context = this;
	 private Handler handler;
	 private String msgErro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alterar_senha);

		edtAltSenhaAtual =  (EditText)findViewById(R.id.edtAltSenhaAtual);
		edtAltNovaSenha =  (EditText)findViewById(R.id.edtAltNovaSenha);
		edtAltConfSenha =  (EditText)findViewById(R.id.edtAltConfSenha);
		btnAlterarSenha =  (Button)findViewById(R.id.btnAlterarSenha);
		
		btnAlterarSenha.setOnClickListener(btnAlterarSenhaOnClick);	
	}
	
	@SuppressLint({ "HandlerLeak" })
	private OnClickListener btnAlterarSenhaOnClick = new OnClickListener() {			
		public void onClick(View v) {
			
			if(edtAltSenhaAtual.getText().toString().equals("")){
				mostrarMensagem("Informe a senha atual!", "Aviso");				
				return;
			} 
			
			if(!edtAltSenhaAtual.getText().toString().equals(Global.usuario.getSenha())){
				mostrarMensagem("Senha atual incorreta!", "Aviso");				
				return;
			} 
			
			if(edtAltNovaSenha.getText().toString().equals("")){
				mostrarMensagem("Informe a nova senha!", "Aviso");				
				return;
			} 
						
			if(edtAltConfSenha.getText().toString().equals("")){
				mostrarMensagem("Informe a confirmação da senha!", "Aviso");				
				return;
			} 			
			
			if(!edtAltNovaSenha.getText().toString().equals(edtAltConfSenha.getText().toString())){
				mostrarMensagem("Senha e confirmação diferentes!", "Aviso");				
				return;
			} 
			
			Progresso("Aguarde");
		       	 new Thread(new Runnable() {
		       	        public void run() {     
		       	        	  Message msg = new Message();
		       	        	  try {
		       	        		  
		       	        		JSONParser jsonParser = new JSONParser();
	                           
	                            boolean conexao = jsonParser.conectado(context);
	                            if(conexao){
	                           	 
	                            
		                                String httpGet = Global.urlConexaoServidor + "alterarSenha.php";
		                                List<NameValuePair> params = new ArrayList<NameValuePair>();
		                                params.add(new BasicNameValuePair("P1","" + Global.usuario.getUsuarioId()));
		                                params.add(new BasicNameValuePair("P2", edtAltNovaSenha.getText().toString()));
		                                
		                                //Buscando Informações - Banco de dados remoto                        
		                              	JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params); 
		                              	
		                                int sucesso = json.getInt("sucesso");
		                                
	                                    if (sucesso == 1) {
	                                   	  DALUsuario dalUsuario = new DALUsuario(getBaseContext());                                      	 
	                                   	  Global.usuario.setSenha(edtAltNovaSenha.getText().toString());	                                   	  
	                                      dalUsuario.atualizaUsuario(Global.usuario);
	                                      dalUsuario = null;
	                                      msg.what = 1;
	                               	   
	                                   }else {	 	                            	 
	 	                            	 	msg.what = 0;
	 	                            	 	msgErro = json.getString("mensagem");   
	 	                             	}
	                           	 
	                           	 
	                            } else {	                           	 
		                           	 msg.what=0;
		                           	 msgErro = "Sem conexão com a internet. Conecte-se!";	                           	 
	                            }
	                            
	                            
		       	        	  }
		       	        	  catch (Exception e) {
		                          e.printStackTrace();	             	        	  	 
		                      }    
		       	        	  handler.sendMessage(msg);
		              	 	
		  	        }
		  	    }).start();
		 	  
		    handler=new Handler(){
		  		 public void handleMessage(Message msg){
		
		  			 progressDialog.cancel();
		  			 if(msg.what==1)
		  			 {
		  				Toast.makeText(AlterarSenhaActivity.this, "Alteração concluída com sucesso!", Toast.LENGTH_LONG).show();		
		  				Intent intent = new Intent(getApplicationContext(), EstabelecimentoActivity.class); 
		  		   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  				startActivity(intent);
		  		 	 }
		  			 else if (msg.what==0){        		                		       
		  				mostrarMensagem(msgErro, "Erro");				
						return;
		  			 }
		  		 }
		  	};
			
			
			
		}
	};
	
	private void mostrarMensagem(String msg, String titulo) {
		new AlertDialog.Builder(this)
				.setTitle(titulo)
				.setMessage(msg)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("OK", null).show();
	
	}
	
	
    public void Progresso(String titulo){
        progressDialog = new ProgressDialog(AlterarSenhaActivity.this);
        progressDialog.setTitle(titulo);
        progressDialog.setMessage("Processando...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
   }
  
    @Override 
    public void onBackPressed(){
    	Intent intent = new Intent(getApplicationContext(), LoginActivity.class); 
   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.alterar_senha, menu);
		return true;
	}

}
