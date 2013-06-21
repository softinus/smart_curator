package com.raimsoft.smartcurator5;

import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;


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
		String strNFC_id= toHexString(tagId);	
		TXT_tagDesc.setText("TagID: " + strNFC_id );
	
		ParseObject testObject = new ParseObject("NFC_List");
		testObject.put("NFC_id", strNFC_id);
		testObject.put("linked_user", "june");
		testObject.saveInBackground();
		
		GetImage(IMG_1, strNFC_id, "File1");
		GetImage(IMG_2, strNFC_id, "File2");
	}

	private void GetImage(final ImageView view, final String strNFC_id, final String strFileColumnName)
	{
		ParseQuery<ParseObject> query= ParseQuery.getQuery("NFC_reg");
		query.whereEqualTo("NFC_id", strNFC_id);
		query.findInBackground(new FindCallback<ParseObject>()
		{			
			@Override
			public void done(List<ParseObject> list, ParseException e)
			{
				if(list.size()==1)
				{
					ParseFile fileImage1= (ParseFile)list.get(0).get(strFileColumnName);
					fileImage1.getDataInBackground(new GetDataCallback()
					{	
						@Override
						public void done(byte[] file, ParseException e)
						{
							BitmapFactory.Options opts = new BitmapFactory.Options();
							opts.inDither= false;
							opts.inSampleSize= 2;
							Bitmap bitmap= BitmapFactory.decodeByteArray(file, 0, file.length, opts);
							view.setImageBitmap(bitmap);
						}
					},
					new ProgressCallback()
					{	
						@Override
						public void done(Integer nProgress)
						{
							if(nProgress==100)
								TXT_1.setText("");
							else
								TXT_1.setText(Integer.toString(nProgress)+"%");
						}
					});
				}
			}
		});
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
