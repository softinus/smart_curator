package com.raimsoft.smartcurator5;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;


public class MainActivity extends Activity 
{
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	
	private TextView TXT_tagDesc;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        TXT_tagDesc = (TextView)findViewById(R.id.txt_tag);
        
		Parse.initialize(this, "vh60sQDbtfnIlFxn5HrK6oBj5SN1rqYeqtixIngY", "23gt0kLNMnZJEPYYYxWXQFVZiRXMceKrU1Ge3kVz"); 
		ParseAnalytics.trackAppOpened(getIntent());
		
		
        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        
        Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tag != null) 
		{
			byte[] tagId = tag.getId();
			TXT_tagDesc.setText("TagID: " + toHexString(tagId));
		
			ParseObject testObject = new ParseObject("NFC_List");
			testObject.put("NFC_id", toHexString(tagId));
			testObject.put("linked_user", "june");
			testObject.saveInBackground();	

		}
    }


	@Override
	protected void onNewIntent(Intent intent) 
	{
		super.onNewIntent(intent);
		
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tag != null) 
		{
			byte[] tagId = tag.getId();
			TXT_tagDesc.setText("TagID: " + toHexString(tagId));
		}
	}
	
	
	public static final String CHARS = "0123456789ABCDEF";
	
	public static String toHexString(byte[] data)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; ++i)
		{
			sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))
				.append(CHARS.charAt(data[i] & 0x0F));
		}
		return sb.toString();
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
