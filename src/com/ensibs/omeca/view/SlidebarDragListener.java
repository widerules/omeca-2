package com.ensibs.omeca.view;

import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Gallery;
import android.widget.RelativeLayout;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.utils.SlidingUpPanelLayout;

public class SlidebarDragListener implements OnDragListener{
	
	private SlidingUpPanelLayout slidebar;
	
	public SlidebarDragListener(SlidingUpPanelLayout slidebar) {
		this.slidebar = slidebar;
	}

	@Override
    public boolean onDrag(View v, DragEvent event) {
		View view = (View) event.getLocalState();
		
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				if(slidebar.isExpanded()){
					DisplayMetrics metrics = GameActivity.getActivity()
							.getApplicationContext().getResources()
							.getDisplayMetrics();
					int height = 2 + metrics.heightPixels / CardView.SIZE;
					int width = (int) (2 + (height / CardView.RATIO));
					LayoutParams params = view.getLayoutParams();
					params.height = height;
					params.width = width;
					view.setLayoutParams(params);
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
    }

}
