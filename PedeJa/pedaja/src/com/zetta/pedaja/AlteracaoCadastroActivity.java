package com.zetta.pedaja;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AlteracaoCadastroActivity extends Activity {

	EditText edtAltNome;
	EditText edtAltEmail;
	EditText edtAltCpf;
	DatePicker dtAltDataNascimento;
	Button btnConfirmarAlteracao;
	

    private ProgressDialog progressDialog;
    private Context context = this;
    private Handler handler;
    private String msgErro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alteracao_cadastro);
		
		edtAltNome =  (EditText)findViewById(R.id.edtAltNome);
		edtAltEmail =  (EditText)findViewById(R.id.edtAltEmail);
		dtAltDataNascimento =  (DatePicker)findViewById(R.id.dtAltDataNascimento);
		edtAltCpf =  (EditText)findViewById(R.id.edtAltCpf);		
		btnConfirmarAlteracao  =  (Button)findViewById(R.id.btnConfirmarAlteracao);	
		
		edtAltNome.setText(Global.usuario.getNome());
		edtAltEmail.setText(Global.usuario.getEmail());
		
		if(Global.usuario.getCPF() != null  &&  !Global.usuario.getCPF().equals("") && !Global.usuario.getCPF().equals("null"))
			edtAltCpf.setText(Global.usuario.getCPF());
		
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Global.usuario.getDataNascimento().getTime());		
		dtAltDataNascimento.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);
		
		btnConfirmarAlteracao.setOnClickListener(btnEncerraContaOnClick);		
		
	}
	
	
	
	@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
	private OnClickListener btnEncerraContaOnClick = new OnClickListener() {			
		public void onClick(View v) {
			
			
			if(edtAltNome.getText().toString().equals("")){
				mostrarMensagem("Informe o nome!", "Aviso");				
				return;
			}
			
			if(edtAltEmail.getText().toString().equals("")){
				mostrarMensagem("Informe o e-mail!", "Aviso");				
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
	                           	 
	                            int mes = dtAltDataNascimento.getMonth()  + 1;
	                           	 String dataNascimento =  dtAltDataNascimento.getDayOfMonth() + "/" +
	                           			 			    mes +  "/" +
					                           			dtAltDataNascimento.getYear();
	                           	 
		                                String httpGet = Global.urlConexaoServidor + "alterarCadastro.php";
		                                List<NameValuePair> params = new ArrayList<NameValuePair>();
		                                params.add(new BasicNameValuePair("P1", edtAltNome.getText().toString()));
		                                params.add(new BasicNameValuePair("P2", edtAltEmail.getText().toString()));    
		                                params.add(new BasicNameValuePair("P3", dataNascimento));
		                                params.add(new BasicNameValuePair("P4", edtAltCpf.getText().toString()));    
		                                params.add(new BasicNameValuePair("P5", "" + Global.usuario.getUsuarioId())); 
		                                
		                                
		                                //Buscando Informações - Banco de dados remoto                        
		                              	JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params); 
		                              	
		                                int sucesso = json.getInt("sucesso");
		                                
	                                   if (sucesso == 1) {
	                                   	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	                                   	  DALUsuario dalUsuario = new DALUsuario(getBaseContext());                                      	 
	                                   	  Global.usuario.setNome(edtAltNome.getText().toString());
	                                   	  Global.usuario.setEmail(edtAltEmail.getText().toString());
	                                   	  Global.usuario.setDataNascimento(dateFormat.parse(dtAltDataNascimento.getYear() + "-" + 
	                                   			  											mes + "-" + 
												                                   			dtAltDataNascimento.getDayOfMonth()));
	                                   	  Global.usuario.setCPF(edtAltCpf.getText().toString());
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
		  				Toast.makeText(AlteracaoCadastroActivity.this, "Alteração concluída com sucesso!", Toast.LENGTH_LONG).show();		
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
        progressDialog = new ProgressDialog(AlteracaoCadastroActivity.this);
        progressDialog.setTitle(titulo);
        progressDialog.setMessage("Processando...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
   }
  
    @Override 
    public void onBackPressed(){
    	Intent intent = new Intent(getApplicationContext(), EstabelecimentoActivity.class); 
   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.alteracao_cadastro, menu);
		return true;
	}

}
