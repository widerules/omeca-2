package com.ensibs.omeca.view;

import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DrawPile;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DrawPileView extends FrameLayout{
	private static final float ratio = 1.452f;
	private DrawPile drawpile;
	private Context context;
	
	public DrawPileView(Context context, DrawPile drawpile) {
		super(context);
		this.drawpile = drawpile;
		this.context = context;
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		int height = 2+metrics.heightPixels/4;
		int width = (int) (2+(height/ratio));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.bottomMargin = 15;
		params.rightMargin = 15;
		setLayoutParams(params);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.drawpile));
		setOnDragListener(new DrawPileDragListener());
		updateView();
	}
	
	public void updateView(){
		this.removeAllViews();
		for(Card c : drawpile.getCards()){
			addView(new CardView(context, c));
		}
	}
	
	private class DrawPileDragListener implements OnDragListener{

		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				View view = (View) event.getLocalState();
				ViewGroup owner = (ViewGroup) view.getParent();
				owner.removeView(view);
				if(view instanceof CardView){
					drawpile.addCard(((CardView)view).getCard());
					updateView();
				}
				else
					view.setVisibility(View.VISIBLE);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				break;
			default:
				break;
		}
		return true;
		}
		
	}

}
