package com.ensibs.omeca.controller;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.view.View;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.utils.NotificationTools;
import com.ensibs.omeca.view.BoardView;

/**
 * Listener of the DrawPile shuffle animation
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * @see AnimatorListener
 */
public class ShuffleAnimationListener implements AnimatorListener {

	private View view;

	/**
	 * Constructor
	 * 
	 * @param view
	 *            The view which is animated
	 */
	public ShuffleAnimationListener(View view) {
		this.view = view;
	}

	@Override
	public void onAnimationCancel(Animator animation) {
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		BoardView boardView = (BoardView) (GameActivity.getActivity()
				.findViewById(R.id.view_board));
		boardView.removeView(view);
	}

	@Override
	public void onAnimationRepeat(Animator animation) {
	}

	@Override
	public void onAnimationStart(Animator animation) {
		if (GA.isSoundToggled())
			NotificationTools.createSoundNotification(GameActivity
					.getActivity().getApplicationContext(),
					R.drawable.shufflecard);
		if (GA.isVibrationToggled())
			NotificationTools.createVibrationNotification(GameActivity
					.getActivity().getApplicationContext(), 1000);
	}
}
