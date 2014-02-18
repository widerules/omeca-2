package com.ensibs.omeca.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DiscardPile;

@SuppressLint("ViewConstructor")
public class DiscardPileView extends FrameLayout{
	private DiscardPile discardPile;
	private Context context;
	
	public DiscardPileView(Context context, DiscardPile discardPile) {
		super(context);
		this.discardPile = discardPile;
		this.context = context;
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		int height = 4+metrics.heightPixels/CardView.SIZE;
		int width = (int) (4+(height/CardView.RATIO));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.bottomMargin = 5;
		params.leftMargin = 5;
		setLayoutParams(params);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.discardpile));
		setOnDragListener(new PileDragListener(this));
		updateView();
	}
	
	@Override
	public void removeViewInLayout(View view) {
		discardPile.removeLastCard();
		GA.board.setDiscardPile(discardPile);
		super.removeViewInLayout(view);
		
	}
	
	@Override
	public void addView(View child) {
		discardPile.addCard(((CardView)child).getCard());
		GA.board.setDiscardPile(discardPile);
		super.addView(child);
	}

	public void updateView() {
		this.removeAllViews();
		for (Card c : discardPile.getCards()) {
			super.addView(new CardView(context, c));
		}
		GA.board.setDiscardPile(discardPile);
	}

	public DiscardPile getDiscardPile() {
		return discardPile;
	}

	public void setDiscardPile(DiscardPile discardPile) {
		this.discardPile = discardPile;
	}
}
