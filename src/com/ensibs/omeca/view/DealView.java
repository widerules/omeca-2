package com.ensibs.omeca.view;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class DealView extends LinearLayout{
	public Button buttonSave;
	public Button buttonCancel;
	
	// number of card to deal by players
	private NumberPicker numberPicker;

	/**
	 * Constructor 
	 * @param drawNumber Total number of card how can be deal
	 * @param playerNumber Number of players in the game 
	 * @param c Context of the avtiviy
	 */
	public DealView(int drawNumber, int playerNumber,Context c) {
		super(c);	
		this.setOrientation(LinearLayout.VERTICAL);
		this.setPadding(30, 10, 30, 10);
		LinearLayout buttonLayout = new LinearLayout(c);
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		buttonLayout.setPadding(0, 5, 0, 0);
		buttonSave = new Button(c);
		buttonSave.setText(GameActivity.getActivity().getResources().getString(R.string.ok));
		buttonCancel = new Button(c);
		buttonCancel.setText(GameActivity.getActivity().getResources().getString(R.string.cancel));
		numberPicker = new NumberPicker(c);
		numberPicker.setMaxValue((int)(drawNumber / playerNumber));
		numberPicker.setMinValue(1);
		buttonLayout.addView(buttonSave,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT,1));
		buttonLayout.addView(buttonCancel,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT,1));
		this.addView(numberPicker,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		this.addView(buttonLayout);
		
		
	}
	
	/**
	 * Return the selected number the the user have chose 
	 * @return Selected number
	 */
	public int getDealNumber(){
		return numberPicker.getValue();
	}
}
