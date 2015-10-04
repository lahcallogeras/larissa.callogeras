package com.zetta.pedaja;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class HomeActivity extends Activity {
	
	protected static final String ZXING_MARKET = "market://search?q=pname:com.google.zxing.client.android";
	protected static final String ZXING_DIRECT = "https://zxing.googlecode.com/files/BarcodeScanner3.1.apk";
	Button btnHomeAbrir;
	Button btnCardapio;
	Button btnVisualizarConta;
	Button btnEncerrarConta;
	Button btnDestaque;
	Button btnUltimoPedido;
	Button btnMeioMeio;
	
	
    ImageView imgHomeEst;
    TextView txtHomeEstNome;
	Context context = this;
	View viewMenu;
    private Handler handler;
    private String msgErro;
	private ProgressDialog progressDialog;
	protected static final int DIALOG_1 = 0; // Dialog 1 ID
	
	ArrayList<ModelItemConta> lstItens;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		btnHomeAbrir = (Button)findViewById(R.id.btnHomeAbrir);
		btnCardapio = (Button)findViewById(R.id.btnCardapio);
		btnVisualizarConta = (Button)findViewById(R.id.btnVisualizarConta);
		btnEncerrarConta = (Button)findViewById(R.id.btnEncerrarConta);
		btnMeioMeio = (Button)findViewById(R.id.btnPedidoMix);
		btnDestaque = (Button)findViewById(R.id.btnDestaque);
		btnUltimoPedido = (Button)findViewById(R.id.btnUltimoPedido);
		
		
		imgHomeEst = (ImageView)findViewById(R.id.imgHomeEst);
		txtHomeEstNome = (TextView)findViewById(R.id.txtHomeEstNome);
		
		
		registerForContextMenu(btnHomeAbrir);
		carregarFoto();
		 
		
		try {
			
			if(Global.empresa.getUltimaAtuCardapio() == null || Global.empresa.getAtualizacaoCardapio().getTime() > Global.empresa.getUltimaAtuCardapio().getTime())
				carregaCardapio();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		btnDestaque.setOnClickListener(btnDestaqueClick);
		btnCardapio.setOnClickListener(btnCardapioOnClick);
		btnHomeAbrir.setOnClickListener(btnHomeAbrirOnClick);	
		btnVisualizarConta.setOnClickListener(btnVisualizarContaOnClick);	
		btnEncerrarConta.setOnClickListener(btnEncerraContaOnClick);		
		btnUltimoPedido.setOnClickListener(btnUltimoPedidoOnClick);			
		btnMeioMeio.setOnClickListener(btnMeioMeioOnClick);		
		
		DALConta dbConta = new DALConta(context);
		Global.conta = dbConta.selecionaContaAberta(Global.empresa.getEmpresaId());
		
		
		if (Global.conta != null  && Global.conta .getEmpresaId() == Global.empresa.getEmpresaId()) 			
			controlarAcessoBotoes(true);
		else 
			controlarAcessoBotoes(false);
		
	}
	
	 
    @Override 
    public void onResume(){
    	super.onResume();

		if(Global.abrirConta && Global.mesaSelecionada != null && !Global.mesaSelecionada.equals(""))
			abrirConta();
		
		if (Global.conta != null  && Global.conta .getEmpresaId() == Global.empresa.getEmpresaId()) 			
			controlarAcessoBotoes(true);
		else 
			controlarAcessoBotoes(false);
    }
	
	private void controlarAcessoBotoes(boolean aberta){
		if(!aberta){
			btnHomeAbrir.setVisibility(View.VISIBLE);
			btnVisualizarConta.setVisibility(View.GONE);
			btnEncerrarConta.setVisibility(View.GONE);
			btnUltimoPedido.setVisibility(View.GONE);
			btnMeioMeio.setVisibility(View.GONE);
		} else {
			btnHomeAbrir.setVisibility(View.GONE);
			btnVisualizarConta.setVisibility(View.VISIBLE);
			btnEncerrarConta.setVisibility(View.VISIBLE);
			btnUltimoPedido.setVisibility(View.VISIBLE);			
			
			DALCategoria dbCategoria = new DALCategoria(context);
			int Total = dbCategoria.TotalMeioMeio(Global.empresa.getEmpresaId());
			dbCategoria = null;
						
			if(Global.empresa.getMixProduto().equals("N") || Total <= 0)
				btnMeioMeio.setVisibility(View.GONE);
			else 
				btnMeioMeio.setVisibility(View.VISIBLE);
				
		}
	}
	
	@SuppressLint("HandlerLeak")
	private void abrirConta(){
		if(Global.abrirConta) {
			Progresso("Aguarde");
		       	 new Thread(new Runnable() {
		       		 	Message msg = new Message();
		       	        public void run() {        	        	
		      	            try {	      
		      	            	
		      	            	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		      	            	JSONParser jsonParser = new JSONParser();   
		       	        		
		       	        		boolean conexao = jsonParser.conectado(context);
	                          
		       	        		if(conexao){	       	        			
		       	        			
		       	        			 DALMesa dbmesa = new DALMesa(context);
		       	        			 ModelMesa mesa = dbmesa.selecionarMesaNumero(Global.empresa.getEmpresaId(), Global.mesaSelecionada);
		       	        			
			       	        		 String httpGet = Global.urlConexaoLocal + "inserirConta.php";
		                             List<NameValuePair> params = new ArrayList<NameValuePair>();
		                             params.add(new BasicNameValuePair("P1", "" + mesa.getEmpresaId()));
		                             params.add(new BasicNameValuePair("P2", "" + mesa.getMesaId()));
		                             params.add(new BasicNameValuePair("P3", "" + Global.usuario.getUsuarioId()));
		                             JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params);
		                             
		                             DALConta dbConta = new DALConta(context);
		                      	   	 JSONArray contaArr = json.getJSONArray("conta");	                      	   
		                      	   	 for (int i = 0; i < contaArr.length(); i++) {
		                                  JSONObject c = contaArr.getJSONObject(i);
		                                  ModelConta conta = new ModelConta();
		                                  conta.setContaId(Integer.parseInt(c.getString("ContaId")));
		                                  conta.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));
		                                  conta.setMesaId(Integer.parseInt(c.getString("MesaId")));
		                                  
		                                  if(c.getString("UsuarioId") != null && !c.getString("UsuarioId").equals(""))
		                                	  conta.setUsuarioId(Integer.parseInt(c.getString("UsuarioId")));
		                                  
		                                  if(c.getString("GarcomId") != null && !c.getString("GarcomId").equals(""))
		                                	  conta.setUsuarioId(Integer.parseInt(c.getString("GarcomId")));
		                                  
		                                  conta.setCodigoAbertura(c.getString("CodigoAbertura"));		                                  
		                                  conta.setDataAbertura(dateFormat.parse(c.getString("DataAbertura")));	                                  
		                                  dbConta.insereConta(conta);
		                                  Global.conta = conta;
		                            }
		                      	   	 

		                      	    JSONArray itemContaArr = json.getJSONArray("ItensConta"); 
		                      	   	for (int i = 0; i < itemContaArr.length(); i++) {
			       			             JSONObject c = itemContaArr.getJSONObject(i);
			       			             ModelItemConta conta = new ModelItemConta();
			       			             conta.setItensContaId(Integer.parseInt(c.getString("ItensContaId")));
			       			             conta.setContaId(Integer.parseInt(c.getString("ContaId")));
			       			             conta.setDataPedido(dateFormat.parse(c.getString("DataPedido")));
			       			             conta.setTipoProdutoId(Integer.parseInt(c.getString("TipoProdutoId")));
			       			             conta.setSequencia(Integer.parseInt(c.getString("Sequencia")));
			       			             conta.setValor(Double.parseDouble(c.getString("Valor")));
			       			             conta.setStatusPedido(c.getString("StatusPedido"));
			       			             conta.setUsuarioId(Integer.parseInt(c.getString("UsuarioId")));
			       			             conta.setDivisao(c.getString("Divisao"));
			       			             dbConta.insereItemConta(conta);
		                      	   	}
		                      	   	
		                      	   	Global.visualizarPedido(context, 0);
		                      	   	 
		                      	   dbConta = null;
		                      	   msgErro = "Conta aberta com sucesso!";
		                      	   msg.what = 1;
		                      	   	 
		       	        		} else {
		       	        			msg.what = 0;
		       	        			msgErro = "Se conecte a internet para abrir a conta.";
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
			  			 if(msg.what==1)       				
			  				controlarAcessoBotoes(true);
			  				
			  			 Toast.makeText(HomeActivity.this, msgErro, Toast.LENGTH_LONG).show();		  			
			  			 
		  				
					} catch  (Exception ex) {
						ex.printStackTrace();
					}	 
		  		 }
		  	};	  	
	    }
	  	Global.abrirConta = false;
			
	}
	
	private OnClickListener btnMeioMeioOnClick = new OnClickListener() {		
		public void onClick(View v) {			
			Intent intent = new Intent(getApplicationContext(), MeioMeioActivity.class); 
	   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};
	
	
	private OnClickListener btnUltimoPedidoOnClick = new OnClickListener() {			
		public void onClick(View v) {
			
			try {
				DALConta dbConta = new DALConta(context);
				lstItens = dbConta.selecionaUltimoPedido(Global.conta.getContaId());
				if(lstItens.size() <= 0){
					mostrarMensagemSimples("Ainda nenhum pedido foi feito!", "Aviso");
				} else {
					
					Boolean meio = false;
					String pedidos = "", ultimoItem = "", primeiroItem = "", acompanhamentos = "";
					for(int i = 0; i < lstItens.size(); i++){
						
						primeiroItem =  lstItens.get(0).getDescricaoCategoria();		
						
						if(!lstItens.get(i).getDivisao().equals("N"))
							meio = true;
						
						if(lstItens.get(i).getTipo().equals("VA")) {
							
							if (!meio)
								pedidos = pedidos + lstItens.get(i).getDescricaoProduto() + 
										  " - " + lstItens.get(i).getDescricaoTipoProduto() + "\n";
							else 							
								pedidos = pedidos + lstItens.get(i).getDescricaoProduto() + "/";						

							ultimoItem = lstItens.get(i).getDescricaoTipoProduto();
						} else {
							
							acompanhamentos = acompanhamentos + lstItens.get(i).getDescricaoTipoProduto() + ", ";
						}			
						
						
					}
					
					acompanhamentos = acompanhamentos.trim();
					acompanhamentos = acompanhamentos.substring(0, acompanhamentos.length() - 1);
					
					
					pedidos = "Meio a Meio - " + primeiroItem + " "+ pedidos.substring(0, pedidos.length()-1) + " - " + ultimoItem;
					
					if(!acompanhamentos.equals("")){
						pedidos = pedidos + "\nAcomp.: " + acompanhamentos;
					}
					
					mostraMsgUltimoPedido(pedidos);
				}
				
				
				dbConta = null;
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	};
	
	
	@SuppressLint("HandlerLeak")
	public void mostraMsgUltimoPedido(String msg){
		new AlertDialog.Builder(this)
		.setTitle("O último pedido foi: ")
		.setMessage(msg + "\nRepetir pedido?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("Confirmar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Progresso("Aguarde");
					       	 new Thread(new Runnable() {
				       	        public void run() {				       	         
			       	        		  Message msg = new Message();
				       	        	  try {
	
				       	        		JSONParser jsonParser = new JSONParser();  
				       	        		boolean conexao = jsonParser.conectado(context);
				       	        		
				       	        		if(conexao){
				       	     		    	int sequencia = 0;
					       	     		    for(int i = 0; i < lstItens.size(); i++){		
					       	     		    	

					       						double Valor = lstItens.get(i).getValor();
					       						if (lstItens.get(i).getDivisao().equals("N")){
					       							DALTipoProduto dbTipoProduto = new DALTipoProduto(context);
					       							Valor = dbTipoProduto.selecionaTipoProdutoId( lstItens.get(i).getTipoProdutoId()).getValor();
					       							dbTipoProduto = null;
					       						}
					       	     		    	
					       	     		    	
					       	     		        int[] retorno  = Global.inserePedido(context, lstItens.get(i).getTipoProdutoId(), Valor, sequencia, lstItens.get(i).getDivisao());					       	     		    	
					       	     		    	msg.what = retorno[0];
					       	     		    	sequencia = retorno[1];
					       	     		    	if(msg.what != 1)
					       	     		    		break;
					       	     		    }				

				       	     		    	Global.visualizarPedido(context, sequencia);
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
					  				Toast.makeText(HomeActivity.this, "Pedido feito com sucesso!", Toast.LENGTH_LONG).show();
					  			 else  if(msg.what == 0)
					  				Toast.makeText(HomeActivity.this, "Erro ao inserir pedido.", Toast.LENGTH_LONG).show();
					  			else  if(msg.what == 3)
					  				Toast.makeText(HomeActivity.this, "Já foi solicitado o fechamento dessa conta. Se a conta encontra-se aberta em seu aparelho, você deve fechá-la.", Toast.LENGTH_LONG).show();
					  			 else  if(msg.what == 2)
					  				Toast.makeText(HomeActivity.this, "Sem Conexão com a internet! Impossivel realizar pedido..", Toast.LENGTH_LONG).show();
					  		 }
					  	};
					}
				})
		.setNegativeButton("Cancelar", null).show();
	}
	
	private OnClickListener btnEncerraContaOnClick = new OnClickListener() {			
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), EncerrarContaActivity.class); 
	   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
		}
	};
	
	private OnClickListener btnDestaqueClick = new OnClickListener() {			
		public void onClick(View v) {
			Global.visuDestaqueCarpadio = false;
			Intent intent = new Intent(getApplicationContext(), DestaqueActivity.class); 
	   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};
	
	private OnClickListener btnVisualizarContaOnClick = new OnClickListener() {			
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), VisualizarContaActivity.class); 
	   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};
	
		
	private OnClickListener btnCardapioOnClick = new OnClickListener() {			
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), CardapioActivity.class); 
	   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};
	
	private OnClickListener btnHomeAbrirOnClick = new OnClickListener() {			
		public void onClick(View v) {
			openContextMenu(v);	
		}
	};
	
	 @Override  
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
		 super.onCreateContextMenu(menu, v, menuInfo);  
		 menu.setHeaderTitle("Selecionar Opção");  
		 menu.add(0, 0, 0, "Automático (Ler QR Code)");  
		 menu.add(1, 1, 1, "Convencional (Selecionar em lista)");  
		 viewMenu = v;
	}  
	 	 
	@Override  
	 public boolean onContextItemSelected(MenuItem item) {  
	     
		 if(item.getItemId() == 0)
			 lerQR(viewMenu);
	     else if(item.getItemId() == 1){
	     	Intent intent = new Intent(getApplicationContext(), MesaActivity.class); 
	    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		startActivity(intent);
	     }	    	 
	     else {
	    	 return false;
	      }  
	     return true;  
	 }  
  	
	private void carregarFoto(){
		Bitmap bitmap ; 
		try{
			 
			 txtHomeEstNome.setText(Global.empresa.getNomeFantasia());
			 
			 
	          if(!Global.empresa.getTipoImagem().equals("") && Global.empresa.getTipoImagem() != null){
		        	String url = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + Global.empresa.getCaminhoImagem()  + Global.empresa.getNomeImagem();
			        File f = new File(url);
			        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
			        bitmap = Bitmap.createBitmap(bitmap);
	        	}
	        	else
	        	{ 	
	        		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.estabelecimento);
	        		
	        	}
	        	
	        } catch(Exception ex){
	        	ex.printStackTrace();
	        	 bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.estabelecimento);
    		
	        }
		 
		  int heightImg =  70;
		  int widthImg =  120;
		  int height = (bitmap.getWidth() * heightImg)/widthImg;			        
	      Bitmap pq = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),height, true);
	      imgHomeEst.setImageBitmap(pq);
	}
	
	@SuppressLint("HandlerLeak")
	private void carregaCardapio(){
		try {
			
			Progresso("Aguarde");
	       	 new Thread(new Runnable() {	       		 
	       	        public void run() {

       	        		Message msg = new Message();
	      	            try {	      
	      	            	
	      	            	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
	      	            	JSONParser jsonParser = new JSONParser();   
	       	        		boolean conexao = jsonParser.conectado(context);
                           
	       	        		if(conexao){	       	        			
	       	        			
		       	        		 String httpGet = Global.urlConexaoLocal + "cardapioEmpresa.php";
	                             List<NameValuePair> params = new ArrayList<NameValuePair>();
	                             params.add(new BasicNameValuePair("P1", "" + Global.empresa.getEmpresaIdLocal()));
	                             JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params);
	                             
	                             DALCategoria dbCategoria = new DALCategoria(context);
	                      	   	 JSONArray categoriaArr = json.getJSONArray("categoria"); 
	                             
	                      	   	 for (int i = 0; i < categoriaArr.length(); i++) {
	                                  JSONObject c = categoriaArr.getJSONObject(i);
	                                  ModelCategoria categoria = new ModelCategoria();
	                                  categoria.setCategoriaId(Integer.parseInt(c.getString("CategoriaId")));
	                                  categoria.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));
	                                  categoria.setDescricao(c.getString("Descricao"));
	                                  categoria.setMixProduto(Integer.parseInt(c.getString("MixProduto"))); 
	                                  dbCategoria.atualizaCategoria(categoria);
	                             }
	                      	   	 dbCategoria = null;
	                      	   	 
	                      	     DALProduto dbProduto = new DALProduto(context);
	                      	   	 JSONArray produtoArr = json.getJSONArray("produto"); 
	                             
	                      	   	 for (int i = 0; i < produtoArr.length(); i++) {
	                                  JSONObject c = produtoArr.getJSONObject(i);
	                                  ModelProduto produto = new ModelProduto();
	                                  produto.setProdutoId(Integer.parseInt(c.getString("ProdutoId")));
	                                  produto.setCategoriaId(Integer.parseInt(c.getString("CategoriaId")));
	                                  produto.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));
	                                  produto.setDescricao(c.getString("Descricao"));
	                                  produto.setDetalhe(c.getString("Detalhe"));
	                                  dbProduto.atualizaProduto(produto);
	                            }
	                      	   dbProduto = null;
	                      	   
		                       DALTipoProduto dbTipoProduto = new DALTipoProduto(context);		                       
		                       dbTipoProduto.apagaTipoProduto(Global.empresa.getEmpresaId());		                       
	                      	   JSONArray tipoProdutoArr = json.getJSONArray("tipoProduto"); 
	                             
	                      	   for (int i = 0; i < tipoProdutoArr.length(); i++) {
	                                  JSONObject c = tipoProdutoArr.getJSONObject(i);
	                                  ModelTipoProduto tipoProduto = new ModelTipoProduto();	                                  
	                                  tipoProduto.setTipoProdutoId(Integer.parseInt(c.getString("TipoProdutoId")));             
	                                  tipoProduto.setProdutoId(Integer.parseInt(c.getString("ProdutoId")));
	                                  tipoProduto.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));
	                                  tipoProduto.setDescricao(c.getString("Descricao"));
	                                  tipoProduto.setValor(Double.parseDouble(c.getString("Valor")));
	                                  tipoProduto.setPorcentagem(Double.parseDouble(c.getString("Porcentagem")));
	                                  tipoProduto.setVisivel(Integer.parseInt(c.getString("Visivel")));
	                                  tipoProduto.setContabil(Integer.parseInt(c.getString("Contabil")));
	                                  tipoProduto.setTamanho(c.getString("Tamanho"));
	                                  tipoProduto.setTipo(c.getString("Tipo"));
	                                  dbTipoProduto.atualizaTipoProduto(tipoProduto);
	                           }
	                      	   
	                           
		                      dbTipoProduto.apagaExcecaoTipoProduto(Global.empresa.getEmpresaId());	 
	                      	  JSONArray excecaoProdutoArr = json.getJSONArray("excecaoTipoProduto");                              
	                      	   for (int i = 0; i < excecaoProdutoArr.length(); i++) {
	                                  JSONObject c = excecaoProdutoArr.getJSONObject(i);
	                                  ModelExcecaoProduto excecaoProduto = new ModelExcecaoProduto();
	                                  excecaoProduto.setExcecaoTipoProdutoId(Integer.parseInt(c.getString("ExcecaoTipoProdutoId")));
	                                  excecaoProduto.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));
	                                  excecaoProduto.setTipoProdutoId(Integer.parseInt(c.getString("TipoProdutoId")));
	                                  excecaoProduto.setDiaSemana(Integer.parseInt(c.getString("DiaSemana")));
	                                  excecaoProduto.setHorarioInicial(Integer.parseInt(c.getString("HorarioInicial")));
	                                  excecaoProduto.setHorarioFInal(Integer.parseInt(c.getString("HorarioFinal")));
	                                  excecaoProduto.setValor(Double.parseDouble(c.getString("Valor"))); 
	                                  dbTipoProduto.atualizaExcecaoTipoProduto(excecaoProduto);
	                           }                      	 
	                      	   
	                      	   dbTipoProduto = null;
	                      	   
	                      	   File f = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + Global.empresa.getCaminhoDestaqueImagem());
	                      	   Global.apagarImagensDiretorio(f);
	                      	   
	                      	   DALDestaque dbDestaque = new DALDestaque(context);
	                      	   dbDestaque.apagaDestaque(Global.empresa.getEmpresaId());	                      	   
		                       JSONArray destaqueArr = json.getJSONArray("destaque"); 
		                       for (int i = 0; i < destaqueArr.length(); i++) {
		                              JSONObject c = destaqueArr.getJSONObject(i);
		                              ModelDestaque destaque = new ModelDestaque();
		                              destaque.setDestaqueId(Integer.parseInt(c.getString("DestaqueId")));	
		                              destaque.setTipoImagem(c.getString("TipoImagem"));
		                              destaque.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));			                              
		                              destaque.setInicioVigencia(dateFormat.parse(c.getString("InicioVigencia")));
		                              destaque.setDescricao(c.getString("Descricao"));
		                              
		                              if(c.getString("FimVigencia") != null && !c.getString("FimVigencia").equals("") && !c.getString("FimVigencia").equals("null"))
		                            	  destaque.setFimVigencia(dateFormat.parse(c.getString("FimVigencia")));
		                            	       
		                              dbDestaque.insereDestaque(destaque);
		                              
		                              Global.gravaFoto(c.getString("Imagem"), Global.empresa.getCaminhoDestaqueImagem(), destaque.getNomeImagem());
		                       }
		                       
		                       dbDestaque.apagaItensDestaque(Global.empresa.getEmpresaId());	                      	   
		                       JSONArray itensDestaqueArr = json.getJSONArray("itensDestaque"); 
		                       for (int i = 0; i < itensDestaqueArr.length(); i++) {
		                              JSONObject c = itensDestaqueArr.getJSONObject(i);
		                              ModelItemDestaque itensDestaque = new ModelItemDestaque();
		                              itensDestaque.setItensDestaqueId(Integer.parseInt(c.getString("ItensDestaqueId")));	
		                              itensDestaque.setDestaqueId(Integer.parseInt(c.getString("DestaqueId")));	
		                              itensDestaque.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));	
		                              itensDestaque.setTipoProdutoId(Integer.parseInt(c.getString("TipoProdutoId")));
		                              dbDestaque.insereItensDestaque(itensDestaque);
		                       }
		                       
		                       
		                       dbDestaque = null;
                             	
	                      	   
	                      	   msg.what = 1;
	                      	   Date data = new Date(); 
	                      	   Date dataAtual = dateFormat.parse(dateFormat.format(data.getTime()));
	                      	   Global.empresa.setUltimaAtuCardapio(dataAtual);
	                      	   DALEmpresa dbEmpresa = new DALEmpresa(context);
	                      	   dbEmpresa.atualizaEmpresa(Global.empresa);
	                      	   
	       	        			
	       	        		}
	       	        		else {
                         	   msg.what = 0;
                         	   msgErro = "Sem conexão com a internet.";
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
	  			try{
	  				progressDialog.cancel();
		  			 if(msg.what == 0) 
		  			 {       		
		  				Toast.makeText(HomeActivity.this, msgErro, Toast.LENGTH_LONG).show();
		  			 }
		  			 
	  				
				} catch  (Exception ex) {
					ex.printStackTrace();
				}	 
	  		 }
	  	};
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void Progresso(String titulo){
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setTitle(titulo);
        progressDialog.setMessage("Processando...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
   }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void lerQR(View view){
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
 
		try {
			startActivityForResult(intent, 0);
 
		} catch (ActivityNotFoundException e) {
			mostrarMensagem();
		}
	}
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			String qrcode = intent.getStringExtra("SCAN_RESULT");
			
			try {
				//qrcode = "1|02|";
				String[] partes = qrcode.split("\\|");
				int Empresa = Integer.parseInt(partes[0]);
				if(Empresa != Global.empresa.getEmpresaId())
					mostrarMensagemSimples("Esta mesa não pertence ao estabelecimento selecionado.", "Aviso");
				else {
					Global.mesaSelecionada = partes[1];
					Global.abrirConta = true;
					abrirConta();
				}
			} catch (Exception ex){
				ex.printStackTrace();
				mostrarMensagemSimples("QR Code inválido!", "Aviso");
			}
			
			
		} 
	}
  
	
	private void mostrarMensagemSimples(String msg, String titulo) {
		new AlertDialog.Builder(this)
				.setTitle(titulo)
				.setMessage(msg)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("OK", null).show();
	
	}
	
	
	private void mostrarMensagem() {
		new AlertDialog.Builder(this)
				.setTitle("Instalar barcode scanner?")
				.setMessage("Para escanear QR code você precisa instalar o ZXing barcode scanner.")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Instalar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ZXING_MARKET));
								try {
									startActivity(intent);
								} catch (ActivityNotFoundException e) { // Se não tiver o Play Store
									intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ZXING_DIRECT));
									startActivity(intent);
								}
							}
						})
				.setNegativeButton("Cancelar", null).show();
 
	}
	
	  
    @Override 
    public void onBackPressed(){
    	Global.empresa = null;
    	Intent intent = new Intent(getApplicationContext(), EstabelecimentoActivity.class); 
   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
    }
    
    
   
   
	

}
