package com.ensibs.omeca.view;

import com.ensibs.omeca.ControllerView;
import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.SlidingUpPanelLayout;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HandViewCardGalleryDragListener implements OnDragListener{
	boolean exited = false;
	@Override
    public boolean onDrag(View v, DragEvent event) {
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_ENTERED:
				Toast.makeText(GameActivity.getActivity(), "Poueeeeeeet",
						   Toast.LENGTH_SHORT).show();
				exited = false;
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				SlidingUpPanelLayout slide = (SlidingUpPanelLayout) GameActivity.getActivity().findViewById(R.id.sliding_layout);
				View slidebar = GameActivity.getActivity().findViewById(R.id.playerview_slidebar_hand);
				if(slide.isExpanded() && event.getY()< slidebar.getHeight() ){
					slide.collapsePane();
					Toast.makeText(GameActivity.getActivity(), "Patamoueeeeeette",
						   Toast.LENGTH_SHORT).show();
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
