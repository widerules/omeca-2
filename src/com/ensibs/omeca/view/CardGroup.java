package com.ensibs.omeca.view;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.model.actions.MoveCardAction;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

/**
 * This class manages the multi-cards moving action
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class CardGroup {

	private ArrayList<View> touching;
	private float x, y;

	/**
	 * Constructor
	 */
	public CardGroup() {
		touching = new ArrayList<View>();
	}

	/**
	 * Returns the list of grouped cards based on the long click card
	 * 
	 * @return list of grouped cards
	 */
	public ArrayList<View> getTouching() {
		return touching;
	}

	/**
	 * Adds inspect to touching if it's touching v
	 * 
	 * @param v
	 *            Long clicked card
	 * @param inspect
	 *            Card to eventually add to touching
	 */
	private void add(View v, View inspect) {
		if (inspect != v) { // touche la principale
			for (int j = touching.size() - 1; j > -1; j--) {
				if (isTuching(touching.get(j), inspect)) {
					inspect.setAlpha(0.5f);
					touching.add(inspect);
					j = -1;
				}
			}
		}
	}

	/**
	 * Verifies which card is touching the group
	 * 
	 * @param cards
	 *            All cards on the board
	 * @param v
	 *            Long clicked view
	 */
	public void startVerify(ArrayList<View> cards, View v) {
		int oldSize = 0;
		touching.add(v);
		while (oldSize != touching.size()) {
			oldSize = touching.size();
			for (int i = cards.size() - 1; i >= 0; i--) {
				this.add(v, cards.get(i));
				if (touching.contains(cards.get(i)))
					cards.remove(i);
			}
		}
	}

	/**
	 * Moves all cards by a distance of (x,y) in the board if some cards leave
	 * the board it stay at the border
	 * 
	 * @param x
	 *            Distance in x
	 * @param y
	 *            Distance in y
	 * @param maxX
	 *            Lateral size of the board
	 * @param maxY
	 *            Vertical size of the board
	 */
	public void move(Float x, Float y, int maxX, int maxY) {
		int left = 0, top = 0, right, bottom;
		for (int i = touching.size() - 1; i > 0; i--) {
			if (touching.get(i).getX() + (x - this.x) > 0)
				if (touching.get(i).getX() + (x - this.x) < (maxX - touching
						.get(i).getWidth()))
					left = (int) (touching.get(i).getX() + (x - this.x));
				// tuching.get(i).setX(tuching.get(i).getX()+ (x-this.x));
				else
					left = maxX - touching.get(i).getWidth();
			// tuching.get(i).setX(maxX);
			// else tuching.get(i).setX(0);

			if (touching.get(i).getY() + (y - this.y) > 0)
				if (touching.get(i).getY() + (y - this.y) < maxY
						- touching.get(i).getHeight())
					top = (int) (touching.get(i).getY() + (y - this.y));
				// tuching.get(i).setY(tuching.get(i).getY()+ (y-this.y));
				else
					top = maxY - touching.get(i).getHeight();// tuching.get(i).setY(maxY);
			// else tuching.get(i).setY(0);
			// tuching.get(i).setY(tuching.get(i).getY()+ (y-this.y));
			touching.get(i).setAlpha(1);
			touching.get(i).setVisibility(View.VISIBLE);

			MarginLayoutParams marginParams = new MarginLayoutParams(touching
					.get(i).getLayoutParams());
			right = (int) (maxX - left + touching.get(i).getWidth());
			bottom = (int) (maxY - top + touching.get(i).getHeight());
			marginParams.setMargins(left, top, right, bottom);
			touching.get(i).setLayoutParams(
					new RelativeLayout.LayoutParams(marginParams));
		}
		touching.get(0).setVisibility(View.VISIBLE);
		touching.clear();
	}

	/**
	 * Prepares the group to be move. This function have to be called before
	 * drag
	 * 
	 * @param x2
	 *            Position of the pointer relatively to board
	 * @param y2
	 *            Position of the pointer relatively to board
	 */
	public void startMove(float x2, float y2) {
		this.x = x2;
		this.y = y2;
		for (int i = 1; i < touching.size(); i++) {
			touching.get(i).setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Tells if a view is touching an other
	 * 
	 * @param c1
	 *            First view
	 * @param c2
	 *            Second view
	 * @return true if it's touching, false else
	 */
	public Boolean isTuching(View c1, View c2) {
		float xUp1, xUp2, yUp1, yUp2;
		xUp1 = c1.getX();
		yUp1 = c1.getY();
		xUp2 = c2.getX();
		yUp2 = c2.getY();
		if (Math.abs(xUp1 - xUp2) <= c1.getWidth()
				&& Math.abs(yUp1 - yUp2) <= c1.getHeight())
			return true;
		else
			return false;
	}

	/**
	 * Clears the touching list
	 */
	public void leave() {
		// multiDrag=false;
		for (int i = 1; i < touching.size(); i++) {
			touching.get(i).setAlpha(1);
			touching.get(i).setVisibility(View.VISIBLE);
		}
		touching.clear();
	}

	/**
	 * Moves all cards to the parent
	 * 
	 * @param parent
	 *            Pile who will receive all cards
	 */
	public void moveToPile(FrameLayout parent) {
		for (int i = touching.size() - 1; i > 0; i--) {
			BoardView boardView = (BoardView) (GameActivity.getActivity()
					.findViewById(R.id.view_board));
			boardView.removeView(touching.get(i));
			touching.get(i).setAlpha(1);
			touching.get(i).setVisibility(View.VISIBLE);
			parent.addView(touching.get(i));
			if (parent instanceof DrawPileView) {
				GameActivity
						.getActivity()
						.getWifiDirectManager()
						.sendEvent(
								new WifiDirectEventImpl(WifiDirectEvent.EVENT,
										new MoveCardAction("BoardView",
												"DrawPileView",
												((CardView) touching.get(i))
														.getCard())));
			} else if (parent instanceof DiscardPileView) {
				GameActivity
						.getActivity()
						.getWifiDirectManager()
						.sendEvent(
								new WifiDirectEventImpl(WifiDirectEvent.EVENT,
										new MoveCardAction("BoardView",
												"DiscardPileView",
												((CardView) touching.get(i))
														.getCard())));
			}
		}
		touching.clear();
	}

	/**
	 * Gives all cards to the player
	 * 
	 * @param p
	 *            Player receiving cards
	 */
	public void moveToPlayer(Player p) {
		for (int i = touching.size() - 1; i > 0; i--) {
			BoardView boardView = (BoardView) (GameActivity.getActivity()
					.findViewById(R.id.view_board));
			CardView cv = (CardView) touching.get(i);
			boardView.removeView(cv);
			cv.setAlpha(1);
			cv.setVisibility(View.VISIBLE);
			p.addCard(cv.getCard());
			GameActivity
					.getActivity()
					.getWifiDirectManager()
					.sendEvent(
							new WifiDirectEventImpl(WifiDirectEvent.EVENT,
									new MoveCardAction("BoardView", cv
											.getCard(), "Player", p.getId())));
		}
		touching.clear();
	}
}
