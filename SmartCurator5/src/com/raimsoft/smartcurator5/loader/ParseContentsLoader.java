package com.raimsoft.smartcurator5.loader;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;

public class ParseContentsLoader
{

	
	public static final String CHARS = "0123456789ABCDEF";
	
	/**
	 * Hex값을 String으로 변환
	 * @param data
	 * @return
	 */	
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

	/**
	 * 이미지를 Parse serverd에서 얻어와서 해당하는 View에 뿌린다.
	 * @param strNFC_id : 해당하는 NFC ID
	 * @param strFileColumnName : 이미지 file column 이름
	 * @param nSampleSize : 이미지 샘플링 사이즈
	 * @param img_view : 이미지 띄울 뷰
	 * @param txt_view : 프로그레스 텍스트 띄울 뷰
	 */
	public static void GetImage(final String strNFC_id, final String strFileColumnName, final int nSampleSize, final ImageView img_view, final TextView txt_view)
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
							opts.inSampleSize= nSampleSize;
							Bitmap bitmap= BitmapFactory.decodeByteArray(file, 0, file.length, opts);
							img_view.setImageBitmap(bitmap);
						}
					},
					new ProgressCallback()
					{	
						@Override
						public void done(Integer nProgress)
						{
							if(nProgress==100)
								txt_view.setText("");
							else
								txt_view.setText(Integer.toString(nProgress)+"%");
						}
					});
				}
			}
		});
	}
	
	
	/**
	 * 이미지를 Parse serverd에서 얻어와서 해당하는 View에 뿌린다.
	 * @param strNFC_id : 해당하는 NFC ID
	 * @param strFileColumnName : 이미지 file column 이름
	 * @param nSampleSize : 이미지 샘플링 사이즈
	 * @param img_view : 이미지 띄울 뷰
	 * @param prg_bar : 프로그레스 띄울 바
	 */
	public static void GetImage(final String strNFC_id, final String strFileColumnName, final int nSampleSize, final ImageView img_view, final ProgressBar prg_bar)
	{
		prg_bar.setVisibility(View.VISIBLE);
		prg_bar.setMax(100);
		prg_bar.setProgress(0);
		
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
							opts.inSampleSize= nSampleSize;
							Bitmap bitmap= BitmapFactory.decodeByteArray(file, 0, file.length, opts);
							img_view.setImageBitmap(bitmap);
						}
					},
					new ProgressCallback()
					{	
						@Override
						public void done(Integer nProgress)
						{
							if(nProgress==100)
								prg_bar.setVisibility(View.INVISIBLE);
							else
								prg_bar.setProgress(nProgress);
						}
					});
				}
			}
		});
	}

}
