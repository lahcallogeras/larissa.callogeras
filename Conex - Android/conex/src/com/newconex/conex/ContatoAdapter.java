package com.newconex.conex;

import java.io.File;
import java.util.List;

import com.newconex.conex.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContatoAdapter extends ArrayAdapter<Contato>{
	 	Context context; 
	    int layoutResourceId;    
	    List<Contato> contatoList = null;
	    
	    public ContatoAdapter(Context context, int layoutResourceId,  List<Contato> contatoList) {
	    	super(context, layoutResourceId, contatoList);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.contatoList = contatoList; 
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        ContatoHolder holder = null;
	        
	        if(row == null)
	        {
	        	LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new ContatoHolder();
	            holder.txtNomeContato = (TextView)row.findViewById(R.id.txtNomeCont);
	            holder.txtIdContato = (TextView)row.findViewById(R.id.txtIdContato);
	            holder.imgFoto = (ImageView)row.findViewById(R.id.imgContato);
	            holder.txtQtdMsg = (TextView)row.findViewById(R.id.txtQtdMsg);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (ContatoHolder)row.getTag();
	        }
	        
	        Contato contato =   contatoList.get(position);
	        holder.txtNomeContato.setText(contato.getNomeUsuCtt());
	        holder.txtIdContato.setText("" + contato.getCodigoUsuCtt());
	        
	        if(contato.getQtdMsg() > 0){
	        	holder.txtQtdMsg.setVisibility(View.VISIBLE);	        	
	        	if(contato.getQtdMsg() == 1)
	    	        holder.txtQtdMsg.setText("1 Nova Mensagem");
	        	else
	    	        holder.txtQtdMsg.setText(contato.getQtdMsg() + " Novas Mensagens");
	        	
	        }
	        else
	        	holder.txtQtdMsg.setVisibility(View.GONE);
	        
	        try{
	        	if(!contato.getNomeFotoCtt().equals("")){
		        	String url = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + contato.getNomeFotoCtt();
			        File f = new File(url);
			        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
			        bitmap = Bitmap.createBitmap(bitmap);
			        holder.imgFoto.setImageBitmap(bitmap);
	        	}
	        	else
	        	{
			        holder.imgFoto.setImageResource(R.drawable.anonimo);
	        	}
	        } catch(Exception ex){
	        	ex.printStackTrace();
			    holder.imgFoto.setImageResource(R.drawable.anonimo);
	        }
	        
	        
	        return row;
	    }
	    	    
	    static class ContatoHolder
	    {
	        ImageView imgFoto;
	        TextView txtNomeContato;
	        TextView txtIdContato;
	        TextView txtQtdMsg;
	    }
}




