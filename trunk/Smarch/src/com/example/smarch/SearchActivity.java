package com.example.smarch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SearchActivity extends Activity implements OnClickListener
{
	private Button BTN_search;
	private EditText EDT_search;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
		
		mContext= this;
		
		setContentView(R.layout.activity_search);
		
		BTN_search= (Button) findViewById(R.id.btn_search);
		EDT_search= (EditText) findViewById(R.id.edt_search);
		
		BTN_search.setOnClickListener(this);
		
		EDT_search.setOnEditorActionListener(new OnEditorActionListener()
		{
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
			if( (actionId == EditorInfo.IME_ACTION_NEXT) ||
			    (actionId == EditorInfo.IME_ACTION_DONE) ||
				(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
				{
					DoSearch();
				}
				return false;
			}
		});	
	}

	@Override
	public void onClick(View v)
	{
		this.DoSearch();
	}
	
	private void DoSearch()
	{
		if( EDT_search.getText().toString() == "" )
		{
			ShowAlertDialog("안내", "검색어를 입력해주세요~", "OK");
		}
		else
		{
			SearchData.strQuery= EDT_search.getText().toString();
			Intent intent= new Intent(SearchActivity.this, MainActivity.class);
			startActivity(intent);
		}
	}
	
	private void ShowAlertDialog(String strTitle, String strContent, String strButton)
	{
		new AlertDialog.Builder(mContext)
		.setTitle( strTitle )
		.setMessage( strContent )
		.setPositiveButton( strButton , null)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setCancelable(false)
		.create()
		.show();
	}

}
