package com.ensibs.omeca.view;

import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DiscardPile;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DiscardPileView extends FrameLayout{
	private static final float ratio = 1.452f;
	private DiscardPile discardPile;
	private Context context;
	
	public DiscardPileView(Context context, DiscardPile discardPile) {
		super(context);
		this.discardPile = discardPile;
		this.context = context;
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		int height = 2+metrics.heightPixels/4;
		int width = (int) (2+(height/ratio));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.bottomMargin = 15;
		params.leftMargin = 15;
		setLayoutParams(params);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.discardpile));
		setOnDragListener(new DiscardPileDragListener());
		updateView();
	}
	
	public void updateView(){
		this.removeAllViews();
		for(Card c : discardPile.getCards()){
			addView(new CardView(context, c));
		}
	}
	
	private class DiscardPileDragListener implements OnDragListener {
		
		private boolean hasExited = false;
		
		@Override
		public boolean onDrag(View v, DragEvent event) {
			View view = (View) event.getLocalState();
			ViewGroup owner = (ViewGroup) view.getParent();
			
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				if(hasExited || owner != DiscardPileView.this){
					discardPile.addCard(((CardView) view).getCard());
					hasExited = false;
				}
				
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				discardPile.removeLastCard();
				hasExited = true;
				break;
			case DragEvent.ACTION_DROP:
				if(owner != DiscardPileView.this){
					owner.removeView(view);
					updateView();
				}
				view.setVisibility(View.VISIBLE);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				hasExited = false;
				break;
			default:
				break;
			}
			return true;
		}

	}	
}
