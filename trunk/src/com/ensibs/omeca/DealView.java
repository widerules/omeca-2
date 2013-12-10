package com.ensibs.omeca;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DealView extends LinearLayout{
	public Button buttonSave;
	public Button buttonCancle;
	public TextView textView;

	public DealView(int drawNumber, int playerNumber,Context c) {
		super(c);	
		this.setOrientation(LinearLayout.VERTICAL);
		this.setPadding(50, 10, 30, 10);
		textView = new TextView(c);
		textView.setText("");
		LinearLayout buttonLayout = new LinearLayout(c);
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		buttonLayout.setPadding(0, 5, 0, 0);
		buttonSave = new Button(c);
		buttonSave.setText("OK");
		buttonCancle = new Button(c);
		buttonCancle.setText("Cancle");
		SeekBar seekBar = new SeekBar(c);
		seekBar.setMax(drawNumber / playerNumber);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) { }
			public void onStartTrackingTouch(SeekBar seekBar) { }
			public void onProgressChanged(SeekBar seekBar,int progress, boolean fromUser) {
				textView.setText(String.valueOf(progress));
			}
		});
		buttonLayout.addView(buttonSave,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT,1));
		buttonLayout.addView(buttonCancle,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT,1));
		this.addView(textView);
		this.addView(seekBar,new LayoutParams(200,LayoutParams.WRAP_CONTENT));
		this.addView(buttonLayout);
		
		
	}
	public int getDealNumber(){
		return Integer.parseInt((String) textView.getText());
	}
}
