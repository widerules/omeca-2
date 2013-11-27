package com.ensibs.omeca.view;

import com.ensibs.omeca.model.entities.Card;

import android.content.ClipData;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CardView extends ImageView{
	Card card;
	private static final float ratio = 1.452f;

	public CardView(Context context, Card card) {
		super(context);
		this.card = card;
		setImageDrawable(
				context.getResources().getDrawable(
						context.getResources().getIdentifier(
								card.getColor()+card.getValue(),
								"drawable",
								context.getApplicationContext().getPackageName()
						)
				)
		);
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		int height = metrics.heightPixels/4;
		int width = (int) (height/ratio);
		setLayoutParams(new RelativeLayout.LayoutParams(width, height));
		setOnTouchListener(new CardTouchListener());
				
	}
	
	private class CardTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
		        ClipData data = ClipData.newPlainText("", "");
		        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		        view.startDrag(data, shadowBuilder, view, 0);
		        view.setVisibility(View.INVISIBLE);
		        return true;
			}else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
				view.setVisibility(View.VISIBLE);
				return true;
		    }
			else
				return false;
		}
		
	}
	

}
