package com.zetta.pedaja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProdutoActivity extends Activity {

	private EditText edtQtd;
	private Button btnMais;
	private Button btnMenos;
	private Button btnPedirProduto;
	private TextView txtProdDescricao;
	private TextView txtProdDetalhe;
	int hora = 0;
	int diaSemana = 0;
	
	Context context = this;
	private ListView  lstTipoProduto;
	AdapterTipoProduto adapter;
	

    private Handler handler;
	private ProgressDialog progressDialog;
	String msgErro;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.produto);
		edtQtd =  (EditText)findViewById(R.id.edtQtd);
		btnMais =  (Button)findViewById(R.id.btnMais);
		btnMenos =  (Button)findViewById(R.id.btnMenos);	
		btnPedirProduto =  (Button)findViewById(R.id.btnPedirProduto);
		txtProdDescricao =  (TextView)findViewById(R.id.txtProdDescricao);
		txtProdDetalhe  =  (TextView)findViewById(R.id.txtProdDetalhe);
		lstTipoProduto =  (ListView)findViewById(R.id.lstTipoProduto);		
		edtQtd.setText("1");	
		btnMais.setOnClickListener(clickMais);
		btnMenos.setOnClickListener(clickMenos);
		btnPedirProduto.setOnClickListener(btnPedirProdutoOnClik);		
		
		edtQtd.setKeyListener(null);
		txtProdDescricao.setText(Global.produto.getDescricao());
		txtProdDetalhe.setText(Global.produto.getDetalhe());	

		if(Global.produto.getDetalhe().equals(""))
			txtProdDetalhe.setVisibility(View.GONE);
		
		
		if (Global.conta != null  && Global.conta .getEmpresaId() == Global.empresa.getEmpresaId()) 
			btnPedirProduto.setVisibility(View.VISIBLE);			
		else
			btnPedirProduto.setVisibility(View.INVISIBLE);
		
		recuperarDiaSemanHora();
	}
	
	private void carregaTipoProduto(){
		try {
			
			DALTipoProduto dbTipoProduto = new DALTipoProduto(context);
			ArrayList<ModelTipoProduto> lst = dbTipoProduto.selecionaTipoProduto(Global.produto.getProdutoId(), "'VA', 'UT'",hora, diaSemana);
			adapter = new AdapterTipoProduto(context, R.layout.item_tipo_produto, lst);		 	          	   	         
			lstTipoProduto.setAdapter((ListAdapter) adapter);
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressLint("HandlerLeak")
	private void recuperarDiaSemanHora(){
		Progresso("Aguarde");
	      	 new Thread(new Runnable() {
	      		 	Message msg = new Message();
	      	        public void run() {        	        	
	     	            try {	      
	     	            	 
	     	            	JSONParser jsonParser = new JSONParser();   
	      	        		
	      	        		boolean conexao = jsonParser.conectado(context);
	                     
	      	        		if(conexao){	      	        			
		       	        	   String httpGet = Global.urlConexaoLocal + "recuperaDiaHora.php";
	                           List<NameValuePair> params = new ArrayList<NameValuePair>();
	                           JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params);
	                           hora = json.getInt("hora");
	                           diaSemana = json.getInt("diaSemana");	                           
	                     	   msg.what = 1;
	                     	   	 
	      	        		} else {
	      	        			msg.what = 0;
	      	        			msgErro = "Conecte-se a internet para visualizar possiveis oferta.";
	      	        		}	       	        		
	     	        }
	     	      	catch (Exception e) {
	     	      		msg.what = 0;
	                   e.printStackTrace();
	               }  
	     	        handler.sendMessage(msg);    
	         	 	
		        }
		    }).start(); 	  
	   handler=new Handler(){
	 		 public void handleMessage(Message msg){
	 			try{
	 				progressDialog.cancel();
		  			if(msg.what == 0)		  				
		  				Toast.makeText(ProdutoActivity.this, msgErro, Toast.LENGTH_LONG).show();		  			
		  			 
		  			carregaTipoProduto();
				} catch  (Exception ex) {
					ex.printStackTrace();
				}	 
	 		 }
	 	};	  	
		
	}
	
	private OnClickListener btnPedirProdutoOnClik = new OnClickListener() {			
		public void onClick(View v) {
			 
			
			if(Global.produtoSelecionado == 0){
				mostrarMensagem("Selecione algum item!.","Aviso");				
			}else{
				
				Global.quantidadeSelecionada = Integer.parseInt(edtQtd.getText().toString());				
				DALTipoProduto dbTipoProduto = new DALTipoProduto(context);
				Global.lstAcompanhamento = dbTipoProduto.selecionaTipoProduto(Global.produto.getProdutoId(), "'AC'", hora, diaSemana);
				if(Global.lstAcompanhamento.size() > 0){
					Intent intent = new Intent(getApplicationContext(), AcompanhamentoActivity.class); 
			   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);					
				} else 
					inserePedido();
			}
			
		}
	};
	
	
	private OnClickListener clickMais = new OnClickListener() {			
		public void onClick(View v) {
			int valor = Integer.parseInt(edtQtd.getText().toString());
			if(valor < 20)
				valor++;
			
			edtQtd.setText("" + valor);			
		}
	};
	
	private OnClickListener clickMenos = new OnClickListener() {			
		public void onClick(View v) {
			int valor = Integer.parseInt(edtQtd.getText().toString());
			if(valor > 1)
				valor--;
			
			edtQtd.setText("" + valor);			
		}
	};
	
	private void mostrarMensagem(String msg, String titulo) {
		new AlertDialog.Builder(this)
				.setTitle(titulo)
				.setMessage(msg)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("OK", null).show();
	
	}
	
	
	@SuppressLint("HandlerLeak")
	private void inserePedido(){
		try {
			 Progresso("Aguarde");
		       	 new Thread(new Runnable() {
	       	        public void run() {      

       	        		 Message msg = new Message();
	       	        	  try {

	       	        		JSONParser jsonParser = new JSONParser();   
	       	        		boolean conexao = jsonParser.conectado(context);
	       	        		
	       	        		if(conexao){     
	       	        			DALTipoProduto   dbTipoProduto = new DALTipoProduto(context);		       	        		
		       	        		ModelTipoProduto tipoProduto = dbTipoProduto.selecionaTipoProdutoId(Global.produtoSelecionado);		
		       	     		    dbTipoProduto = null;	
			       	     		
		       	     		    for(int j = 0; j < Global.quantidadeSelecionada; j++){		       	     		    
			       	     		    int[] retorno = Global.inserePedido(context,  tipoProduto.getTipoProdutoId(), tipoProduto.getValor(), 0, "N");		       	     		    
			       	     		    msg.what = retorno[0];
			       	     		    Global.visualizarPedido(context,  retorno[1]);
		       	     		    }
	       	        		}
	       	        		else 
	       	        			msg.what = 2;
	       	        		

	       	        	  }
       	        	  catch (Exception e) {
                          e.printStackTrace();
     	        		  msg.what = 0;
                      }  
	       	          handler.sendMessage(msg);     	              			              	 	
	  	        }
	  	    }).start();
	 	  
		    handler=new Handler(){
		  		 public void handleMessage(Message msg){
		
		  			 progressDialog.cancel();
		  			 if(msg.what==1)
		  			 {
		  				Toast.makeText(ProdutoActivity.this, "Pedido feito com sucesso!", Toast.LENGTH_LONG).show();
		  				Global.produtoSelecionado = 0;
		  		    	Intent intent = new Intent(getApplicationContext(), CardapioActivity.class); 
		  		   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  				startActivity(intent);
		  		 	 }
		  			 else  if(msg.what == 0)
		  				Toast.makeText(ProdutoActivity.this, "Erro ao inserir pedido.", Toast.LENGTH_LONG).show();
		  			else  if(msg.what == 3)
		  				Toast.makeText(ProdutoActivity.this, "Já foi solicitado o fechamento dessa conta. Se a conta encontra-se aberta em seu aparelho, você deve fechá-la.", Toast.LENGTH_LONG).show();
		  			 else  if(msg.what == 2)
		  				Toast.makeText(ProdutoActivity.this, "Sem Conexão com a internet! Impossivel realizar pedido..", Toast.LENGTH_LONG).show();
		  		 }
		  	};
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
	}
	

   public void Progresso(String titulo){
         progressDialog = new ProgressDialog(ProdutoActivity.this);
         progressDialog.setTitle(titulo);
         progressDialog.setMessage("Processando...");
         progressDialog.setIndeterminate(true);
         progressDialog.setCancelable(true);
         progressDialog.show();
    }
   
	   
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.produto, menu);
		return true;
	}
	
	 @Override 
	    public void onBackPressed(){
		 	Global.produto = null;
			Global.produtoSelecionado = 0;
	    	Intent intent = new Intent(getApplicationContext(), CardapioActivity.class); 
	   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
	    }
		

}
