package com.ensibs.omeca.view;

import android.content.ClipData;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ensibs.omeca.model.entities.Card;

public class CardView extends ImageView{
	private Card card;
	private Context context;

	public static final float RATIO = 1.452f;
	public static final int SIZE = 5;

	public CardView(Context context, Card card) {
		super(context);
		this.context = context;
		this.card = card;
		if(card.isFaceUp())
			setFaceUpBackground();
		else
			setBackBackground();
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		int height = metrics.heightPixels/SIZE;
		int width = (int) (height/RATIO);
		setLayoutParams(new RelativeLayout.LayoutParams(width, height));
		setOnTouchListener(new CardTouchListener());
		setOnLongClickListener(new CardLongClickListener());
				
	}
	
	public void turnCard(){
		if(card.isFaceUp())
			setBackBackground();
		else
			setFaceUpBackground();
		
		card.setFaceUp(!card.isFaceUp());
	}
	
	private void setBackBackground(){
		setImageDrawable(
				context.getResources().getDrawable(
						context.getResources().getIdentifier(
								Card.CARDBACK,
								"drawable",
								context.getApplicationContext().getPackageName()
						)
				)
		);
	}
	
	private void setFaceUpBackground(){
		setImageDrawable(
				context.getResources().getDrawable(
						context.getResources().getIdentifier(
								card.getColor()+card.getValue(),
								"drawable",
								context.getApplicationContext().getPackageName()
						)
				)
		);
	}
	
	private class CardTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
		        return false;
			}else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
				turnCard();
				view.setVisibility(View.VISIBLE);
				return false;
		    }
			else
				return true;
		}
		
	}
	
	private class CardLongClickListener implements OnLongClickListener{

		@Override
		public boolean onLongClick(View view) {
			view.setVisibility(View.INVISIBLE);
			ClipData data = ClipData.newPlainText("", "");
	        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	        view.startDrag(data, shadowBuilder, view, 0);
	        
	        return true;
		}
		
	}
	
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}
	

}
