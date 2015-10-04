package com.newconex.conex;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.newconex.conex.R;
import com.newconex.conex.JSONParser;

public class MainActivity extends Activity {
	EditText txtUsuario;
    EditText txtSenha;
    CheckBox cbManterConectado;
    private ProgressDialog progressDialog;
    Context context = this;
    Handler handler;
    
	private DataHelper dh; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnCriarConta = (Button)findViewById(R.id.btnCriarConta);
        Button btnEntrar = (Button)findViewById(R.id.btnEntrar);
        ImageButton btnSobre = (ImageButton)findViewById(R.id.btnSobre);
        
        txtUsuario = (EditText)findViewById(R.id.txtUsuario);
        txtSenha = (EditText)findViewById(R.id.txtSenha);
        cbManterConectado = (CheckBox)findViewById(R.id.cbManterConectado);

 	    dh = new DataHelper(getBaseContext());
 	    
 	    Usuario usuario = dh.selectUltimoUsu();
 	    if(usuario != null){

	 	    txtUsuario.setText(usuario.getUsuarioUsu());
 	    	if(usuario.getManterConUsu().equals("S")){
 	 	    	Globais.usuario = usuario;
 	 	    	txtSenha.setText(Globais.usuario.getSenhaUsu());
 	 	    	cbManterConectado.setChecked(true);
				carregaInformacoes();
 	    		
 	    	}
 	    	
 	    }
 	    
        
        btnCriarConta.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				  //Chamar site do conex pra incrição
				  Intent viewIntent = new Intent("android.intent.action.VIEW", 
						  Uri.parse("http://www.newconex.heliohost.org/inscricao.php"));
				  startActivity(viewIntent);				
			}
		});
        
        
        btnSobre.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				context.startActivity( new Intent(context, com.newconex.conex.SobreActivity.class));
			}
		});
        
        
        btnEntrar.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				//Verificando se o usuário foi informado
				if(txtUsuario.getText().toString().equals("")){
					Toast.makeText(MainActivity.this, "Informe o usuário!", Toast.LENGTH_LONG).show();
					
					return;
				}
				
				//Verificando se a senha foi informando
				if(txtSenha.getText().toString().equals("")){
					Toast.makeText(MainActivity.this, "Informe a senha!", Toast.LENGTH_LONG).show();
					return;
				}
				
				carregaInformacoes();
				
			}
		});
    }


    @Override
    public void onResume() {
        super.onResume();
        if(Globais.usuario != null){
        	if(Globais.usuario.getManterConUsu().equals("S"))
        		cbManterConectado.setChecked(true);
        	else
        		cbManterConectado.setChecked(false);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
   
    public void Progresso(String titulo){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle(titulo);
        progressDialog.setMessage("Processando...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
   }
    
    public void gravaFoto(String fotoS, String filename){
    	try { 
    		byte[] encodeByte  = Base64.decode(fotoS, Base64.DEFAULT); 
    		Bitmap bmImg = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
    		bmImg = Bitmap.createBitmap(bmImg);
           
    		File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File dest = new File(sd, filename);	              	
	            
	    	if (!sd.isDirectory()){
	    		sd.mkdir();
	    	}
	        
	    	FileOutputStream out = new FileOutputStream(dest);
	        bmImg.compress(Bitmap.CompressFormat.PNG, 90, out);
	        out.flush();
	        out.close();
   	                 
	            	
	    } catch (Exception e) {
	         e.printStackTrace();
	    }
	    
    	
    	
    }

    
    @SuppressLint("HandlerLeak")
	public void carregaInformacoes() {
        try {      
        	
        	Progresso("Aguarde");
        	 new Thread(new Runnable() {
        	        public void run() {        	        	
        	        	  try {

                              	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
                        		//Abrindo Classe JSon        	        		  
                                JSONParser jsonParser = new JSONParser();
                              	Message msg=new Message(); 
                              	
                              	boolean conexao = jsonParser.conectado(context);
                                //Verifico se tenho conexão com a internet
                              	//conexao = true;
                                if(conexao){
	                                //Definindo endereço onde será buscado as informações para o login
	                                String httpGet = "http://www.newconex.heliohost.org/loginA.php";
	                              	//Parametros a serem passados para o PHP
	                                List<NameValuePair> params = new ArrayList<NameValuePair>();
	                                params.add(new BasicNameValuePair("P1", txtUsuario.getText().toString()));
	                                params.add(new BasicNameValuePair("P2", txtSenha.getText().toString()));             
	                                //Buscando Informações - Banco de dados remoto                        
	                              	JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params); 
	                              	                             	                              	 
	                              	 try {
	                                       // Checking for SUCCESS TAG
	                                       int success = json.getInt("sucesso");
	                        
	                                       if (success == 1) {
	
	                                    	  
	                                    	   //CARREGA USUÁRIO /////////////////////////////////
	                                    	  //////////////////////////////////////////////////// 
	                                    	  JSONArray usuarioArr = json.getJSONArray("usuario"); 
	                                          for (int i = 0; i < usuarioArr.length(); i++) {
	                                               JSONObject c = usuarioArr.getJSONObject(i);
	                                               Usuario user = new Usuario();
	                                               user.setCodigoUsu(Integer.parseInt(c.getString("codigoUsu")));
	                                               user.setUsuarioUsu(c.getString("usuarioUsu"));         
	                                               user.setSenhaUsu(c.getString("senhaUsu"));                         
	                                               user.setNomeUsu(c.getString("nomeUsu"));                                                                        
	                                               user.setDataNascUsu(dateFormat.parse(c.getString("dataNascUsu")));
	                                               user.setCodigoPaisUsu(Integer.parseInt(c.getString("codigoPaisUsu")));        
	                                               user.setEmailUsu(c.getString("emailUsu"));  
	                                               user.setSexoUsu(c.getString("sexoUsu"));     
	                                               user.setDataCadUsu(dateFormat.parse(c.getString("dataCadUsu")));                                             
	                                               Date data = new Date();
	                                               user.setDataLoginUsu(dateFormat.parse(dateFormat.format(data.getTime())));
	                                               
	                                               if(cbManterConectado.isChecked())
	                                            	   user.setManterConUsu("S");
	                                               else
	                                            	   user.setManterConUsu("N");
	                                               
	                                               
	                                               Globais.usuario = user;
	 	                                           Globais.usuario.setNomeFotoUsu("");
	                                               ////Deleta contatos do usuário para carrega-los novamente
	                                               dh.deletaContatoUsu(user);                                               
	                                           }
	                                    	  //////////////////////////////////////////////////// 
	                                          
	                                          //CARREGA FOTO     /////////////////////////////////
	                                    	  //////////////////////////////////////////////////// 
	                                    	  JSONArray fotoUsuarioArr = json.getJSONArray("fotoUsuario"); 
	                                          for (int i = 0; i < fotoUsuarioArr.length(); i++) {
	                                               JSONObject c = fotoUsuarioArr.getJSONObject(i);
	                                               
	                                               if(c.getInt("qtdFto") > 0){
	                                            	   String filename = c.getString("nomeFto") + "." + c.getString("tipoFto");    
	                                            	   Globais.usuario.setNomeFotoUsu(filename);	                 	        	               
	                                            	   gravaFoto(c.getString("foto"), filename);
	                                               }
	                                           }
	                                          

                                              dh.atualizaUsuario(Globais.usuario);
	                                    	  //////////////////////////////////////////////////// 
	                                          
	                                          
	                                          //CARREGA CONTATOS /////////////////////////////////
	                                    	  //////////////////////////////////////////////////// 
	                                    	  JSONArray contatoArr = json.getJSONArray("contatos"); 
	                                    	  for (int i = 0; i < contatoArr.length(); i++) {
	                                               JSONObject c = contatoArr.getJSONObject(i);                                               
	                                               Contato contato = new Contato();
	                               				   contato.setCodigoCtt(Integer.parseInt(c.getString("codigoCtt")));
	                               				   contato.setCodigoUsuLocalCtt(Integer.parseInt(c.getString("codigoUsuLocalCtt")));                               				   
	                               				   contato.setStatusCtt(c.getString("statusCtt"));     
	                               				   contato.setCodigoUsuCtt(Integer.parseInt(c.getString("codigoUsuCtt")));                                   				   
	                               				   contato.setUsuarioUsuCtt(c.getString("usuarioUsuCtt"));                                      				   
	                               				   contato.setNomeUsuCtt(c.getString("nomeUsuCtt"));                                        				 
	                                               dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");                                      
	                                               contato.setDataNascUsuCtt(dateFormat.parse(c.getString("dataNascUsuCtt")));                                  				   
	                               				   contato.setEmailUsuCtt(c.getString("emailUsuCtt"));                                    				   
	                               				   contato.setSexoUsuCtt(c.getString("sexoUsuCtt"));                                    
	                                               contato.setDataCadUsuCtt(dateFormat.parse(c.getString("dataCadUsuCtt"))); 
	                                               contato.setNomeFotoCtt("");
	                                             
	                                               //foto contato////////////////////////////////////////////
	                                               if(c.getDouble("qtdFoto") > 0){
	                                            	   String filename = c.getString("nomeFto") + "." + c.getString("tipoFto");     
		                 	        	               contato.setNomeFotoCtt(filename);
			                 	        	           gravaFoto(c.getString("foto"), filename);
			                                             
	                                               }
	                                               dh.insertContato(contato);	                                               
		              	  	        	           ///////////////////////////////////////////////////////
	                                           }
	                                    	  
	                                    	  //CARREGA MENSAGEM /////////////////////////////////
	                                    	  //////////////////////////////////////////////////// 
	                                    	  JSONArray msgArr = json.getJSONArray("msg"); 
	                                          for (int i = 0; i < msgArr.length(); i++) {
	                                               JSONObject c = msgArr.getJSONObject(i);
	                                               Mensagem mensagem = new Mensagem();	                                               
	                                               mensagem.setCodigoMsg(Integer.parseInt(c.getString("codigoMsg")));	     
	                                               mensagem.setUsuOrig(dh.selectContato(Integer.parseInt(c.getString("codigoUsuOriMsg"))));
	                                               mensagem.setUsuDestino(null);
	                                               mensagem.setDataMsg(dateFormat.parse(c.getString("dataMsg")));  
	                                               mensagem.setTextoMsg(c.getString("textoMsg"));     
	             	           	   	        	   mensagem.setStatusLida("N");                     
	                                               dh.insertMsg(Globais.usuario, mensagem);
	                                   			                         
	                                           }
	                                    	  //////////////////////////////////////////////////// 
	                                    	  
	                                          msg.what=1;//Deu tudo certo
	                       	        	   	  handler.sendMessage(msg);
	                                       }
	                                       else{
	                                            msg.what=0;//Deu errado
	                         	        	  	handler.sendMessage(msg);
	                         	        	  
	                                       }
	                                       
	                                   } catch (JSONException e) {
	                                       e.printStackTrace();
	                                   }
        	        	  			}
                                	else{
                                		//Nâo tenho conexão com a internet
                                		Usuario usuario = new Usuario();
                                		usuario.setUsuarioUsu(txtUsuario.getText().toString());
                                		usuario.setSenhaUsu(txtSenha.getText().toString());
                                		Globais.usuario  = dh.autenticaUsu(usuario);
                                		if(Globais.usuario == null)
                                			msg.what=2;//Deu errado
                                		else{
                                			 Date data = new Date();
                                			 Globais.usuario.setDataLoginUsu(dateFormat.parse(dateFormat.format(data.getTime())));
                                             
                                             if(cbManterConectado.isChecked())
                                            	 Globais.usuario.setManterConUsu("S");
                                             else
                                            	 Globais.usuario.setManterConUsu("N");
                                             
                                             dh.atualizaUsuario(Globais.usuario);
                                			msg.what=1;//Deu tudo certo
                                		}

                     	        	  	handler.sendMessage(msg);
                                	}
                   	        	
                              } catch (Exception e) {
                                e.printStackTrace();
                              }        	              
                    	 	
        	        }
        	    }).start();
       	  
          handler=new Handler(){
        		 public void handleMessage(Message msg){

        			 progressDialog.cancel();
        			 if(msg.what==0){
        				 Toast.makeText(MainActivity.this, "Usuário ou senha inválidos!", Toast.LENGTH_LONG).show();        
        		 	}
        			else if (msg.what==1){        		                		       
        		        context.startActivity( new Intent(context, com.newconex.conex.ContatoActivity.class));
        			 }
        			 else if(msg.what==2){
        				 Toast.makeText(MainActivity.this, "Você está sem conexão com a internet. Ou o usuário não existe no banco local ou a usuário ou e senha são inválidos.", Toast.LENGTH_LONG).show();        
        		 	}
        		 }
        	};
       

        } catch (Exception e) {
          e.printStackTrace();
        }
      }

}
