package com.raimsoft.smartcurator5;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

public class LoginActivity extends Activity implements OnClickListener
{
	EditText EDT_ID, EDT_PW;
	Button BTN_signIN;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.activity_login);
		
//        Baas.io().init(this,
//        		"https://api.baas.io",
//        		"june",
//        		"smartcurator");
		
		Parse.initialize(this, "vh60sQDbtfnIlFxn5HrK6oBj5SN1rqYeqtixIngY", "23gt0kLNMnZJEPYYYxWXQFVZiRXMceKrU1Ge3kVz"); 
		ParseAnalytics.trackAppOpened(getIntent());
		
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();	
		
		EDT_ID= (EditText) findViewById(R.id.email);
		EDT_PW= (EditText) findViewById(R.id.password);
		BTN_signIN= (Button) findViewById(R.id.sign_in_button);
		
		BTN_signIN.setOnClickListener(this);
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v)
	{
//		BaasioUser.signInInBackground(this, EDT_ID.getText().toString(), EDT_PW.getText().toString()
//				,
//				new BaasioSignInCallback()
//				{
//					
//					@Override
//					public void onException(BaasioException e)
//					{
//						ShowAlertDialog("onException", e.toString(), "ok");
//						if(e.getErrorCode()==201)
//						{
//							ShowAlertDialog("[로그인]", "아이디 또는 비밀번호가 일치하지 않습니다.", "확인");
//						}
//					}
//					
//					@Override
//					public void onResponse(BaasioUser response)
//					{
//						if(response != null)
//							//ShowAlertDialog("onResponse", response.toString(), "ok");
//							ShowAlertDialog("[로그인]", "로그인 성공!", "확인");
//						
//					}
//				});
		
	}


	
	
	private void ShowAlertDialog(String strTitle, String strContent, String strButton)
	{
		new AlertDialog.Builder(this)
		.setTitle( strTitle )
		.setMessage( strContent )
		.setPositiveButton( strButton , null)
		.setCancelable(false)
		.create()
		.show();
	}
}
