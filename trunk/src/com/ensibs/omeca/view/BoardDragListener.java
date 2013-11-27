package com.ensibs.omeca.view;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class BoardDragListener implements OnDragListener{

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
				if(v != owner){
					owner.removeView(view);
					RelativeLayout container = (RelativeLayout) v;
					container.addView(view);
				}
				MarginLayoutParams marginParams = new MarginLayoutParams(view.getLayoutParams());
				int left = (int)(event.getX() - (view.getWidth()/2));
				int top = (int)(event.getY() - (view.getHeight()/2));
				marginParams.setMargins(left, top, 0, 0);
				view.setLayoutParams(new RelativeLayout.LayoutParams(marginParams));
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
