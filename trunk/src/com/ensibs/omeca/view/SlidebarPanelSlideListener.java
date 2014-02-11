package com.ensibs.omeca.view;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Gallery;
import android.widget.LinearLayout;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.utils.SlidingUpPanelLayout;

public class SlidebarPanelSlideListener extends
		SlidingUpPanelLayout.SimplePanelSlideListener {

	private SlidingUpPanelLayout slidebar;
	private boolean isExpanded = false;

	public SlidebarPanelSlideListener(SlidingUpPanelLayout slidebar) {
		this.slidebar = slidebar;
	}

	@Override
	public void onPanelExpanded(View panel) {
		if (!isExpanded) {
			LinearLayout linear = (LinearLayout) slidebar
					.findViewById(R.id.view_slidebar);
			linear.findViewById(R.id.board_rightpart).setVisibility(View.GONE);
			linear.findViewById(R.id.linear_slidebar_hand).setVisibility(
					View.VISIBLE);

			slidebar.setDragView(slidebar.findViewById(R.id.collapse));

			Gallery g = (Gallery) slidebar
					.findViewById(R.id.playerview_slider_board_cardgallery);
			if (g != null) {
				SliderbarCardGallery a = (SliderbarCardGallery) g.getAdapter();
				a.notifyDataSetChanged();
			}

			isExpanded = true;
			DrawerLayout mDrawerLayout = (DrawerLayout) GameActivity.getActivity().findViewById(R.id.drawer_layout);
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}
	}

	@Override
	public void onPanelCollapsed(View panel) {
		if (isExpanded) {
			LinearLayout linear = (LinearLayout) slidebar
					.findViewById(R.id.view_slidebar);
			linear.findViewById(R.id.board_rightpart).setVisibility(
					View.VISIBLE);
			linear.findViewById(R.id.linear_slidebar_hand).setVisibility(
					View.GONE);

			SlidingUpPanelLayout slide = (SlidingUpPanelLayout) slidebar
					.findViewById(R.id.sliding_layout);
			slide.setDragView(slidebar.findViewById(R.id.expand));

			isExpanded = false;
			DrawerLayout mDrawerLayout = (DrawerLayout) GameActivity.getActivity().findViewById(R.id.drawer_layout);
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		}
	}

	@Override
	public void onPanelAnchored(View panel) {

	}

	@Override
	public void onPanelSlide(View panel, float slideOffset) {
	}

}
