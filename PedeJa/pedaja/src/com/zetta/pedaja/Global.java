package com.zetta.pedaja;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

public  class Global {
	
	public static ModelUsuario usuario ; 
	public static ModelEmpresa empresa ; 
	public static ModelProduto produto ; 
	public static ModelConta conta; 
	public static boolean telaCadatro ; 
	public static boolean primeiraVez = true; 
	public static boolean visuDestaqueCarpadio = false; 
	public static String mesaSelecionada; 
	public static boolean abrirConta = true;
	public static boolean encerrarConta = false;  
	public static int produtoSelecionado = 0; 
	public static int quantidadeSelecionada = 0; 
	public static  ArrayList<ModelTipoProduto> lstAcompanhamento;
	public static String urlConexaoServidor = "http://www.direttasoft.com.br/projeto/";
	public static String urlConexaoLocal = "";
	
	public static int MetadeSelecionada;
	public static ModelMetade modelMetade01;
	public static ModelMetade modelMetade02;
	public static ModelMetade modelMetade03;
	public static boolean pedidoMetade = false;

	public static int categoriaMeioMeio = 0; 
	public static String Tamanho = ""; 
	
	public static boolean apagarImagensDiretorio(File path) {
		try { 
			if(path.exists()) {
		      
		    	File[] files = path.listFiles();
		      
		    	if (files == null) 
		          return true;
		      
		       for(int i=0; i<files.length; i++) {
		         if(files[i].isDirectory()) 
		        	 apagarImagensDiretorio(files[i]);	         
		         else 
		           files[i].delete();	         
		       }
		    }
		} 
		catch (Exception e) {
	         e.printStackTrace();
	    }
		
	    return(path.delete());
	  }
	
	
	public static void gravaFoto(String fotoS, String path, String filename){
	   	try { 
	   		byte[] encodeByte  = Base64.decode(fotoS, Base64.DEFAULT); 
	   		Bitmap bmImg = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	   		bmImg = Bitmap.createBitmap(bmImg);
	          
	   		   File sd = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + path);
	           File dest = new File(sd, filename);	              	
		            
	          
	           
		    	if (!sd.isDirectory() || !sd.exists()){
		    		sd.mkdirs();
		    	}
		        
		    	FileOutputStream out = new FileOutputStream(dest);
		        bmImg.compress(Bitmap.CompressFormat.PNG, 90, out);
		        out.flush();
		        out.close();
	  	                 
		            	
		    } catch (Exception e) {
		         e.printStackTrace();
		    }
	   }
	
	
	public static void AtualizarCidade(Context context){
		
		try {

	  		JSONParser jsonParser = new JSONParser();
			String httpGet = Global.urlConexaoServidor + "cidade.php";
			List<NameValuePair>   params = new ArrayList<NameValuePair>();
			JSONObject  json = jsonParser.makeHttpRequest(httpGet,"POST", params);	  		                              	 
	        	  
	    	DALEstado dalEstado = new DALEstado(context);
	    	dalEstado.apagaTodosEstados();
	        JSONArray estadoArr = json.getJSONArray("estado"); 
	        for (int i = 0; i < estadoArr.length(); i++) {
	             JSONObject c = estadoArr.getJSONObject(i);
	             ModelEstado estado = new ModelEstado();
	             estado.setEstadoId(Integer.parseInt(c.getString("EstadoId")));
	             estado.setUf(c.getString("Uf"));         
	             estado.setNome(c.getString("Nome"));    
	             dalEstado.insereEstado(estado);
	         }
	         dalEstado = null;
	         
	         DALCidade dalCidade = new DALCidade(context);
	         dalCidade.apagaTodasCidades();
	    	 JSONArray cidadeArr = json.getJSONArray("cidade"); 
	         for (int i = 0; i < cidadeArr.length(); i++) {
	             JSONObject c = cidadeArr.getJSONObject(i);
	             ModelCidade cidade = new ModelCidade();
	             cidade.setCidadeId(Integer.parseInt(c.getString("CidadeId")));
	             cidade.setEstadoId(Integer.parseInt(c.getString("EstadoId")));     
	             cidade.setNome(c.getString("Nome"));    
	             dalCidade.insereCidade(cidade);
	          }
	          dalCidade = null;
	          
          } catch (Exception ex) {
        	  ex.printStackTrace();
        	  
          }	
	}
	
	
	
	@SuppressLint("SimpleDateFormat")
	public static int[] inserePedido(Context context, int tipoProdutoId, double valor, int sequencia, String divisao){

		int sucesso = 0, sequenciaRetorno = 0;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			
	  		JSONParser jsonParser = new JSONParser();
			String httpGet = Global.urlConexaoLocal + "itemConta.php";
			List<NameValuePair>   params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("P1", "" + Global.conta.getContaId()));
			params.add(new BasicNameValuePair("P2", "" + tipoProdutoId));
			params.add(new BasicNameValuePair("P3", "" + valor));
			params.add(new BasicNameValuePair("P4", "" + sequencia));
			params.add(new BasicNameValuePair("P5", "" + Global.usuario.getUsuarioId()));
			params.add(new BasicNameValuePair("P6", "" + divisao));
			
			
			JSONObject  json = jsonParser.makeHttpRequest(httpGet,"POST", params);	  		                              	 
			sucesso = json.getInt("sucesso");
			if(sucesso == 1) {				
		    	DALConta dalConta = new DALConta(context);
		        JSONArray itemContaArr = json.getJSONArray("itemConta"); 
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
		             
		             sequenciaRetorno = conta.getSequencia();
		             dalConta.insereItemConta(conta);
		         }
		        dalConta = null;
			}
			
	          
        } catch (Exception ex) {
        	  ex.printStackTrace();
        	  sucesso = 0;
        }	
		
		
		int[] retorno = new int[] {sucesso, sequenciaRetorno};
		return retorno;		
		
	}
	
	
	
	
	
	public static int visualizarPedido(Context context, int sequencia){

		int sucesso = 0;
		
		try {
			
	  		JSONParser jsonParser = new JSONParser();
			String httpGet = Global.urlConexaoLocal + "visualizarConta.php";
			List<NameValuePair>   params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("P1", "" + Global.empresa.getEmpresaId()));
			params.add(new BasicNameValuePair("P2", "" + Global.conta.getContaId()));
			params.add(new BasicNameValuePair("P3", "" + Global.usuario.getUsuarioId()));
			params.add(new BasicNameValuePair("P4", "" + sequencia));
			
			
			JSONObject  json = jsonParser.makeHttpRequest(httpGet,"POST", params);	  	
	    	DALVisualizarPedido dbVisualizarPedido = new DALVisualizarPedido(context);
	        JSONArray itemContaArr = json.getJSONArray("pedidoCompleto"); 
	        for (int i = 0; i < itemContaArr.length(); i++) {
	             JSONObject c = itemContaArr.getJSONObject(i);
	             ModelVisualizarPedido model = new ModelVisualizarPedido();
	             
	             model.setContaId(Integer.parseInt(c.getString("ContaId")));
	             model.setQtdPedida(Integer.parseInt(c.getString("QtdPedida")));
	             model.setSequencia(Integer.parseInt(c.getString("Sequencia")));
	             model.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));
	             model.setDescCategoria(c.getString("DescCategoria"));
	             model.setProduto(c.getString("Produto"));
	             model.setDescricao(c.getString("Descricao"));	             
	             model.setValor(Double.parseDouble(c.getString("Valor")));
	             model.setStatusPedido(c.getString("StatusPedido"));
	             model.setTipoProdutoIdVar(Integer.parseInt(c.getString("TipoProdutoIdVar")));
	             model.setDescricaoAcomp(c.getString("DescricaoAcomp"));	     
	             model.setTipoProdutoIdAcomp(c.getString("TipoProdutoIdAcomp"));	  
	             model.setValorAcomp(Double.parseDouble(c.getString("ValorAcomp")));  
	             model.setDescricaoMeioMeio(c.getString("DescricaoMeioMeio"));	   
	             model.setValorMeioMeio(Double.parseDouble(c.getString("ValorMeioMeio")));   
	             model.setQtdMeioMeio(Integer.parseInt(c.getString("QtdMeioMeio")));		             
	             
	             dbVisualizarPedido.inserirVisualizarPedido(model);
	         }
	        dbVisualizarPedido = null;
			
			
	          
        } catch (Exception ex) {
        	  ex.printStackTrace();
        	  sucesso = 0;
        }	
		
		
		return sucesso;		
		
	}
	
	
	
	
	
}
