package com.zetta.pedaja;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

@SuppressLint("SimpleDateFormat")
public class DALDestaque extends DAL {
	
	
	 public DALDestaque(Context context) {
		super(context);
	}
	 
	 
	 
	 public void inserirVisualizacaoDestaque(ModelVisualizacaoDestaque visuDestaque) {
     	 	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    try{	  
				   String  sql = " INSERT INTO VisualizacaoDestaque " +
				    		  	   " (EmpresaId, " +
				    		  	   "  UsuarioId,  " +			  	 
				    		  	   "  DataVisu)  " + 		  	
				    		  	   " values ( " +
				    		  	   visuDestaque.getEmpresaId() + ", " +
				    		  	   visuDestaque.getUsuarioId() + ", '" +		    		  	   
				    		  	   formato.format(visuDestaque.getDataVisu())  + "') " ;
				      
				  SQLiteStatement insertStmt = null;	     
				  insertStmt = db.compileStatement(sql);
				  insertStmt.executeInsert();
		    	
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		}
	 
	 public int visualizoesDestaque (int EmpresaId, int UsuarioId) {     	 
     	 int quantidade = 0;
     	 DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
     	 Date UltimaVisu = null;
		  try{
		   		  
			    String  sql = " select max(strftime('%Y-%m-%d %H:%M:%S', DataVisu)) DataVisu  " +
			    			  "  from VisualizacaoDestaque a " + 
			    			  "  where EmpresaId = " + EmpresaId +
			    			  "	   and UsuarioId = " + UsuarioId;
	    	
			    Cursor c = db.rawQuery(sql, null);		    
			    c.moveToFirst();
		        if (c.isAfterLast() == false) {
		        	UltimaVisu = formato.parse(c.getString(0));		            
		        }
		        c.close();
		        

			   if(UltimaVisu != null){
				   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				   Date data = new Date(); 
				   Date dataAtual = dateFormat.parse(dateFormat.format(data.getTime()));
				   
				   if(dataAtual.getTime() == UltimaVisu.getTime())
					   quantidade = 1;
					   
				   
			   }
				  
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}   
		  
		   return quantidade;
	  }
	 
	 
	 public List<ModelDestaque> selecionaDestaques(int EmpresaId) {
     	 DateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
     	 List<ModelDestaque> lstDestaque = new ArrayList<ModelDestaque>(); 
		  try{
		   		  
			    String  sql = " select * from Destaque a " + 
			    			  "  where strftime('%Y-%m-%d','now') >=  strftime('%Y-%m-%d %H:%M:%S', InicioVigencia)   " +
			    			  "    and (FimVigencia is null or strftime('%Y-%m-%d','now') <= strftime('%Y-%m-%d %H:%M:%S', FimVigencia) ) " +
			    			  "    and EmpresaId = " + EmpresaId;
			    Cursor c = db.rawQuery(sql, null);		    
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelDestaque destaque = new ModelDestaque();
		        	
		        	
		        	destaque.setDestaqueId(Integer.parseInt(c.getString(0)));//DestaqueId
		        	destaque.setEmpresaId(Integer.parseInt(c.getString(1)));//EmpresaId
		        	destaque.setTipoImagem(c.getString(2));//TipoImagem
		        	destaque.setInicioVigencia(formato.parse(c.getString(3)));//InicioVigencia	        	
		        	
		        	if(c.getString(4) != null && !c.getString(4).equals(""))
		        		destaque.setFimVigencia(formato.parse(c.getString(4)));//FimVigencia
		        	
		        	destaque.setDescricao(c.getString(5));//Descricao
		        	
		        	c.moveToNext();
		        	lstDestaque.add(destaque);
		            
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}   
		  
		   return lstDestaque;
	  }

	public void apagaItensDestaque(int EmpresaId) {		   
		  try{		  
			    String  sql = " DELETE FROM ItensDestaque " +
			    		"	Where EmpresaId = " + EmpresaId;	

			    SQLiteStatement deleteStmt = null;	     
			    deleteStmt = this.db.compileStatement(sql);
			    deleteStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
	    }
	
	  public void apagaDestaque(int EmpresaId) {		   
		  try{		  
			    String  sql = " DELETE FROM Destaque " +
			    		"	Where EmpresaId = " + EmpresaId;	

			    SQLiteStatement deleteStmt = null;	     
			    deleteStmt = this.db.compileStatement(sql);
			    deleteStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
	    }
	  
	    public void insereDestaque(ModelDestaque destaque) {

	    	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		    try{	  
		    	
		    	String FimVigencia = "null";
		    	if(destaque.getFimVigencia() != null)
		    		FimVigencia = "'" +	formato.format(destaque.getFimVigencia()) + "'";
		    	
			    String  sql = " INSERT INTO Destaque " +
			    		  	   " (DestaqueId, " +
			    		  	   "  InicioVigencia,  " +	
			    		  	   "  FimVigencia,  " +			
			    		  	   "  TipoImagem,  " +		
			    		  	   "  EmpresaId, " +
			    		  	   "  Descricao)  " + 		  	
			    		  	   " values ( " +
			    		  	 destaque.getDestaqueId() + ", '" +
			    		  	 formato.format(destaque.getInicioVigencia()) + "', " +
			    		  	 FimVigencia  + ", '" +
			    		  	 destaque.getTipoImagem() + "', " +
			    		  	 destaque.getEmpresaId() + ", '"+
			    		  	 destaque.getDescricao() + "') " ;
			    
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		}
	    
	    public void insereItensDestaque(ModelItemDestaque itemDestaque) {
 
		    try{	  
		    	
			    String  sql = " INSERT INTO ItensDestaque " +
			    		  	   " (ItensDestaqueId, " +
			    		  	   "  DestaqueId,  " +	
			    		  	   "  TipoProdutoId,  " +		
			    		  	   "  EmpresaId)  " + 		  	
			    		  	   " values ( " +
			    		  	 itemDestaque.getItensDestaqueId() + ", " +
			    		  	 itemDestaque.getDestaqueId() + ", " +
			    		  	 itemDestaque.getTipoProdutoId() + ", " +
			    		  	 itemDestaque.getEmpresaId() + ") " ;
			    
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		}
	    
	    
	    public List<ModelItemDestaque> selecionaProdutoDestaque(int DestaqueId) {
	     	 List<ModelItemDestaque> lstItemDestaque = new ArrayList<ModelItemDestaque>(); 
			  try{
			   		  
				    String  sql = " select a.*, p.Valor " +
				    			  "    from ItensDestaque a, TipoProduto p " +
				    			  "  where DestaqueId  = " + DestaqueId +
				    			  "    and a.TipoProdutoId = p.TipoProdutoId ";
				    Cursor c = db.rawQuery(sql, null);		    
				    c.moveToFirst();
			        while (c.isAfterLast() == false) {
			        	ModelItemDestaque itemDestaque = new ModelItemDestaque();
			        	itemDestaque.setItensDestaqueId(Integer.parseInt(c.getString(0)));//ItensDestaqueId
			        	itemDestaque.setDestaqueId(Integer.parseInt(c.getString(1)));//DestaqueId
			        	itemDestaque.setEmpresaId(Integer.parseInt(c.getString(2)));//EmpresaId
			        	itemDestaque.setTipoProdutoId(Integer.parseInt(c.getString(3)));//TipoProdutoId
			        	itemDestaque.setValorProduto(Double.parseDouble(c.getString(4)));//Valor
			        	c.moveToNext();
			        	lstItemDestaque.add(itemDestaque);
			            
			        }
			        c.close();
			      
				} catch(Exception ex){   			
					ex.printStackTrace();
				}   
			  
			   return lstItemDestaque;
		  }

}
