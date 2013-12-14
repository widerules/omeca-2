package com.ensibs.omeca.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DiscardPile;

public class DiscardPileView extends FrameLayout{
	private static final float ratio = 1.452f;
	private DiscardPile discardPile;
	private Context context;
	
	public DiscardPileView(Context context, DiscardPile discardPile) {
		super(context);
		this.discardPile = discardPile;
		this.context = context;
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		int height = 2+metrics.heightPixels/4;
		int width = (int) (2+(height/ratio));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.bottomMargin = 15;
		params.leftMargin = 15;
		setLayoutParams(params);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.discardpile));
		setOnDragListener(new PileDragListener(this));
		updateView();
	}
	
	@Override
	public void removeView(View view) {
		discardPile.removeLastCard();
		super.removeView(view);
		
	}
	
	@Override
	public void addView(View child) {
		discardPile.addCard(((CardView)child).getCard());
		super.addView(child);
	}

	public void updateView() {
		this.removeAllViews();
		for (Card c : discardPile.getCards()) {
			super.addView(new CardView(context, c));
		}
	}	
}
