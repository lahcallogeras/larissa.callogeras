package com.zetta.pedaja;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteStatement;

@SuppressLint("SimpleDateFormat")
public class DALUsuario extends DAL {

	public DALUsuario(Context context) {
		super(context);
	}
	
	 public ModelUsuario selecionaUltimoUsuario() {
		 
		 ModelUsuario usuario = null; 
		  try{
		   		  
			    String  sql = " select * from Usuario a " +
			    			  "  where strftime('%Y-%m-%d %H:%M:%S', a.UltimaAcesso) = " +
			    			  "				   (select max(strftime('%Y-%m-%d %H:%M:%S', b.UltimaAcesso)) " +
			    			  "						  from Usuario b )	";
			      
			    Cursor c = db.rawQuery(sql, null);		    
		        if (c.moveToFirst()) {
		        	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		        	usuario = new ModelUsuario();
		        	usuario.setUsuarioId(Integer.parseInt(c.getString(0)));//UsuarioId
		        	usuario.setNome(c.getString(1));//Nome
		        	usuario.setEmail(c.getString(2));//Email
		        	usuario.setDataNascimento(formato.parse(c.getString(3)));//DataNascimento	
		        	usuario.setCPF(c.getString(4));//CPF	
		        	usuario.setDataCriacao(formato.parse(c.getString(5)));//DataCriacao	
		        	
		        	if(!c.getString(6).equals(""))
		        		usuario.setUltimaAtu(formato.parse(c.getString(6)));//UltimaAtu
		        	
		        	usuario.setUltimoAcesso(formato.parse(c.getString(7)));//UltimaAcesso	
		        	usuario.setCidadeId(Integer.parseInt(c.getString(8)));//CidadeId
		        	usuario.setUsuario(c.getString(9));//Usuario	  
		        	usuario.setSenha(c.getString(10));//Senha	
		        	c.moveToNext();
		            
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}   
		   return usuario;
	  }
	
	@SuppressLint("SimpleDateFormat")
	public boolean atualizaUsuario(ModelUsuario usuario) {
		boolean Novo = false;
    	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
	    try{	  
	    		    	
	    	String sql = " select count(*) from Usuario " +
			   		     " where UsuarioId= " + usuario.getUsuarioId();
	    	
	    	int total = (int) DatabaseUtils.longForQuery(db, sql, null);
	    	
	    	String ultimaAtu = "";
	    	if(usuario.getUltimaAtu() != null)
	    		ultimaAtu =	formato.format(usuario.getUltimaAtu());
	    	
	    	if (total <= 0) {	    	
	    	
			      sql = " INSERT INTO Usuario " +
			    		  	   " (UsuarioId, " +
			    		  	   "  Nome,  " +			    		  	 		  	   
			    		  	   "  Email, " +    		  	 		  	   
			    		  	   "  DataNascimento, " +	 		  	   
			    		  	   "  CPF, " +	  	   
			    		  	   "  DataCriacao, " +
			    		  	   "  UltimaAtu, " +
			    		  	   "  UltimaAcesso, " +	  	 
			    		  	   "  CidadeId, " +	    		  	 
			    		  	   "  Usuario, " +   
			    		  	   "  Senha)  " + 		  	
			    		  	   " values ( " +
			    		  	   usuario.getUsuarioId() + ", '" +
			    		  	   usuario.getNome() + "', '" +
			    		  	   usuario.getEmail() + "', '" +
			    		  	   formato.format(usuario.getDataNascimento()) + "', '" +
			    		  	   usuario.getCPF() + "', '" +
			    		  	   formato.format(usuario.getDataCriacao()) + "', '" +
			    		  	   ultimaAtu + "', '" +
			    		  	   formato.format(usuario.getUltimoAcesso()) + "', " +
			    		  	   usuario.getCidadeId() + ", '" +
			    		  	   usuario.getUsuario() + "', '" +
			    		  	   usuario.getSenha() + "') " ;
			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
			      Novo = true;
	    	} else {
	    		
	    		 sql = " UPDATE  Usuario  SET " +    		  	   
		    		  	 "  Nome = '" + usuario.getNome() + "', " +
		    		  	 "  Email = '" + usuario.getEmail() + "', " +    		  	
		    		  	 "  DataNascimento = '" + formato.format(usuario.getDataNascimento()) + "', " +	  	
		    		  	 "  CPF = '" + usuario.getCPF() + "', " +	  		
		    		  	 "  DataCriacao = '" + formato.format(usuario.getDataCriacao()) + "', " +	  		
		    		  	 "  UltimaAtu = '" + ultimaAtu + "', " +	  		
		    		  	 "  UltimaAcesso = '" + formato.format(usuario.getUltimoAcesso())  + "', " +	
		    		  	 "  CidadeId = " + usuario.getCidadeId() + ", " +	   	
		    		  	 "  Usuario = '" + usuario.getUsuario() + "', " +	  	
		    		  	 "  Senha = '" + usuario.getSenha() + "' " +    		  
		    		  	 " WHERE UsuarioId = " + usuario.getUsuarioId();		      	  	
  		  	   
  		  	   
			      SQLiteStatement alteraStatemente = null;	     
			      alteraStatemente = this.db.compileStatement(sql);
			      alteraStatemente.executeInsert();
	    	}
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	    return Novo;
	}
	
	
	public ModelUsuario efetuaLogin(String Usuario, String Senha) {
		 
		 ModelUsuario usuario = null; 
		  try{
		   		  
			    String  sql = " select * from Usuario a " +
			    			  "  where Usuario =  '" + Usuario + "' " +
			    			  "	   and Senha = '" + Senha + "'";
			      
			    Cursor c = db.rawQuery(sql, null);		    
		        if (c.moveToFirst()) {
		        	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		        	usuario = new ModelUsuario();
		        	usuario.setUsuarioId(Integer.parseInt(c.getString(0)));//UsuarioId
		        	usuario.setNome(c.getString(1));//Nome
		        	usuario.setEmail(c.getString(2));//Email
		        	usuario.setDataNascimento(formato.parse(c.getString(3)));//DataNascimento	
		        	usuario.setCPF(c.getString(4));//CPF	
		        	usuario.setDataCriacao(formato.parse(c.getString(5)));//DataCriacao	
		        	
		        	if(!c.getString(6).equals(""))
		        		usuario.setUltimaAtu(formato.parse(c.getString(6)));//UltimaAtu
		        	
		        	usuario.setUltimoAcesso(formato.parse(c.getString(7)));//UltimaAcesso	
		        	usuario.setCidadeId(Integer.parseInt(c.getString(8)));//CidadeId
		        	usuario.setUsuario(c.getString(9));//Usuario	  
		        	usuario.setSenha(c.getString(10));//Senha	
		        	c.moveToNext();
		            
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}   
		   return usuario;
	  }

}
