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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EstabelecimentoActivity extends Activity {
	Context context = this;
    private Handler handler;
    private String msgErro;
	private ProgressDialog progressDialog;
    byte[] encodeByte; 
    ImageView image;
    private ListView lstEstabelecimento;
    AdapterEmpresa adapter;    
    EditText edtEstBuscar;
    List<ModelEmpresa> lstEmpresa;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.estabelecimento);
		lstEstabelecimento = (ListView)findViewById(R.id.lstEstabelecimento);	  
		edtEstBuscar  = (EditText)findViewById(R.id.edtEstBuscar); 
		
		lstEstabelecimento.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {				
						try{

							DALEmpresa dbEmpresa = new DALEmpresa(context);							
							Global.empresa = dbEmpresa.selecinarEmpresaId(Integer.parseInt((String)((TextView)view.findViewById(R.id.txtIdEstabelecimento)).getText()));
							Global.urlConexaoLocal = Global.empresa.getServidorLocal();
							Global.empresa.setEmpresaId(Global.empresa.getEmpresaIdLocal());
							Intent intent = new Intent(getApplicationContext(), HomeActivity.class); 
					   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							
						}catch(Exception ex){
							ex.printStackTrace();
						}
						
				}  
	        });		
		edtEstBuscar.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				carregaEstabelecimento();				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	
		
		
		carregaEstabelecimentos();
	}
	
	
	@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
	private void carregaEstabelecimentos(){
		 try
	     {   
			 boolean buscar = false;
			 
			 if(Global.usuario.getUltimaAtu() == null)
				 buscar = true;
			 else {
				 
				 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				 Date data = new Date(); 
				 Date dataAtual = dateFormat.parse(dateFormat.format(data.getTime()));
				 Date ultimaAtualizado = dateFormat.parse(dateFormat.format(Global.usuario.getUltimaAtu()));
				
				 if(dataAtual.getTime() != ultimaAtualizado.getTime())
					 buscar = true;				 
			 }
			 
			 if(buscar){				 
					
					Progresso("Aguarde");
				       	 new Thread(new Runnable() {
				       	        public void run() {     
				       	        	Message msg = new Message();
				      	            try {	      	            	
				      	            	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				      	            	JSONParser jsonParser = new JSONParser();
				       	        		boolean conexao = jsonParser.conectado(context);
			                           
				       	        		if(conexao){
				       	        			
				       	        		   String httpGet = Global.urlConexaoServidor + "estabelecimentos.php";
			                               List<NameValuePair> params = new ArrayList<NameValuePair>();
			                               params.add(new BasicNameValuePair("P1", "" + Global.usuario.getCidadeId()));
			                               JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params);			                               
			                               int sucesso = json.getInt("sucesso");
			                               
			                               if (sucesso == 1) {
			                            	   
			                            	   DALEmpresa dbEmpresa = new DALEmpresa(context);
			                            	   dbEmpresa.apagaEmpresa();
			                            	   JSONArray empresaArr = json.getJSONArray("empresa"); 
			                                   for (int i = 0; i < empresaArr.length(); i++) {
			                                        JSONObject c = empresaArr.getJSONObject(i);
			                                        ModelEmpresa empresa = new ModelEmpresa();
			                                        empresa.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));
			                                        empresa.setRazaoSocial(c.getString("RazaoSocial"));
			                                        empresa.setNomeFantasia(c.getString("NomeFantasia"));      
			                                        empresa.setCidadeId(Integer.parseInt(c.getString("CidadeId"))); 
			                                        empresa.setLogradouro(c.getString("Logradouro"));    
			                                        empresa.setNumero(c.getString("Numero"));    
			                                        empresa.setComplemento(c.getString("Complemento"));    
			                                        empresa.setBairro(c.getString("Bairro"));    
			                                        empresa.setCep(c.getString("Cep"));    
			                                        empresa.setLatitude(c.getString("Latitude"));    
			                                        empresa.setLongitude(c.getString("Longitude"));    
			                                        empresa.setDataCriacao(dateFormat.parse(c.getString("DataCriacao")));
			                                        empresa.setTipoImagem(c.getString("TipoImagem")); 
			                                        empresa.setEmpresaIdLocal(Integer.parseInt(c.getString("EmpresaIdLocal")));
			                                        empresa.setServidorLocal(c.getString("ServidorLocal")); 
			                                        empresa.setMixProduto(c.getString("MixProduto")); 
			                                        
			                                        if(!c.getString("EmpresaMatrizId").equals("") && !c.getString("EmpresaMatrizId").equals("null")  && c.getString("EmpresaMatrizId") != null)
				                                        empresa.setEmpresaMatrizId(Integer.parseInt(c.getString("EmpresaMatrizId")));
			                                                                                       
			                				  		empresa.setAtualizacaoCardapio(dateFormat.parse(c.getString("AtualizacaoCardapio")));
			                				  		Date data = new Date();                                        
			                                        empresa.setUltimaAtu(dateFormat.parse(dateFormat.format(data.getTime())));    
			                                        dbEmpresa.atualizaEmpresa(empresa);
			                                        
			                                        if(!empresa.getTipoImagem().equals("")){                     	        	               
		                                             	 Global.gravaFoto(c.getString("LogoMedio"), empresa.getCaminhoImagem(), empresa.getNomeImagem());
		                                            }
			                                        
			                                   }	
			                                   
			                                   dbEmpresa.apagaTelefoneEmpresa();
			                                   JSONArray telefoneEmpresaArr = json.getJSONArray("telefoneEmpresa"); 
			                                   for (int i = 0; i < telefoneEmpresaArr.length(); i++) {
			                                        JSONObject c = telefoneEmpresaArr.getJSONObject(i);
			                                        ModelTelefoneEmpresa telefoneEmpresa = new ModelTelefoneEmpresa();
			                                        telefoneEmpresa.setTelefoneEmpresaId(Integer.parseInt(c.getString("TelefoneEmpresaId")));
			                                        telefoneEmpresa.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));
			                                        telefoneEmpresa.setDDD(c.getString("DDD"));
			                                        telefoneEmpresa.setTelefone(c.getString("Telefone"));      
			                                        telefoneEmpresa.setDescricao(c.getString("Descricao")); 
			                                        dbEmpresa.atualizaTelefoneEmpresa(telefoneEmpresa);
			                                   }
			                                   
			                                   
			                                   
			                                   DALMesa dbMesa = new DALMesa(context);
			                                   dbMesa.apagaMesa();
			                                   JSONArray mesaArr = json.getJSONArray("mesas"); 
			                                   for (int i = 0; i < mesaArr.length(); i++) {
			                                        JSONObject c = mesaArr.getJSONObject(i);
			                                        ModelMesa mesa = new ModelMesa();
			                                        mesa.setMesaId(Integer.parseInt(c.getString("MesaId")));			                                        
			                                        mesa.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));
			                                        mesa.setNumero(c.getString("Numero"));
			                                        mesa.setSetor(c.getString("Setor"));      
			                                        dbMesa.atualizaMesa(mesa);
			                                   }
			                                   dbMesa = null;		                                   
			                                   			                                   
			                                   dbEmpresa.apagaModoPagamento();
			                                   JSONArray modoPgtoArr = json.getJSONArray("modoPagamento"); 
			                                   for (int i = 0; i < modoPgtoArr.length(); i++) {
			                                        JSONObject c = modoPgtoArr.getJSONObject(i);
			                                        ModelModoPagamento modoPgto = new ModelModoPagamento();
			                                        modoPgto.setModoPagamentoId(Integer.parseInt(c.getString("ModoPagamentoId")));
			                                        modoPgto.setDescricao(c.getString("Descricao"));      
			                                        dbEmpresa.insereModoPagamento(modoPgto);
			                                   }
			                                   
			                                   dbEmpresa.apagaModoPagamentoEmpresa();
			                                   JSONArray modoPgtoEmpArr = json.getJSONArray("modoPgtoEmpresa"); 
			                                   for (int i = 0; i < modoPgtoEmpArr.length(); i++) {
			                                        JSONObject c = modoPgtoEmpArr.getJSONObject(i);
			                                        ModelModoPagEmpresa modoPgtoEmp = new ModelModoPagEmpresa();
			                                        modoPgtoEmp.setModoPagamentoEmpresaId(Integer.parseInt(c.getString("ModoPagamentoEmpresaId")));
			                                        modoPgtoEmp.setModoPagamentoId(Integer.parseInt(c.getString("ModoPagamentoId")));
			                                        modoPgtoEmp.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));
			                                        dbEmpresa.insereModoPagamentoEmpresa(modoPgtoEmp);
			                                   }
			                                   
			                                   dbEmpresa = null;
			                                   Date data = new Date();                                        
		                                       Global.usuario.setUltimaAtu(dateFormat.parse(dateFormat.format(data.getTime())));   
			                                   DALUsuario dbUsuario = new DALUsuario(context);
			                                   dbUsuario.atualizaUsuario(Global.usuario);
			                                   
			                                   msg.what = 1;			                            	   
			                               }
			                               else {
			                            	   msg.what = 0;
			                            	   msgErro = "Sem conexão com a internet.";
			                               }
				       	        		
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
					  			 {       				
						  				//Toast.makeText(EstabelecimentoActivity.this, "Ok", Toast.LENGTH_LONG).show();
					  			 }
					  			 else if(msg.what == 0) 
					  			 {       		
					  				Toast.makeText(EstabelecimentoActivity.this, msgErro, Toast.LENGTH_LONG).show();
					  			 }
					  			 
					  			carregaEstabelecimento();
				  				
							} catch  (Exception ex) {
								ex.printStackTrace();
							}	 
				  		 }
				  	};
					
				 
				 
			 } else 
		  		carregaEstabelecimento();
			 
			 
			 
	     }
		 catch(Exception ex){
			 ex.printStackTrace();
		 }
		
	}
	

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try
        {      	 
        	menu.add(0,0,0,"Selecionar Cidade");      	
        	menu.add(1,1,1,"Alterar Cadastro");  
        	menu.add(2,2,2,"Alterar Senha");       	
        	menu.add(3,3,3,"Trocar Usuário");      
        	menu.add(4,4,4,"Sair");
        	
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
		   		Global.telaCadatro = false;				
				Intent intent = new Intent(getApplicationContext(), CidadeActivity.class); 
		   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;    
	   	  }
	      case 1:{
				Intent intent = new Intent(getApplicationContext(), AlteracaoCadastroActivity.class); 
		   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
	      }
	      case 2:{
				Intent intent = new Intent(getApplicationContext(), AlterarSenhaActivity.class); 
		   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
	      }
	      case 3:{
				Global.primeiraVez  = false;
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class); 
		   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
	      }
	      case 4:{

			  Global.primeiraVez = true;
	          EstabelecimentoActivity.this.finish();
	          break;
	      }
	   }

	   return true;
   }
   
   @Override 
   public void onBackPressed(){
	   
   }
   
	
   public void Progresso(String titulo){
         progressDialog = new ProgressDialog(EstabelecimentoActivity.this);
         progressDialog.setTitle(titulo);
         progressDialog.setMessage("Processando...");
         progressDialog.setIndeterminate(true);
         progressDialog.setCancelable(true);
         progressDialog.show();
    }
   
   
   
   private void carregaEstabelecimento() {
	   
	   try {
		   
		    DALEmpresa dbEmpresa = new DALEmpresa(context);
	        lstEmpresa = dbEmpresa.selecinarEmpresaCid(Global.usuario.getCidadeId(), edtEstBuscar.getText().toString());	   	               
	        adapter = new AdapterEmpresa(context, R.layout.item_estabelecimento, lstEmpresa);		 	          	   	         
		   	lstEstabelecimento.setAdapter((ListAdapter) adapter);
		   	registerForContextMenu(lstEstabelecimento);
		   
	   } catch (Exception ex) {
		   ex.printStackTrace();
	   }
   }
   
   @Override
   public void onCreateContextMenu(ContextMenu menu, View v,  ContextMenuInfo menuInfo) {
     if (v.getId() == R.id.lstEstabelecimento) {
       
    	 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    	 menu.setHeaderTitle(lstEmpresa.get(info.position).getNomeFantasia());
    	 String[] menuItens = getResources().getStringArray(R.array.opcoesEmpresa);
    	 for (int i = 0; i < menuItens.length; i++) {
    		 menu.add(Menu.NONE, i, i, menuItens[i]);
    	 }
     }
   }
   
   @Override
   public boolean onContextItemSelected(MenuItem item) {
     AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
     int menuItemIndex = item.getItemId();
    
     if(menuItemIndex == 0) {
    	 DALEmpresa dbEmpresa = new DALEmpresa(context);
    	 Global.empresa = dbEmpresa.selecinarEmpresaId(lstEmpresa.get(info.position).getEmpresaId());
    	 Global.urlConexaoLocal = Global.empresa.getServidorLocal();
     	 dbEmpresa = null;
     	 Intent intent = new Intent(getApplicationContext(), DetalhesEmpresaActivity.class); 
   		 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(intent);
     }
     return true;
   }
   
}
