package com.zetta.pedaja;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

public class DALEstado extends DAL {

	public DALEstado(Context context) {
		super(context);
	}
	
	 public void apagaTodosEstados() {		   
	  try{		  
		    String  sql = " DELETE FROM Estado ";	
		      
		    SQLiteStatement deleteStmt = null;	     
		    deleteStmt = this.db.compileStatement(sql);
		    deleteStmt.executeInsert();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		}
   }
	
	public void insereEstado(ModelEstado estado) {
		   
	    try{	  
		    String  sql = " INSERT INTO Estado " +
		    		  	   " (EstadoId, " +
		    		  	   "  Uf,  " +			
		    		  	   "  Nome)  " + 		  	
		    		  	   " values ( " +
		    		  	estado.getEstadoId() + ", '" +
		    		  	estado.getUf() + "', '" +
		    		  	estado.getNome() + "') " ;
		      
		      SQLiteStatement insertStmt = null;	     
		      insertStmt = db.compileStatement(sql);
		      insertStmt.executeInsert();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	}
	
	public ArrayList<ModelEstado> selecionaEstado() {
		 
		   ArrayList<ModelEstado> lstEstado = new ArrayList<ModelEstado>();
		  try{
			  
		   		  
			    String  sql = " select * from Estado ";			      
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelEstado estado = new ModelEstado();
		        	estado.setEstadoId(Integer.parseInt(c.getString(0)));		        
		        	estado.setUf(c.getString(1));	        
		        	estado.setNome(c.getString(2));
		        	c.moveToNext();
		        	lstEstado.add(estado);
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} finally{   			
				//db.close();
			}   
		   
		   return lstEstado;
	  }
	   
	 
	

}
