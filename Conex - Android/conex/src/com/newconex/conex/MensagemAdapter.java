package com.newconex.conex;

import java.io.File;
import java.text.SimpleDateFormat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MensagemAdapter extends ArrayAdapter<Mensagem>{
	 	Context context; 
	    int layoutResourceId;    
	    List<Mensagem> msgList = null;
	    
	    public MensagemAdapter(Context context, int layoutResourceId,  List<Mensagem> msgList) {
	    	super(context, layoutResourceId, msgList);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.msgList = msgList; 
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        MensagemHolder holder = null;
	        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy hh:mm"); 
	        
	        if(row == null)
	        {
	        	LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new MensagemHolder();
	            holder.txtMsgCont = (TextView)row.findViewById(R.id.txtMsgCont);
	            holder.txtMsgDataCont = (TextView)row.findViewById(R.id.txtMsgDataCont);
	            holder.imgCont = (ImageView)row.findViewById(R.id.imgMsgCont);
	            holder.txtMsgUsu = (TextView)row.findViewById(R.id.txtMsgUsu);
	            holder.txtMsgDataUsu = (TextView)row.findViewById(R.id.txtMsgDataUsu);
	            holder.imgUsu = (ImageView)row.findViewById(R.id.imgMsgUsu);
	            holder.relContato = (RelativeLayout)row.findViewById(R.id.relContato);
	            holder.relUsu = (RelativeLayout)row.findViewById(R.id.relUsu);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (MensagemHolder)row.getTag();
	        }
	        
	        Mensagem msg =   msgList.get(position);
	       
	        if(msg.getUsuOrig() != null){

	           holder.relUsu.setVisibility(View.GONE);
	           holder.relContato.setVisibility(View.VISIBLE);
	           
	           
		       holder.txtMsgCont.setText(msg.getTextoMsg());		        
		       holder.txtMsgDataCont.setText(formatador.format(msg.getDataMsg()));  
		       
	        	try{
	   	        	if(!msg.getUsuOrig().getNomeFotoCtt().equals("")){
	   		        	String url = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + msg.getUsuOrig().getNomeFotoCtt();
	   			        File f = new File(url);
	   			        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
	   			        bitmap = Bitmap.createBitmap(bitmap);
	   			        holder.imgCont.setImageBitmap(bitmap);
	   	        	}
	   	        	else
	   	        	{
	   			        holder.imgCont.setImageResource(R.drawable.anonimo);
	   	        	}
	   	        } catch(Exception ex){
	   	        	ex.printStackTrace();
	   	        }
	        }
	        else{
		        holder.relUsu.setVisibility(View.VISIBLE);
		        holder.relContato.setVisibility(View.GONE);
	        	holder.txtMsgUsu.setText(msg.getTextoMsg());
			    holder.txtMsgDataUsu.setText(formatador.format(msg.getDataMsg()));  
	        	
	        	
	        	try{
	   	        	if(!Globais.usuario.getNomeFotoUsu().equals("")){
	   		        	String url = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + Globais.usuario.getNomeFotoUsu();
	   			        File f = new File(url);
	   			        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
	   			        bitmap = Bitmap.createBitmap(bitmap);
	   			        holder.imgUsu.setImageBitmap(bitmap);
	   	        	}
	   	        	else
	   	        	{
	   			        holder.imgUsu.setImageResource(R.drawable.anonimo);
	   	        	}
	   	        } catch(Exception ex){
	   	        	ex.printStackTrace();
   			        holder.imgUsu.setImageResource(R.drawable.anonimo);
	   	        }
	        	
	        	
	        	
	        	
	        }
	     
	       
	        
	        return row;
	    }
	    static class MensagemHolder
	    {
	        ImageView imgCont;
	        TextView txtMsgCont;
	        TextView txtMsgDataCont;
	        ImageView imgUsu;
	        TextView txtMsgUsu;
	        TextView txtMsgDataUsu;
	        RelativeLayout relUsu;
	        RelativeLayout relContato;
	        
	    }
}




