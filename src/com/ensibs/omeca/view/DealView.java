package com.ensibs.omeca.view;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

/**
 * This class represents the View of the window for dealing the cards
 * automatically
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class DealView extends LinearLayout {
	/**
	 * Save button of DealView
	 */
	public Button buttonSave;

	/**
	 * Cancel button of DealView
	 */
	public Button buttonCancel;

	// number of card to deal by players
	private NumberPicker numberPicker;

	/**
	 * Constructor
	 * 
	 * @param drawNumber
	 *            Total number of cards that can be dealt
	 * @param playerNumber
	 *            Number of players in the game
	 * @param c
	 *            Context of the activity
	 */
	public DealView(int drawNumber, int playerNumber, Context c) {
		super(c);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setPadding(30, 10, 30, 10);
		LinearLayout buttonLayout = new LinearLayout(c);
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		buttonLayout.setPadding(0, 5, 0, 0);
		buttonSave = new Button(c);
		buttonSave.setText(GameActivity.getActivity().getResources()
				.getString(R.string.ok));
		buttonCancel = new Button(c);
		buttonCancel.setText(GameActivity.getActivity().getResources()
				.getString(R.string.cancel));
		numberPicker = new NumberPicker(c);
		numberPicker.setMaxValue((int) (drawNumber / playerNumber));
		numberPicker.setMinValue(1);
		buttonLayout.addView(buttonSave, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		buttonLayout.addView(buttonCancel, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		this.addView(numberPicker, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		this.addView(buttonLayout);

	}

	/**
	 * Returns the selected number that the user have chosen
	 * 
	 * @return Selected number
	 */
	public int getDealNumber() {
		return numberPicker.getValue();
	}
}
