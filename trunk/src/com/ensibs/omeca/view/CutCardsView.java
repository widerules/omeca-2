package com.ensibs.omeca.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.actions.CutCardsAction;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DrawPile;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

/**
 * This class represents the View of a spread deck for a cut
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class CutCardsView extends LinearLayout {

	Context context;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The context
	 */
	public CutCardsView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The context
	 * @param attrs
	 *            The attributes
	 */
	public CutCardsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init(context);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The context
	 * @param attrs
	 *            The attributes
	 * @param defStyle
	 *            The style
	 */
	public CutCardsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * Initializes the CutCardsView
	 * 
	 * @param context
	 *            The context
	 */
	private void init(Context context) {
		this.context = context;
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setGravity(Gravity.BOTTOM);
		setLayoutParams(params);
		setBackgroundColor(Color.argb(168, 0, 0, 0));
	}

	/**
	 * Updates the CutCardsView
	 */
	public void updateView() {
		removeAllViews();
		if (!GA.board.getDrawPile().getCards().isEmpty()) {
			DisplayMetrics metrics = context.getApplicationContext()
					.getResources().getDisplayMetrics();
			CardView cv = new CardView(context, GA.board.getDrawPile()
					.getCards().get(0));
			LinearLayout.LayoutParams params = new LayoutParams(
					cv.getLayoutParams().width * 2,
					cv.getLayoutParams().height * 2);
			params.leftMargin = 20;
			params.bottomMargin = 30;
			params.rightMargin = ((((GA.board.getDrawPile().getNumberOfCards() + 1) * params.width) - metrics.widthPixels) / -GA.board
					.getDrawPile().getNumberOfCards()) - params.leftMargin;

			for (Card c : GA.board.getDrawPile().getCards()) {
				cv = new CardView(context, c);
				cv.setRotationY(30);
				cv.setOnTouchListener(new CutCardsTouchListener());
				addView(cv, params);
			}
		}
	}

	/**
	 * Touch listener for a CardView from the CutCardsView
	 * 
	 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain
	 *         RIO - Lindsay ROZIER)
	 * 
	 */
	private class CutCardsTouchListener implements OnTouchListener {

		/**
		 * OnTouch event actions
		 */
		@Override
		public boolean onTouch(View v, MotionEvent mE) {
			switch (mE.getAction()) {
			case MotionEvent.ACTION_UP:
				DrawPile dp = GA.board.getDrawPile();
				dp.cut(indexOfChild(v) + 1);
				Card[] cards = new Card[dp.getCards().size()];
				int i = 0;
				for (Card c : dp.getCards()) {
					cards[i] = c;
					i++;
				}
				GA.board.setDrawPile(dp);
				Log.w("X", v.getX() + "");
				Log.w("Y", v.getY() + "");
				AlphaAnimation anim = new AlphaAnimation(1, 0.5f);
				anim.setRepeatCount(5);
				anim.setDuration(100);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						BoardView boardView = (BoardView) GameActivity
								.getActivity().findViewById(R.id.view_board);
						boardView.getCutCardsView().setVisibility(View.GONE);
					}
				});
				v.startAnimation(anim);
				GameActivity.getActivity().getOmecaHandler()
						.sendEmptyMessage(OmecaHandler.CUT);
				GameActivity
						.getActivity()
						.getWifiDirectManager()
						.sendEvent(
								new WifiDirectEventImpl(WifiDirectEvent.EVENT,
										new CutCardsAction(cards)));

				break;
			default:
				break;
			}
			return true;
		}

	}
}
