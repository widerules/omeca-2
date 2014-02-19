package com.ensibs.omeca.view;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

/**
 * Drag listener for the CardViews in HandView
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class HandViewCardGalleryDragListener implements OnDragListener{
	/**
	 * OnDrag event actions
	 */
	@Override
    public boolean onDrag(View v, DragEvent event) {
		switch (event.getAction()) {
			case DragEvent.ACTION_DROP:
				View view = (View) event.getLocalState();
				HandView hv =(HandView) view.getParent();
				hv.updateView(false);
				view.setVisibility(View.VISIBLE);
				break;
			default:
				break;
		}
		return true;
    }
}
