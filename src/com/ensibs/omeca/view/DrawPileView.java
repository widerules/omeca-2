package com.ensibs.omeca.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DrawPile;

@SuppressLint("ViewConstructor")
public class DrawPileView extends FrameLayout {
	private DrawPile drawpile;
	private Context context;

	public DrawPileView(Context context, DrawPile drawpile) {
		super(context);
		this.drawpile = drawpile;
		this.context = context;
		DisplayMetrics metrics = context.getApplicationContext().getResources()
				.getDisplayMetrics();
		int height = 2 + metrics.heightPixels / CardView.SIZE;
		int width = (int) (2 + (height / CardView.RATIO));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				width, height);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.bottomMargin = 15;
		params.rightMargin = 15;
		setLayoutParams(params);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.drawpile));
		setOnDragListener(new PileDragListener(this));
		updateView();
	}
	
	@Override
	public void removeViewInLayout(View view) {
		drawpile.removeLastCard();
		super.removeView(view);
		
	}
	
	@Override
	public void addView(View child) {
		drawpile.addCard(((CardView)child).getCard());
		super.addView(child);
	}

	public void updateView() {
		this.removeAllViews();
		for (Card c : drawpile.getCards()) {
			super.addView(new CardView(context, c));
		}
	}

	public DrawPile getDrawpile() {
		return drawpile;
	}
	
}
