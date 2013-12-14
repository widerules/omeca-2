package com.ensibs.omeca.view;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;

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
			owner.removeView(view);
			pile.addView(view);
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
