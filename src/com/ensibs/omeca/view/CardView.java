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
		private float x;
		private float y;
		private final float SCROLL_THRESHOLD = 10;
		private boolean isOnClick;
		
		@Override
		public boolean onTouch(View view, MotionEvent mE) {
			switch (mE.getAction()){
				case MotionEvent.ACTION_DOWN:
					x = mE.getX();
					y = mE.getY();
					isOnClick = true;
					break;
				case MotionEvent.ACTION_UP:
					if(isOnClick){
						turnCard();
						view.setVisibility(View.VISIBLE);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if(isOnClick && 
							Math.sqrt((x-mE.getX())*(x-mE.getX()) + (y-mE.getY())*(y-mE.getY())) > SCROLL_THRESHOLD){
						view.setVisibility(View.INVISIBLE);
						ClipData data = ClipData.newPlainText("", "");
				        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				        view.startDrag(data, shadowBuilder, view, 0);
				        isOnClick = false;
					}
					break;
				default:
					break;
			}	
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
