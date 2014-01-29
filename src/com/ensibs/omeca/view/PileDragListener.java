package com.ensibs.omeca.view;

import com.ensibs.omeca.R;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.TextView;

public class PileDragListener implements OnDragListener {
	private ViewGroup pile;
	
	public PileDragListener(ViewGroup pile){
		this.pile = pile;
	}
	
	@Override
	public boolean onDrag(View v, DragEvent event) {
		View view = (View) event.getLocalState();
		ViewGroup owner = (ViewGroup) view.getParent();
		
		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			
			break;
		case DragEvent.ACTION_DROP:
			if(view instanceof CardView){
				owner.removeViewInLayout(view);
				pile.addView(view);
				view.setVisibility(View.VISIBLE);
				if(v instanceof DiscardPileView){
					DiscardPileView pile = (DiscardPileView)v;
					TextView text = (TextView)((View)v.getParent()).findViewById(R.id.nbDiscardPileCards);
					text.setText(""+pile.getDiscardPile().getNumberOfCards());
				}
				else if(v instanceof DrawPileView){
					DrawPileView pile = (DrawPileView)v;
					TextView text = (TextView)((View)v.getParent()).findViewById(R.id.nbDrawPileCards);
					text.setText(""+pile.getDrawpile().getNumberOfCards());
				}
			}
			else
				view.setVisibility(View.VISIBLE);
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			if(view instanceof CardView){
				if(v instanceof DiscardPileView){
					DiscardPileView pile = (DiscardPileView)v;
					TextView text = (TextView)((View)v.getParent()).findViewById(R.id.nbDiscardPileCards);
					text.setText(""+pile.getDiscardPile().getNumberOfCards());
				}
				else if(v instanceof DrawPileView){
					DrawPileView pile = (DrawPileView)v;
					TextView text = (TextView)((View)v.getParent()).findViewById(R.id.nbDrawPileCards);
					text.setText(""+pile.getDrawpile().getNumberOfCards());
				}
			}
			break;
		default:
			break;
		}
		return true;
	}

}
