package com.ensibs.omeca.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DrawPile;

/**
 * This class represents the View of a draw pile on the board
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class DrawPileView extends FrameLayout {
	private DrawPile drawpile;
	private Context context;

	/**
	 * Contructor
	 * 
	 * @param context
	 *            The context
	 * @param discardPile
	 *            The DrawPile model
	 */
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

	/**
	 * Removes a CardView from the DrawPileView
	 * 
	 * @param view
	 *            The view to remove
	 */
	@Override
	public void removeViewInLayout(View view) {
		drawpile.removeLastCard();
		GA.board.setDrawPile(drawpile);
		super.removeViewInLayout(view);

	}

	/**
	 * Adds a CardView to the DrawPileView
	 * 
	 * @param view
	 *            The view to add
	 */
	@Override
	public void addView(View child) {
		drawpile.addCard(((CardView) child).getCard());
		GA.board.setDrawPile(drawpile);
		super.addView(child);
	}

	/**
	 * Updates the DiscardPileView
	 */
	public void updateView() {
		this.removeAllViews();
		for (Card c : drawpile.getCards()) {
			super.addView(new CardView(context, c));
		}
		GA.board.setDrawPile(drawpile);
	}

	/**
	 * Getter on DrawPile model
	 * 
	 * @return drawpile
	 */
	public DrawPile getDrawpile() {
		return drawpile;
	}

	/**
	 * Sets the DrawPile model
	 * 
	 * @param drawpile
	 *            The new DrawPile model
	 */
	public void setDrawpile(DrawPile drawpile) {
		this.drawpile = drawpile;
	}

}
