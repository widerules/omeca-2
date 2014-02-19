package com.ensibs.omeca.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DiscardPile;

/**
 * This class represents the View of a discard pile on the board
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class DiscardPileView extends FrameLayout {
	private DiscardPile discardPile;
	private Context context;

	/**
	 * Contructor
	 * 
	 * @param context
	 *            The context
	 * @param discardPile
	 *            The DiscardPile model
	 */
	public DiscardPileView(Context context, DiscardPile discardPile) {
		super(context);
		this.discardPile = discardPile;
		this.context = context;
		DisplayMetrics metrics = context.getApplicationContext().getResources()
				.getDisplayMetrics();
		int height = 4 + metrics.heightPixels / CardView.SIZE;
		int width = (int) (4 + (height / CardView.RATIO));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				width, height);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.bottomMargin = 5;
		params.leftMargin = 5;
		setLayoutParams(params);
		setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.discardpile));
		setOnDragListener(new PileDragListener(this));
		updateView();
	}

	/**
	 * Removes a CardView from the DiscardPileView
	 * 
	 * @param view
	 *            The view to remove
	 */
	@Override
	public void removeViewInLayout(View view) {
		discardPile.removeLastCard();
		GA.board.setDiscardPile(discardPile);
		super.removeViewInLayout(view);

	}

	/**
	 * Adds a CardView to the DiscardPileView
	 * 
	 * @param view
	 *            The view to add
	 */
	@Override
	public void addView(View child) {
		discardPile.addCard(((CardView) child).getCard());
		GA.board.setDiscardPile(discardPile);
		super.addView(child);
	}

	/**
	 * Updates the DiscardPileView
	 */
	public void updateView() {
		this.removeAllViews();
		for (Card c : discardPile.getCards()) {
			super.addView(new CardView(context, c));
		}
		GA.board.setDiscardPile(discardPile);
	}

	/**
	 * Getter on DiscardPile model
	 * 
	 * @return discardPile
	 */
	public DiscardPile getDiscardPile() {
		return discardPile;
	}

	/**
	 * Sets the DiscardPile model
	 * 
	 * @param discardPile
	 *            The new DiscardPile model
	 */
	public void setDiscardPile(DiscardPile discardPile) {
		this.discardPile = discardPile;
	}
}
