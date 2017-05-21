package com.mfpad.wineproof;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.graphics.Color;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText editText;
	WebView wv;
	ProgressDialog pd;
	Handler handler;
	OnClickListener listener;
	Button btn;
	MediaPlayer mp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		 
	        
		//findViewById(R.id.layout).setBackgroundColor(Color.MAGENTA); //改背景
		editText=(EditText)findViewById(R.id.editText1);
		Button button1=(Button)findViewById(R.id.button1);  // Check number
		Button button2=(Button)findViewById(R.id.button2);  // Web page go back
		Button button3=(Button)findViewById(R.id.button3);  // QRcode scan
		
		button1.setOnClickListener(listener);  
		button2.setOnClickListener(listener);  
		button3.setOnClickListener(listener);  
		
		
		button1.setOnClickListener(new OnClickListener()
		{
		public void onClick(View v)
		{
			
				 // 關閉軟鍵盤 
				InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);				
				 
				final AlertDialog alertDialog_lenErr = getAlertDialog("Number check","Please input 12 numbers!");
				final AlertDialog alertDialog_numErr = getAlertDialog("Number error","Please re-check your number!");
				
				Boolean pass_flag = false;
				
				String num = editText.getText().toString();
				// String num = "123456789045";  //測試用途
				 
				 if( num.length() ==12 )
				 {
					 int sum = 0;
					 
					 for( int i=0; i<10; i++)
					 {
						 String subStr = num.substring(i, i+1);
						 int intValue = Integer.valueOf(subStr);
						 sum += intValue;
					 }
					 
					 String chkstr = num.substring(10, 12);
					 int    chkint = Integer.valueOf(chkstr);
	 
					if( sum == chkint )
						    pass_flag = true; 		
					else	alertDialog_numErr.show();   //檢查碼錯誤
					
					
				 }
				 else
					 alertDialog_lenErr.show();  // 長度錯誤
				 
				 if( pass_flag )
				 {
				    init();
				    loadurl(wv,"http://www.evinite.fr/chateau-smith-haut-lafite-2005");
				 }
				 
				
		}}
		);
		button2.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				
				wv.goBack();   
			}
		});
		
		button3.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				
					//連結ZXING的API
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");	//開啟條碼掃描器
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");	//設定QR Code參數
				startActivityForResult(intent, 1);
			}
		});
		
		 
		
	}
	//----------------------------------------------------------
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
 
		if (requestCode == 1) {	//startActivityForResult回傳值
			if (resultCode == RESULT_OK) {
				String contents = data.getStringExtra("SCAN_RESULT");	//取得QR Code內容
				//txt_hello.setText(contents);
				new Thread(){
		            public void run(){
		            	 mp = MediaPlayer.create( MainActivity.this, R.raw.d001); 
		            	 mp.start();   //播放聲音
		           
		        }
			  }.start();
			  
				init();
				loadurl(wv,contents);
			}
		}
	}
	//----------------------------------------------------------         
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	//----------------------------------------------------------
	public void init(){//初始化
    	wv=(WebView)findViewById(R.id.webView1);
        wv.getSettings().setJavaScriptEnabled(true);//可用JS
        wv.setScrollBarStyle(0);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
      
        wv.setWebViewClient(new WebViewClient(){   
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            	loadurl(view,url);//载入网页
                return true;   
            }//重写点击动作,用webview载入
        });
        
        wv.setWebViewClient(new WebViewClient(){
        	public void onProgressChanged(WebView view,int progress){//载入进度改变而触发 
             	if(progress==100){
            		handler.sendEmptyMessage(1);//如果全部载入,隐藏进度对话框
            	}   
             	this.onProgressChanged(view, progress);
               // super.onProgressChanged(view, progress);   
            }   
        });
 
    	pd=new ProgressDialog(MainActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("数据载入中，请稍候！");
    }
	
      //----------------------------------------------------------
        public void loadurl(final WebView view,final String url){
        	new Thread(){
            	public void run(){
            //		handler.sendEmptyMessage(0);
            		view.loadUrl(url);//载入网页
            	}
            }.start();
        }
     //----------------------------------------------------------
        private AlertDialog getAlertDialog(String title,String message)
        {
       
          Builder builder = new AlertDialog.Builder(MainActivity.this);
       
        builder.setTitle(title);
      
        builder.setMessage(message);
        
        builder.setPositiveButton(R.id.button1, new DialogInterface.OnClickListener() 	{
         @Override
         	public void onClick(DialogInterface dialog, int which) {
        
        		//Toast.makeText(MainActivity.this, "您按下OK按鈕", Toast.LENGTH_SHORT).show();
         } });

        //利用Builder物件建立AlertDialog
        		return builder.create();
        	}   
      //----------------------------------------------------------
         
     
        
}
