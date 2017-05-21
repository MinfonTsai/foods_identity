package com.mfpad.wineproof;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;


public class Dingdong extends Activity {

	
	MediaPlayer mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		 new Thread(){
	            public void run(){
	            	 mp = MediaPlayer.create( Dingdong.this, R.raw.d001); 
	            	 mp.start();
	           
	        }
		  }.start();
		  
		finish();
		 
	}
	
	 public String Exec_proof_main()
	 {
		 String packageName1 = "com.mfpad.wineproof";
    	 String className1 = ".PDcaptureActivity";
    	 PackageInfo pi=null;
    	 
    	 try {
			pi = getPackageManager().getPackageInfo( packageName1 , 0 );
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			pi=null;
		} // (packageName, 0);
    	 
    	 if( pi != null)
    	 {
    	Intent intent = new Intent( ); 
       	ComponentName cn = new ComponentName(packageName1, className1);
    	intent.setComponent(cn);
    	intent.setAction("android.intent.action.MAIN"); 
		//intent.setFlags( intent.FLAG_ACTIVITY_SINGLE_TOP | intent.FLAG_ACTIVITY_CLEAR_TOP );
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP );        
   	    intent.addCategory(Intent.CATEGORY_DEFAULT);   
   	    intent.putExtra( "Status" , "1" );
      	startActivity(intent); 
    	 }
		 return "";
	 }
	 
}