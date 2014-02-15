package com.ensibs.omeca.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.ActionController;
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
		int height = 4 + metrics.heightPixels / CardView.SIZE;
		int width = (int) (4 + (height / CardView.RATIO));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				width, height);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.bottomMargin = 5;
		params.rightMargin = 5;
		setLayoutParams(params);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.drawpile));
		setOnDragListener(new PileDragListener(this));
		updateView();
	}
	
	@Override
	public void removeViewInLayout(View view) {
		drawpile.removeLastCard();
		ActionController.board.setDrawPile(drawpile);
		super.removeViewInLayout(view);
		
	}
	
	@Override
	public void addView(View child) {
		drawpile.addCard(((CardView)child).getCard());
		ActionController.board.setDrawPile(drawpile);
		super.addView(child);
	}

	public void updateView() {
		this.removeAllViews();
		for (Card c : drawpile.getCards()) {
			super.addView(new CardView(context, c));
		}
		ActionController.board.setDrawPile(drawpile);
	}

	public DrawPile getDrawpile() {
		return drawpile;
	}

	public void setDrawpile(DrawPile drawpile) {
		this.drawpile = drawpile;
	}
	
	
}
