package com.ensibs.omeca.view;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class SlidebarDragListener implements OnDragListener{

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
