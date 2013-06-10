package com.raimsoft.smartcurator5;

import android.app.Application;

import com.kth.baasio.Baas;

public class MainApplication extends Application {

	@Override
	public void onCreate()
	{
		Baas.io().init(
				this
				,"https://api.baas.io"
				,"a769dd7d-4a53-11e2-b5a4-06ebb80000ba"
				,"977706bf-d02a-11e2-9d52-06f4fe0000b5");
		super.onCreate();
	}

	@Override
	public void onTerminate()
	{
		Baas.io().uninit(this);
		super.onTerminate();
	}

}
