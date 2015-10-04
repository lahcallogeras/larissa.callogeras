package com.zetta.pedaja;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DestaqueActivity extends Activity {
	Context context = this;
	Button btnProximo;
	Button btnAnterior;
	Button btnDestaquePedir;
	Button btnDestaqueCarpadio;
	TextView txtDestaque;
	ImageView imgDestaque;
    private Handler handler;
	private ProgressDialog progressDialog;
	Bitmap bitmap; 
	List<ModelDestaque> lstDestaque;
    int widthImgDest = 0;
    int heightImgDest = 0;
	float downXValue = 0;
	float downYValue = 0; 
	int i = 0;
	int qtdProdutos = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.destaque);
		btnProximo  = (Button)findViewById(R.id.btnProximo); 
		btnAnterior  = (Button)findViewById(R.id.btnAnterior); 
		btnDestaquePedir = (Button)findViewById(R.id.btnDestaquePedir); 
		btnDestaqueCarpadio = (Button)findViewById(R.id.btnDestaqueCarpadio); 
		imgDestaque = (ImageView)findViewById(R.id.imgDestaque); 
		txtDestaque = (TextView)findViewById(R.id.txtDestaque); 
		
		btnProximo.setText(">>");
		btnAnterior.setText("<<");

		btnProximo.setOnClickListener(btnProximoOnClick);
		btnAnterior.setOnClickListener(btnAnteriorOnClick);
		btnDestaqueCarpadio.setOnClickListener(btnDestaqueCarpadioOnClick);
		btnDestaquePedir.setOnClickListener(btnDestaquePedirClick);
		imgDestaque.getViewTreeObserver().addOnGlobalLayoutListener(imgDestOnGlobal);
		
		
		if (Global.conta != null  && Global.conta .getEmpresaId() == Global.empresa.getEmpresaId()) 
			btnDestaquePedir.setVisibility(View.VISIBLE);			
		else
			btnDestaquePedir.setVisibility(View.INVISIBLE);
		
		
		atualizaVisuDestaque();		
		carregarDestaque();
		
		
		
		imgDestaque.setOnTouchListener(imgDestOnTouch);
		
	}
	
	
	private OnClickListener btnDestaquePedirClick = new OnClickListener() {			
		public void onClick(View v) {
					
			final AlertDialog.Builder alert = new AlertDialog.Builder(context);
		    final EditText input = new EditText(context);
		    //input.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
		    input.setInputType(InputType.TYPE_CLASS_NUMBER);
		    input.setText("1");
		    input.setSelection(input.getText().length());
		    alert.setView(input);
		    alert.setTitle("Digite a quantidade que será pedida desse destaque.");
		    alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		        	qtdProdutos = Integer.parseInt(input.getText().toString().trim());
		        	inserePedido();
		            
		        }
		    });

		    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            dialog.cancel();
		        }
		    });
		    alert.show();
		    
		}
	};
	
	

	private OnTouchListener imgDestOnTouch= new OnTouchListener() {

		public boolean onTouch(View arg0, MotionEvent event) {
			 switch (event.getAction())
		        {
		        	
		            case MotionEvent.ACTION_DOWN:
		            {       
		            	downXValue = event.getX();
		            	downYValue  = event.getY(); 
		                break; 
		            }
		            case MotionEvent.ACTION_UP: {
		            	
		            	if(lstDestaque.size() == 1)
		            		return true;
		            	
	                    // Get the X value when the user released his/her finger
	                    float currentX = event.getX();
	                    float currentY = event.getY();
	                    // check if horizontal or vertical movement was bigger

	                    if (Math.abs(downXValue - currentX) > Math.abs(downYValue - currentY)) {
	                      
	                        // going backwards: pushing stuff to the right
	                        if (downXValue < currentX) {
	                        	avancar();

	                        }

	                        // going forwards: pushing stuff to the left
	                        if (downXValue > currentX) {
	                        	recuar();

	                        }

	                    } 
	                    break;
	                }
		        }
		        return true;
		}	
		
		
	};
	
	

	@SuppressLint("SimpleDateFormat")
	private void atualizaVisuDestaque(){
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");           	
			DALDestaque db  = new DALDestaque(context);
			ModelVisualizacaoDestaque visualizacaoDestaque = new ModelVisualizacaoDestaque();	
			visualizacaoDestaque.setEmpresaId(Global.empresa.getEmpresaId());
			visualizacaoDestaque.setUsuarioId(Global.usuario.getUsuarioId());			
			Date data = new Date();                                        
			visualizacaoDestaque.setDataVisu(dateFormat.parse(dateFormat.format(data.getTime())));  
			db.inserirVisualizacaoDestaque(visualizacaoDestaque);
			db = null;			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	private OnGlobalLayoutListener imgDestOnGlobal= new OnGlobalLayoutListener() {			
		public void onGlobalLayout() {
			
				try {
			       widthImgDest = imgDestaque.getWidth();
			       heightImgDest = imgDestaque.getHeight();
			       
			       if(widthImgDest > 0 && heightImgDest > 0)
			    	   redimensionarImagem();
				} catch (Exception ex){
					ex.printStackTrace();
				}
		    }
	};	
	
	
	private OnClickListener btnDestaqueCarpadioOnClick = new OnClickListener() {			
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), CardapioActivity.class); 
	   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};
	
	private void avancar(){
		if(i < lstDestaque.size() - 1) 	{
			i++;
			carregaImagemDestaque();
		}
		
		if(i == lstDestaque.size() - 1)
			btnProximo.setVisibility(View.INVISIBLE);

		btnAnterior.setVisibility(View.VISIBLE);	
	}
	
	private OnClickListener btnProximoOnClick = new OnClickListener() {			
		public void onClick(View v) {
			avancar();
			
		}
	};	
	
	private void recuar(){
		if(i > 0) 	{
			i--;
			carregaImagemDestaque();
		}
		
		if(i == 0)
			btnAnterior.setVisibility(View.INVISIBLE);	
		

		btnProximo.setVisibility(View.VISIBLE);		
	}
	
	private OnClickListener btnAnteriorOnClick = new OnClickListener() {			
		public void onClick(View v) {
			recuar();
		}
	};
	
	

	private void carregarDestaque(){
		try {
			btnAnterior.setVisibility(View.INVISIBLE);
			DALDestaque dbDestaque = new DALDestaque(context);
			
			lstDestaque  = dbDestaque.selecionaDestaques(Global.empresa.getEmpresaId());
			
			if(lstDestaque.size()  <= 0){
				btnDestaquePedir.setVisibility(View.INVISIBLE);
				btnDestaqueCarpadio.setVisibility(View.INVISIBLE);
				mostrarMensagem("Sem destaques no momento.", "Aviso");
			}
			else 
				carregaImagemDestaque();
			
			if(lstDestaque.size() == 1 || lstDestaque.size()  <= 0)
				btnProximo.setVisibility(View.INVISIBLE);
			
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	private void carregaImagemDestaque(){
		
		try {
			
			txtDestaque.setText(lstDestaque.get(i).getDescricao());			
			String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + Global.empresa.getCaminhoDestaqueImagem() + lstDestaque.get(i).getNomeImagem();
	        File f = new File(dir);
	        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
	        bitmap = Bitmap.createBitmap(bitmap);		       
	        if(widthImgDest != 0)
	        	redimensionarImagem();
		      
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	private void redimensionarImagem(){
		int heightImg =  heightImgDest;
		int widthImg =  widthImgDest;
		int height = (bitmap.getWidth() * heightImg)/widthImg;			        
	    Bitmap pq = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),height, true);
	    imgDestaque.setImageBitmap(pq);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.destaque, menu);
		return true;
	}
	
	 @Override 
    public void onBackPressed(){
		 if(Global.visuDestaqueCarpadio){
			 Intent intent = new Intent(getApplicationContext(), CardapioActivity.class);
			 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		     startActivity(intent);
		 }
		 else
		 {
			finish();
		 }
   		
    }
    
	@SuppressLint("NewApi")
	private void mostrarMensagem(String msg, String titulo) {
		new AlertDialog.Builder(this)
				.setTitle(titulo)
				.setMessage(msg)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setCancelable(isRestricted())
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent intent = new Intent(getApplicationContext(), HomeActivity.class); 
				   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
				}
			}).show();
		
		
		
	
	}
	
	
	@SuppressLint("HandlerLeak")
	private void inserePedido(){
		try {
			 Progresso("Aguarde");
		       	 new Thread(new Runnable() {
	       	        public void run() {        	        	
	       	        	  try {

	       	        		JSONParser jsonParser = new JSONParser();   
	       	        		Message msg = new Message();
	       	        		boolean conexao = jsonParser.conectado(context);
	       	        		
	       	        		if(conexao){     

	       	        			DALDestaque   dbDestaque = new DALDestaque(context);	
	       	        			List<ModelItemDestaque>  lstItemDestaque = dbDestaque.selecionaProdutoDestaque(lstDestaque.get(i).getDestaqueId());
	       	        			for(int j = 0; j < qtdProdutos; j++){		       	        				       	        		
		       	        			int sequencia = 0;		
		       	        			for(int k = 0; k < lstItemDestaque.size(); k++){	       	        				
		       	        				int[] retorno = Global.inserePedido(context, lstItemDestaque.get(k).getTipoProdutoId(), lstItemDestaque.get(k).getValorProduto(), sequencia, "N");
		       	        				msg.what  = retorno[0];
		       	        				sequencia = retorno[1];
		       	        				Global.visualizarPedido(context, sequencia);
		       	        				if(msg.what == 0){
		       	        					break;
		       	        				}
		       	        			}	       	        	
	       	        			
	       	        			}		
	       	        			dbDestaque = null;	  
	       	        		}
	       	        		else 
	       	        			msg.what = 2;
	       	        		

                         handler.sendMessage(msg);
	       	        	  }
       	        	  catch (Exception e) {
                          e.printStackTrace();
                      }        	              			              	 	
	  	        }
	  	    }).start();
	 	  
		    handler=new Handler(){
		  		 public void handleMessage(Message msg){
		
		  			 progressDialog.cancel();
		  			 if(msg.what==1)
		  			 {
		  				Toast.makeText(DestaqueActivity.this, "Pedido feito com sucesso!", Toast.LENGTH_LONG).show();		  				
		  		    	Intent intent = new Intent(getApplicationContext(), CardapioActivity.class); 
		  		   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  				startActivity(intent);
		  		 	 }
		  			 else  if(msg.what == 0)
		  				Toast.makeText(DestaqueActivity.this, "Erro ao inserir pedido.", Toast.LENGTH_LONG).show();
		  			else  if(msg.what == 3)
		  				Toast.makeText(DestaqueActivity.this, "Já foi solicitado o fechamento dessa conta. Se a conta encontra-se aberta em seu aparelho, você deve fechá-la.", Toast.LENGTH_LONG).show();
		  			 else  if(msg.what == 2)
		  				Toast.makeText(DestaqueActivity.this, "Sem Conexão com a internet! Impossivel realizar pedido..", Toast.LENGTH_LONG).show();
		  		 }
		  	};
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	
	public void Progresso(String titulo){
        progressDialog = new ProgressDialog(DestaqueActivity.this);
        progressDialog.setTitle(titulo);
        progressDialog.setMessage("Processando...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
   }
	    

}
