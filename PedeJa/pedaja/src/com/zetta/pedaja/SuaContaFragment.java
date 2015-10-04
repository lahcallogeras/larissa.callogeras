package com.zetta.pedaja;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.zetta.pedaja.VisualizarContaActivity.DigitarDivisaoFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.Context;
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
public class SuaContaFragment extends Fragment  {
	Context context ;
	private Handler handler;
    private String msgErro;
	private ProgressDialog progressDialog;
	ListView  lstSuaConta;
	TextView txtExibeTotal;
	Button btnDividir;
	double total;
	//ListView lstSuaConta;
	
	public SuaContaFragment(Context context){
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.sua_conta, container, false);
        lstSuaConta = (ListView)  rootView.findViewById(R.id.lstSuaConta);
        txtExibeTotal = (TextView)  rootView.findViewById(R.id.txtExibeTotal);
        btnDividir = (Button)  rootView.findViewById(R.id.btnDividir);
        

        btnDividir.setOnClickListener(btnDividirClick);
        
        //atualizarItensConta();
        exibeConta();
        
        
        
        return rootView;
    }
	  
	  private OnClickListener btnDividirClick = new OnClickListener() {			
			public void onClick(View v) {
				try{
					
					Handler handler = new Handler();
					handler.post(new Runnable() {
					    
					    public void run() {
					    	 DialogFragment newFragment = DigitarDivisaoFragment.newInstance(total);
					    	 newFragment.show(getFragmentManager(),"alert");	
					    }
					});
				           	
	        	} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	

	public double somaConta(ArrayList<ModelVisualizarPedido>  lstConta){
		double soma = 0;
		for(int i = 0; i < lstConta.size(); i++)
		{
			soma = soma + lstConta.get(i).getValor()  + lstConta.get(i).getValorAcomp()  + lstConta.get(i).getValorMeioMeio();
		}
		return soma;		
	}
	
	@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
	private void atualizarItensConta(){
		
			Progresso("Aguarde");
		       	 new Thread(new Runnable() {
		       		 	Message msg = new Message();
		       	        public void run() {        	        	
		      	            try {	      
		      	            	
		      	            	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		      	            	JSONParser jsonParser = new JSONParser();   
		       	        		
		       	        		boolean conexao = jsonParser.conectado(context);
	                          
		       	        		if(conexao){	       	        			
		       	        			
		       	        			
			       	        		 String httpGet = Global.urlConexaoLocal + "contaIndividual.php";
		                             List<NameValuePair> params = new ArrayList<NameValuePair>();
		                             params.add(new BasicNameValuePair("P1", "" + Global.conta.getContaId()));
		                             params.add(new BasicNameValuePair("P2", "" + Global.usuario.getUsuarioId()));
		                             JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params);
		                             
		                             DALConta dbConta = new DALConta(context);
		                      	   	 JSONArray itensContaArr = json.getJSONArray("itensConta");	                      	   
		                      	   	 for (int i = 0; i < itensContaArr.length(); i++) {
		                                  JSONObject c = itensContaArr.getJSONObject(i);
		                                  ModelItemConta itemConta = new ModelItemConta();
		                                  itemConta.setItensContaId(Integer.parseInt(c.getString("ItensContaId")));
		                                  itemConta.setContaId(Integer.parseInt(c.getString("ContaId")));                            
		                                  itemConta.setDataPedido(dateFormat.parse(c.getString("DataPedido")));	
		                                  itemConta.setTipoProdutoId(Integer.parseInt(c.getString("TipoProdutoId"))); 
		                                  itemConta.setValor(Double.parseDouble(c.getString("Valor"))); 
		                                  itemConta.setStatusPedido(c.getString("StatusPedido")); 
		                                  
		                                  if(c.getString("DataBaixa") != null && !c.getString("DataBaixa").equals(""))                   
			                                  itemConta.setDataBaixa(dateFormat.parse(c.getString("DataBaixa")));     
		                                  dbConta.alteraItemConta(itemConta);
		                            }
		                      	   dbConta = null;
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
			  			 if(msg.what == 0 && msgErro != null && !msgErro.equals(""))
				  			 Toast.makeText(context, msgErro, Toast.LENGTH_LONG).show();	
			  			 
			  			 exibeConta();
					} catch  (Exception ex) {
						ex.printStackTrace();
					}	 
		  		 }
		  	};	  	
	   
			
	}
	
	
	public void exibeConta(){
		try{
			DALVisualizarPedido dbVisu = new DALVisualizarPedido(context);
		    ArrayList<ModelVisualizarPedido> lstConta = dbVisu.selecionarVisualizarPedido();	   	               
	        AdapterItemConta adapter = new AdapterItemConta(context, R.layout.item_conta, lstConta);		 	          	   	         
	        lstSuaConta.setAdapter((ListAdapter) adapter);     
	        total = somaConta(lstConta);
	        txtExibeTotal.setText(String.format("R$ %.2f",  total));
        } catch (Exception ex){
        	ex.printStackTrace();
        }
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
