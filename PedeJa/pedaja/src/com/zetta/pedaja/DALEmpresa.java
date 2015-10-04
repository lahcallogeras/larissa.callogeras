package com.zetta.pedaja;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteStatement;

@SuppressLint("SimpleDateFormat")
public class DALEmpresa  extends DAL {

	public DALEmpresa(Context context) {
		super(context);
	}
	
	@SuppressLint("SimpleDateFormat")
	public void atualizaEmpresa(ModelEmpresa empresa) {
		
    	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
	    try{	  
	    		    	
	    	String sql = " select count(*) from Empresa " +
			   		     " where EmpresaId= " + empresa.getEmpresaId();
	    	
	    	int total = (int) DatabaseUtils.longForQuery(db, sql, null);
	    	
	    	String ultimaAtu = "";
	    	if(empresa.getUltimaAtu() != null)
	    		ultimaAtu =	formato.format(empresa.getUltimaAtu());
	    	

	    	String ultimaAtuCardapio = "null";
	    	if(empresa.getUltimaAtuCardapio() != null)
	    		ultimaAtuCardapio = "'" +	formato.format(empresa.getUltimaAtuCardapio()) + "'";
	    	
	    	
	    	
	    	
	    	if (total <= 0) {	    	
	    		
	    	
			      sql = " INSERT INTO Empresa " +
			    		  	   " (EmpresaId, " +
			    		  	   "  RazaoSocial,  " +			    		  	 		  	   
			    		  	   "  NomeFantasia, " +    		  	 		  	   
			    		  	   "  CidadeId, " +	 		  	   
			    		  	   "  Logradouro, " +	  	   
			    		  	   "  Numero, " +
			    		  	   "  Complemento, " +
			    		  	   "  Bairro, " +		
			    		  	   "  Cep, " +		 
			    		  	   "  Latitude, " +		 
			    		  	   "  Longitude, " +		 
			    		  	   "  EmpresaMatrizId, " +		 
			    		  	   "  DataCriacao, " +		
			    		  	   "  TipoImagem, " +
			    		  	   "  AtualizacaoCardapio,  " + 	
			    		  	   "  EmpresaIdLocal,  " + 
			    		  	   "  ServidorLocal,  " + 
			    		  	   "  MixProduto)  " + 	  				    		  	   
			    		  	   " values ( " +
			    		  	   empresa.getEmpresaId() + ", '" +
			    		  	   empresa.getRazaoSocial() + "', '" +
			    		  	   empresa.getNomeFantasia() + "', " +
			    		  	   empresa.getCidadeId() + ", '" +			    		  	   
			    		  	   empresa.getLogradouro() + "', '" +		    		  	   
			    		  	   empresa.getNumero() + "', '" +		    		  	   
			    		  	   empresa.getComplemento() + "', '" +		    		  	   
			    		  	   empresa.getBairro() + "', '" +		    		  	   
			    		  	   empresa.getCep() + "', '" +	    		  	   
			    		  	   empresa.getLatitude() + "', '" +	    		  	   
			    		  	   empresa.getLongitude() + "', " +
			    		  	   empresa.getEmpresaMatrizId() + ", '" +			    		  	   
			    		  	   formato.format(empresa.getDataCriacao()) + "', '" +  	   
			    		  	   empresa.getTipoImagem() + "', '" +
			    		  	   formato.format(empresa.getAtualizacaoCardapio()) + "', " + 
			    		  	   empresa.getEmpresaIdLocal() + ", '" +
			    		  	   empresa.getServidorLocal() + "', '" +
			    		  	   empresa.getMixProduto() + "') " ;
			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
	    	} else {
	    		
	    		 sql = " UPDATE  Empresa  SET " +    		  	   
		    		  	 "  RazaoSocial = '" + empresa.getRazaoSocial() + "', " +
		    		  	 "  NomeFantasia = '" + empresa.getNomeFantasia() + "', " +    		  	
		    		  	 "  CidadeId = " + empresa.getCidadeId() + ", " +	  	
		    		  	 "  Logradouro = '" + empresa.getLogradouro() + "', " +	  		
		    		  	 "  Numero = '" + empresa.getNumero() + "', " +	  		
		    		  	 "  Complemento = '" + empresa.getComplemento() + "', " +	  		 	
		    		  	 "  Bairro = '" + empresa.getBairro() + "', " +	  		 	
		    		  	 "  Cep = '" + empresa.getCep() + "', " +	  		 	
		    		  	 "  Latitude = '" + empresa.getLatitude() + "', " +	  	
		    		  	 "  Longitude = '" + empresa.getLongitude() + "', " +	 	
		    		  	 "  EmpresaMatrizId = " + empresa.getEmpresaMatrizId() + ", " +	
		    		  	 "  DataCriacao = '" + formato.format(empresa.getDataCriacao()) + "', " +		
		    		  	 "  UltimaAtu = '" + ultimaAtu  + "', " +
		    		  	 "  UltimaAtuCardapio = " + ultimaAtuCardapio  + ", " +
		    		  	 "  TipoImagem = '" + empresa.getTipoImagem()  + "', " +
		    		  	 "  AtualizacaoCardapio = '" + formato.format(empresa.getAtualizacaoCardapio()) + "', " +
		    		  	 "  EmpresaIdLocal = " + empresa.getEmpresaIdLocal() + ", " +	 	
		    		  	 "  ServidorLocal = '" + empresa.getServidorLocal() + "', " + 	
		    		  	 "  MixProduto = '" + empresa.getMixProduto() + "' " + 			    		  	 
		    		  	 " WHERE EmpresaId = " + empresa.getEmpresaId();	
  		  	   
	    		 
			      SQLiteStatement alteraStatemente = null;	     
			      alteraStatemente = this.db.compileStatement(sql);
			      alteraStatemente.executeInsert();
	    	}
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	}
	
	 public void apagaEmpresa() {		   
		  try{		  
			    String  sql = " DELETE FROM Empresa ";	
			      
			    SQLiteStatement deleteStmt = null;	     
			    deleteStmt = this.db.compileStatement(sql);
			    deleteStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
	    }
	
	 
	 public void apagaTelefoneEmpresa() {		   
		  try{		  
			    String  sql = " DELETE FROM TelefoneEmpresa ";	
			      
			    SQLiteStatement deleteStmt = null;	     
			    deleteStmt = this.db.compileStatement(sql);
			    deleteStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
	    }
	
	
	public void atualizaTelefoneEmpresa(ModelTelefoneEmpresa telefoneEmpresa) {
		 
	    try{	  
	    		    	
	    	String sql = " select count(*) from TelefoneEmpresa " +
			   		     " where TelefoneEmpresaId= " + telefoneEmpresa.getTelefoneEmpresaId();
	    	
	    	int total = (int) DatabaseUtils.longForQuery(db, sql, null);
	    	
	    	if (total <= 0) {	    	
	    		
	    	
			      sql = " INSERT INTO TelefoneEmpresa " +
			    		  	   " (TelefoneEmpresaId, " +
			    		  	   "  EmpresaId,  " +			    		  	 		  	   
			    		  	   "  DDD, " +    		  	 		  	   
			    		  	   "  Telefone, " +	 		  	 
			    		  	   "  Descricao)  " + 		  	
			    		  	   " values ( " +
			    		  	   telefoneEmpresa.getTelefoneEmpresaId() + ", " +
			    		  	   telefoneEmpresa.getEmpresaId() + ", '" +
			    		  	   telefoneEmpresa.getDDD() + "', '" +
			    		  	   telefoneEmpresa.getTelefone() + "', '" +			    		  	   
			    		  	   telefoneEmpresa.getDescricao() + "') " ;
			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
	    	} else {
	    		
	    		 sql = " UPDATE  TelefoneEmpresa  SET " +    		  	   
		    		   "  EmpresaId = " + telefoneEmpresa.getEmpresaId() + ", " +
		    		   "  DDD = '" + telefoneEmpresa.getDDD() + "', " +    		  	
		    		   "  Telefone = " + telefoneEmpresa.getTelefone() + ", " +	  	
		    		   "  Descricao = '" + telefoneEmpresa.getDescricao() + "' " +
		    		   " WHERE EmpresaId = " + telefoneEmpresa.getEmpresaId();	
  		  	   
	    		 
			      SQLiteStatement alteraStatemente = null;	     
			      alteraStatemente = this.db.compileStatement(sql);
			      alteraStatemente.executeInsert();
	    	}
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	}
	
	
	public ArrayList<ModelEmpresa> selecinarEmpresaCid(int CidadeId, String Nome) {
		 
		  ArrayList<ModelEmpresa> lstEmpresa = new ArrayList<ModelEmpresa>();
       	  DateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		  try{
			  
		   		  
			    String  sql = " select   EmpresaId, " +
				  		"	  RazaoSocial,	" +
				  		"	  NomeFantasia, " +
				  		"	  CidadeId, " +
				  		"	  Logradouro, " +
				  		"	  Numero, " +
				  		"	  Complemento, " +
				  		"	  Bairro, " +
				  		"	  Cep, " +
				  		"	  Latitude, " +
				  		"	  Longitude, " +
				  		"	  EmpresaMatrizId, " +
				  		"	  DataCriacao, " +
				  		"	  UltimaAtu, " +		
				  		"	  UltimaAtuCardapio, " +	
				  		"	  TipoImagem, " +		
				  		"     AtualizacaoCardapio,  " +
				  		"     EmpresaIdLocal,  " +
				  		"     ServidorLocal,  " +
				  		"     MixProduto   " +
				  		"  from Empresa where CidadeId = " + CidadeId + 
				  		"   and NomeFantasia like '%" + Nome + "%'";			      
			    
			    
			    		  	 
   		  	 
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelEmpresa empresa = new ModelEmpresa();
		        	
		        	empresa.setEmpresaId(Integer.parseInt(c.getString(0)));//EmpresaId
		        	empresa.setRazaoSocial(c.getString(1));//RazaoSocial
		        	empresa.setNomeFantasia(c.getString(2));//NomeFantasia
		        	empresa.setCidadeId(CidadeId);//CidadeId
		        	empresa.setLogradouro(c.getString(4));//Logradouro
		        	empresa.setNumero(c.getString(5));//Numero
		        	empresa.setComplemento(c.getString(6));//Complemento
		        	empresa.setBairro(c.getString(7));//Bairro
		        	empresa.setCep(c.getString(8));//Cep
		        	empresa.setLatitude(c.getString(9));//Latitude
		        	empresa.setLongitude(c.getString(10));//Longitude
		        	empresa.setEmpresaMatrizId(Integer.parseInt(c.getString(11)));//EmpresaMatrizId
		        	empresa.setDataCriacao(formato.parse(c.getString(12)));//DataCriacao	
		        			        	
		        	if(c.getString(13) != null && !c.getString(13).equals("")) //UltimaAtu
		        		empresa.setUltimaAtu(formato.parse(c.getString(13)));//UltimaAtu

		        	if(c.getString(14) != null && !c.getString(14).equals("")) //UltimaAtuCardapio
		        		empresa.setUltimaAtuCardapio(formato.parse(c.getString(14)));//UltimaAtuCardapio
		        	

		        	empresa.setTipoImagem(c.getString(15));//TipoImagem
		        	
		        	if(c.getString(16) != null &&  !c.getString(16).equals("")) //AtualizacaoCardapio
		        		empresa.setAtualizacaoCardapio(formato.parse(c.getString(16)));//AtualizacaoCardapio
		        	
		        	empresa.setEmpresaIdLocal(Integer.parseInt(c.getString(17)));//EmpresaIdLocal
		        	empresa.setServidorLocal(c.getString(18));//ServidorLocal
		        	empresa.setMixProduto(c.getString(19));//MixProduto
		        	
		        	c.moveToNext();
		        	lstEmpresa.add(empresa);
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
		   
		   return lstEmpresa;
	  }
	
	
	public ModelEmpresa selecinarEmpresaId(int EmpresaId) {
		  ModelEmpresa empresa = new ModelEmpresa();
		
     	  DateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		  try{
			  
		   		  
			    String  sql = " select   EmpresaId, " +
				  		"	  RazaoSocial,	" +
				  		"	  NomeFantasia, " +
				  		"	  CidadeId, " +
				  		"	  Logradouro, " +
				  		"	  Numero, " +
				  		"	  Complemento, " +
				  		"	  Bairro, " +
				  		"	  Cep, " +
				  		"	  Latitude, " +
				  		"	  Longitude, " +
				  		"	  EmpresaMatrizId, " +
				  		"	  DataCriacao, " +
				  		"	  UltimaAtu, " +		
				  		"	  UltimaAtuCardapio, " +	
				  		"	  TipoImagem, " +		
				  		"     AtualizacaoCardapio,  " +
				  		"     EmpresaIdLocal,  " +
				  		"     ServidorLocal,  " +
				  		"     MixProduto   " +
				  		"  from Empresa where EmpresaId = " + EmpresaId;			      
			    
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	
		        	
		        	empresa.setEmpresaId(Integer.parseInt(c.getString(0)));//EmpresaId
		        	empresa.setRazaoSocial(c.getString(1));//RazaoSocial
		        	empresa.setNomeFantasia(c.getString(2));//NomeFantasia
		        	empresa.setCidadeId(Integer.parseInt(c.getString(3)));//CidadeId
		        	empresa.setLogradouro(c.getString(4));//Logradouro
		        	empresa.setNumero(c.getString(5));//Numero
		        	empresa.setComplemento(c.getString(6));//Complemento
		        	empresa.setBairro(c.getString(7));//Bairro
		        	empresa.setCep(c.getString(8));//Cep
		        	empresa.setLatitude(c.getString(9));//Latitude
		        	empresa.setLongitude(c.getString(10));//Longitude
		        	empresa.setEmpresaMatrizId(Integer.parseInt(c.getString(11)));//EmpresaMatrizId
		        	empresa.setDataCriacao(formato.parse(c.getString(12)));//DataCriacao	
		        			        	
		        	if(c.getString(14) != null && !c.getString(13).equals("")) //UltimaAtu
		        		empresa.setUltimaAtu(formato.parse(c.getString(13)));//UltimaAtu

		        	if(c.getString(14) != null && !c.getString(14).equals("")) //UltimaAtuCardapio
		        		empresa.setUltimaAtuCardapio(formato.parse(c.getString(14)));//UltimaAtuCardapio
		        	

		        	empresa.setTipoImagem(c.getString(15));//TipoImagem
		        	
		        	if(c.getString(16) != null && !c.getString(16).equals("")) //AtualizacaoCardapio
		        		empresa.setAtualizacaoCardapio(formato.parse(c.getString(16)));//AtualizacaoCardapio
		        	
		        	empresa.setEmpresaIdLocal(Integer.parseInt(c.getString(17)));//EmpresaIdLocal
		        	empresa.setServidorLocal(c.getString(18));//ServidorLocal
		        	empresa.setMixProduto(c.getString(19));//MixProduto
		        	
		        	c.moveToNext();
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
		   
		   return empresa;
	  }
	
	
	public ArrayList<ModelTelefoneEmpresa> selecionarTelefone(int empresaId) {
		 
		   ArrayList<ModelTelefoneEmpresa> lstTelefone = new ArrayList<ModelTelefoneEmpresa>();
		  try{
			  
		   		  
			    String  sql = " select * from TelefoneEmpresa  where EmpresaId = " + empresaId;			      
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelTelefoneEmpresa telefone = new ModelTelefoneEmpresa();
		        	telefone.setTelefoneEmpresaId(Integer.parseInt(c.getString(0)));	
		        	telefone.setEmpresaId(Integer.parseInt(c.getString(1)));	
		        	telefone.setDDD(c.getString(2));
		        	telefone.setTelefone(c.getString(3));   
		        	telefone.setDescricao(c.getString(4));
		        	c.moveToNext();
		        	lstTelefone.add(telefone);
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
		   
		   return lstTelefone;
	  }
	
	 public void apagaModoPagamento() {		   
		  try{		  
			    String  sql = " DELETE FROM ModoPagamento ";	
			      
			    SQLiteStatement deleteStmt = null;	     
			    deleteStmt = this.db.compileStatement(sql);
			    deleteStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
	    }
	   
	 public void apagaModoPagamentoEmpresa() {		   
		  try{		  
			    String  sql = " DELETE FROM ModoPagamentoEmpresa ";	
			      
			    SQLiteStatement deleteStmt = null;	     
			    deleteStmt = this.db.compileStatement(sql);
			    deleteStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
	    }
	 
	 public void insereModoPagamento(ModelModoPagamento modoPgto) {
		   
		    try{	  
			    String  sql = " INSERT INTO ModoPagamento " +
			    		  	   " (ModoPagamentoId, " +
			    		  	   "  Descricao)  " + 		  	
			    		  	   " values ( " +
			    		  	 modoPgto.getModoPagamentoId() + ", '" +
			    		  	 modoPgto.getDescricao() + "') " ;
			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		}
	 
	 public void insereModoPagamentoEmpresa(ModelModoPagEmpresa modoPgtoEmp) {
		   
		    try{	  
			    String  sql = " INSERT INTO ModoPagamentoEmpresa " +
			    		  	   " (ModoPagamentoEmpresaId, " +
			    		  	   "  ModoPagamentoId,   " +
			    		  	   "  EmpresaId)  " + 		  	
			    		  	   " values ( " +
			    		  	 modoPgtoEmp.getModoPagamentoEmpresaId() + ", " +
			    		  	 modoPgtoEmp.getModoPagamentoId() + ", " +
			    		  	 modoPgtoEmp.getEmpresaId() + ") " ;
			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		}
	 
	 public ArrayList<ModelModoPagamento> selecionaModoPagamento(int empresaId) {
		 
		   ArrayList<ModelModoPagamento> lstModoPagamento = new ArrayList<ModelModoPagamento>();
		  try{
			  
		   		  
			    String  sql = " select m.* " +
			    		"    from ModoPagamento m, ModoPagamentoEmpresa me " +
			    		"  where me.ModoPagamentoId = m.ModoPagamentoId  " +
			    		"    and me.EmpresaId = " + empresaId;
			    
			    
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelModoPagamento modo = new ModelModoPagamento();
		        	modo.setModoPagamentoId(Integer.parseInt(c.getString(0)));
		        	modo.setDescricao(c.getString(1));
		        	c.moveToNext();
		        	lstModoPagamento.add(modo);
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
		   
		   return lstModoPagamento;
	  }
	   
	   

}
