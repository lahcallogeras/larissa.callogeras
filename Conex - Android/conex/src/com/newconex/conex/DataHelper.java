package com.newconex.conex;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.database.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DataHelper {
	
	private static final String DATABASE_NAME = "conex.db";
	private static final int DATABASE_VERSION = 9;
    private Context context;
	private SQLiteDatabase db;
	
   public DataHelper(Context context) {
      this.context = context;
      OpenHelper openHelper = new OpenHelper(this.context);
      db = openHelper.getWritableDatabase();
   }

   
   public void  atualizaUsuario(Usuario usuario) {
	   
	  try{
		   String sql = " select count(*) from cadUsuario " +
				   		" where usuarioUsu= '"+ usuario.getUsuarioUsu() + "'";
		   int total = (int) DatabaseUtils.longForQuery(db, sql, null);
		   
		   DateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		   if(total <= 0 ){	   
		      sql = " INSERT INTO cadUsuario " +
		    		  	   " (codigoUsu, " +
		    		  	   "  usuarioUsu,  " +
		    		  	   "  senhaUsu,  " +
		    		  	   "  nomeUsu,  " +
		    		  	   "  dataNascUsu,  " +
		    		  	   "  codigoPaisUsu,  " +
		    		  	   "  emailUsu,  " +
		    		  	   "  sexoUsu,  " +
		    		  	   "  manterConUsu,  " +
		    		  	   "  nomeFotoUsu,  " +
		    		  	   "  dataLoginUsu,  " +	    		  	   
		    		  	   "  dataCadUsu)  " + 		  	
		    		  	   " values ( " +
		    		  	   usuario.getCodigoUsu() + ", " +
		    		  	   "'" + usuario.getUsuarioUsu() + "', " +
		    		  	   "'" + usuario.getSenhaUsu() + "', " +
		    		  	   "'" + usuario.getNomeUsu() + "', " +
		    		  	   "'" + formato.format(usuario.getDataNascUsu()) + "', " +
		    		  	   usuario.getCodigoPaisUsu() + ", " +
		    		  	   "'" + usuario.getEmailUsu() + "', " +
		    		  	   "'" + usuario.getManterConUsu() + "', " +
		    		  	   "'" + usuario.getNomeFotoUsu() + "', " +
		    		  	   "'" + formato.format(usuario.getDataLoginUsu()) + "', " +
		    		  	   "'" + usuario.getSexoUsu() + "', " +	    		  	   
		    		  	   "'" + formato.format(usuario.getDataCadUsu()) + "') " ;
		      
		      SQLiteStatement insertStmt = null;	     
		      insertStmt = this.db.compileStatement(sql);
		      insertStmt.executeInsert();
		   }
		   else {
			   
			   sql = " UPDATE  cadUsuario  SET " +    		  	   
	    		  	 "  senhaUsu = '" + usuario.getSenhaUsu() + "', " +
	    		  	 "  nomeUsu = '" + usuario.getNomeUsu() + "', " +    		  	
	    		  	 "  emailUsu = '" + usuario.getEmailUsu() + "', " +	  	
	    		  	 "  manterConUsu = '" + usuario.getManterConUsu() + "', " +	  	
	    		  	 "  nomeFotoUsu = '" + usuario.getNomeFotoUsu() + "', " +	  	
	    		  	 "  dataLoginUsu = '" + formato.format(usuario.getDataLoginUsu()) + "' " +    		  
	    		  	 " WHERE codigoUsu = " + usuario.getCodigoUsu();
	      
		      SQLiteStatement alteraStatemente = null;	     
		      alteraStatemente = this.db.compileStatement(sql);
		      alteraStatemente.executeInsert();
		   }
		   
   		} catch(Exception ex){   			
   			ex.printStackTrace();
   		} finally{   			
   			//db.close();
   		}
  }
   
   public void deletaContatoUsu(Usuario usuario) {
	   
	  try{		  
		    String  sql = " DELETE FROM cadContatos " +
		    			  " WHERE codigoUsuLocalCtt = " + usuario.getCodigoUsu();	
		      
		    SQLiteStatement deleteStmt = null;	     
		    deleteStmt = this.db.compileStatement(sql);
		    deleteStmt.executeInsert();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} finally{   			
			//db.close();
		}   
	   
  }
   
   public void insertContato(Contato contato) {
	   
	  try{

		   
		   DateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	   		  
		    String  sql = " INSERT INTO cadContatos " +
		    		  	   " (codigoCtt, " +
		    		  	   "  codigoUsuLocalCtt,  " +
		    		  	   "  statusCtt,  " +
		    		  	   "  codigoUsuCtt,  " +
		    		  	   "  usuarioUsuCtt,  " +
		    		  	   "  nomeUsuCtt,  " +
		    		  	   "  dataNascUsuCtt,  " +
		    		  	   "  emailUsuCtt,  " +
		    		  	   "  sexoUsuCtt,  " +    		  	   
		    		  	   "  dataCadUsuCtt, " +
		    		  	   "  nomeFotoCtt)  " + 		  	
		    		  	   " values ( " +
		    		  	 contato.getCodigoCtt() + ", " +
		    		  	   "'" + contato.getCodigoUsuLocalCtt() + "', " +
		    		  	   "'" + contato.getStatusCtt() + "', " +
		    		  	   "'" + contato.getCodigoUsuCtt() + "', " +
		    		  	   "'" + contato.getUsuarioUsuCtt() + "', " +
		    		  	   "'" + contato.getNomeUsuCtt() + "', " +
		    		  	   "'" + formato.format(contato.getDataNascUsuCtt()) + "', " +
		    		  	   "'" + contato.getEmailUsuCtt() + "', " +
		    		  	   "'" + contato.getSexoUsuCtt() + "', " +	    		  	   
		    		  	   "'" + formato.format(contato.getDataCadUsuCtt()) + "', " +
		    		  	   "'" + contato.getNomeFotoCtt() + "') " ;
		    
		    

		      
		      SQLiteStatement insertStmt = null;	     
		      insertStmt = this.db.compileStatement(sql);
		      insertStmt.executeInsert();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} finally{   			
			//db.close();
		}   
	   
  }
   
   public ArrayList<Contato> selectContatos(Usuario usuario) {
	 
	   ArrayList<Contato> lstCont = new ArrayList<Contato>();
	  try{
		  
	   		  
		    String  sql = " select *,  " +
		    			  "	 (select count(*) from cadMensagens  " +
		    			  "    where (codigoUsuOriMsg = codigoUsuLocalCtt or  codigoUsuOriMsg = codigoUsuCtt) " +
		    			  "      and (codigoUsuDestMsg = codigoUsuLocalCtt or  codigoUsuDestMsg = codigoUsuCtt) " +
		    			  "		 and statusLida = 'N' ) " +
		    			  "	from cadContatos " +
		    			  "  where codigoUsuLocalCtt = " + usuario.getCodigoUsu()	+
		    			  "  order by nomeUsuCtt";
		      
		    Cursor c = db.rawQuery(sql, null);
		    c.moveToFirst();
	        while (c.isAfterLast() == false) {
	        	Contato contato = new Contato();
	        	contato.setCodigoCtt(Integer.parseInt(c.getString(0)));
	        	contato.setCodigoUsuLocalCtt(Integer.parseInt(c.getString(1)));
	        	contato.setStatusCtt(c.getString(2));
	        	contato.setCodigoUsuCtt(Integer.parseInt(c.getString(3)));
	        	contato.setUsuarioUsuCtt(c.getString(4));
	        	contato.setNomeUsuCtt(c.getString(5));
	        	DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                contato.setDataNascUsuCtt(format.parse(c.getString(6)));
	        	contato.setEmailUsuCtt(c.getString(7));
	        	contato.setSexoUsuCtt(c.getString(8));                                  
                contato.setDataCadUsuCtt(format.parse(c.getString(9)));
	        	contato.setNomeFotoCtt(c.getString(10));        
	        	contato.setQtdMsg(Integer.parseInt(c.getString(11)));//quantidade novas mensagens   
	        	lstCont.add(contato);    
	        	c.moveToNext();
	            
	        }
	        c.close();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} finally{   			
			//db.close();
		}   
	   
	   return lstCont;
  }
   
   public Usuario selectUltimoUsu() {
		 
	  Usuario usuario = null; 
	  try{
	   		  
		    String  sql = " select * from cadUsuario a " +
		    			  "  where strftime('%Y-%m-%d %H:%M:%S', a.dataLoginUsu) = " +
		    			  "				   (select max(strftime('%Y-%m-%d %H:%M:%S', b.dataLoginUsu)) " +
		    			  "						  from cadUsuario b )	";
		      
		    Cursor c = db.rawQuery(sql, null);		    
	        if (c.moveToFirst()) {
	        	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	        	usuario = new Usuario();
	        	usuario.setCodigoUsu(Integer.parseInt(c.getString(0)));//codigoUsu
	        	usuario.setUsuarioUsu(c.getString(1));//usuarioUsu
	        	usuario.setSenhaUsu(c.getString(2));//senhaUsu
	        	usuario.setNomeUsu(c.getString(3));//nomeUsu	            
	        	usuario.setDataNascUsu(formato.parse(c.getString(4)));//dataNascUsu	        	
	        	usuario.setCodigoPaisUsu(Integer.parseInt(c.getString(5)));//codigoPaisUsu
	        	usuario.setEmailUsu(c.getString(6));//emailUsu	     
	        	usuario.setSexoUsu(c.getString(7));//sexoUsu	 
	        	usuario.setManterConUsu(c.getString(8));//manterConUsu	
	        	usuario.setDataCadUsu(formato.parse(c.getString(9)));//dataCadUsu	     
	        	usuario.setDataLoginUsu(formato.parse(c.getString(10)));//dataLoginUsu	  
	        	usuario.setNomeFotoUsu(c.getString(11));//nomeFotoUsu	              	  
	        	c.moveToNext();
	            
	        }
	        c.close();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		}   
	     finally{   			
			//db.close();
		}   
	   
	   return usuario;
  }
   

   public Usuario autenticaUsu(Usuario usuario) {
		 
	  try{
	   		  
		    String  sql = " select * from cadUsuario  " +
		    			  "  where usuarioUsu =  '" + usuario.getUsuarioUsu() + "' " +
		    			  "	   and senhaUsu = '" + usuario.getSenhaUsu() + "' ";
		      
		    Cursor c = db.rawQuery(sql, null); 
		    
		    if (c.moveToFirst()) {

	        	DateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	        	usuario = new Usuario();
	        	usuario.setCodigoUsu(Integer.parseInt(c.getString(0)));//codigoUsu
	        	usuario.setUsuarioUsu(c.getString(1));//usuarioUsu
	        	usuario.setSenhaUsu(c.getString(2));//senhaUsu
	        	usuario.setNomeUsu(c.getString(3));//nomeUsu	            
	        	usuario.setDataNascUsu(formato.parse(c.getString(4)));//dataNascUsu	        	
	        	usuario.setCodigoPaisUsu(Integer.parseInt(c.getString(5)));//codigoPaisUsu
	        	usuario.setEmailUsu(c.getString(6));//emailUsu	     
	        	usuario.setSexoUsu(c.getString(7));//sexoUsu	 
	        	usuario.setManterConUsu(c.getString(8));//manterConUsu	s
	        	usuario.setDataCadUsu(formato.parse(c.getString(9)));//dataCadUsu	     
	        	usuario.setDataLoginUsu(formato.parse(c.getString(10)));//dataLoginUsu
	        	usuario.setNomeFotoUsu(c.getString(11));//nomeFotoUsu	           
	        }
	        else 
	        	usuario = null;
	        
	        
	        c.close();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} finally{   			
			//db.close();
		}   
	   
	   return usuario;
  }
  
   
   public Contato selectContato(int codUsu) {
		 
	  Contato contato = new Contato();
	  try{
	   		  
		    String  sql = " select *,  " +
		    			  "	 (select count(*) from cadMensagens  " +
		    			  "    where (codigoUsuOriMsg = codigoUsuLocalCtt or  codigoUsuOriMsg = codigoUsuCtt) " +
		    			  "      and (codigoUsuDestMsg = codigoUsuLocalCtt or  codigoUsuDestMsg = codigoUsuCtt) " +
		    			  "		 and statusLida = 'N' ) " +
		    			  "  from cadContatos " +
		    			  "  where codigoUsuCtt = " + codUsu;
		      
		    Cursor c = db.rawQuery(sql, null);
		    if (c.moveToFirst()) {	        	
	        	contato.setCodigoCtt(Integer.parseInt(c.getString(0)));
	        	contato.setCodigoUsuLocalCtt(Integer.parseInt(c.getString(1)));
	        	contato.setStatusCtt(c.getString(2));
	        	contato.setCodigoUsuCtt(Integer.parseInt(c.getString(3)));
	        	contato.setUsuarioUsuCtt(c.getString(4));
	        	contato.setNomeUsuCtt(c.getString(5));
	        	DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                contato.setDataNascUsuCtt(format.parse(c.getString(6)));
	        	contato.setEmailUsuCtt(c.getString(7));
	        	contato.setSexoUsuCtt(c.getString(8));                                  
                contato.setDataCadUsuCtt(format.parse(c.getString(9)));
	        	contato.setNomeFotoCtt(c.getString(10));	            
	        	contato.setQtdMsg(Integer.parseInt(c.getString(11)));//quantidade novas mensagens   
	        }
		    else 
		    	contato = null;
		    
	        c.close();
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} finally{   			
			//db.close();
		}   
	   
	   return contato;
  }
   
   
   public void insertMsg(Usuario usuario, Mensagem msg) {
	   
		  try{

			   
			   DateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			   String  sql = "";
			   if(msg.getUsuDestino() != null){
				   
				   sql = " INSERT INTO cadMensagens " +
			    		  	   " (codigoMsg, " +
			    		  	   "  codigoUsuOriMsg,  " +
			    		  	   "  codigoUsuDestMsg,  " +
			    		  	   "  dataMsg,  " +
			    		  	   "  textoMsg, " +
			    		  	   "  statusLida)  " + 		  	
			    		  	   " values ( " +
			    		  	   msg.getCodigoMsg() + ", " +
			    		  	   "'" + usuario.getCodigoUsu() + "', " +		
			    		  	   "'" + msg.getUsuDestino().getCodigoUsuCtt() + "', " +		  	   
			    		  	   "'" + formato.format(msg.getDataMsg()) + "', " +
			    		  	   "'" + msg.getTextoMsg()+ "', " + 
			    		  	   "'" + msg.getStatusLida() + "') " ;	
			    
			   }
			   else
			   {
				    sql = " INSERT INTO cadMensagens " +
		    		  	   " (codigoMsg, " +
		    		  	   "  codigoUsuOriMsg,  " +
		    		  	   "  codigoUsuDestMsg,  " +
		    		  	   "  dataMsg,  " +
		    		  	   "  textoMsg, " +
		    		  	   "  statusLida)  " + 		  	
		    		  	   " values ( " +
		    		  	   msg.getCodigoMsg() + ", " +
		    		  	   "'" + msg.getUsuOrig().getCodigoUsuCtt() + "', " +		
		    		  	   "'" + usuario.getCodigoUsu() + "', " +		  	   
		    		  	   "'" + formato.format(msg.getDataMsg()) + "', " +
		    		  	   "'" + msg.getTextoMsg()+ "', " + 
		    		  	   "'" + msg.getStatusLida() + "') " ;				   
			   }

			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = this.db.compileStatement(sql);
			      insertStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} finally{   			
				//db.close();
			}   
		   
	  }
   
   public ArrayList<Mensagem> selectMsg(Usuario usu, Contato contato) {
		 
	  ArrayList<Mensagem> lstMsg = new ArrayList<Mensagem>();
	  try{
      	  DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      	  
      	  
      	  String  sql = " update cadMensagens  " +
	        	  " set statusLida = 'S' " +	
		    	  "  where (codigoUsuOriMsg = " + usu.getCodigoUsu() + " or codigoUsuOriMsg = " + contato.getCodigoUsuCtt() + ") " + 
		    	  "    and (codigoUsuDestMsg = " + contato.getCodigoUsuCtt() + " or codigoUsuDestMsg = " + usu.getCodigoUsu() + ") ";
	      
		  SQLiteStatement updateStmt = null;	     
		  updateStmt = this.db.compileStatement(sql);
		  updateStmt.executeInsert();
	  	  
		  sql = " Select * from " +
				  		" (select * from cadMensagens  " +
		    			"  where codigoUsuOriMsg = " + usu.getCodigoUsu()	+
		    			"    and  codigoUsuDestMsg = " + contato.getCodigoUsuCtt()	+
		    			" union " +
		    			" select * from cadMensagens  " +
		    			"  where codigoUsuOriMsg = " + contato.getCodigoUsuCtt()	+
		    			"    and  codigoUsuDestMsg = " +  usu.getCodigoUsu() + ") "	+
		    			"  order by strftime('%Y-%m-%d %H:%M:%S', dataMsg)  ";
		      
		    Cursor c = db.rawQuery(sql, null);
		    c.moveToFirst();
	        while (c.isAfterLast() == false) {
	        	Mensagem msg = new Mensagem();
	        	
	        	msg.setCodigoMsg(Integer.parseInt(c.getString(0)));//codigoMsg	        	
	        	
	        	if(Integer.parseInt(c.getString(1)) != usu.getCodigoUsu())
	        		msg.setUsuOrig(selectContato(Integer.parseInt(c.getString(1))));//codigoUsuOriMsg
	        	else
	        		msg.setUsuDestino(selectContato(Integer.parseInt(c.getString(2))));//codigoUsuDestMsg

                msg.setDataMsg(format.parse(c.getString(3)));//dataMsg
                msg.setTextoMsg(c.getString(4));  //textoMsg
                msg.setStatusLida(c.getString(5));  //statusLida
	        	    
                lstMsg.add(msg);    
	        	c.moveToNext();
	            
	        }
	        c.close();
	        
	   
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} finally{   			
			//db.close();
		}   
	   
	   return lstMsg;
  }
   
   public void deletaMensagens(Usuario usuario, Contato contato) {
	   
	  try{		  
				  
		  
		    String  sql = " DELETE FROM cadMensagens " +
		    			  " WHERE codigoUsuOriMsg = " + usuario.getCodigoUsu() +
			  			  " AND codigoUsuDestMsg = " + contato.getCodigoUsuCtt();
		      
		    SQLiteStatement deleteStmt = null;	     
		    deleteStmt = this.db.compileStatement(sql);
		    deleteStmt.executeInsert();
			  
			  
		    sql = " DELETE FROM cadMensagens " +
		    			  " WHERE codigoUsuDestMsg = " + usuario.getCodigoUsu() +
			  			  " AND codigoUsuOriMsg = " + contato.getCodigoUsuCtt();
		      
		    deleteStmt = null;	     
		    deleteStmt = this.db.compileStatement(sql);
		    deleteStmt.executeInsert();
	      
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} finally{   			
			//db.close();
		}   
	   
  }
   
  private static class OpenHelper extends SQLiteOpenHelper {

	      OpenHelper(Context context) {
	         super(context, DATABASE_NAME, null, DATABASE_VERSION);
	      }

	      @Override
	      public void onCreate(SQLiteDatabase db) {
	    	  
	    	String sql = "  CREATE TABLE cadPais ( "  +
	    		"	  codigoPais  integer " +
	    		"	  siglaPais  varchar(3), " +
	    		"	  nomePais  varchar(200) not null) ";

	         db.execSQL(sql);
	         
	         sql = "   CREATE TABLE cadUsuario ( " +
	    		"	  codigoUsu  integer, " +
	    		"	  usuarioUsu varchar(10) not null, " +
	    		"	  senhaUsu  varchar(50) not null, " +
	    		"	  nomeUsu  varchar(100) not null, " +
	    		"	  dataNascUsu  varchar(10),  " +
	    		"	  codigoPaisUsu integer not null,  " +
	    		"	  emailUsu varchar(60) not null, " +
	    		"	  sexoUsu char(01) not null, " +
	    		"	  manterConUsu char(01) not null, " +
	    		"	  dataCadUsu  varchar(10),  " +
	    		"	  dataLoginUsu  varchar(10), " +
	    		"  	  nomeFotoUsu varchar(255) ) ";

	 	     db.execSQL(sql);
    
	    			

		 	sql = "    CREATE TABLE cadContatos ( " +
	    			"  codigoCtt  integer , " +
	    			"  codigoUsuLocalCtt integer not null, " +
	    			"  statusCtt  char(1) not null,  " +
					"  codigoUsuCtt  integer, " +
					"  usuarioUsuCtt varchar(10) not null, " +
					"  nomeUsuCtt  varchar(100) not null, " +
					"  dataNascUsuCtt  varchar(10),  " +
					"  emailUsuCtt varchar(60) not null, " +
					"  sexoUsuCtt char(01) not null, " +
					"  dataCadUsuCtt  varchar(10), " +
					"  nomeFotoCtt  varchar(255))  ";

			db.execSQL(sql);
			

		 	sql = "     CREATE TABLE cadMensagens ( " +
	    		  "	  codigoMsg integer, " +
	    		  "	  codigoUsuOriMsg integer , " +
	    		  "	  codigoUsuDestMsg integer, " +
	    		  "	  dataMsg  varchar(10), " +
	    		  "	  textoMsg varChar(255) not null, " +
	    		  "	  statusLida varchar(1) not null)  ";

			db.execSQL(sql);	    
	    	  
	      }

	      @Override
	      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
	         db.execSQL("DROP TABLE IF EXISTS cadPais");
	         db.execSQL("DROP TABLE IF EXISTS cadUsuario");
	         db.execSQL("DROP TABLE IF EXISTS cadContatos");
	         db.execSQL("DROP TABLE IF EXISTS cadMensagens");
	         onCreate(db);
	      }
	   }
}
