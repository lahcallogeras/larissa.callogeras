package com.zetta.pedaja;
 
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteStatement;

public class DALCategoria extends DAL {
	
	public DALCategoria(Context context) {
		super(context);
	}
	

	public int  TotalMeioMeio(int EmpresaId ) {
		
		int Total = 0; 
	    try{	  
	    		    	
	    	String sql = " select count(*) from Categoria " +
			   		     " where MixProduto > 1" +
			   		     "  and EmpresaId = " + EmpresaId;
	    	
	    	Total = (int) DatabaseUtils.longForQuery(db, sql, null);
    	} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	    
	    return Total;
	}

	
	public boolean atualizaCategoria(ModelCategoria categoria) {
		boolean Novo = false; 
	    try{	  
	    		    	
	    	String sql = " select count(*) from Categoria " +
			   		     " where CategoriaId= " +  categoria.getCategoriaId();
	    	
	    	int total = (int) DatabaseUtils.longForQuery(db, sql, null);
	    		    	
	    	if (total <= 0) {	    	
	    	
			      sql = " INSERT INTO Categoria " +
			    		  	   " (CategoriaId, " +
			    		  	   "  EmpresaId,  " +		
			    		  	   "  Descricao," +
			    		  	   "  MixProduto)  " + 		  	
			    		  	   " values ( " +
			    		  	   categoria.getCategoriaId() + ", " +
			    		  	   categoria.getEmpresaId()  + " , '" +
			    		  	   categoria.getDescricao() + "' ,  " +
			    		  	   categoria.getMixProduto() + " ) " ;
			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
			      Novo = true;
	    	} else {
	    		
	    		 sql = " UPDATE  Categoria  SET " +    		  	   
		    		  	 "  EmpresaId = " + categoria.getEmpresaId()   + " , " +
		    		  	 "  Descricao = '" + categoria.getDescricao()  + "', " +  
		    		  	 "  MixProduto = " + categoria.getMixProduto() +    			  
		    		  	 " WHERE CategoriaId = " + categoria.getCategoriaId();	
  		  	   
			      SQLiteStatement alteraStatemente = null;	     
			      alteraStatemente = this.db.compileStatement(sql);
			      alteraStatemente.executeInsert();
	    	}
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	    return Novo;
	}
	
	public ArrayList<ModelCategoria> selecionaCategoria(int empresaId) {
		 
		   ArrayList<ModelCategoria> lstCategoria = new ArrayList<ModelCategoria>();
		  try{
			  
			  	DALProduto dbProduto = new DALProduto(context);
		   		  
			    String  sql = " select * from Categoria " +
			    		"  where EmpresaId =  " + empresaId +
					    "  and CategoriaId in (Select c.CategoriaId " +
			    		"						 from TipoProduto b, Produto c " +
					    "						   where b.ProdutoId = c.ProdutoId	 " +
			    		"						     and b.Visivel = 1) ";
			    
			    
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelCategoria categoria = new ModelCategoria();
		        	categoria.setCategoriaId(Integer.parseInt(c.getString(0)));	
		        	categoria.setEmpresaId(Integer.parseInt(c.getString(1)));	
		        	categoria.setDescricao(c.getString(2));
		        	categoria.setMixProduto(c.getInt(3));
		        	categoria.setLstProduto(dbProduto.selecionaProduto(empresaId, categoria.getCategoriaId()));
		        	c.moveToNext();
		        	lstCategoria.add(categoria);
		        }
		        c.close();
		        
		        dbProduto = null;
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return lstCategoria;
	  }
	
	
	public ArrayList<ModelCategoria> selecionaCategoriaMeioMeio(int empresaId) {
		 
		  ArrayList<ModelCategoria> lstCategoria = new ArrayList<ModelCategoria>();
		  try{
			  
			    String  sql = " select * from Categoria " +
			    		"  where EmpresaId =  " + empresaId +
					    "  and MixProduto > 1 ";
			    
			    
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelCategoria categoria = new ModelCategoria();
		        	categoria.setCategoriaId(Integer.parseInt(c.getString(0)));	
		        	categoria.setEmpresaId(Integer.parseInt(c.getString(1)));	
		        	categoria.setDescricao(c.getString(2));
		        	categoria.setMixProduto(c.getInt(3));
		        	c.moveToNext();
		        	lstCategoria.add(categoria);
		        }
		        c.close();
		        
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return lstCategoria;
	  }
	
	
	public ModelCategoria selecionaCategoriaId(int CategoriaId) {
		 
		   ModelCategoria categoria = new ModelCategoria();
		  try{
			  		   		  
			    String  sql = " select * from Categoria " +
			    		"  where CategoriaId =  " + CategoriaId;
			    
			    
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {		       
		        	categoria.setCategoriaId(Integer.parseInt(c.getString(0)));	
		        	categoria.setEmpresaId(Integer.parseInt(c.getString(1)));	
		        	categoria.setDescricao(c.getString(2));
		        	categoria.setMixProduto(c.getInt(3));
		        	c.moveToNext();
		        }
		        c.close();
		        		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return categoria;
	  }
	

}
