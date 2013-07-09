package com.raimsoft.smartcurator5.contents;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.raimsoft.smartcurator5.R;
import com.raimsoft.smartcurator5.loader.Global;
import com.raimsoft.smartcurator5.loader.ParseContentsLoader;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends Activity
{
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	
	ImageView IMG_1;
	ProgressBar PRG_splash;
	TextView TXT_1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.activity_splash);
		
		IMG_1= (ImageView) findViewById(R.id.img_splash);
		PRG_splash= (ProgressBar) findViewById(R.id.prg_splash);
		TXT_1=  (TextView) findViewById(R.id.txt_splash);
		
		Parse.initialize(this, "vh60sQDbtfnIlFxn5HrK6oBj5SN1rqYeqtixIngY", "23gt0kLNMnZJEPYYYxWXQFVZiRXMceKrU1Ge3kVz"); 
		ParseAnalytics.trackAppOpened(getIntent());
		        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
        GetNFCTag();
        
		super.onCreate(savedInstanceState);
	}
	
	private void GetNFCTag()
	{
		Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tag == null)
			return;
		
		byte[] tagId = tag.getId();
		Global.NFC_id= ParseContentsLoader.toHexString(tagId);	// 찍힌 NFC ID
	
		ParseObject recentObject = new ParseObject("NFC_List");
		recentObject.put("NFC_id", Global.NFC_id);
		recentObject.put("linked_user", "june");
		recentObject.saveInBackground();
				
		//ParseContentsLoader.GetImage(Global.NFC_id, "File_splash", 2, IMG_1, PRG_splash);
		ParseContentsLoader.GetImage(Global.NFC_id, "File_Splash", 2, IMG_1, TXT_1);
	}
	

	@Override
	protected void onNewIntent(Intent intent) 
	{
		super.onNewIntent(intent);
		
		GetNFCTag();
	}
	
	

	
	@Override
	protected void onPause()
	{
		if (nfcAdapter != null)
		{
			nfcAdapter.disableForegroundDispatch(this);
		}
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (nfcAdapter != null)
		{
			nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
		}
	}

}

