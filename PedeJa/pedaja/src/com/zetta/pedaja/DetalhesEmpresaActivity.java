package com.zetta.pedaja;

import java.io.File;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DetalhesEmpresaActivity extends Activity {
	ImageView imgDetEst;
	TextView txtDetEstNome;
	TextView txtDetEndereco;
	ListView lstTelefoneEmpresa;
	ArrayList<ModelTelefoneEmpresa>  lstTelefone;
	Context context = this;
	int _posicao; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalhes_empresa);
		imgDetEst  = (ImageView)findViewById(R.id.imgDetEst); 
		txtDetEstNome  = (TextView)findViewById(R.id.txtDetEstNome); 
		txtDetEndereco  = (TextView)findViewById(R.id.txtDetEndereco); 
		lstTelefoneEmpresa  = (ListView)findViewById(R.id.lstTelefoneEmpresa); 
		carregaTela();
		
		DALEmpresa dbEmpresa = new DALEmpresa(context);
		lstTelefone =  dbEmpresa.selecionarTelefone(Global.empresa.getEmpresaId());
		AdapterTelefoneEmpresa adapter = new AdapterTelefoneEmpresa(context, R.layout.item_telefone, lstTelefone);		 	          	   	         
		lstTelefoneEmpresa.setAdapter((ListAdapter) adapter);
		lstTelefoneEmpresa.setOnItemLongClickListener(lstOnItemLongClick);
		
		
		
		
	}

	
	private OnItemLongClickListener lstOnItemLongClick = new OnItemLongClickListener() {

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int posicao, long arg3) {
			_posicao = posicao;
			registerForContextMenu(lstTelefoneEmpresa);
			return false;
		}

				
		
	};
	
	private void carregaTela(){
		Bitmap bitmap ; 
		try{
			 
			txtDetEstNome.setText(Global.empresa.getNomeFantasia());
			String Endereco = Global.empresa.getLogradouro();
	        
	        if(!Global.empresa.getNumero().equals(""))
	        	Endereco = Endereco + ", " + Global.empresa.getNumero();
	        
	        if(!Global.empresa.getComplemento().trim().equals(""))
	        	Endereco = Endereco + " - " + Global.empresa.getComplemento();
	        
	        if(!Global.empresa.getBairro().trim().equals(""))
	        	Endereco = Endereco + " - " + Global.empresa.getBairro();
	        
	        txtDetEndereco.setText(Endereco);
			 
			 
	          if(!Global.empresa.getTipoImagem().equals("") && Global.empresa.getTipoImagem() != null){
		        	String url = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + Global.empresa.getCaminhoImagem()  + Global.empresa.getNomeImagem();
			        File f = new File(url);
			        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
			        bitmap = Bitmap.createBitmap(bitmap);
			        
	        	}
	        	else
	        	{ 	
	        		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.estabelecimento);
	        		
	        	}
	        	
	        } catch(Exception ex){
	        	ex.printStackTrace();
	        	 bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.estabelecimento);
    		
	        }
		 
		  int heightImg =  70;
		  int widthImg =  120;
		  int height = (bitmap.getWidth() * heightImg)/widthImg;			        
	      Bitmap pq = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),height, true);
	      imgDetEst.setImageBitmap(pq);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.detalhes_empresa, menu);
		return true;
	}

   
   @Override
   public void onCreateContextMenu(ContextMenu menu, View v,  ContextMenuInfo menuInfo) {
     if (v.getId() == R.id.lstTelefoneEmpresa) {
       
    	 
    	 menu.setHeaderTitle("Ligar");
    	 String[] menuItens = getResources().getStringArray(R.array.opcoesChamada);
    	 for (int i = 0; i < menuItens.length; i++) {
    		 menu.add(Menu.NONE, i, i, menuItens[i]);
    	 }
     }
   }
   
   @Override
   public boolean onContextItemSelected(MenuItem item) {
    
     int menuItemIndex = item.getItemId();    
     if(menuItemIndex == 0) {
    	 Intent chamada = new Intent(Intent.ACTION_DIAL);
         chamada.setData(Uri.parse("tel:" + lstTelefone.get(_posicao).getDDD() + lstTelefone.get(_posicao).getTelefone()));
         startActivity(chamada);
     }
     return true;
   }
   
   @Override 
   public void onBackPressed(){
	   Intent intent = new Intent(getApplicationContext(), EstabelecimentoActivity.class); 
  		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
   }

}
