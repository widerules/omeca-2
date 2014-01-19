package com.ensibs.omeca.view;

import android.support.v4.view.MotionEventCompat;
import android.text.Layout;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;

import com.ensibs.omeca.ControllerView;
import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.utils.SlidingUpPanelLayout;

public class HandViewCardGalleryDragListener implements OnDragListener{
	boolean exited = false;
	int width; 
	@Override
    public boolean onDrag(View v, DragEvent event) {
		final float initX = event.getX(); 
		final float initY = event.getY();
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
				}/*}else if( event.getX() - (hv.getHeight()/2)== Math.abs(hv.getHeight()/3) ){ // ordonner une carte
					int position = (int) (event.getY()/width);
				
					ControllerView.user.getCards().remove(view.getCard());
					ControllerView.user.getCards().add(position+ hv.getFirstVisiblePosition(), view.getCard());
				}*/
				hv.updateView();
				break;
			default:
				break;
		}
		return true;
    }
	
	public void setWidth(int w){
		this.width = w; 
	}
}
