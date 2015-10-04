package com.zetta.pedaja;


import android.support.v4.app.FragmentManager ;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;


public class TabsPagerAdapter extends FragmentPagerAdapter {
	Context context; 
	
	public TabsPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
        
    }

	@Override
	public Fragment getItem(int index) {
		   Fragment f = null; 
		   switch (index) {
		   	  
	          case 0:{
		       f =  new SuaContaFragment(context);
	            break;
	          }
	          case 1:{
		        	f =  new ContaGarcomFragment(context);
		            break;
		          }
	          case 2:{
		        	f =  new ContaTotalFragment(context);
		            break;
	          }
	        }
		   
		return f;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	

	
}
