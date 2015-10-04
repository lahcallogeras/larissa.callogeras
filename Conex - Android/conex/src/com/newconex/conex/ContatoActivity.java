package com.newconex.conex;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.newconex.conex.R;

 
public class ContatoActivity extends Activity  {
	
    private ProgressDialog progressDialog;
    Context context = this;
    Handler handler; 
    byte[] encodeByte; 
    ImageView image;
    private ListView lstContato;
    ContatoAdapter adapter;    
    DataHelper dh;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contatos);
		lstContato = (ListView)findViewById(R.id.lstContato);	  
        dh = new DataHelper(context);
        lstContato.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {				
					try{
						Globais.contato = dh.selectContato(Integer.parseInt((String)((TextView)view.findViewById(R.id.txtIdContato)).getText()));
						
						Intent i = new Intent(ContatoActivity.this,MensagemActivity.class); 
						startActivity(i);
						
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
			}  
        });		
        
        // timer
        mHandlerContato.removeCallbacks(mUpdateTimeTaskContato);
        mHandlerContato.postDelayed(mUpdateTimeTaskContato, 100);   
        
       
            
    }
	
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try
        {      	        	
        	menu.add(0,0,0,"Gerenciar Contatos").setIcon(R.drawable.contato); 
        	menu.add(1,1,1,"Sair").setIcon(R.drawable.desconectar);            
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
    	    	  Intent viewIntent = new Intent("android.intent.action.VIEW", 
						  Uri.parse("http://www.newconex.heliohost.org/contatos.php"));
				  startActivity(viewIntent);			
    	      }case 1:{
    	    	  this.finish();
    	      }
    	         
    	   }
    	   return true;
    	}
    
    @Override 
    public void onBackPressed(){
  	  	Globais.usuario.setManterConUsu("N");
  	  	dh.atualizaUsuario(Globais.usuario);
    	super.onBackPressed();    	
    }
    
    @Override
    public void onDestroy(){
        super.onDestroy();
        mHandlerContato.removeCallbacks(mUpdateTimeTaskContato);
    }
    
    
    @Override 
    public void onResume(){
    	super.onResume();
    	carregaContatos();
    }
    
    public void Progresso(String titulo){
        progressDialog = new ProgressDialog(ContatoActivity.this);
        progressDialog.setTitle(titulo);
        progressDialog.setMessage("Processando...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
   }
    
    public void carregaContatos(){
    	
    	//Progresso("Aguarde");
        List<Contato> stateList = dh.selectContatos(Globais.usuario);	   	               
        adapter = new ContatoAdapter(context, R.layout.lst_row_contato, stateList);		 	          	   	         
	   	lstContato.setAdapter((ListAdapter) adapter);
	   	//progressDialog.cancel(); 
	   	
	    
        
    }
    

	@SuppressWarnings("deprecation")
	public void criarNotificacao(Context context, MensagemAlerta messagesAlerts, ContatoActivity activity) {
		
		// Recupera o serviço do NotificationManager	
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);	
		Notification notificaction = new Notification(R.drawable.ic_launcher, messagesAlerts.getTitle(), System.currentTimeMillis());
		notificaction.sound = Uri.parse("file:///sdcard/Notifications/eu ");
	
		// Flag que vibra e emite um sinal sonoro até o usuário clicar na notificação
		notificaction.flags |= Notification.FLAG_INSISTENT;
		// Flag utilizada para remover a notificação da toolbar quando usuário tiver clicado nela.
		notificaction.flags |= Notification.FLAG_AUTO_CANCEL;
		// PendingIntent para executar a Activity se o usuário selecionar a notificão
		PendingIntent p;
		if(Globais.contato.isNotificacao()){
			p = PendingIntent.getActivity(this, 0, new Intent(this.getApplicationContext(), MensagemActivity.class), 0);		
		} else{
			p = PendingIntent.getActivity(this, 0, new Intent(this.getApplicationContext(), ContatoActivity.class), 0);
		}
		
		// Informações
		notificaction.setLatestEventInfo(this, messagesAlerts.getSubTitle(),messagesAlerts.getBody(), p);	
		// espera 100ms e vibra por 1000ms, depois espera por 1000 ms e vibra por 1000ms.	
		notificaction.vibrate = new long[] { 100, 1000, 1000, 1000 };	
		// id que identifica esta notificação	
		notificationManager.notify(R.string.app_name, notificaction); 
	
	}


    private Handler mHandlerContato = new Handler();
    private Runnable mUpdateTimeTaskContato = new Runnable() 
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
          	   	   			String httpGet = "http://newconex.heliohost.org/recebeTodasMsgA.php";
          	   	   			//Parametros a serem passados para o PHP
          		              List<NameValuePair> params = new ArrayList<NameValuePair>();
          		              params.add(new BasicNameValuePair("P1", "" + Globais.usuario.getCodigoUsu()));
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
          	          	   	        		
          	          	   	        		boolean notificacao = false;
          	          	   	        		if(Globais.contato != null )
          	          	   	        		{
          	          	   	        			if(Globais.contato.getCodigoUsuCtt() != mensagem.getUsuOrig().getCodigoUsuCtt() || (Globais.contato.isNotificacao() || Globais.contato.getCodigoUsuCtt() == mensagem.getUsuOrig().getCodigoUsuCtt()))
          	          	   	        				notificacao = true;
          	          	   	        		}
          	          	   	        		else
          	          	   	        			notificacao = true;
          	          	   	        		
          	          	   	        		if(notificacao){
          	          	   	        			MensagemAlerta msgAlerta = new MensagemAlerta(mensagem.getUsuOrig().getNomeUsuCtt() + "diz:", mensagem.getTextoMsg(), mensagem.getUsuOrig().getNomeUsuCtt() + " diz:");
	          	          	   	        		criarNotificacao(getApplicationContext(), msgAlerta, ContatoActivity.this);
          	          	   	        		}
          	          	   	        		

          	                          }

          	                     }
          	                     
          	   	   		}

          	      	}catch(Exception ex){
          	      		ex.printStackTrace();
          	      	}	
      	        	
      	        }
   	   	}).start();     
    		
         carregaContatos();
         mHandlerContato.postDelayed(mUpdateTimeTaskContato, (300 * 1000)) ;
       }
    };
    
    
    
    
}
