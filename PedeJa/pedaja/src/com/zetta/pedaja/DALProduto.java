package com.zetta.pedaja;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteStatement;

public class DALProduto  extends DAL {

	public DALProduto(Context context) {
		super(context);
	}
	
	
	public boolean atualizaProduto(ModelProduto produto) {
		boolean Novo = false; 
	    try{	  
	    		    	
	    	String sql = " select count(*) from Produto " +
			   		     " where ProdutoId = " +  produto.getProdutoId();
	    	
	    	int total = (int) DatabaseUtils.longForQuery(db, sql, null);
	    		    	
	    	if (total <= 0) {	    	
	    	
			      sql = " INSERT INTO Produto " +
			    		  	   " (ProdutoId, " +
			    		  	   "  CategoriaId, " +
			    		  	   "  EmpresaId,  " +	
			    		  	   "  Detalhe,  " +		
			    		  	   "  Descricao)  " + 		  	
			    		  	   " values ( " +
			    		  	 produto.getProdutoId() + ", " +
			    		  	 produto.getCategoriaId() + ", " +
			    		  	 produto.getEmpresaId()  + ", '" +
			    		  	 produto.getDetalhe()  + "', '" +
			    		  	 produto.getDescricao() + "') " ;
			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
			      Novo = true;
	    	} else {
	    		
	    		 sql = " UPDATE  Produto  SET " +    		  	   
		    		  	 "  CategoriaId = " + produto.getCategoriaId() + ", " +
		    		  	 "  EmpresaId = " + produto.getEmpresaId() + ", " +
		    		  	 "  Detalhe = '" + produto.getDetalhe() + "', " +    
		    		  	 "  Descricao = '" + produto.getDescricao() + "' " +    			  
		    		  	 " WHERE ProdutoId = " + produto.getProdutoId();	
  		  	   
			      SQLiteStatement alteraStatemente = null;	     
			      alteraStatemente = this.db.compileStatement(sql);
			      alteraStatemente.executeInsert();
	    	}
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	    return Novo;
	}
	
	public ArrayList<ModelProduto> selecionaProduto(int empresaId, int categoriaId) {
		 
		   ArrayList<ModelProduto> lstProduto = new ArrayList<ModelProduto>();
		  try{
			  
		   		  
			    String  sql = " select * from Produto " +
			    		" where EmpresaId =  " + empresaId +
			    		"  and CategoriaId = " + categoriaId +			    
			    		"  and ProdutoId in (Select b.ProdutoId " +
			    		"						 from TipoProduto b " +
			    		"						   where b.Visivel = 1) ";
			    		
			    
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelProduto produto = new ModelProduto();
		        	produto.setProdutoId(Integer.parseInt(c.getString(0)));	
		        	produto.setCategoriaId(Integer.parseInt(c.getString(1)));	
		        	produto.setEmpresaId(Integer.parseInt(c.getString(2)));	
		        	produto.setDescricao(c.getString(3));
		        	produto.setDetalhe(c.getString(4));
		        	c.moveToNext();
		        	lstProduto.add(produto);
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return lstProduto;
	  }
	
	public ModelProduto selecionaProdutoId(int produtoId) {

    	ModelProduto produto = new ModelProduto();
		  try{
			  
		   		  
			    String  sql = " select * from Produto " +
			    		" where ProdutoId =  " + produtoId;
			    
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	produto.setProdutoId(Integer.parseInt(c.getString(0)));	
		        	produto.setCategoriaId(Integer.parseInt(c.getString(1)));	
		        	produto.setEmpresaId(Integer.parseInt(c.getString(2)));	
		        	produto.setDescricao(c.getString(3));
		        	produto.setDetalhe(c.getString(4));
		        	c.moveToNext();
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return produto;
	  }

}
