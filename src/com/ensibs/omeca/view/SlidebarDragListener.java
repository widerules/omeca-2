package com.ensibs.omeca.view;

import com.ensibs.omeca.utils.SlidingUpPanelLayout;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;

public class SlidebarDragListener implements OnDragListener{
	

	@Override
    public boolean onDrag(View v, DragEvent event) {
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				Log.w("Text", "entered");
				SlidingUpPanelLayout slidebar = (SlidingUpPanelLayout)v.getParent().getParent().getParent();
				if(slidebar.isExpanded())
					slidebar.collapsePane();
					
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				View view = (View) event.getLocalState();
				ViewGroup parent = (ViewGroup)(view.getParent());
		        parent.addView(view);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				break;
			default:
				break;
		}
		return true;
    }

}
