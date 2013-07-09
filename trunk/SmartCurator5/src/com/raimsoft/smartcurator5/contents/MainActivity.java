package com.raimsoft.smartcurator5.contents;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.raimsoft.smartcurator5.R;
import com.raimsoft.smartcurator5.loader.ParseContentsLoader;


public class MainActivity extends Activity 
{
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	
	private TextView TXT_tagDesc, TXT_1, TXT_2;
	private ImageView IMG_1, IMG_2;

	private void GetNFCTag()
	{
		Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tag == null)
			return;
		
		
		
		byte[] tagId = tag.getId();
		String strNFC_id= ParseContentsLoader.toHexString(tagId);	
		TXT_tagDesc.setText("TagID: " + strNFC_id );
	
		ParseObject recentObject = new ParseObject("NFC_List");
		recentObject.put("NFC_id", strNFC_id);
		recentObject.put("linked_user", "june");
		recentObject.saveInBackground();
				
		ParseContentsLoader.GetImage(strNFC_id, "File1", 1, IMG_1, TXT_1);
		ParseContentsLoader.GetImage(strNFC_id, "File2", 1, IMG_2, TXT_2);
	}

	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        TXT_tagDesc = (TextView)findViewById(R.id.txt_tag);
        IMG_1= (ImageView) findViewById(R.id.imageView1);
        IMG_2= (ImageView) findViewById(R.id.imageView2);
        TXT_1= (TextView) findViewById(R.id.textView1);
        TXT_2= (TextView) findViewById(R.id.textView2);
        
		Parse.initialize(this, "vh60sQDbtfnIlFxn5HrK6oBj5SN1rqYeqtixIngY", "23gt0kLNMnZJEPYYYxWXQFVZiRXMceKrU1Ge3kVz"); 
		ParseAnalytics.trackAppOpened(getIntent());
		
		
        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        
        GetNFCTag();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
