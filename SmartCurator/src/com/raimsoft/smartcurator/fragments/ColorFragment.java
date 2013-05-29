package com.raimsoft.smartcurator.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.raimsoft.smartcurator.R;

@SuppressLint("ValidFragment")
public class ColorFragment extends Fragment {
	
	private int mColorRes = -1;
	private ImageView mImageBG;
	private Context mContext;
	
	public ColorFragment() { 
		
	}
	
	public ColorFragment(Context context, int colorRes) {
		mColorRes = colorRes;
		mContext= context;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//if (savedInstanceState != null)
			//mColorRes = savedInstanceState.getInt("mColorRes");
//		int color = getResources().getColor(mColorRes);
		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		
		RelativeLayout.LayoutParams btnParam= new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		btnParam.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		mImageBG= new ImageView(mContext);
		mImageBG.setImageResource(R.drawable.sample);
		
		v.addView(v, btnParam);
		//v.setBackgroundColor(color);
		//v.setBackgroundResource(R.drawable.sample);
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", mColorRes);
	}
	
}
