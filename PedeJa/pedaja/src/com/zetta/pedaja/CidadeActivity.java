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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class CidadeActivity extends Activity {

    Spinner spEstado;    
    Spinner spCidade;    
    Button btnCidConfirmar;
    Context context = this;
    private int CidadeId;
    private ProgressDialog progressDialog;
    private Handler handler;
    private String msgErro;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cidade);
		spEstado = (Spinner) findViewById(R.id.spEstado);
		spCidade = (Spinner) findViewById(R.id.spCidade);		
		btnCidConfirmar = (Button) findViewById(R.id.btnCidConfirmar);		
		carregaEstado();			
		
		btnCidConfirmar.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				confirmarCidade();
			}
		});
		
		
	}
	
	
	public void confirmarCidade() { 		
		try {
			if(CidadeId == 0)
				Toast.makeText(CidadeActivity.this, "Uma cidade deve ser selecionada!", Toast.LENGTH_LONG).show();
			else {				

				Progresso("Aguarde");
		       	 new Thread(new Runnable() {
		       	        public void run() {        	        	
		       	        	  try {

	                            JSONParser jsonParser = new JSONParser();   
		       	        		Message msg = new Message();
		       	        		boolean conexao = jsonParser.conectado(context);
	                             
		       	        		if(conexao){     	
	 		       	                List<NameValuePair> params = new ArrayList<NameValuePair>();		       	        		
	 		       	        		String httpGet = Global.urlConexaoServidor + "atualizaUsuario.php";   
	 		       	                String UsuarioId = "" + Global.usuario.getUsuarioId();
	 		       	                String Cidade = "" + CidadeId;
	 		       	                params.add(new BasicNameValuePair("P1", UsuarioId));
	 		       	                params.add(new BasicNameValuePair("P2", Cidade));    
	 		       	            	JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params);               	
	 		       	                int sucesso = json.getInt("sucesso");
	 		       					if(sucesso == 1) {
	 		       						Global.usuario.setCidadeId(CidadeId);
	 		       						Global.usuario.setUltimaAtu(null);
	 		       						DALUsuario dbUsuario = new DALUsuario(context);
	 		       						dbUsuario.atualizaUsuario(Global.usuario);
	 			       					 msg.what = 1;
	 		       					}
	 		       					else {
		                            	msg.what = 0;
		                            	msgErro = "Problemas ao atualizar Cidade!";	 		       						
	 		       					}

	                                 handler.sendMessage(msg);
	                            	 
	                            } else {
	                            	 msg.what = 2;
	                            	 msgErro = "Sem conexão com a internet!";
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
							Intent intent = new Intent(getApplicationContext(), EstabelecimentoActivity.class); 
					   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
			  		 	 }
			  			 else if(msg.what==0) 
			  				Toast.makeText(CidadeActivity.this, msgErro, Toast.LENGTH_LONG).show();
			  			else if(msg.what==2) 
			  			{
			  				Toast.makeText(CidadeActivity.this, msgErro, Toast.LENGTH_LONG).show();
			  				Intent intent = new Intent(getApplicationContext(), EstabelecimentoActivity.class);
				  			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				  			startActivity(intent);
			  				
			  			}
			  			 
			  			  
			  		 }
			  	};
			  	
			  	
                
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
			
		
	}
	
	public void carregaEstado(){
		DALEstado dbEstado = new DALEstado(context);		
		ArrayList<ModelEstado> lstEstado = dbEstado.selecionaEstado();
		dbEstado = null;
		AdapterEstado adpEstado = new AdapterEstado(lstEstado, context);
		spEstado.setAdapter(adpEstado);
		spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {	
			public void onItemSelected(AdapterView<?> parent, View arg1, int posicao, long id) {

				try{
					ModelEstado estado = (ModelEstado) parent.getItemAtPosition(posicao);
					carregaCidade(estado);  
				} catch (Exception ex) {
		        	ex.printStackTrace();
		        }
				 				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
	 
	       
	    });		
	}
	
	
	public void carregaCidade(ModelEstado estado){	
		if(estado != null) {
			try{
				 DALCidade dbCidade = new DALCidade(context);		
				 ArrayList<ModelCidade> lstCidade = dbCidade.selecionarCidadeEst(estado.getEstadoId());
				 AdapterCidade adpCidade = new AdapterCidade(lstCidade, context);
				 dbCidade  = null;
				 spCidade.setAdapter(adpCidade);
				 spCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		
					public void onItemSelected(AdapterView<?> parent, View arg1, int posicao, long id) {
					
						try{
							ModelCidade cidade  = (ModelCidade) parent.getItemAtPosition(posicao);
							CidadeId = cidade.getCidadeId();						
					    } catch (Exception ex) {
				        	ex.printStackTrace();
				        }
					}
					
					public void onNothingSelected(AdapterView<?> arg0) {
						
					}						 
				 });
		 	} catch (Exception ex) {
	        	ex.printStackTrace();
	        }
		 }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,0,0,"Atualizar Cidades"); 
		return true;
	}
	
	@SuppressLint("HandlerLeak")
	@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
    	   switch (item.getItemId()) {
    	      case 0:{
    	    	  
    	    	  Progresso("Aguarde");
	 		       	 new Thread(new Runnable() {
 		       	        public void run() {        	        	
 		       	        	  try {

 		       	        		JSONParser jsonParser = new JSONParser();   
 		       	        		Message msg = new Message();
 		       	        		boolean conexao = jsonParser.conectado(context);
 	                             
 		       	        		if(conexao){     
 		       	        		  	Global.AtualizarCidade(context);
 		       	        			msg.what = 1;
 		       	        		}
 		       	        		else 
 		       	        			msg.what = 0;
 		       	        		

                                 handler.sendMessage(msg);
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
				  				Toast.makeText(CidadeActivity.this, "Atualizado com sucesso!", Toast.LENGTH_LONG).show();
				    	    	carregaEstado();
				  		 	 }
				  			 else
				  				Toast.makeText(CidadeActivity.this, "Sem Conexão com a internet! Impossivel atualizar.", Toast.LENGTH_LONG).show();
				  		 }
				  	};
    	      }
    	         
    	   }
    	   return true;
    	}

	  public void Progresso(String titulo){
	        progressDialog = new ProgressDialog(CidadeActivity.this);
	        progressDialog.setTitle(titulo);
	        progressDialog.setMessage("Processando...");
	        progressDialog.setIndeterminate(true);
	        progressDialog.setCancelable(true);
	        progressDialog.show();
	   }
	  
	  @Override 
	   public void onBackPressed(){
		   Intent intent = null;
		   if(Global.telaCadatro)
			   intent = new Intent(getApplicationContext(), CadastroActivity.class);
		   else 
			   intent = new Intent(getApplicationContext(), EstabelecimentoActivity.class); 
		   			
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
	 	  	    	
	   }
	  
	  
}
