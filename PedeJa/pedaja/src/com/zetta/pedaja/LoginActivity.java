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
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	Button btnNovoUsuario;
	Button btnEntrar;	
	EditText edtUsuario;
	EditText edtSenha;	
	String email;
	Context context = this;
    private Handler handler;
    private String msgErro;
	private ProgressDialog progressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btnNovoUsuario = (Button)findViewById(R.id.btnNovoUsuario);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);
        edtUsuario = (EditText)findViewById(R.id.edtUsuario);
        edtSenha = (EditText)findViewById(R.id.edtSenha);
        
        if(Global.primeiraVez){
        	carregarUsuario();
        }
        
        
        
        btnNovoUsuario.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
			}
		});
        
        btnEntrar.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				efetuarLogin();
			}
		});
        
        
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try
        {      	        	
        	menu.add(0,0,0,"Esqueci usuário ou senha");       
        }
        catch (Exception e) {
            e.printStackTrace();
        }            
        return super.onCreateOptionsMenu(menu);        
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 	   switch (item.getItemId()) {
 	   	  case 0:{
 		   		EsqueciUsuarioSenha();
 				break;    
 	   	  }
 	   }

 	   return true;
    }
    
    @SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
	public void efetuarLogin(){
    	
    	if(edtUsuario.getText().toString().equals("")){
			mostrarMensagem("Informe o usuário!", "Aviso");				
			return;
		}	
    	
    	if(edtSenha.getText().toString().equals("")){
			mostrarMensagem("Informe o usuário!", "Aviso");				
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
	       	        		   
	       	        		   String httpGet = Global.urlConexaoServidor + "login.php";
                               List<NameValuePair> params = new ArrayList<NameValuePair>();
                               params.add(new BasicNameValuePair("P1", edtUsuario.getText().toString()));
                               params.add(new BasicNameValuePair("P2", edtSenha.getText().toString()));
                               JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params);
                               boolean novoUsuario = false;
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
                                        
                                        if(c.getString("CidadeId") != "")
                                        	user.setCidadeId(Integer.parseInt(c.getString("CidadeId")));                                       
                                        
                                        user.setUsuario(c.getString("Usuario")); 
                                        user.setSenha(c.getString("Senha")); 	                                               
                                        Date data = new Date();                                        
                                        user.setUltimoAcesso(dateFormat.parse(dateFormat.format(data.getTime())));    
                                        novoUsuario = dalUsuario.atualizaUsuario(user);	                                               
                                        Global.usuario = user;                                           
                                        
                                   }
                                   
                                   dalUsuario = null;
                                   
                                   if(novoUsuario)
                                	   Global.AtualizarCidade(context);
                                   
                                   if(Global.usuario.getCidadeId() != 0)
                                	   msg.what = 1;
                                   else 
                                	   msg.what = 2;
                                   
                               }
                               else {
                            	   msg.what = 0;
                            	   msgErro = "Usuário ou senha inválidos!";            	   
                               }
	       	        			
	       	        		}
	       	        		else {
	       	        			
	       	        			DALUsuario db = new DALUsuario(context);
	       	        			Global.usuario = db.efetuaLogin(edtUsuario.getText().toString(), edtSenha.getText().toString());
	       	        			
	       	        			if(Global.usuario == null){	       	        			
	                         	   msg.what = 0;
	                         	   msgErro =  "Você está sem conexão com a internet. Ou o usuário não existe no banco local ou a usuário ou/e senha são inválidos.";
                         	    }
	       	        			else {
	       	        				if(Global.usuario.getCidadeId() != 0)
                                	   msg.what = 1;
                                    else 
                                	   msg.what = 2;
	       	        			}
                                                            	   
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
	  			 if(msg.what==1) //Usuario logado Com cidade 
	  			 {       				

					Intent intent = new Intent(getApplicationContext(), EstabelecimentoActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
	  				
	  		 	 }
	  			 else if(msg.what==2) //Usuario logado Sem cidade
	  			 {	  				

					Intent intent = new Intent(getApplicationContext(), CidadeActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
	  				
	  		 	 }
	  			 else if (msg.what==0){        		                		       
	  				mostrarMensagem(msgErro, "Aviso");				
					return;
	  			 }
	  		 }
	  	};
		
    	
		
    	
    }
    
	private void mostrarMensagem(String msg, String titulo) {
		new AlertDialog.Builder(this)
				.setTitle(titulo)
				.setMessage(msg)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("OK", null).show();
	
	}
	
	private void carregarUsuario(){
		try {
			DALUsuario db = new DALUsuario(context);
			ModelUsuario usuario = db.selecionaUltimoUsuario();
			if(usuario != null){
				Global.usuario = usuario;
				Intent intent =  null;
				if(Global.usuario.getCidadeId() == 0)
					intent = new Intent(getApplicationContext(), CidadeActivity.class);
				if(Global.empresa != null )
					intent = new Intent(getApplicationContext(), HomeActivity.class);
				else
					 intent = new Intent(getApplicationContext(), EstabelecimentoActivity.class);
				
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();			
		}		
	}
	
	
	
	@SuppressLint("HandlerLeak")
	private void EsqueciUsuarioSenha(){
		try{
			
			final AlertDialog.Builder alert = new AlertDialog.Builder(context);
		    final EditText input = new EditText(context);
		    //input.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
		    input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		    alert.setView(input);
		    alert.setTitle("Digite o e-mail informado no cadastro.");
		    alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            email = input.getText().toString().trim();
		            
		            
			      	Progresso("Aguarde");
			       	 new Thread(new Runnable() {
			       	        public void run() {     
			       	        	  Message msg = new Message();
			       	        	  try {
			       	        		 
			       	        		if(email.equals(""))
			       	        		{			       	            	 
			                           	 msg.what=0;
			                           	 msgErro = "Informe o e-mail!";	
			       	        			
			       	        		}	
			       	        		else {
			       	        		  
				       	        		JSONParser jsonParser = new JSONParser();		                           
			                            boolean conexao = jsonParser.conectado(context);
			                            if(conexao){		                           	 
			                            
				                                String httpGet = Global.urlConexaoServidor + "esqueciUsuarioSenha.php";
				                                List<NameValuePair> params = new ArrayList<NameValuePair>();
				                                params.add(new BasicNameValuePair("P1", email));			                                
				                                
				                              	JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params);
				                                int sucesso = json.getInt("sucesso");			                                
			                                    if (sucesso == 1) 
			                                      msg.what = 1; 
			                                    else {	 	                            	 
			 	                            	 	msg.what = 0;
			 	                            	 	msgErro = json.getString("mensagem");   
			 	                             	}
			                           	 
			                           	 
			                            } else {	                           	 
				                           	 msg.what=0;
				                           	 msgErro = "Sem conexão com a internet. Conecte-se!";	                           	 
			                            }
			       	        		}
		                            
			       	        	  }
			       	        	  catch (Exception e) {
			                          e.printStackTrace();	 
			                          msg.what=0;
			                      }    
			       	        	  handler.sendMessage(msg);
			  	        }
			  	    }).start();
			 	  
			    handler=new Handler(){
			  		 public void handleMessage(Message msg){
			
			  			 progressDialog.cancel();
			  			 if(msg.what==1)
			  			 {
			  				mostrarMensagem("Usuário/Senha enviados com sucesso!", "Concluído");	
			  		 	 }
			  			 else if (msg.what==0){        		                		       
			  				mostrarMensagem(msgErro, "Erro");				
							return;
			  			 }
			  		 }
			  	};
		            
		        }
		    });

		    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            dialog.cancel();
		        }
		    });
		    alert.show();
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
  public void Progresso(String titulo){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle(titulo);
        progressDialog.setMessage("Processando...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
   }
	
	
	
    
	 
}
