package com.zetta.pedaja;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteStatement;

public class DALVisualizarPedido extends DAL {

	public DALVisualizarPedido(Context context) {
		super(context);
	}
	
	@SuppressLint("SimpleDateFormat")
	public boolean inserirVisualizarPedido(ModelVisualizarPedido model) {
		boolean Novo = false;
	    try{	  
	    		    	
	    	String sql = " select count(*) from VisualizarPedido " +
			   		     " where ContaId = " + model.getContaId() + 
			   		     "  and  Sequencia = " + model.getSequencia();
	    	
	    	int total = (int) DatabaseUtils.longForQuery(db, sql, null);
	    	
	    	if (total <= 0) {	    	
	    	
			      sql = " INSERT INTO VisualizarPedido " +
			    		  	   " (ContaId, " +
			    		  	   "  Sequencia,  " +			    		  	 		  	   
			    		  	   "  QtdPedida, " +  
			    		  	   "  EmpresaId, " +	 		  	   
			    		  	   "  DescCategoria, " +	  	   
			    		  	   "  Produto, " +
			    		  	   "  Descricao, " +
			    		  	   "  Valor, " +	  	 
			    		  	   "  StatusPedido, " +	    		  	 
			    		  	   "  TipoProdutoIdVar, " +  
			    		  	   "  DescricaoAcomp, " +	 		  	   
			    		  	   "  TipoProdutoIdAcomp, " +	  	   
			    		  	   "  ValorAcomp, " +
			    		  	   "  DescricaoMeioMeio, " +
			    		  	   "  ValorMeioMeio, " +	 
			    		  	   "  QtdMeioMeio)  " +	
			    		  	   " values ( " +
			    		  	   model.getContaId() 			 + " ,  " +
			    		  	   model.getSequencia() 		 + " ,  " +
			    		  	   model.getQtdPedida() 		 + " ,  " +
			    		  	   model.getEmpresaId() 	 	 + " , '" +
			    		  	   model.getDescCategoria()		 + "', '" +
			    		  	   model.getProduto()			 + "', '" +
			    		  	   model.getDescricao()			 + "',  " +
			    		  	   model.getValor()				 + " , '" +
			    		  	   model.getStatusPedido()		 + "',  " +
			    		  	   model.getTipoProdutoIdVar()	 + " , '" +			    		  	   
			    		  	   model.getDescricaoAcomp()	 + "', '" +	    		  	   
			    		  	   model.getTipoProdutoIdAcomp() + "',  " +
			    		  	   model.getValorAcomp() 		 + " , '" +
			    		  	   model.getDescricaoMeioMeio()	 + "',  " +
			    		  	   model.getValorMeioMeio() 	 + " ,  " +
			    		  	   model.getQtdMeioMeio() 	 	 + " ) ";
			    		  	   
			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
			      Novo = true;
	    	}  
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	    return Novo;
	}
	
	
	public ArrayList<ModelVisualizarPedido> selecionarVisualizarPedido() {
		  ArrayList<ModelVisualizarPedido> lista = new ArrayList<ModelVisualizarPedido>();
		  try{

		   		  
			  String sql = " select  ContaId, " +
				  	   "  			 Sequencia, " +			    		  	 		  	   
				  	   "  			 QtdPedida, " +  
				  	   "  			 EmpresaId, " +	 		  	   
				  	   "  			 DescCategoria, " +	  	   
				  	   "  			 Produto, " +
				  	   "             Descricao, " +
				  	   "  			 Valor, " +	  	 
				  	   "  			 StatusPedido, " +	    		  	 
				  	   "  			 TipoProdutoIdVar, " +  
				  	   "  			 DescricaoAcomp, " +	 		  	   
				  	   "  			 TipoProdutoIdAcomp, " +	  	   
				  	   "             ValorAcomp, " +
				  	   "  			 DescricaoMeioMeio, " +
				  	   "  			 ValorMeioMeio, " +	 
				  	   "  			 QtdMeioMeio " +
			  			"	from VisualizarPedido a " +
			  			"     where a.ContaId = " + Global.conta.getContaId() +
			  			"       and a.EmpresaId = " + Global.conta.getEmpresaId() +
			  			"   order by ContaId, Sequencia ";			 
			  
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelVisualizarPedido model = new ModelVisualizarPedido();

		        	model.setContaId(Integer.parseInt(c.getString(0)));//ContaId  
		        	model.setSequencia(Integer.parseInt(c.getString(1)));//Sequencia   
		        	model.setQtdPedida(Integer.parseInt(c.getString(2)));//QtdPedida  
		        	model.setEmpresaId(Integer.parseInt(c.getString(3)));//EmpresaId   		        	
		        	model.setDescCategoria(c.getString(4));//DescCategoria	        	
		        	model.setProduto(c.getString(5));//Produto      	
		        	model.setDescricao(c.getString(6));//Descricao
		        	model.setValor(Double.parseDouble(c.getString(7)));//Valor  
		        	model.setStatusPedido(c.getString(8));//StatusPedido
		        	model.setTipoProdutoIdVar(Integer.parseInt(c.getString(9)));//TipoProdutoIdVar   	
		        	model.setDescricaoAcomp(c.getString(10));//DescricaoAcomp
		        	model.setTipoProdutoIdAcomp(c.getString(11));//TipoProdutoIdAcomp
		        	model.setValorAcomp(Double.parseDouble(c.getString(12)));//ValorAcomp  
		        	model.setDescricaoMeioMeio(c.getString(13));//DescricaoMeioMeio
		        	model.setValorMeioMeio(Double.parseDouble(c.getString(14)));//ValorMeioMeio
		        	model.setQtdMeioMeio(Integer.parseInt(c.getString(15)));//QtdMeioMeio   
		        	
		        	c.moveToNext();
		        	lista.add(model);
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return lista;
	  }
	
	

}
