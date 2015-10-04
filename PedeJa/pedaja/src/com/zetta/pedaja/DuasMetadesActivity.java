package com.zetta.pedaja;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DuasMetadesActivity extends Activity {


	private LinearLayout lnPizza;
	private Button btnEsquerda;
	private Button btnDireita;
	private TextView txtCategoria;
	private TextView txtTamanho;
	private TextView txtMetade01;
	private TextView txtMetade02;
	private TextView txtTotal;
	private Button btnPedir;
	private EditText edtQtd;
	private Button btnMais;
	private Button btnMenos;
	
	private Handler handler;
	private ProgressDialog progressDialog;
	String msgErro;
	
	private ModelCategoria modelCategoria;
	private static PizzaDuasMetades  pie; 
	private Context context;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.duas_metades);
		lnPizza = (LinearLayout) findViewById(R.id.lnPizza);
		btnEsquerda = (Button) findViewById(R.id.btnEsquerda);
		btnDireita = (Button) findViewById(R.id.btnDireita);
		txtCategoria = (TextView) findViewById(R.id.txtCategoria);
		txtTamanho = (TextView) findViewById(R.id.txtTamanho);
		txtMetade01 = (TextView) findViewById(R.id.txtMetade01);
		txtMetade02 = (TextView) findViewById(R.id.txtMetade02);
		txtTotal= (TextView) findViewById(R.id.txtTotal);
		btnPedir= (Button) findViewById(R.id.btnPedir);
		edtQtd =  (EditText)findViewById(R.id.edtQtd);
		btnMais =  (Button)findViewById(R.id.btnMais);
		btnMenos =  (Button)findViewById(R.id.btnMenos);	
		
		edtQtd.setText("1");	
		edtQtd.setKeyListener(null);
		btnMais.setOnClickListener(clickMais);
		btnMenos.setOnClickListener(clickMenos);
		
		btnEsquerda.setOnClickListener(btnEsquerdaOnClick);
		btnDireita.setOnClickListener(btnDireitaOnClick);
		btnPedir.setOnClickListener(btnPedirOnClick);

		context = this;	
		
		DALCategoria dbCategoria = new DALCategoria(context);
		modelCategoria = dbCategoria.selecionaCategoriaId(Global.categoriaMeioMeio);
		txtCategoria.setText(modelCategoria.getDescricao());
		dbCategoria = null;		
		
		pie = new PizzaDuasMetades(this);
		lnPizza.addView(pie);
		
		String Tamanho = "Grande";
		if(Global.Tamanho == "M")
			Tamanho = "Média";
		else if(Global.Tamanho == "P")
				Tamanho = "Pequena";
		
		txtTamanho.setText(Tamanho);
		
		
		//pieContainer.getChildAt(0).invalidate();
	        
	}
	
	private OnClickListener clickMais = new OnClickListener() {			
		public void onClick(View v) {
			int valor = Integer.parseInt(edtQtd.getText().toString());
			if(valor < 20)
				valor++;
			
			edtQtd.setText("" + valor);			
		}
	};
	
	private OnClickListener clickMenos = new OnClickListener() {			
		public void onClick(View v) {
			int valor = Integer.parseInt(edtQtd.getText().toString());
			if(valor > 1)
				valor--;
			
			edtQtd.setText("" + valor);			
		}
	};
	
	@Override	
	protected void onResume() {
		super.onResume();
		
			double Total = 0;

			
			if(Global.modelMetade01.getTipoProdutoId() > 0){				
				pie.CorMetade01 = "#C40000";
				txtMetade01.setText(Global.modelMetade01.getDescricao() + " - " + String.format("R$ %.2f",  Global.modelMetade01.getValor()));
				
			}
			
			
			if(Global.modelMetade02.getTipoProdutoId() > 0){
				pie.CorMetade02 = "#FFFF00";
				txtMetade02.setText(Global.modelMetade02.getDescricao() + " - " + String.format("R$ %.2f",  Global.modelMetade02.getValor()));
				Global.modelMetade02.setValorParcial(Global.modelMetade02.getValor());
			}
			
			
			if(Global.empresa.getMixProduto().equals("D")) {
				Total = (Global.modelMetade01.getValor() + Global.modelMetade02.getValor())/2;				
				Global.modelMetade01.setValorParcial(Total);
				Global.modelMetade02.setValorParcial(Total);				
				Global.modelMetade01.setDivisao("M");			
				Global.modelMetade02.setDivisao("S");
			}
			else  if(Global.empresa.getMixProduto().equals("M")){
				
				if(Global.modelMetade01.getValor()  > Global.modelMetade02.getValor() ){
					
					Global.modelMetade01.setValorParcial(Global.modelMetade01.getValor());					
					Global.modelMetade01.setDivisao("M");			
					Global.modelMetade02.setValorParcial(0);		
					Global.modelMetade02.setDivisao("S");
					Total = (Global.modelMetade01.getValorParcial());		
				}
				else { 
					Global.modelMetade02.setValorParcial(Global.modelMetade02.getValor());					
					Global.modelMetade02.setDivisao("M");			
					Global.modelMetade01.setValorParcial(0);		
					Global.modelMetade01.setDivisao("S");
					Total = (Global.modelMetade02.getValorParcial());
				}
				
			}
			
			txtTotal.setText(String.format("TOTAL: R$ %.2f", Total));	
			lnPizza.getChildAt(0).invalidate();
		
		
	}
	
	OnClickListener btnPedirOnClick = new OnClickListener() {
		
		public void onClick(View v) {
			try {
				
				if(Global.modelMetade01.getTipoProdutoId() <=0 || Global.modelMetade02.getTipoProdutoId() <=0 )
				{
					mostrarMensagem("Você deve selecionar os sabores das duas metades!", "Aviso");
					return;
				}

				Global.quantidadeSelecionada = Integer.parseInt(edtQtd.getText().toString());	
				
				int ProdutoId = 0;
				if(Global.modelMetade01.getDivisao().equals("M"))
					ProdutoId = Global.modelMetade01.getProdutoId();
				else
					ProdutoId = Global.modelMetade02.getProdutoId();
				
				
				DALTipoProduto dbTipoProduto = new DALTipoProduto(context);
				Global.lstAcompanhamento = dbTipoProduto.selecionaTipoProduto(ProdutoId, "'AC'", 0, 0);
				if(Global.lstAcompanhamento.size() > 0){
					Global.pedidoMetade = true;
					Intent intent = new Intent(getApplicationContext(), AcompanhamentoActivity.class); 
			   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);			
					finish();
				} else 
					InserirPedidoMeio();
				
				
			} catch (Exception ex){
				ex.printStackTrace();
			}
			
		}
	};
	
	@SuppressLint("HandlerLeak")
	private void InserirPedidoMeio(){
		
		try {
			 Progresso("Aguarde");
		       	 new Thread(new Runnable() {
	       	        public void run() {      

      	        		 Message msg = new Message();
	       	        	  try {

	       	        		JSONParser jsonParser = new JSONParser();   
	       	        		boolean conexao = jsonParser.conectado(context);
	       	        		
	       	        		if(conexao){     
	       	        			
			       	     		for(int j = 0; j < Global.quantidadeSelecionada; j++){			       	     			
			       	     			int sequencia = 0;
			       	     			
			       	     			int[] retorno  = Global.inserePedido(context, Global.modelMetade01.getTipoProdutoId(), Global.modelMetade01.getValorParcial(), sequencia, Global.modelMetade01.getDivisao());
			       	     			msg.what = retorno[0];
			       	     			sequencia = retorno[1];			       	     			
			       	     			
			       	     			retorno  = Global.inserePedido(context,  Global.modelMetade02.getTipoProdutoId(), Global.modelMetade02.getValorParcial(), sequencia, Global.modelMetade02.getDivisao());
			       	     			msg.what = retorno[0];
			       	     			Global.visualizarPedido(context, sequencia);
			       	     		}
	       	        			
	       	        		}
	       	        		else 
	       	        			msg.what = 2;
	       	        		

	       	        	  }
      	        	  catch (Exception e) {
                         e.printStackTrace();
    	        		  msg.what = 0;
                     }  
	       	          handler.sendMessage(msg);     	              			              	 	
	  	        }
	  	    }).start();
	 	  
		    handler=new Handler(){
		  		 public void handleMessage(Message msg){
		
		  			 progressDialog.cancel();
		  			 if(msg.what==1)
		  			 {
		  				Toast.makeText(DuasMetadesActivity.this, "Pedido feito com sucesso!", Toast.LENGTH_LONG).show();
		  				onBackPressed();
		  		 	 }
		  			 else  if(msg.what == 0)
		  				Toast.makeText(DuasMetadesActivity.this, "Erro ao inserir pedido.", Toast.LENGTH_LONG).show();
		  			else  if(msg.what == 3)
		  				Toast.makeText(DuasMetadesActivity.this, "Já foi solicitado o fechamento dessa conta. Se a conta encontra-se aberta em seu aparelho, você deve fechá-la.", Toast.LENGTH_LONG).show();
		  			 else  if(msg.what == 2)
		  				Toast.makeText(DuasMetadesActivity.this, "Sem Conexão com a internet! Impossivel realizar pedido..", Toast.LENGTH_LONG).show();
		  		 }
		  	};
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	OnClickListener btnEsquerdaOnClick = new OnClickListener() {
		
		public void onClick(View v) {
			Global.MetadeSelecionada = 1;			
			AbrirSelecao();	
		}
	};	
	
	
	
	OnClickListener btnDireitaOnClick = new OnClickListener() {
		
		public void onClick(View v) {
			Global.MetadeSelecionada = 2;
			AbrirSelecao();			
		}
	};
	
	private void AbrirSelecao(){		
		Intent intent = new Intent(getApplicationContext(), SelecionarMetadeActivity.class); 
   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	
	
	public void Progresso(String titulo){
         progressDialog = new ProgressDialog(DuasMetadesActivity.this);
         progressDialog.setTitle(titulo);
         progressDialog.setMessage("Processando...");
         progressDialog.setIndeterminate(true);
         progressDialog.setCancelable(true);
         progressDialog.show();
    }
	   
	private void mostrarMensagem(String msg, String titulo) {
		new AlertDialog.Builder(this)
				.setTitle(titulo)
				.setMessage(msg)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("OK", null).show();
	
	}


}
