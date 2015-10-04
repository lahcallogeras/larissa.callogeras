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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class EncerrarContaActivity extends Activity {

    Spinner spModoPagamento;  
    Button btnEncConfirmar;
    Context context = this;
    int ModoSelecionado;
    private Handler handler;
	private ProgressDialog progressDialog;
	String msgErro;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.encerrar_conta);

		spModoPagamento = (Spinner) findViewById(R.id.spModoPagamento);		
		btnEncConfirmar = (Button) findViewById(R.id.btnEncConfirmar);

		btnEncConfirmar.setOnClickListener(btnEncConfirmarOnClick);		
		carreModoPagamento();
		
	}
	
	private OnClickListener btnEncConfirmarOnClick = new OnClickListener() {			
		public void onClick(View v) {
			mostraMsg();		
			
		}
	};
	
	public void mostraMsg(){
		new AlertDialog.Builder(this)
		.setTitle("Fechar conta.")
		.setMessage("Confirma o encerramento da conta?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("Confirmar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						encerrarConta();
					}
				})
		.setNegativeButton("Cancelar", null).show();
	}
	

	@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
	public void encerrarConta(){		
			Progresso("Aguarde");
	      	 new Thread(new Runnable() {
	      		 	Message msg = new Message();
	      	        public void run() {        	        	
	     	            try {	      
	     	            	
	     	            	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
	     	            	JSONParser jsonParser = new JSONParser();   
	      	        		
	      	        		boolean conexao = jsonParser.conectado(context);
	                    
	      	        		if(conexao){	       	        			
	      	        			
	      	        			
		       	        		String httpGet = Global.urlConexaoLocal + "fecharConta.php";
	                            List<NameValuePair> params = new ArrayList<NameValuePair>();
	                            params.add(new BasicNameValuePair("P1", "" + Global.conta.getContaId() ));
	                            params.add(new BasicNameValuePair("P2", "" + ModoSelecionado));
	                            params.add(new BasicNameValuePair("P3", "" + Global.empresa.getEmpresaId()));
	                            JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params);
	                            
	                            int sucesso = json.getInt("sucesso");
	                            if(sucesso == 1){	                            	 
	                           	 
	                           	 	 DALConta dbConta = new DALConta(context);
	                           	 	  ModelConta conta = new ModelConta();
			                      	 conta.setContaId(Global.conta.getContaId());
			                      	 conta.setModoPagamentoId(ModoSelecionado);
			                      	 Date data = new Date();                                        
			                      	 conta.setDataEncerramento(dateFormat.parse(dateFormat.format(data.getTime())));    
			                      	 dbConta.encerraConta(conta);
			                      	 
			                      	 JSONArray itemContaArr = json.getJSONArray("itemConta"); 
			      			         for (int i = 0; i < itemContaArr.length(); i++) {
			      			             JSONObject c = itemContaArr.getJSONObject(i);
			      			             ModelItemConta itemConta = new ModelItemConta();
			      			             itemConta.setItensContaId(Integer.parseInt(c.getString("ItensContaId")));
			      			             itemConta.setContaId(Integer.parseInt(c.getString("ContaId")));
			      			             itemConta.setDataPedido(dateFormat.parse(c.getString("DataPedido")));
			      			             itemConta.setTipoProdutoId(Integer.parseInt(c.getString("TipoProdutoId")));
			      			             itemConta.setSequencia(Integer.parseInt(c.getString("Sequencia")));
			      			             itemConta.setValor(Double.parseDouble(c.getString("Valor")));
			      			           	 itemConta.setStatusPedido(c.getString("StatusPedido"));
			      			           	 itemConta.setDivisao(c.getString("Divisao"));
			      			           
			      			             dbConta.insereItemConta(itemConta);
			      			         }
			                      	 
			                      	 
		                      	     dbConta = null;
		                      	     
	                            } else {
		       	        			msg.what = 0;
		       	        			msgErro = "Erro ao encerrar conta.";		                            	 
	                            }
	                            
	      	        			
	                     	   msgErro = "Conta encerrada com sucesso!";
	                     	   msg.what = 1;
	                     	   	 
	      	        		} else {
	      	        			msg.what = 0;
	      	        			msgErro = "Se conecte a internet para encerrar a conta.";
	      	        		}	       	        		
	     	        }
	     	      	catch (Exception e) {
	     	      		msg.what = 0;
		        			msgErro = "Erro ao encerrar conta.";
	                   e.printStackTrace();
	              }  
	     	        handler.sendMessage(msg);    
	        	 	
	        }
	    }).start(); 	  
	   handler=new Handler(){
	 		 public void handleMessage(Message msg){
	 			try{
	 				progressDialog.cancel();
		  			 Toast.makeText(EncerrarContaActivity.this, msgErro, Toast.LENGTH_LONG).show();
		  			 
		  			 if(msg.what == 1){
		  				Global.encerrarConta = true;			  				 
		  				Intent intent = new Intent(getApplicationContext(), VisualizarContaActivity.class); 
		  		   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  				startActivity(intent);
		  			 }
	 				
				} catch  (Exception ex) {
					ex.printStackTrace();
				}	 
	 		 }
	 	};	  	
	}
	
	public void carreModoPagamento(){
		DALEmpresa dbEmpresa = new DALEmpresa(context);		
		ArrayList<ModelModoPagamento> lstModoPagamento = dbEmpresa.selecionaModoPagamento(Global.empresa.getEmpresaId());
		dbEmpresa = null;
		AdapterModoPagamento adpModoPagamento = new AdapterModoPagamento(lstModoPagamento, context);
		spModoPagamento.setAdapter(adpModoPagamento);
		spModoPagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {	
			public void onItemSelected(AdapterView<?> parent, View arg1, int posicao, long id) {

				try{
					
					ModoSelecionado = ((ModelModoPagamento) parent.getItemAtPosition(posicao)).getModoPagamentoId();
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
		//getMenuInflater().inflate(R.menu.encerrar_conta, menu);
		return true;
	}
	
	public void Progresso(String titulo){
        progressDialog = new ProgressDialog(EncerrarContaActivity.this);
        progressDialog.setTitle(titulo);
        progressDialog.setMessage("Processando...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
   }
	
	  @Override 
	   public void onBackPressed(){
		   Intent intent = new Intent(getApplicationContext(), HomeActivity.class); 
	  		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
	   }

}
