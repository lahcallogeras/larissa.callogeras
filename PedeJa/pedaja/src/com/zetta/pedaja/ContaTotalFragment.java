package com.zetta.pedaja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.zetta.pedaja.VisualizarContaActivity.DigitarDivisaoFragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class ContaTotalFragment  extends Fragment  {
	Context context ;
	private Handler handler;
    private String msgErro;
	private ProgressDialog progressDialog;
	ListView  lstTotalConta;
	TextView txtTotalConta;
	ArrayList<ModelVisualizarPedido> lista;
	Button btnContaTotalDividir;
	double total;
	
	public ContaTotalFragment(Context context){
		this.context = context;
	}
	
	
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {		 
			
	        View rootView = inflater.inflate(R.layout.conta_total, container, false);
	        lstTotalConta = (ListView)  rootView.findViewById(R.id.lstTotalConta);
	        txtTotalConta = (TextView)  rootView.findViewById(R.id.txtTotalConta);
	        btnContaTotalDividir = (Button)  rootView.findViewById(R.id.btnContaTotalDividir);
	        btnContaTotalDividir.setOnClickListener(btnContaTotalDividirClick);
	        

	        atualizarItensConta();
	        

	        return rootView;
	    }
	  
	  
	  private OnClickListener btnContaTotalDividirClick = new OnClickListener() {			
			public void onClick(View v) {
						
				Handler handler = new Handler();
				handler.post(new Runnable() {
				    
				    public void run() {
				    	 DialogFragment newFragment = DigitarDivisaoFragment.newInstance(total);
				    	 newFragment.show(getFragmentManager(),"alert");	
				    }
				});
			}
		};
	  
	  
	  @SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
		private void atualizarItensConta(){			
				Progresso("Aguarde");
			       	 new Thread(new Runnable() {
			       		 	Message msg = new Message();
			       	        public void run() {        	        	
			      	            try {	      
			      	            	
			      	            	JSONParser jsonParser = new JSONParser();   
			      	            	lista = new ArrayList<ModelVisualizarPedido>();
			      	            	
			       	        		boolean conexao = jsonParser.conectado(context);		                          
			       	        		if(conexao){	       	        			
			       	        			
			       	        			
				       	        		String httpGet = Global.urlConexaoLocal + "visualizarConta.php";
				       	     			List<NameValuePair>   params = new ArrayList<NameValuePair>();
				       	     			params.add(new BasicNameValuePair("P1", "" + Global.empresa.getEmpresaId()));
				       	     			params.add(new BasicNameValuePair("P2", "" + Global.conta.getContaId()));
				       	     			params.add(new BasicNameValuePair("P3", ""));
				       	     			params.add(new BasicNameValuePair("P4", "0"));		       	     			
				       	     			
				       	     			JSONObject  json = jsonParser.makeHttpRequest(httpGet,"POST", params);	  	
				       	     	        JSONArray itemContaArr = json.getJSONArray("pedidoCompleto"); 
				       	     	        for (int i = 0; i < itemContaArr.length(); i++) {
				       	     	             JSONObject c = itemContaArr.getJSONObject(i);
				       	     	             ModelVisualizarPedido model = new ModelVisualizarPedido();
				       	     	             
				       	     	             model.setContaId(Integer.parseInt(c.getString("ContaId")));
				       	     	             model.setQtdPedida(Integer.parseInt(c.getString("QtdPedida")));
				       	     	             model.setSequencia(Integer.parseInt(c.getString("Sequencia")));
				       	     	             model.setEmpresaId(Integer.parseInt(c.getString("EmpresaId")));
				       	     	             model.setDescCategoria(c.getString("DescCategoria"));
				       	     	             model.setProduto(c.getString("Produto"));
				       	     	             model.setDescricao(c.getString("Descricao"));	             
				       	     	             model.setValor(Double.parseDouble(c.getString("Valor")));
				       	     	             model.setStatusPedido(c.getString("StatusPedido"));
				       	     	             model.setTipoProdutoIdVar(Integer.parseInt(c.getString("TipoProdutoIdVar")));
				       	     	             model.setDescricaoAcomp(c.getString("DescricaoAcomp"));	     
				       	     	             model.setTipoProdutoIdAcomp(c.getString("TipoProdutoIdAcomp"));	  
				       	     	             model.setValorAcomp(Double.parseDouble(c.getString("ValorAcomp")));  
				       	     	             model.setDescricaoMeioMeio(c.getString("DescricaoMeioMeio"));	   
				       	     	             model.setValorMeioMeio(Double.parseDouble(c.getString("ValorMeioMeio")));   
				       	     	             model.setQtdMeioMeio(Integer.parseInt(c.getString("QtdMeioMeio")));		             
				       	     	             lista.add(model);
				       	     	         }
				                      	 msg.what = 1;
			                      	   	 
			       	        		} else {
			       	        			msg.what = 0;
			       	        			msgErro = "Sem conexão com a Internet.";
			       	        		}	       	        		
			      	        }
			      	      	catch (Exception e) {
			      	      		msg.what = 0;
		                        e.printStackTrace();
		                    }  
			      	        handler.sendMessage(msg);    
		              	 	
		  	        }
		  	    }).start(); 	  
			    handler=new Handler(){
			  		 public void handleMessage(Message msg){
			  			try{
			  				progressDialog.cancel();
				  			 if(msg.what == 0)
					  			 Toast.makeText(context, msgErro, Toast.LENGTH_LONG).show();	
				  			 
				  			AdapterItemConta adapter = new AdapterItemConta(context, R.layout.item_conta, lista);		 	          	   	         
					        lstTotalConta.setAdapter((ListAdapter) adapter);       
					        total =  somaConta(lista);
					        txtTotalConta.setText(String.format("R$ %.2f", total));
					        
					        
						} catch  (Exception ex) {
							ex.printStackTrace();
						}	 
			  		 }
			  	};	  	
		   
				
		}
	  
	  
	  

	public double somaConta(ArrayList<ModelVisualizarPedido>  lstConta){
		double soma = 0;
		for(int i = 0; i < lstConta.size(); i++)
		{
			soma = soma + lstConta.get(i).getValor() + lstConta.get(i).getValorAcomp() + lstConta.get(i).getValorMeioMeio();
		}
		return soma;		
	}
	  
	  public void Progresso(String titulo){
	        progressDialog = new ProgressDialog(getActivity());
	        progressDialog.setTitle(titulo);
	        progressDialog.setMessage("Processando...");
	        progressDialog.setIndeterminate(true);
	        progressDialog.setCancelable(true);
	        progressDialog.show();
	   }
	  
	 
	  
	

}
