package com.zetta.pedaja;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

public class DALCidade extends DAL {

	public DALCidade(Context context) {
		super(context);
	}
	
	 public void apagaTodasCidades() {		   
	  try{		  
		    String  sql = " DELETE FROM Cidade ";	
		      
		    SQLiteStatement deleteStmt = null;	     
		    deleteStmt = this.db.compileStatement(sql);
		    deleteStmt.executeInsert();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		}
    }
 	
	public void insereCidade(ModelCidade cidade) {
		   
	    try{	  
		    String  sql = " INSERT INTO Cidade " +
		    		  	   " (CidadeId, " +
		    		  	   "  EstadoId,  " +			
		    		  	   "  Nome)  " + 		  	
		    		  	   " values ( " +
		    		  	 cidade.getCidadeId() + ", " +
		    		  	 cidade.getEstadoId() + ", '" +
		    		  	 cidade.getNome() + "') " ;
		      
		      SQLiteStatement insertStmt = null;	     
		      insertStmt = db.compileStatement(sql);
		      insertStmt.executeInsert();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	}
	
	public ArrayList<ModelCidade> selecionarCidadeEst(int EstadoId) {
		 
		   ArrayList<ModelCidade> lstCidade = new ArrayList<ModelCidade>();
		  try{
			  
		   		  
			    String  sql = " select * from Cidade where EstadoId = " + EstadoId;			      
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelCidade cidade = new ModelCidade();
		        	cidade.setCidadeId(Integer.parseInt(c.getString(0)));	
		        	cidade.setEstadoId(Integer.parseInt(c.getString(1)));	   
		        	cidade.setNome(c.getString(2));
		        	c.moveToNext();
		        	lstCidade.add(cidade);
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
		   
		   return lstCidade;
	  }
	   
	 
	 
	 
	 

}
