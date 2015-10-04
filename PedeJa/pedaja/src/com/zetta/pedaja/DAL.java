package com.zetta.pedaja;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DAL {
	public static final String DATABASE_NAME = "pedeJa.db";
	public static final int DATABASE_VERSION = 1;
	public Context context;
	public SQLiteDatabase db;
	
	
   public DAL(Context context) {
      this.context = context;
      OpenDAL openHelper = new OpenDAL(this.context);
      db = openHelper.getWritableDatabase();
   }

   private static class OpenDAL extends SQLiteOpenHelper {

		public OpenDAL(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);		  
	    }
		
	
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	try{	
		    	String sql = "  CREATE TABLE Estado ( "  +
			  		"	  EstadoId  	integer not null, " +
			  		"	  Uf  			varchar(2) not null, " +
			  		"	  Nome  		varchar(200))  ";	
		    	db.execSQL(sql);       
		       
		       
		       sql = "  CREATE TABLE Cidade ( "  +
				  		"	  CidadeId  	integer not null, " +
				  		"	  EstadoId  	integer not null, " +
				  		"	  Nome  		varchar(200) not null) ";	
		    	
		       db.execSQL(sql);
		       
		        
		       
		       sql = "  CREATE TABLE Usuario ( "  +
				  		"	  UsuarioId		 integer not null, " +
			  			"	  Nome 			 varchar(200) not null, " +
			  			"	  Email			 varchar(300) not null, " +
				  		"	  DataNascimento varchar(10),	" +
				  		"	  CPF  			 varchar(11),	" +
				  		"	  DataCriacao    varchar(20),	" +		
				  		"	  UltimaAtu      varchar(20),	" +		
				  		"	  UltimaAcesso   varchar(20),	" +			  		
				  		"	  CidadeId  	 integer, " +
				  		"	  Usuario		 varchar(100) not null,	" +
				  		"	  Senha	    	 varchar(100) not null) ";	
		    	
		       db.execSQL(sql);
		       
		       
		       sql = "  CREATE TABLE Empresa ( "  +
				  		"	  EmpresaId				integer not null, " +
				  		"	  RazaoSocial  			varchar(200),	" +
				  		"	  NomeFantasia 	 		varchar(200),	" +
				  		"	  CidadeId  			integer not null,	" +
				  		"	  Logradouro  			varchar(200),	" +
				  		"	  Numero  	    		varchar(10),	" +
				  		"	  Complemento   		varchar(100),	" +
				  		"	  Bairro  	    		varchar(200),	" +
				  		"	  Cep   	    		varchar(15),	" +
				  		"	  Latitude  			varchar(30),	" +
				  		"	  Longitude   			varchar(30),	" +
				  		"	  EmpresaMatrizId  		integer,		" +
				  		"	  DataCriacao       	varchar(20), 	" +
				  		"	  UltimaAtu      		varchar(20),	" +		
				  		"	  UltimaAtuCardapio  	varchar(20),	" +		
				  		"	  TipoImagem		  	varchar(10),	" +
				  		"     AtualizacaoCardapio 	varchar(20), 	" +
				  		"	  EmpresaIdLocal  		integer,		" +
				  		"     ServidorLocal		 	varchar(1000), 	" +
				  		"     MixProduto		 	varchar(1) ) ";	
		    	
		       db.execSQL(sql);	   
		       
		       sql = "  CREATE TABLE TelefoneEmpresa ( "  +
				  		"	  TelefoneEmpresaId		integer not null, " +
				  		"	  EmpresaId				integer not null, " +
				  		"	  DDD					varchar(2) not null, " +
				  		"	  Telefone				varchar(20) not null, " +
				  		"	  Descricao				varchar(100) not null ) ";	
		    	
		       db.execSQL(sql);	  
		       
		       
		       sql = "  CREATE TABLE Mesas ( "  +
				  		"	  MesaId		integer not null, " +
				  		"	  EmpresaId		integer not null, " +
				  		"	  Numero		varchar(5), " +
				  		"	  Setor			varchar(5)) ";	
		    	
		       
		       db.execSQL(sql);	  
		       
		       sql = "  CREATE TABLE ModoPagamento ( "  +
				  		"	  ModoPagamentoId		integer not null, " +
				  		"	  Descricao				varchar(100) not null ) ";	
		    	
		       db.execSQL(sql);	   
		       
		       
		       sql = "  CREATE TABLE ModoPagamentoEmpresa ( "  +
				  		"	  ModoPagamentoEmpresaId	integer not null, " +
				  		"	  ModoPagamentoId			integer not null, " +
				  		"	  EmpresaId					integer not null ) ";	
		    	
		       db.execSQL(sql);	   		       

		       sql = "  CREATE TABLE Categoria ( "  +
				  		"	  CategoriaId	integer not null, " +
				  		"	  EmpresaId		integer not null, " +
				  		"	  Descricao		varchar(200) not null," +
				  		" 	  MixProduto	varchar(1)) ";	
		    	
		       db.execSQL(sql);	  


		       sql = "  CREATE TABLE Produto ( "  +
				  		"	  ProdutoId		integer not null, " +
				  		"	  EmpresaId		integer not null, " +
				  		"	  CategoriaId	integer not null, " +
				  		"	  Descricao		varchar(200), " +	
		  			    "	  Detalhe		varchar(500)) ";	
		    	
		       db.execSQL(sql);	 

		       sql = "  CREATE TABLE TipoProduto ( "  +
				  		"	  TipoProdutoId	integer not null, " +
				  		"	  EmpresaId		integer not null, " +
				  		"	  ProdutoId		integer not null, " +
				  		"	  Descricao  	varchar(200),	" +
				  		"	  Valor  	    real,	" +
				  		"	  Porcentagem	real,	" +
				  		"	  Visivel 		integer not null, " +
				  		"	  Contabil 		integer not null, " +
				  		"	  Tipo  	    varchar(2)," +
				  		" 	  Tamanho  	    varchar(2)) ";	
		    	
		       db.execSQL(sql);	 

		       sql = "  CREATE TABLE ExcecaoTipoProduto ( "  +
				  		"	  ExcecaoTipoProdutoId	integer not null, " +
				  		"	  EmpresaId				integer not null, " +
				  		"	  TipoProdutoId			integer not null, " +
				  		"	  DiaSemana				integer, " +
				  		"	  HorarioInicial		integer, " +
				  		"	  HorarioFInal			integer, " +
				  		"	  Valor  	    		real ) ";	
		    	
		       db.execSQL(sql);	 
		       
		       
		       sql = "  CREATE TABLE Conta ( "  +
				  		"	  ContaId			integer not null, " +
				  		"	  EmpresaId			integer not null, " +
				  		"	  MesaId			integer not null, " +
				  		"	  UsuarioId			integer, " +
				  		"	  GarcomId			integer, " +
				  		"	  DataAbertura		varchar(20), " +
				  		"	  DataEncerramento	varchar(20), " +
				  		"	  DataBaixa			varchar(20), " +
				  		"	  ModoPagamentoId	integer, " +
				  		"	  CodigoAbertura	varchar(20)) ";	
		       
		       
		       db.execSQL(sql);	
		       
		       
		       sql = "  CREATE TABLE ItensConta ( "  +
				  		"	  ItensContaId		integer not null, " +
				  		"	  ContaId			integer not null, " +
				  		"	  DataPedido		varchar(20), " +
				  		"	  DataBaixa			varchar(20), " +
				  		"	  TipoProdutoId		integer not null, " +
				  		"	  Sequencia 		integer not null, " +
				  		"	  UsuarioId			integer, " +
				  		"	  GarcomId			integer, " +
				  		"	  Valor  	    	real, " +
				  		"	  StatusPedido		char(2)," +
				  		"	  Divisao			varchar(2)) ";	
		    	
		       db.execSQL(sql);	
		       
		       

		       sql = "  CREATE TABLE Destaque ( "  +
				  		"	  DestaqueId		integer not null, " +
				  		"	  EmpresaId			integer not null, " +
				  		"	  TipoImagem		varchar(20) not null, " +
				  		"	  InicioVigencia	varchar(20) not null, " +
				  		"	  FimVigencia		varchar(20), " +
				  		" 	  Descricao 		varchar(1000) ) ";	
		    	
		       db.execSQL(sql);	
		       
		       

		       sql = "  CREATE TABLE ItensDestaque ( "  +
				  		"	  ItensDestaqueId		integer not null, " +
				  		"	  DestaqueId			integer not null, " +
				  		"	  EmpresaId				integer not null, " +
				  		"	  TipoProdutoId		    integer not null) ";	
		    	
		       db.execSQL(sql);	
		       


		       sql = "  CREATE TABLE VisualizacaoDestaque ( "  +
				  		"	  EmpresaId				integer not null, " +
				  		"	  UsuarioId				integer not null, " +
				  		"	  DataVisu		        varchar(20) not null) ";	
		    	
		       db.execSQL(sql);	
		       
		       

		       sql = "  CREATE TABLE VisualizarPedido ( "  +
				  		"	  ContaId				integer,	 	" +
				  		"	  Sequencia				integer, 		" +
				  		"	  QtdPedida				integer, 		" +
				  		"	  EmpresaId				integer, 		" +
				  		"	  DescCategoria			varchar(500), 	" +
				  		"	  Produto				varchar(500), 	" +
				  		"	  Descricao				varchar(500), 	" +
				  		"	  Valor  	    		real, 			" +
				  		"	  StatusPedido  		varchar(20), 	" +
				  		"	  TipoProdutoIdVar		integer, 		" +
				  		"	  DescricaoAcomp  		varchar(500), 	" +
				  		"	  TipoProdutoIdAcomp	varchar(500),	" +
				  		"	  ValorAcomp  	    	real, 			" +
				  		"	  DescricaoMeioMeio  	varchar(500), 	" +
				  		"	  ValorMeioMeio  	    real, 			" +				  		
				  		"	  QtdMeioMeio			integer ) 		";	
		    	
		       db.execSQL(sql);	
		       
		       
		
		        
		       
	    	} catch(Exception ex){   			
				ex.printStackTrace();
			}   
	       
	    }
	    


	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	       Log.w("Example", "Upgrading database, this will drop tables and recreate.");
	       db.execSQL("DROP TABLE IF EXISTS Estado");
	       db.execSQL("DROP TABLE IF EXISTS Cidade");
	       db.execSQL("DROP TABLE IF EXISTS Usuario");       
	       db.execSQL("DROP TABLE IF EXISTS Empresa");
	       db.execSQL("DROP TABLE IF EXISTS TelefoneEmpresa");
	       db.execSQL("DROP TABLE IF EXISTS Mesas");
	       db.execSQL("DROP TABLE IF EXISTS ModoPagamento");       
	       db.execSQL("DROP TABLE IF EXISTS ModoPagamentoEmpresa");
	       db.execSQL("DROP TABLE IF EXISTS Categoria");
	       db.execSQL("DROP TABLE IF EXISTS Produto");
	       db.execSQL("DROP TABLE IF EXISTS TipoProduto");       
	       db.execSQL("DROP TABLE IF EXISTS ExcecaoTipoProduto");
	       db.execSQL("DROP TABLE IF EXISTS Conta");
	       db.execSQL("DROP TABLE IF EXISTS Destaque");
	       db.execSQL("DROP TABLE IF EXISTS ItensDestaque");
	       db.execSQL("DROP TABLE IF EXISTS VisualizacaoDestaque");
	       db.execSQL("DROP TABLE IF EXISTS VisualizarPedido");
	       onCreate(db);
	    }
    }
}
