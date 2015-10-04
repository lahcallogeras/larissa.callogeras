package com.zetta.pedaja;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

@SuppressLint("SimpleDateFormat")
public class DALConta extends DAL{

	public DALConta(Context context) {
		super(context);
	}
	
	public void insereConta(ModelConta conta) {

    	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      
	    try{	  
	    	
		    String  sql = " INSERT INTO Conta " +
		    		  	   " (ContaId, " +
		    		  	   "  EmpresaId,  " +	
		    		  	   "  MesaId,  " +	
		    		  	   "  UsuarioId,  " +
		    		  	   "  GarcomId,  " +
		    		  	   "  CodigoAbertura,  " +			
		    		  	   "  DataAbertura)  " + 		  	
		    		  	   " values ( " +
		    		  	 conta.getContaId() + ", " +
		    		  	 conta.getEmpresaId() + ", " +
		    		  	 conta.getMesaId() + ", " +
		    		  	 conta.getUsuarioId() + ", " +
		    		  	 conta.getGarcomId() + ", '" +
		    		  	 conta.getCodigoAbertura() + "', '" +
		    		  	 formato.format(conta.getDataAbertura())+ "') " ;
		      
		      SQLiteStatement insertStmt = null;	     
		      insertStmt = db.compileStatement(sql);
		      insertStmt.executeInsert();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	}
	
	
	public void insereItemConta(ModelItemConta itemConta) {

    	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      
	    try{	  
	    	
	    	
		    String  sql = " INSERT INTO ItensConta " +
		    		  	   " (ItensContaId, " +
		    		  	   "  ContaId,  " +	
		    		  	   "  DataPedido,  " +	
		    		  	   "  TipoProdutoId,  " +	
		    		  	   "  Sequencia,  " +	
		    		  	   "  Valor,  " +		
		    		  	   "  UsuarioId,  " +
		    		  	   "  GarcomId,  " +
		    		  	   "  StatusPedido," +
		    		  	   "  Divisao)  " + 		  	
		    		  	   " values ( " +
		    		  	 itemConta.getItensContaId() + ", " +
		    		  	 itemConta.getContaId() + ", '" +
		    		  	 formato.format(itemConta.getDataPedido()) + "', " +
		    		  	 itemConta.getTipoProdutoId() + ", " +
		    		  	 itemConta.getSequencia() + ", " +
		    		  	 itemConta.getValor() + ", " +
		    		  	 itemConta.getUsuarioId() + ", " +
		    		  	 itemConta.getGarcomId() + ", '" +
		    		  	 itemConta.getStatusPedido() + "', '" +
		    		  	 itemConta.getDivisao() + "') " ;
		      
		      SQLiteStatement insertStmt = null;	     
		      insertStmt = db.compileStatement(sql);
		      insertStmt.executeInsert();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	}
	
	
	public void alteraItemConta(ModelItemConta itemConta) {

    	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      
    	
    	String DataBaixa = "null";
    	if(itemConta.getDataBaixa() != null)
    		DataBaixa =	"'" + formato.format(itemConta.getDataBaixa()) + "'";
    		
	    try{	  
	    	
	    	
	    	 String sql = " UPDATE  ItensConta  SET " +    		  	   
	    		  	 "  TipoProdutoId = " + itemConta.getTipoProdutoId() + ", " +
	    		  	 "  Valor = " + itemConta.getValor() + ", " +    		  	
	    		  	 "  StatusPedido = '" + itemConta.getStatusPedido() + "', " +
	    		  	 "  DataBaixa = " + DataBaixa +
	    		  	 " WHERE ItensContaId = " + itemConta.getItensContaId();	
	    	 
		      SQLiteStatement insertStmt = null;	     
		      insertStmt = db.compileStatement(sql);
		      insertStmt.executeInsert();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	}
	

	public ModelConta selecionaContaAberta(int EmpresaId) {
		ModelConta conta = null;
		  try{

		      DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
		   		  
			  String sql = " select * from Conta " +
			   		     " where EmpresaId = " +  EmpresaId +  
			   		     "   and DataEncerramento is null ";
			  
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        if (c.isAfterLast() == false) {
		        	conta = new ModelConta();
		        	conta.setContaId(Integer.parseInt(c.getString(0)));//ContaId 
		        	conta.setEmpresaId(Integer.parseInt(c.getString(1)));//EmpresaId    
		        	conta.setMesaId(Integer.parseInt(c.getString(2)));//MesaId     
		        	conta.setUsuarioId(Integer.parseInt(c.getString(3)));//UsuarioId     
		        	conta.setDataAbertura(formato.parse(c.getString(4)));//DataAbertura
		        	conta.setCodigoAbertura(c.getString(8));//CodigoAbertura   
		        	c.moveToNext();
		        	 	
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return conta;
	  }
	
	

	public int selecionaUltimaSequencia(int ContaId) {
		  int Sequencia = 1;
		  try{
  
		   		  
			  String sql = " select Max(Sequencia) + 1  Sequencia " +
			  			 "  from ItensConta " +
			   		     " where ContaId = " +  ContaId;
			  
			    Cursor c = db.rawQuery(sql, null);
		        if (c.moveToFirst()) { 
		        	Sequencia = Integer.parseInt(c.getString(0));//Sequencia 		        	
		        	c.moveToNext();
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return Sequencia;
	  }
			
	public ArrayList<ModelItemConta> selecionaUltimoPedido(int ContaId) {
		  ArrayList<ModelItemConta> lstItemConta = new ArrayList<ModelItemConta>();
		  try{

		   		  
			  String sql = " select p.Descricao   " +
			  		    "    , b.Descricao " +
			  		    "    , b.Tipo " +
			  			"	 , a.TipoProdutoId " +
			  			"    , a.Valor, a.Divisao, c.Descricao  " +
			  			"	from ItensConta a, TipoProduto b, Produto p, Categoria c " +
			  			"     where a.ContaId = " + ContaId +
			  			"       and a.TipoProdutoId = b.TipoProdutoId " +
			  			"       and b.ProdutoId = p.ProdutoId      " +
			  			"       and p.CategoriaId = c.CategoriaId      " +
			  			"       and p.EmpresaId = c.EmpresaId      " +
			  			"       and a.StatusPedido not in ('CA')  " +
			  			"       and a.Sequencia =  " +
					    "			 (select Max(Sequencia) Sequencia " +
			  			" 					 from ItensConta " +
			   		    " 					where ContaId = " +  ContaId + ")" +
			  			"   order by p.Descricao,  b.Descricao, a.Divisao ";
			  
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelItemConta itemConta = new ModelItemConta();
		        	
		        	itemConta.setDescricaoProduto(c.getString(0));//Descricao
		        	itemConta.setDescricaoTipoProduto(c.getString(1));
		        	itemConta.setTipo(c.getString(2));//Tipo
		        	itemConta.setTipoProdutoId(Integer.parseInt(c.getString(3)));//TipoProdutoId   
		        	itemConta.setQuantidadeProduto(1);//Quantidade Produtos    
		        	itemConta.setValor(Double.parseDouble(c.getString(4)));//Valor  
		        	itemConta.setDivisao(c.getString(5));//Di
		        	itemConta.setDescricaoCategoria(c.getString(6));//Descrição Categoria
		        	c.moveToNext();
		        	lstItemConta.add(itemConta);
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return lstItemConta;
	  }
	
	public String[] selecionaDescTipoProduto(int TipoProdutoId) {
		String[] descProduto = {"", ""};
		  try{

		   
			  String sql = " select p.Descricao, tp.Descricao " +
					  	 " from Produto p, TipoProduto tp" +
			  			 "  where tp.ProdutoId = p.ProdutoId " +
			   		     "   and tp.TipoProdutoId = " +  TipoProdutoId;
			  
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        if (c.isAfterLast() == false) {
		        	
		        	descProduto[0] = c.getString(0);
		        	descProduto[1] = c.getString(1);
		        	c.moveToNext();
		        	 	
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return descProduto;
	  }
	
	
	
	
	public ArrayList<ModelItemConta> selecionaVisualizacoConta(int ContaId, int UsuarioId) {
		  
		ArrayList<ModelItemConta> lstItemConta = new ArrayList<ModelItemConta>();
		  try{
    
			  String sql = " select p.Descricao   " +
			  		    "    , b.Descricao " +
			  		    "    , b.Tipo " +
			  			"	 , a.TipoProdutoId " +
			  			"    , count(distinct a.ItensContaId) " +
			  			"    , sum(a.Valor)  " +
			  			"	from ItensConta a, TipoProduto b, Produto p " +
			  			"     where a.ContaId = " + ContaId +
			  			"       and a.UsuarioId = " +  UsuarioId +
			  			"       and a.TipoProdutoId = b.TipoProdutoId " +
			  			"       and b.Contabil = 1 " +
			  			"       and b.ProdutoId = p.ProdutoId      " +
			  			"       and a.StatusPedido not in ('CA')  " +
			  			"    group by b.Descricao, a.TipoProdutoId " +
			  			"   order by p.Descricao,  b.Descricao ";
			  
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelItemConta itemConta = new ModelItemConta();
		        	
		        	String tipo = "";
		        	if(c.getString(2).equals("AC"))
		        		tipo = " - Acomp.";
		        	
		        	itemConta.setDescricaoProduto(c.getString(0));//Descricao
		        	itemConta.setDescricaoTipoProduto(c.getString(1) + tipo);
		        	itemConta.setQuantidadeProduto(Integer.parseInt(c.getString(4)));//Quantidade Produtos    
		        	itemConta.setValor(Double.parseDouble(c.getString(5)));//Valor     
		        	c.moveToNext();
		        	lstItemConta.add(itemConta);
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return lstItemConta;
	  }
	
	public void encerraConta(ModelConta conta) {

    	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");          	
    		
	    try{	  
	    	 String sql = " UPDATE  Conta  SET " +    		  	   
	    		  	 "  ModoPagamentoId = " + conta.getModoPagamentoId() + ", " +
	    		  	 "  DataEncerramento = '" + formato.format(conta.getDataEncerramento()) + "'" +
	    		  	 " WHERE ContaId = " + conta.getContaId();	
	    	 
		      SQLiteStatement insertStmt = null;	     
		      insertStmt = db.compileStatement(sql);
		      insertStmt.executeInsert();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	}

}
