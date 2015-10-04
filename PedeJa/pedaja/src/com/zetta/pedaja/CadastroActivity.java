package com.zetta.pedaja;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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

public class CadastroActivity extends Activity {


	private EditText edtNome;
	private EditText edtEmail;
	private DatePicker dtDataNascimento;
	private EditText edtCpf;
	private EditText edtCadUsu;
	private EditText edtCadSenha;
	private EditText edtCadConfSenha;
	private Button btnNovoUsuario;
    private ProgressDialog progressDialog;
    private Context context = this;
    private Handler handler;
    private String msgErro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro);
		
		edtNome =  (EditText)findViewById(R.id.edtNome);
		edtEmail =  (EditText)findViewById(R.id.edtEmail);
		dtDataNascimento =  (DatePicker)findViewById(R.id.dtDataNascimento);
		edtCpf =  (EditText)findViewById(R.id.edtCpf);
		edtCadUsu =  (EditText)findViewById(R.id.edtCadUsu);
		edtCadSenha =  (EditText)findViewById(R.id.edtCadSenha);
		edtCadConfSenha =  (EditText)findViewById(R.id.edtCadConfSenha);
		btnNovoUsuario =  (Button)findViewById(R.id.btnNovoUsuario);
		
	     btnNovoUsuario.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				CadastrarUsuario();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cadastro, menu);
		return true;
	}

    @SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
	public void CadastrarUsuario(){
		
		try {
			
			if(edtNome.getText().toString().equals("")){
				mostrarMensagem("Informe o nome!", "Aviso");				
				return;
			}
			
			if(edtEmail.getText().toString().equals("")){
				mostrarMensagem("Informe o e-mail!", "Aviso");				
				return;
			} 
			
			
			if(edtCadUsu.getText().toString().equals("")){
				mostrarMensagem("Informe o usuário!", "Aviso");				
				return;
			} 
			
			if(edtCadSenha.getText().toString().equals("")){
				mostrarMensagem("Informe a senha!", "Aviso");				
				return;
			} 
						
			if(edtCadConfSenha.getText().toString().equals("")){
				mostrarMensagem("Informe a confirmação da senha!", "Aviso");				
				return;
			} 			
			
			if(!edtCadSenha.getText().toString().equals(edtCadConfSenha.getText().toString())){
				mostrarMensagem("Senha e confirmação diferentes!", "Aviso");				
				return;
			} 
			
			
			Progresso("Aguarde");
		       	 new Thread(new Runnable() {
		       	        public void run() {        	        	
		       	        	  try {
		       	        		  
		       	        		 JSONParser jsonParser = new JSONParser();
	                             Message msg = new Message();
	                             boolean conexao = jsonParser.conectado(context);
	                             if(conexao){
	                            	 
	                            	 String dataNascimento =  dtDataNascimento.getDayOfMonth() + "/" +
	                            			 				  dtDataNascimento.getMonth() + "/" +
	                            			 				  dtDataNascimento.getYear();
	                            	 
		                                String httpGet = Global.urlConexaoServidor + "cadastro.php";
		                                List<NameValuePair> params = new ArrayList<NameValuePair>();
		                                params.add(new BasicNameValuePair("P1", edtNome.getText().toString()));
		                                params.add(new BasicNameValuePair("P2", edtEmail.getText().toString()));    
		                                params.add(new BasicNameValuePair("P3", dataNascimento));
		                                params.add(new BasicNameValuePair("P4", edtCpf.getText().toString()));    
		                                params.add(new BasicNameValuePair("P5", edtCadUsu.getText().toString()));
		                                params.add(new BasicNameValuePair("P6", edtCadSenha.getText().toString()));    
		                                
		                                
		                                //Buscando Informações - Banco de dados remoto                        
		                              	JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params); 
		                              	
		                                int sucesso = json.getInt("sucesso");
		                                
                                        if (sucesso == 1) {
                                        	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
                                        	  DALUsuario dalUsuario = new DALUsuario(getBaseContext());                                      	 
	                                    	  JSONArray usuarioArr = json.getJSONArray("usuario"); 
	                                          for (int i = 0; i < usuarioArr.length(); i++) {
	                                               JSONObject c = usuarioArr.getJSONObject(i);
	                                               ModelUsuario user = new ModelUsuario();
	                                               user.setUsuarioId(Integer.parseInt(c.getString("UsuarioId")));
	                                               user.setNome(c.getString("Nome"));         
	                                               user.setEmail(c.getString("Email"));                                      
	                                               user.setDataNascimento(dateFormat.parse(c.getString("DataNascimento")));
	                                               user.setCPF(c.getString("CPF"));     
	                                               user.setDataCriacao(dateFormat.parse(c.getString("DataCriacao")));	                                               
	                                               user.setUsuario(c.getString("Usuario")); 
	                                               user.setSenha(c.getString("Senha")); 	                                               
	                                               Date data = new Date();
	                                               user.setUltimoAcesso(dateFormat.parse(dateFormat.format(data.getTime())));	                                               
	                                               
	                                               dalUsuario.atualizaUsuario(user);	                                               
	                                               Global.usuario = user;                                           
	                                               
	                                          }
	                                           
	                                          dalUsuario = null;
	                                          Global.AtualizarCidade(getBaseContext());
	                                          msg.what = 1;
	                                          handler.sendMessage(msg);
                                    	   
                                        }else {
      	                            	 
      	                            	 	msg.what = 0;
      	                            	 	msgErro = json.getString("mensagem");
                        	        	  	handler.sendMessage(msg);
      	                            	 
      	                             	}
	                            	 
	                            	 
	                             } else {
	                            	 
	                            	 msg.what=0;
	                            	 msgErro = "Sem conexão com a internet. Conecte-se!";
                  	        	  	 handler.sendMessage(msg);
	                            	 
	                             }
	                             
	                             
		       	        	  }
		       	        	  catch (Exception e) {
		                          e.printStackTrace();
		                        }        	              
		              	 	
		  	        }
		  	    }).start();
		 	  
		    handler=new Handler(){
		  		 public void handleMessage(Message msg){
		
		  			 progressDialog.cancel();
		  			 if(msg.what==1)
		  			 {
		  				Global.telaCadatro = true;
						startActivity(new Intent(CadastroActivity.this,CidadeActivity.class));  
		  		 	 }
		  			 else if (msg.what==0){        		                		       
		  				mostrarMensagem(msgErro, "Erro");				
						return;
		  			 }
		  		 }
		  	};
					
			
		} catch (Exception ex){
            ex.printStackTrace();
		}
		
		
		
	}


	private void mostrarMensagem(String msg, String titulo) {
		new AlertDialog.Builder(this)
				.setTitle(titulo)
				.setMessage(msg)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("OK", null).show();
	
	}
	
	
    public void Progresso(String titulo){
        progressDialog = new ProgressDialog(CadastroActivity.this);
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
	

}
