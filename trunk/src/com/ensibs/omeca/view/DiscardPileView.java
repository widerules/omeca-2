package com.ensibs.omeca.view;

import com.ensibs.omeca.R;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class DiscardPileView extends FrameLayout{
	private static final float ratio = 1.452f;
	
	public DiscardPileView(Context context) {
		super(context);
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		int height = 2+metrics.heightPixels/4;
		int width = (int) (2+(height/ratio));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.bottomMargin = 15;
		params.leftMargin = 15;
		setLayoutParams(params);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.discardpile));
	}

}
