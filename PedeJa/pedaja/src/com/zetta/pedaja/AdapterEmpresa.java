package com.zetta.pedaja;

import java.io.File;
import java.util.List;

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


public class AdapterEmpresa extends ArrayAdapter<ModelEmpresa>{
 	Context context; 
    int layoutResourceId;    
    List<ModelEmpresa> empresaList = null;
    
    public AdapterEmpresa(Context context, int layoutResourceId,  List<ModelEmpresa> empresaList) {
    	super(context, layoutResourceId, empresaList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.empresaList = empresaList; 
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        try {
	        EmpresaHolder holder = null;
	        
	        if(row == null)
	        {
	        	LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new EmpresaHolder();
	            holder.txtEstEndereco = (TextView)row.findViewById(R.id.txtEstEndereco);
	            holder.txtEstNome = (TextView)row.findViewById(R.id.txtEstNome);
	            holder.imgEstabelecimento = (ImageView)row.findViewById(R.id.imgEstabelecimento);
	            holder.txtIdEstabelecimento = (TextView)row.findViewById(R.id.txtIdEstabelecimento);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (EmpresaHolder)row.getTag();
	        }
	        
	        ModelEmpresa empresa =   empresaList.get(position);
	        holder.txtEstNome.setText(empresa.getNomeFantasia());
	        holder.txtIdEstabelecimento.setText("" + empresa.getEmpresaId());
	
	        String Endereco = empresa.getLogradouro();
	        
	        if(!empresa.getNumero().equals(""))
	        	Endereco = Endereco + ", " + empresa.getNumero();
	        
	        if(!empresa.getComplemento().trim().equals(""))
	        	Endereco = Endereco + " - " + empresa.getComplemento();
	        
	        if(!empresa.getBairro().trim().equals(""))
	        	Endereco = Endereco + " - " + empresa.getBairro();
	        
	        holder.txtEstEndereco.setText(Endereco);
	        
	        try{
	        	if(!empresa.getTipoImagem().equals("") && empresa.getTipoImagem() != null){
		        	String url = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + empresa.getCaminhoImagem() + empresa.getNomeImagem();
			        File f = new File(url);
			        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
			        bitmap = Bitmap.createBitmap(bitmap);			       
			        holder.imgEstabelecimento.setImageBitmap(bitmap);
			        
			       
	        	}
	        	else
	        	{ 	
	        		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.estabelecimento);
	        		int height = (bitmap.getWidth()*holder.imgEstabelecimento.getHeight())/holder.imgEstabelecimento.getWidth();			        
			        Bitmap pq = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),height, true);
			        holder.imgEstabelecimento.setImageBitmap(pq);
	        	}
	        	
	        } catch(Exception ex){
	        	ex.printStackTrace();
	        	Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.estabelecimento);
        		int height = (bitmap.getWidth()*holder.imgEstabelecimento.getHeight())/holder.imgEstabelecimento.getWidth();			        
		        Bitmap pq = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),height, true);
		        holder.imgEstabelecimento.setImageBitmap(pq);
	        }
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        return row;
    }
    	    
    static class EmpresaHolder
    {
        ImageView imgEstabelecimento;
        TextView txtEstNome;
        TextView txtEstEndereco;
        TextView txtIdEstabelecimento;
    }
}
