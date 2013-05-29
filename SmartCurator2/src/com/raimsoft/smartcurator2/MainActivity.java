package com.raimsoft.smartcurator2;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener, android.view.View.OnClickListener
{
	
	VideoView VDO_view;
	ImageView IMG_icon;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3), }), this);
		
//		VDO_view= (VideoView) findViewById(R.id.videoView1);
//		MediaController MC= new MediaController(this);
//		MC.setAnchorView(VDO_view);
//		
//		URL url= null;
//		String loc= null;
//		
//		try
//		{
//			url= new URL("http://tangible2.cafe24.com/files/test2.mp4");
//			HttpURLConnection conn= null;
//			conn= (HttpURLConnection) url.openConnection();
//			loc= conn.getHeaderField("Location");
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Uri uri;
//		if(loc==null)
//		{
//			uri= Uri.parse("http://tangible2.cafe24.com/files/test2.mp4");
//		}
//		else
//		{
//			uri= Uri.parse(loc);
//		}
//		
//		VDO_view.setMediaController(MC);
//		VDO_view.setVideoURI(uri);
//		VDO_view.requestFocus();
//		VDO_view.start();
		
		IMG_icon= (ImageView) findViewById(R.id.imageView1);
		IMG_icon.setOnClickListener(this);
		

	}
	@Override
	public void onClick(View arg0)
	{
		if(arg0.getId()==R.id.imageView1)			
		{		
			Intent it = new Intent(Intent.ACTION_VIEW);
			//재생할 동영상 주소
			Uri uri = Uri.parse("http://tangible2.cafe24.com/files/test2.mp4");
			//재생할 동영상주소와 동영상코덱 설정
			it.setDataAndType(uri, "video/mp4");
			//액티비티 실행
			startActivity(it);
		}		
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) 
	{
		// When the given dropdown item is selected, show its contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	}
	


	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment
	{
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) 
		{
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}





}
