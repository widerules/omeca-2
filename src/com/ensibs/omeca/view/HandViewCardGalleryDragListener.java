package com.ensibs.omeca.view;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.utils.SlidingUpPanelLayout;

public class HandViewCardGalleryDragListener implements OnDragListener{
	boolean exited = false;
	@Override
    public boolean onDrag(View v, DragEvent event) {
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_ENTERED:
				exited = false;
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				SlidingUpPanelLayout slide = (SlidingUpPanelLayout) GameActivity.getActivity().findViewById(R.id.sliding_layout);
				View slidebar = GameActivity.getActivity().findViewById(R.id.playerview_slidebar_hand);
				if(slide.isExpanded() && event.getY()< slidebar.getHeight() ){
					slide.collapsePane();
					exited=true;
				}
				break;
			case DragEvent.ACTION_DROP:
				CardView view = (CardView) event.getLocalState();
				HandView hv =(HandView) view.getParent();
				if(exited){
					hv.removeView(view);
				}
				hv.updateView();
				break;
			default:
				break;
		}
		
		return true;
    }
}
