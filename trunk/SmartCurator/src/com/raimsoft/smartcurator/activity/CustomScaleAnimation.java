package com.raimsoft.smartcurator.activity;

import android.graphics.Canvas;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.raimsoft.smartcurator.R;



public class CustomScaleAnimation extends CustomAnimation {

	public CustomScaleAnimation() {
		super(R.string.app_name, new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.scale(percentOpen, 1, 0, 0);
			}			
		});
	}

}
