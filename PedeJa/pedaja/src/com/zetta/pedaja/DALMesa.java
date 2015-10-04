package com.zetta.pedaja;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteStatement;

public class DALMesa extends DAL {

	public DALMesa(Context context) {
		super(context);
	}
	
	 public void apagaMesa() {		   
		  try{		  
			    String  sql = " DELETE FROM Mesas ";	
			      
			    SQLiteStatement deleteStmt = null;	     
			    deleteStmt = this.db.compileStatement(sql);
			    deleteStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
	    }
	
	public boolean atualizaMesa(ModelMesa mesa) {
		boolean Novo = false; 
	    try{	  
	    		    	
	    	String sql = " select count(*) from Mesas " +
			   		     " where MesaId = " +  mesa.getMesaId();
	    	
	    	int total = (int) DatabaseUtils.longForQuery(db, sql, null);
	    
	    	
	    	if (total <= 0) {	    	
	    	
			      sql = " INSERT INTO Mesas " +
			    		  	   " (MesaId, " +
			    		  	   "  EmpresaId, " +
			    		  	   "  Numero,  " +		
			    		  	   "  Setor)  " + 		  	
			    		  	   " values ( " +
			    		  	 mesa.getMesaId() + ", " +
			    		  	 mesa.getEmpresaId() + ", '" +
			    		  	 mesa.getNumero()  + "', '" +
			    		  	 mesa.getSetor() + "') " ;
			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
			      Novo = true;
	    	} else {
	    		
	    		 sql = " UPDATE  Mesas  SET " +    		  	   
		    		  	 "  EmpresaId = " + mesa.getEmpresaId() + ", " +
		    		  	 "  Numero = '" + mesa.getNumero() + "', " +
		    		  	 "  Descricao = '" + mesa.getSetor() + "' " +    			  
		    		  	 " WHERE MesaId = " + mesa.getMesaId();	
  		  	   
			      SQLiteStatement alteraStatemente = null;	     
			      alteraStatemente = this.db.compileStatement(sql);
			      alteraStatemente.executeInsert();
	    	}
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	    return Novo;
	}
	

	public ArrayList<ModelMesa> selecionarMesas(int EmpresaId) {
		 
		   ArrayList<ModelMesa> lstMesa = new ArrayList<ModelMesa>();
		  try{
			  
		   		  
			  String sql = " select * from Mesas " +
			   		     " where EmpresaId = " +  EmpresaId;
			  
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelMesa mesa = new ModelMesa();
		        	mesa.setMesaId(Integer.parseInt(c.getString(0)));//MesaId 
		        	mesa.setEmpresaId(Integer.parseInt(c.getString(1)));//EmpresaId        
		        	mesa.setNumero(c.getString(2));//Numero      
		        	mesa.setSetor(c.getString(3));//Setor   
		        	c.moveToNext();
		        	lstMesa.add(mesa);
		        	 	
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} finally{   			
				//db.close();
			}   
		   
		   return lstMesa;
	  }
	
	
	public ModelMesa selecionarMesaNumero(int EmpresaId, String Numero) {
		  ModelMesa mesa = new ModelMesa();
		  try{
			  
		   		  
			  String sql = " select * from Mesas " +
			   		     " where EmpresaId = " +  EmpresaId +  
			   		     "   and Numero = '" + Numero + "'";
			  
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	
		        	mesa.setMesaId(Integer.parseInt(c.getString(0)));//MesaId 
		        	mesa.setEmpresaId(Integer.parseInt(c.getString(1)));//EmpresaId        
		        	mesa.setNumero(c.getString(2));//Numero      
		        	mesa.setSetor(c.getString(3));//Setor   
		        	c.moveToNext();
		        	 	
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return mesa;
	  }

}
