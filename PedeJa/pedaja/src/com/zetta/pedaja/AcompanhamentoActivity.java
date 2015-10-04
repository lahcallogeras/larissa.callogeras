package com.zetta.pedaja;


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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AcompanhamentoActivity extends Activity {


	private ListView  lstViewAcompanhamento;
	AdapterAcompanhamento adapter;
	private Button btnConfAcomp;
	Context context = this;
	private TextView txtProdDescricao;
	private TextView txtProdDetalhe;
	String AcompanhamentoSelecionado;
	String[] lstAcomp;

    private Handler handler;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acompanhamento);
		lstViewAcompanhamento =  (ListView)findViewById(R.id.lstViewAcompanhamento);		
		btnConfAcomp =  (Button)findViewById(R.id.btnConfAcomp);
		txtProdDescricao =  (TextView)findViewById(R.id.txtProdDescricao);
		txtProdDetalhe  =  (TextView)findViewById(R.id.txtProdDetalhe);
		btnConfAcomp.setOnClickListener(btnConfAcompOnClick);			
		
		if(!Global.pedidoMetade) {	
			txtProdDescricao.setText(Global.produto.getDescricao());	
			DALTipoProduto dbTipoProduto = new DALTipoProduto(context);
			ModelTipoProduto tipoProduto = dbTipoProduto.selecionaTipoProdutoId(Global.produtoSelecionado);		
			txtProdDetalhe.setText(tipoProduto.getDescricao() + " - "  + String.format("R$ %.2f", tipoProduto.getValor() ));	
			dbTipoProduto  = null;
		} 
		else {
			
			DALCategoria dbCategoria = new DALCategoria(context);
			ModelCategoria modelCategoria = dbCategoria.selecionaCategoriaId(Global.categoriaMeioMeio);			
			dbCategoria = null;		
			
			String Descricao =  Global.modelMetade01.getDescricao() + "/" + Global.modelMetade02.getDescricao();
			
			if(Global.modelMetade03.getTipoProdutoId() > 0)
				Descricao =  "/" + Global.modelMetade03.getDescricao();			
			
			txtProdDescricao.setText(modelCategoria.getDescricao());
			txtProdDetalhe.setText(Descricao);
			
		}
		
		
		
		carregaAcompanhamento();
	}

	private OnClickListener btnConfAcompOnClick = new OnClickListener() {			
		public void onClick(View v) {
			AcompanhamentoSelecionado = "";
			for(int k = 0; k < Global.lstAcompanhamento.size() ;k++){							
					if(Global.lstAcompanhamento.get(k).getSelecionado())
						AcompanhamentoSelecionado =  Global.lstAcompanhamento.get(k).getTipoProdutoId()  + ","  + AcompanhamentoSelecionado;
				 
				 
			  }
			 
			lstAcomp = AcompanhamentoSelecionado.split("\\,");
			if(AcompanhamentoSelecionado.equals(""))
				mostrarMensagem();
			else 
				insereAcompanhamento();
		}
	};
	
	
	private void mostrarMensagem() {
		new AlertDialog.Builder(this)
				.setTitle("Sem acompanhamento.")
				.setMessage("Confirma a efetivação do pedido sem acompanhamentos?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								inserePedido();
							}
						})
				.setNegativeButton("Não", null).show();
 
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.acompanhamento, menu);
		return true;
	}
	
	 public void Progresso(String titulo){
         progressDialog = new ProgressDialog(AcompanhamentoActivity.this);
         progressDialog.setTitle(titulo);
         progressDialog.setMessage("Processando...");
         progressDialog.setIndeterminate(true);
         progressDialog.setCancelable(true);
         progressDialog.show();
    }
   
	   
	
	private void carregaAcompanhamento(){
		try {
			
			adapter = new AdapterAcompanhamento(context, R.layout.item_acompanhamento, Global.lstAcompanhamento);		 	          	   	         
			lstViewAcompanhamento.setAdapter((ListAdapter) adapter);
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override 
    public void onBackPressed(){
		
		Intent intent  = null;
		if(Global.pedidoMetade)
		{
			if(Global.modelMetade03.getTipoProdutoId() > 0)			
				intent = new Intent(getApplicationContext(), TresMetadesActivity.class);
			else 
				intent = new Intent(getApplicationContext(), DuasMetadesActivity.class);
	   		
			
		} else
			intent = new Intent(getApplicationContext(), ProdutoActivity.class); 
		
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
    }
	
	
	@SuppressLint("HandlerLeak")
	private void insereAcompanhamento(){
				
			try {
				 Progresso("Aguarde");
			       	 new Thread(new Runnable() {
		       	        public void run() {        	        	
		       	        	  try {
	
		       	        		JSONParser jsonParser = new JSONParser();   
		       	        		Message msg = new Message();
		       	        		boolean conexao = jsonParser.conectado(context);
		       	        		
		       	        		if(conexao){  

		       	        			DALTipoProduto   dbTipoProduto = new DALTipoProduto(context);	
		       	        			
		       	        			for(int j = 0; j < Global.quantidadeSelecionada; j++){
			       	        			
		       	        				int sequencia = 0;
			       	        			for(int i = 0; i < lstAcomp.length; i++){ 
				       	        			    	        		
					       	        		ModelTipoProduto tipoProduto = dbTipoProduto.selecionaTipoProdutoId(Integer.parseInt(lstAcomp[i]));		 	        		
					       	     		    int[] retorno = Global.inserePedido(context, tipoProduto.getTipoProdutoId() , tipoProduto.getValor(), sequencia, "N");					       	     		    
					       	     		    msg.what = retorno[0];
					       	     		    sequencia = retorno[1];
			       	        			}	       	      
		       	        			
			       	        				
		       	        				if(!Global.pedidoMetade) {
		       	        					ModelTipoProduto tipoProduto = dbTipoProduto.selecionaTipoProdutoId(Global.produtoSelecionado);
		       	        					int[] retorno = Global.inserePedido(context, tipoProduto.getTipoProdutoId() , tipoProduto.getValor(), sequencia, "N");
		       	        					msg.what = retorno[0];		       	        					
		       	        				}
		       	        			    else {
		       	        				
			       	        				int[] retorno = Global.inserePedido(context, Global.modelMetade01.getTipoProdutoId(), Global.modelMetade01.getValorParcial(), sequencia, Global.modelMetade01.getDivisao());
			       	        				retorno = Global.inserePedido(context,  Global.modelMetade02.getTipoProdutoId(), Global.modelMetade02.getValorParcial(), sequencia, Global.modelMetade02.getDivisao());	  
					       	     		   
					       	     		    if(Global.modelMetade03.getTipoProdutoId() > 0)
					       	     		    	retorno = Global.inserePedido(context, Global.modelMetade03.getTipoProdutoId(), Global.modelMetade03.getValorParcial(), sequencia, Global.modelMetade03.getDivisao());
	
					       	     		    msg.what = retorno[0];
		       	        			    }
			       	        			
			       	        			Global.visualizarPedido(context, sequencia);
		       	        			
		       	        			}		
			       	        		dbTipoProduto = null;	
		       	        			
		       	        		}
		       	        		else 
		       	        			msg.what = 2;
		       	        		
	
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
			  				Toast.makeText(AcompanhamentoActivity.this, "Pedido feito com sucesso!", Toast.LENGTH_LONG).show();
			  				
			  				if(Global.pedidoMetade) {
			  					Global.pedidoMetade = false;
			  					finish();
			  				} else {
			  				
				  				Global.produtoSelecionado = 0;
				  		    	Intent intent = new Intent(getApplicationContext(), CardapioActivity.class); 
				  		   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				  				startActivity(intent);
			  				}
			  		 	 }
			  			 else  if(msg.what == 0)
			  				Toast.makeText(AcompanhamentoActivity.this, "Erro ao inserir pedido.", Toast.LENGTH_LONG).show();
			  			 else  if(msg.what == 3)
				  				Toast.makeText(AcompanhamentoActivity.this, "Já foi solicitado o fechamento dessa conta. Se a conta encontra-se aberta em seu aparelho, você deve fechá-la.", Toast.LENGTH_LONG).show();			  			 
			  			 else  if(msg.what == 2)
			  				Toast.makeText(AcompanhamentoActivity.this, "Sem Conexão com a internet! Impossivel realizar pedido..", Toast.LENGTH_LONG).show();
			  		 }
			  	};
				
			} catch (Exception ex){
				ex.printStackTrace();
			}
		
	}
	

	@SuppressLint("HandlerLeak")
	private void inserePedido(){
		try {
			 Progresso("Aguarde");
		       	 new Thread(new Runnable() {
	       	        public void run() {        	        	
	       	        	  try {

	       	        		JSONParser jsonParser = new JSONParser();   
	       	        		Message msg = new Message();
	       	        		boolean conexao = jsonParser.conectado(context);
	       	        		
	       	        		if(conexao){     	       	        				 
		       	     		     
	       	        			
	       	        			for(int j = 0 ; j < Global.quantidadeSelecionada; j++){
		       	        	        
		       	        	        if(!Global.pedidoMetade) {
		       	        	        	DALTipoProduto   dbTipoProduto = new DALTipoProduto(context);		       	        		
				       	        		ModelTipoProduto tipoProduto = dbTipoProduto.selecionaTipoProdutoId(Global.produtoSelecionado);
		       	        	        	int[] retorno = Global.inserePedido(context, tipoProduto.getTipoProdutoId(), tipoProduto.getValor(), 0,"N");	       	        	        	
		       	        	        	msg.what = retorno[0];
		       	        	        	Global.visualizarPedido(context, retorno[1]);
		       	        	        	dbTipoProduto = null;
	    	        				} else {    	        					
	
	    	        					int sequencia = 0;
	    	        					int[] retorno = Global.inserePedido(context, Global.modelMetade01.getTipoProdutoId(), Global.modelMetade01.getValorParcial(), sequencia, Global.modelMetade01.getDivisao());
	    	        					sequencia = retorno[1];
	    	        					
	    	        					retorno  = Global.inserePedido(context, Global.modelMetade02.getTipoProdutoId(), Global.modelMetade02.getValorParcial(), sequencia, Global.modelMetade02.getDivisao());	  
				       	     		   
				       	     		    if(Global.modelMetade03.getTipoProdutoId() > 0)
				       	     		    	retorno  = Global.inserePedido(context, Global.modelMetade03.getTipoProdutoId(), Global.modelMetade03.getValorParcial(), sequencia, Global.modelMetade03.getDivisao());
				       	     		    
		       	        	        	Global.visualizarPedido(context, sequencia);
				       	     		    msg.what =  retorno[0];
	    	        				}
	       	        			}
		       	     		 
	       	        			
	       	        		}
	       	        		else 
	       	        			msg.what = 2;
	       	        		

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
		  				 
		  				if(Global.pedidoMetade) {

		  					Global.pedidoMetade = false;
		  					finish();
		  				}
		  			 	else {
			  				Toast.makeText(AcompanhamentoActivity.this, "Pedido feito com sucesso!", Toast.LENGTH_LONG).show();
			  				Global.produtoSelecionado = 0;
			  		    	Intent intent = new Intent(getApplicationContext(), CardapioActivity.class); 
			  		   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			  				startActivity(intent);
		  				}
		  		 	 }
		  			 else  if(msg.what == 0)
		  				Toast.makeText(AcompanhamentoActivity.this, "Erro ao inserir pedido.", Toast.LENGTH_LONG).show();
		  			else  if(msg.what == 3)
		  				Toast.makeText(AcompanhamentoActivity.this, "Já foi solicitado o fechamento dessa conta. Se a conta encontra-se aberta em seu aparelho, você deve fechá-la.", Toast.LENGTH_LONG).show();			  			 
		  			else  if(msg.what == 2)
		  				Toast.makeText(AcompanhamentoActivity.this, "Sem Conexão com a internet! Impossivel realizar pedido..", Toast.LENGTH_LONG).show();
		  		 }
		  	};
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
	}
	

}
