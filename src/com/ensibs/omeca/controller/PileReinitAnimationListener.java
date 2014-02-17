package com.ensibs.omeca.controller;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.view.View;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.view.BoardView;

public class PileReinitAnimationListener implements AnimatorListener{
	
	private View view;
	
	public PileReinitAnimationListener(View view){
		this.view = view;
	}

	@Override
	public void onAnimationCancel(Animator animation) {
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		BoardView boardView = (BoardView) (GameActivity.getActivity().findViewById(R.id.view_board));
		boardView.removeView(view);
	}

	@Override
	public void onAnimationRepeat(Animator animation) {
	}

	@Override
	public void onAnimationStart(Animator animation) {
	}
}
