package com.ensibs.omeca.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class DealView extends LinearLayout{
	public Button buttonSave;
	public Button buttonCancel;
	private TextView nbCards;
	private SeekBar seekBar;

	public DealView(int drawNumber, int playerNumber,Context c) {
		super(c);	
		this.setOrientation(LinearLayout.VERTICAL);
		this.setPadding(30, 10, 30, 10);
		LinearLayout buttonLayout = new LinearLayout(c);
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		buttonLayout.setPadding(0, 5, 0, 0);
		buttonSave = new Button(c);
		buttonSave.setText("OK");
		buttonCancel = new Button(c);
		buttonCancel.setText("Cancel");
		seekBar = new SeekBar(c);
		seekBar.setProgress(0);
		seekBar.setMax((int)(drawNumber / playerNumber));
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) { }
			public void onStartTrackingTouch(SeekBar seekBar) { }
			public void onProgressChanged(SeekBar seekBar,int progress, boolean fromUser) {
				nbCards.setText(""+progress);
			}
		});
		nbCards = new TextView(c);
		nbCards.setText(""+seekBar.getProgress());
		buttonLayout.addView(buttonSave,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT,1));
		buttonLayout.addView(buttonCancel,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT,1));
		this.addView(nbCards);
		this.addView(seekBar,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		this.addView(buttonLayout);
		
		
	}
	public int getDealNumber(){
		return seekBar.getProgress();
	}
}
