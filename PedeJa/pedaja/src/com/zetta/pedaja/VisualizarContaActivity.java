package com.zetta.pedaja;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.widget.EditText;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

@SuppressLint("ValidFragment")
public class VisualizarContaActivity extends FragmentActivity implements ActionBar.TabListener  {

	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    
    private String[] tabs = { "Sua", "Garçom", "Total"};
    
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
       //Não tirar - acerta erro ao rotacionar a tela
		if (savedInstanceState != null)
	    {
	         savedInstanceState.remove ("android:support:fragments");
	    } 

	    super.onCreate (savedInstanceState);
        setContentView(R.layout.visualizar_conta);
        
       
 
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        
        
        
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), getApplicationContext());
 
        viewPager.setAdapter(mAdapter);
      //  actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
 
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
 
    }
 
    
	
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		viewPager.setCurrentItem(tab.getPosition());
		
	}

	
	 @Override 
	    public void onBackPressed(){
		 	if(Global.encerrarConta)
		 	{
		 		Global.encerrarConta  = false;
		 		Global.conta = null;
		 	}
		 
			Intent intent =   new Intent(getApplicationContext(), HomeActivity.class);			 
	   		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
	    }



	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
         
		
	}



	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	public static class DigitarDivisaoFragment extends DialogFragment {
		static double total;
		static Context mn;
	    public static DigitarDivisaoFragment newInstance(double t) {
	    	total = t;
	    	DigitarDivisaoFragment frag = new DigitarDivisaoFragment();	    	
	        return frag;
	    }

	    public Dialog onCreateDialog(Bundle savedInstanceState) {

	    		 final EditText input = new EditText(getActivity());
			    //input.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
			    input.setInputType(InputType.TYPE_CLASS_NUMBER);
			    input.setText("2");
			    input.setSelection(input.getText().length());	    	
			    AlertDialog.Builder  alert = new AlertDialog.Builder(getActivity());
			    alert.setView(input); 
			    alert.setTitle("Dividr por quantas pessoas?");
			    alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			        	int qtdProdutos = 0;
			        	String msg = "";			        	
			        	try {
				        	if(input.getText().toString().equals("")){
				        		msg = "Digite o número de pessoas.";			        		
				        	} else {
				        	
					        	qtdProdutos = Integer.parseInt(input.getText().toString().trim());			        	
					        	if(qtdProdutos <= 0)
					        		msg = "Número de pessoas deve ser superior a 0.";
					        	else{			        		
					        		msg  = "Cada pessoa deve pagar: " + String.format("\nR$ %.2f", total/qtdProdutos);			        					        		
					        	}
				        	}
			        	} catch (Exception ex){
			        		ex.printStackTrace();
			        	}
			        	
			        	DialogFragment newFragment = ExibeMsgFragment.newInstance(msg);
					    newFragment.show(getFragmentManager(), "alert");
			            
			        }
			    });

			    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            dialog.cancel();
			        }
			    });

	        return alert.create();
	        
	    }
	}
	
	public static class ExibeMsgFragment extends DialogFragment {
		static String msg;
	    public static ExibeMsgFragment newInstance(String m) {
	    	msg = m;
	    	ExibeMsgFragment frag = new ExibeMsgFragment();
	        return frag;
	    }

	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        return new AlertDialog.Builder(getActivity()).setTitle("Aviso")
	                .setMessage(msg).create();
	    }
	}

}
