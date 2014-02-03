package com.ensibs.omeca.view;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

import com.ensibs.omeca.utils.SlidingUpPanelLayout;

public class SlidebarDragListener implements OnDragListener{
	

	@Override
    public boolean onDrag(View v, DragEvent event) {
		View view = (View) event.getLocalState();
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				SlidingUpPanelLayout slidebar = (SlidingUpPanelLayout)v.getParent().getParent().getParent();
				if(slidebar.isExpanded()){
					slidebar.collapsePane();
					view.setVisibility(View.VISIBLE);
				}
					
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				view.setVisibility(View.VISIBLE);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				break;
			default:
				break;
		}
		return true;
		
		
		/*public boolean onDrag(View v, DragEvent event) {
			SlidingUpPanelLayout slidebar = (SlidingUpPanelLayout)v.getParent().getParent().getParent();
			View view = (View) event.getLocalState();
			switch (event.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(slidebar.isExpanded()){
						slidebar.collapsePane();
						if(view.getParent() instanceof HandView)
							((HandView)view.getParent()).updateView(true);
					}
						
					break;
				case DragEvent.ACTION_DROP:				
					view.setVisibility(View.VISIBLE);
					break;
				default:
					break;
			}
			return true;*/
    }

}
