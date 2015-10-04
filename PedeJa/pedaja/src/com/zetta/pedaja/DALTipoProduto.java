package com.zetta.pedaja;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteStatement;

public class DALTipoProduto  extends DAL {

	public DALTipoProduto(Context context) {
		super(context);
	}
	
	
	 public void apagaTipoProduto(int EmpresaId) {		   
		  try{		  
			    String  sql = " DELETE FROM TipoProduto " + 
			    			  "   where EmpresaId = " + EmpresaId;	
			      
			    SQLiteStatement deleteStmt = null;	     
			    deleteStmt = this.db.compileStatement(sql);
			    deleteStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
    }
	 	
	
	public boolean atualizaTipoProduto(ModelTipoProduto tipoProduto) {
		boolean Novo = false; 
	    try{	  
	    		    	
	    	String sql = " select count(*) from TipoProduto " +
			   		     " where TipoProdutoId = " +  tipoProduto.getTipoProdutoId();
	    	
	    	int total = (int) DatabaseUtils.longForQuery(db, sql, null);
	    	
	    	
	    		    	
	    	if (total <= 0) {	    	
	    	
			      sql = " INSERT INTO TipoProduto " +
			    		  	   " (TipoProdutoId, " +
			    		  	   "  EmpresaId, " +
			    		  	   "  ProdutoId, " +
			    		  	   "  Descricao, " +
			    		  	   "  Valor, " +
			    		  	   "  Porcentagem,  " +
			    		  	   "  Visivel, " +
			    		  	   "  Contabil, " +
			    		  	   "  Tipo, " +
			    		  	   "  Tamanho)  " + 		  	
			    		  	   " values ( " +
			    		  	 tipoProduto.getTipoProdutoId() + ", " +
			    		  	 tipoProduto.getEmpresaId() + ", " +
			    		  	 tipoProduto.getProdutoId() + ", '" +
			    		  	 tipoProduto.getDescricao() + "', " +
			    		  	 tipoProduto.getValor() + ", " +
			    		  	 tipoProduto.getPorcentagem() + ", " +
			    		  	 tipoProduto.getVisivel() + ", " +
			    		  	 tipoProduto.getContabil() + ", '" +
			    		  	 tipoProduto.getTipo() + "', '" +
			    		  	 tipoProduto.getTamanho() + "') " ;
			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
			      Novo = true;
	    	} else {
	    		
	    		 sql = " UPDATE  TipoProduto  SET " +    		  	   
		    		  	 "  EmpresaId = " + tipoProduto.getEmpresaId() + ", " +
		    		  	 "  ProdutoId = " + tipoProduto.getProdutoId() + ", " +
		    		  	 "  Descricao = '" + tipoProduto.getDescricao() + "', " +  
		    		  	 "  Valor = " + tipoProduto.getValor() + ", " +
		    		  	 "  Porcentagem = " + tipoProduto.getPorcentagem() + ", " +
		    		  	 "  Visivel = " + tipoProduto.getVisivel() + ", " +
		    		  	 "  Contabil = " + tipoProduto.getContabil() + ", " +
		    		  	 "  Tipo = '" + tipoProduto.getTipo() + "', " +    	
		    		  	 "  Tamanho = '" + tipoProduto.getTamanho() + "' " +		
		    		  	 " WHERE TipoProdutoId = " + tipoProduto.getTipoProdutoId();	
	    		
  		  	   
			      SQLiteStatement alteraStatemente = null;	     
			      alteraStatemente = this.db.compileStatement(sql);
			      alteraStatemente.executeInsert();
	    	}
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	    return Novo;
	}
	
	 public void apagaExcecaoTipoProduto(int EmpresaId) {		   
		  try{		  
			    String  sql = " DELETE FROM ExcecaoTipoProduto " + 
			    			  "   where EmpresaId = " + EmpresaId;	
			      
			    SQLiteStatement deleteStmt = null;	     
			    deleteStmt = this.db.compileStatement(sql);
			    deleteStmt.executeInsert();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			}
   }

	
	
	public boolean atualizaExcecaoTipoProduto(ModelExcecaoProduto excecaoTipoProduto) {
		boolean Novo = false; 
	    try{	  
	    		    	
	    	String sql = " select count(*) from ExcecaoTipoProduto " +
			   		     " where ExcecaoTipoProdutoId = " +  excecaoTipoProduto.getExcecaoTipoProdutoId();
	    	
	    	int total = (int) DatabaseUtils.longForQuery(db, sql, null);
	    	
	    	
	    		    	
	    	if (total <= 0) {	    	
	    	
			      sql = " INSERT INTO ExcecaoTipoProduto " +
			    		  	   " (ExcecaoTipoProdutoId, " +
			    		  	   "  EmpresaId, " +
			    		  	   "  TipoProdutoId, " +
			    		  	   "  DiaSemana, " +
			    		  	   "  HorarioInicial, " +
			    		  	   "  HorarioFInal,  " +
			    		  	   "  Valor)  " + 		  	
			    		  	   " values ( " +
			    		  	 excecaoTipoProduto.getExcecaoTipoProdutoId() + ", " +
			    		  	 excecaoTipoProduto.getEmpresaId() + ", " +
			    		  	 excecaoTipoProduto.getTipoProdutoId() + ", " +
			    		  	 excecaoTipoProduto.getDiaSemana() + ", " +
			    		  	 excecaoTipoProduto.getHorarioInicial() + ", " +
			    		  	 excecaoTipoProduto.getHorarioFInal() + ", " +
			    		  	 excecaoTipoProduto.getValor() + ") " ;
			      
			      SQLiteStatement insertStmt = null;	     
			      insertStmt = db.compileStatement(sql);
			      insertStmt.executeInsert();
			      Novo = true;
	    	} else {
	    		
	    		 sql = " UPDATE  ExcecaoTipoProduto  SET " +    		  	   
		    		  	 "  EmpresaId = " + excecaoTipoProduto.getEmpresaId() + ", " +
		    		  	"  	TipoProdutoId = " + excecaoTipoProduto.getTipoProdutoId() + ", " +
		    		  	"  	DiaSemana = " + excecaoTipoProduto.getDiaSemana() + ", " +
		    		  	"  	HorarioInicial = " + excecaoTipoProduto.getHorarioInicial() + ", " +
		    		  	"  	HorarioFInal = " + excecaoTipoProduto.getHorarioFInal() + ", " +
		    		  	"  	Valor = " + excecaoTipoProduto.getValor() +		    		  	 	  
		    		  	" WHERE ExcecaoTipoProdutoId = " + excecaoTipoProduto.getExcecaoTipoProdutoId();	
	    		
  		  	   
			      SQLiteStatement alteraStatemente = null;	     
			      alteraStatemente = this.db.compileStatement(sql);
			      alteraStatemente.executeInsert();
	    	}
	      
		} catch(Exception ex){   			
			ex.printStackTrace();
		} 
	   
	    return Novo;
	}
	
	
	public ArrayList<ModelTipoProduto> selecionaTipoProduto(int produtoId, String tipo, int hora, int diaSemana) 
	{
		ArrayList<ModelTipoProduto> lstTipoProduto = new ArrayList<ModelTipoProduto>();
		  try{			  
			    String  sql = " Select p.TipoProdutoId, p.EmpresaId, p.ProdutoId, p.Descricao, " +
			    			"	        ep.Valor, p.Porcentagem, p.Visivel,  p.Tipo, p.Tamanho  " +
					    	"     from  ExcecaoTipoProduto ep, TipoProduto p " +
					    	"  where p.TipoProdutoId = ep.TipoProdutoId  " +
					    	"    and ep.DiaSemana = " + diaSemana +
					    	"    and " + hora + " between HorarioInicial and HorarioFInal " +
					    	"    and p.Tipo in (" + tipo + ") " +
					    	"    and p.ProdutoId =  " + produtoId +
					    	"    and p.Visivel =  1 " +
					    	" union " +
					    	" Select p.TipoProdutoId, p.EmpresaId, p.ProdutoId, p.Descricao, " +
			    			"	        p.Valor, p.Porcentagem, p.Visivel,  p.Tipo, p.Tamanho  " +
			    			"    from TipoProduto p  " +
					    	" where  Visivel = 1  " + 
					    	"  and p.Tipo  in (" + tipo + ") " +
					    	"  and p.ProdutoId =  " + produtoId +
					    	"  and p.TipoProdutoId  not in (Select p.TipoProdutoId  " +
					    	"     from  ExcecaoTipoProduto ep, TipoProduto p " +
					    	"  where p.TipoProdutoId = ep.TipoProdutoId  " +
					    	"    and ep.DiaSemana = " + diaSemana +
					    	"    and " + hora + " between HorarioInicial and HorarioFInal " +
					    	"    and p.Tipo in (" + tipo + ") " +
					    	"    and p.ProdutoId =  " + produtoId +
					    	"    and p.Visivel =  1) ";
			    
			    
			    	
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelTipoProduto tipoProduto = new ModelTipoProduto();
		        	tipoProduto.setTipoProdutoId(Integer.parseInt(c.getString(0)));	
		        	tipoProduto.setEmpresaId(Integer.parseInt(c.getString(1)));	
		        	tipoProduto.setProdutoId(Integer.parseInt(c.getString(2)));	
		        	tipoProduto.setDescricao(c.getString(3));
		        	tipoProduto.setValor(Double.parseDouble(c.getString(4)));	
		        	tipoProduto.setPorcentagem(Double.parseDouble(c.getString(5)));
		        	tipoProduto.setVisivel(Integer.parseInt(c.getString(6)));	
		        	tipoProduto.setTipo(c.getString(7));
		        	tipoProduto.setTamanho(c.getString(8));
		        	tipoProduto.setSelecionado(false);
		        	
		        	c.moveToNext();
		        	lstTipoProduto.add(tipoProduto);
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return lstTipoProduto;
	  }
	
	
	public ModelTipoProduto selecionaTipoProdutoId(int tipoProdutoId) {
          
		  ModelTipoProduto tipoProduto = new ModelTipoProduto();
		  try{
			  
		   		  
			    String  sql = " select * from TipoProduto " +
			    		" where  Visivel = 1  " + 
			    		"  and TipoProdutoId =  " + tipoProdutoId ;
			    
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        if (c.isAfterLast() == false) {
		        	tipoProduto.setTipoProdutoId(Integer.parseInt(c.getString(0)));	
		        	tipoProduto.setEmpresaId(Integer.parseInt(c.getString(1)));	
		        	tipoProduto.setProdutoId(Integer.parseInt(c.getString(2)));	
		        	tipoProduto.setDescricao(c.getString(3));
		        	tipoProduto.setValor(Double.parseDouble(c.getString(4)));	
		        	tipoProduto.setPorcentagem(Double.parseDouble(c.getString(5)));
		        	tipoProduto.setVisivel(Integer.parseInt(c.getString(6)));	
		        	tipoProduto.setTipo(c.getString(7));	
		        	tipoProduto.setTamanho(c.getString(8));
		        	tipoProduto.setSelecionado(false);
		        	c.moveToNext();
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return tipoProduto;
	  }
	
	
	public  List<ModelMetade> selecionarSaboresMetade() {
        
		  List<ModelMetade> lista = new ArrayList<ModelMetade>();
		  try{
			  
		   		  
			    String  sql = " select a.TipoProdutoId, b.Descricao DescricaoProduto, " +
			    	    " a.Descricao DescricaoTipoProduto, b.Detalhe,  a.Valor, b.ProdutoId " +
			    	    " from TipoProduto a, Produto b" +
			    		" where a.Visivel = 1  " + 
			    	    "  and  a.Tipo = 'VA' " +
			    		"  and  a.ProdutoId = b.ProdutoId " +
			    		"  and  b.CategoriaId = " + Global.categoriaMeioMeio +
			    		"  and  a.EmpresaId = " + Global.empresa.getEmpresaId() +
			    		"  and  a.Tamanho = '" + Global.Tamanho + "'" ;
			    
			    Cursor c = db.rawQuery(sql, null);
			    c.moveToFirst();
		        while (c.isAfterLast() == false) {
		        	ModelMetade model = new ModelMetade();
		        	model.setTipoProdutoId(Integer.parseInt(c.getString(0)));	
		        	model.setDescricao(c.getString(1) + " - " + c.getString(2));	
		        	model.setDetalhe(c.getString(3));			        	
		        	model.setValor(Double.parseDouble(c.getString(4)));	
		        	model.setProdutoId(Integer.parseInt(c.getString(5)));	
		        	model.setDivisao("S");
		        	lista.add(model);
		        	c.moveToNext();
		        }
		        c.close();
		      
			} catch(Exception ex){   			
				ex.printStackTrace();
			} 
		   
		   return lista;
	  }
	

}
