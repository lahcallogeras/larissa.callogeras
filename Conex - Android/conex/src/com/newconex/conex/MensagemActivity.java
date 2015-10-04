package com.newconex.conex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.newconex.conex.R;
 
@SuppressLint("HandlerLeak")
public class MensagemActivity extends Activity  {
	
    private ProgressDialog progressDialog;
    Context context = this;
    Handler handler;
    byte[] encodeByte; 
    ImageView image;
    ImageButton btnEnviar;
    EditText txtMsg;
    private ListView lstMensagem;
    MensagemAdapter adapter;    
    DataHelper dh;

    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensagens);         
		lstMensagem = (ListView)findViewById(R.id.lstMensagem);	    
		btnEnviar =(ImageButton)findViewById(R.id.btnEnviar);
        txtMsg =  (EditText)findViewById(R.id.txtMsg);	 
        // Hide the title bar 
       //requestWindowFeature(Window.FEATURE_NO_TITLE);
        
    
        // timer
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 100);
        Globais.contato.setNotificacao(false); 
		
        try{

            dh = new DataHelper(context);
            carregaMensagem();

            btnEnviar.setOnClickListener(new OnClickListener() {			
    			public void onClick(View v) {
    				  //Chamar site do conex pra incrição
    				if(txtMsg.getText().toString().equals(""))
    				{
    	   				 Toast.makeText(MensagemActivity.this, "Escreva o conteúdo da mensagem!", Toast.LENGTH_LONG).show();     
    	   				 return;
    				}
    			
    				enviarMensagem();				
    			}
    		});     
            
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        
    }
	
	
 @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try
        {      	
        	menu.add(0,0,0,"Deletar Mensagens").setIcon(R.drawable.deletar);            
        }
        catch (Exception e) {
            e.printStackTrace();
        }            
        return super.onCreateOptionsMenu(menu);        
    }
	 
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	  // Captura o menu selecionado
    	   switch (item.getItemId()) {
    	      case 0:{
    	    	  
    	    	  deletarMensagens();
    	      }
    	         
    	   }
    	   return true;
    	}
    
    @Override
    public void onStop() 
    {
        super.onStop();
        Globais.contato.setNotificacao(true);
        mHandler.removeCallbacks(mUpdateTimeTask);
    }
        
    
    @Override
    public void onStart(){
        super.onStart();
        carregaMensagem();
        Globais.contato.setNotificacao(false);    	
    }
    
    
    @Override
    public void onBackPressed() {
        Globais.contato.setNotificacao(true);
        super.onBackPressed();	
    }
    
    public void Progresso(String titulo){
        progressDialog = new ProgressDialog(MensagemActivity.this);
        progressDialog.setTitle(titulo);
        progressDialog.setMessage("Processando...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
   }
    
	@SuppressLint("NewApi")
	public void carregaMensagem(){    	
    	
        List<Mensagem> stateList = dh.selectMsg(Globais.usuario, Globais.contato);	   	               
        adapter = new MensagemAdapter(context, R.layout.lst_row_mensagem, stateList);		    
		lstMensagem.setAdapter((ListAdapter) adapter);
	   	//Posicionar ultimo item da list
	   	if(lstMensagem.getCount()> 0)
	   		lstMensagem.setSelection(lstMensagem.getCount()-1);

		     	        
		
    }
    
 public void enviarMensagem(){
	   	
	   	Progresso("Aguarde");
	   	new Thread(new Runnable() {
   	        public void run() {        	        	
   	        	try{
   	        		
   	        		JSONParser jsonParser = new JSONParser();
   	     	   		Message msg=new Message(); 	   	
   	     	   		boolean conexao = jsonParser.conectado(context);
   	     	   		//conexao = true;
   	     	   		if(conexao){
	   	     	   	  String httpGet = "http://www.newconex.heliohost.org/enviaMsgA.php";
	                  //Parametros a serem passados para o PHP
	   	     	   	  
	                  List<NameValuePair> params = new ArrayList<NameValuePair>();
	                  params.add(new BasicNameValuePair("P1", "" + Globais.usuario.getCodigoUsu()));
	                  params.add(new BasicNameValuePair("P2", "" + Globais.contato.getCodigoUsuCtt()));      
	                  params.add(new BasicNameValuePair("P3", txtMsg.getText().toString()));              
	                  //Buscando Informações - Banco de dados remoto                        
	                  JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params); 
	                  int success = json.getInt("sucesso");
                      
                      if (success == 1) {
                    	  //CARREGA USUÁRIO /////////////////////////////////
                    	  //////////////////////////////////////////////////// 
                    	  JSONArray msgArr = json.getJSONArray("msg"); 
                          for (int i = 0; i < msgArr.length(); i++) {
                               JSONObject c = msgArr.getJSONObject(i);
                               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
           	   	        		Mensagem mensagem = new Mensagem();
           	   	        		mensagem.setCodigoMsg(Integer.parseInt(c.getString("codigoMsg")));
           	   	        		mensagem.setUsuDestino(dh.selectContato(Integer.parseInt(c.getString("codigoUsuDestMsg"))));
           	   	        		mensagem.setDataMsg(dateFormat.parse(c.getString("dataMsg")));             	   	        	
           	   	        		mensagem.setTextoMsg(c.getString("textoMsg"));  
           	   	        		mensagem.setStatusLida("N");
           	   	        		dh.insertMsg(Globais.usuario, mensagem);
                           }
                          
                          JSONArray msgRecArr = json.getJSONArray("msgRec"); 
	                         for (int i = 0; i < msgRecArr.length(); i++) {
	                              JSONObject c = msgRecArr.getJSONObject(i);
	                              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	                              Mensagem mensagem = new Mensagem();
	          	   	        	  mensagem.setCodigoMsg(Integer.parseInt(c.getString("codigoMsg")));
	          	   	        	  mensagem.setUsuOrig(dh.selectContato(Integer.parseInt(c.getString("codigoUsuOriMsg"))));
	          	   	        	  mensagem.setDataMsg(dateFormat.parse(c.getString("dataMsg")));      
	          	   	        	  mensagem.setTextoMsg(c.getString("textoMsg"));    
	           	   	        	  mensagem.setStatusLida("N");
	          	   	        	  dh.insertMsg(Globais.usuario, mensagem);
	                          }

   	     	   				msg.what=1;//Deu certo
                      }
                      else{
     	     	   			msg.what=2;//Problemas ao enviar msg
                      }
	                  
   	     	   			
   	     	   		}
   	     	   		else
   	     	   			msg.what=0;//Sem conexao
   	     	   		
 	        	  	handler.sendMessage(msg);
   	     	   		
	   	         	
   	     	   		
   	        	}catch(Exception ex){
   	        		ex.printStackTrace();
   	        	}	
   	        }
	   	}).start();
   	 
	   	
	    handler=new Handler(){
   		 public void handleMessage(Message msg){

   			 progressDialog.cancel();
   			 if(msg.what==0){
   				 Toast.makeText(MensagemActivity.this, "Sem conexão com a internet, não é possível enviar esta mensagem!", Toast.LENGTH_LONG).show();        
   		 	}
   			else if (msg.what==1){
   				txtMsg.setText("");
   			   	carregaMensagem();
   			}
   			else if (msg.what==2){        
  				 Toast.makeText(MensagemActivity.this, "Problemas ao enviar mensagem!", Toast.LENGTH_LONG).show();       
    		}
   		 }
   	};
  
		     	        
		
    }
 
 
 private Handler mHandler = new Handler();
 private Runnable mUpdateTimeTask = new Runnable() 
 {
    public void run() 
    {
    		new Thread(new Runnable() {
       	        public void run() {  

       	     	try{
       	        	JSONParser jsonParser = new JSONParser();
       	   	   		//Message msg=new Message(); 	   	
       	   	   		boolean conexao = jsonParser.conectado(context);
       	   	   		//conexao = true;
       	   	   		if(conexao){
       	   	   			String httpGet = "http://newconex.heliohost.org/recebeMsgA.php";
       	   	   			//Parametros a serem passados para o PHP
       		              List<NameValuePair> params = new ArrayList<NameValuePair>();
       		              params.add(new BasicNameValuePair("P1", "" + Globais.usuario.getCodigoUsu()));
       		              params.add(new BasicNameValuePair("P2", "" + Globais.contato.getCodigoUsuCtt()));
       		              //Buscando Informações - Banco de dados remoto                        
       		              JSONObject json = jsonParser.makeHttpRequest(httpGet,"POST", params); 
       		              int success = json.getInt("sucesso");	                                           
       	                     if (success == 1) {
       	                   	  //CARREGA USUÁRIO /////////////////////////////////
       	                   	  //////////////////////////////////////////////////// 
       	                   	  JSONArray msgArr = json.getJSONArray("msg"); 
       	                         for (int i = 0; i < msgArr.length(); i++) {
       	                              JSONObject c = msgArr.getJSONObject(i);
       	                              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       	          	   	        		Mensagem mensagem = new Mensagem();
       	          	   	        		mensagem.setCodigoMsg(Integer.parseInt(c.getString("codigoMsg")));
       	          	   	        		mensagem.setUsuOrig(dh.selectContato(Integer.parseInt(c.getString("codigoUsuOriMsg"))));
       	          	   	        		mensagem.setDataMsg(dateFormat.parse(c.getString("dataMsg")));      
       	          	   	        		mensagem.setTextoMsg(c.getString("textoMsg"));  
      	           	   	        	    mensagem.setStatusLida("N");
       	          	   	        		dh.insertMsg(Globais.usuario, mensagem);
       	                          }

       	                     }
       	                     
       	   	   		}

       	      	}catch(Exception ex){
       	      		ex.printStackTrace();
       	      	}	
   	        	
   	        }
	   	}).start();     	    		
    	carregaMensagem();
        mHandler.postDelayed(mUpdateTimeTask, (5 * 1000)) ;
    }
 };
 
	public void deletarMensagens(){    	

	   	Progresso("Aguarde");
	   	dh.deletaMensagens(Globais.usuario, Globais.contato);
	   	carregaMensagem();
	   	progressDialog.cancel();
      

		     	        
		
    }
 
 
	 
 
}
