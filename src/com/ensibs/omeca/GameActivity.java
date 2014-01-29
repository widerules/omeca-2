package com.ensibs.omeca;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.utils.OmecaPopupMenu;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.utils.SlidingUpPanelLayout;
import com.ensibs.omeca.utils.SlidingUpPanelLayout.PanelSlideListener;
import com.ensibs.omeca.view.BoardView;
import com.ensibs.omeca.view.DealView;
import com.ensibs.omeca.view.PlayerView;
import com.ensibs.omeca.view.SlideBarCardGalleryDragListener;
import com.ensibs.omeca.view.SlidebarDragListener;
import com.ensibs.omeca.wifidirect.WifiDirectManager;
import com.ensibs.omeca.wifidirect.event.ConnectionWifiDirectEvent;

public class GameActivity extends Activity implements Observer {

	WifiDirectManager wifiDirectManager;
	ActionController controller;
	AlertDialog popupMenu;
	OmecaApplication app;
	private static Activity instance;

	public static Activity getActivity() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		// Retrieve application
		app = (OmecaApplication) getApplication();

		// Create controller
		controller = app.getControler();

		// Creates the WifiDirectManager
		/*
		 * wifiDirectManager = app.getWifiDirectManager();
		 * wifiDirectManager.addObserver(this); wifiDirectManager.setRole(true);
		 * wifiDirectManager.discoverPeers();
		 */

		// Hides titlebar and actionbar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Launches the stuff
		LayoutInflater inflater = this.getLayoutInflater();
		View gameView = inflater.inflate(R.layout.view_game, null);
		setContentView(gameView);
		BoardView boardView = (BoardView) (gameView
				.findViewById(R.id.view_board));
		Board board = new Board();
		board.initDrawPile(true);
		boardView.buildBoard(board);
		
		findViewById(R.id.board_actions).setOnDragListener(
				new SlidebarDragListener());

		SlidingUpPanelLayout slide = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

		((PlayerView) (slide.findViewById(R.id.playerview_slidebar_board)))
				.setPlayer(ActionController.user, true);

		Gallery g = (Gallery) findViewById(R.id.playerview_slider_board_cardgallery);
		g.setAdapter(new SliderbarCardGallery(this));
		g.setSelection(ActionController.user.getNumberOfCards() / 2);
		g.setOnDragListener(new SlideBarCardGalleryDragListener());
		
		//Number of card initialization
		TextView nbDis = ((TextView) findViewById(R.id.nbDiscardPileCards));
		TextView nbDra = ((TextView) findViewById(R.id.nbDrawPileCards));
		nbDis.setText(""+board.getDiscardPile().getNumberOfCards());
		nbDra.setText(""+board.getDrawPile().getNumberOfCards());
		ViewGroup parent = (ViewGroup)nbDis.getParent();
		parent.removeViewInLayout(nbDis);
		parent.addView(nbDis);
		parent.removeViewInLayout(nbDra);
		parent.addView(nbDra);
		
		// Setting up slider
		slide.setPanelSlideListener(new PanelSlideListener() {

			private boolean isExpanded = false;
			private int height = -1;

			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				if (height == -1) {
					if (findViewById(R.id.view_slidebar) != null) {
						height = findViewById(R.id.view_slidebar).getHeight();
					}
				}
			}

			@Override
			public void onPanelExpanded(View panel) {
				if (!isExpanded) {
					LinearLayout linear = (LinearLayout) findViewById(R.id.linear_slidinguppanel);
					linear.removeView(findViewById(R.id.view_slidebar));
					linear.removeView(findViewById(R.id.linear_slidebar_board));
					linear.addView(View.inflate(panel.getContext(),
							R.layout.view_slidebar_hand, null), 0,
							new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									height));
					linear.findViewById(R.id.hand_actions).setOnDragListener(
							new SlidebarDragListener());
					((PlayerView) (findViewById(R.id.playerview_slidebar_hand)))
							.setPlayer(ActionController.user, true);
					SlidingUpPanelLayout slide = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
					slide.setDragView(findViewById(R.id.hand_actions));
					isExpanded = true;
				}
			}

			@Override
			public void onPanelCollapsed(View panel) {
				if (isExpanded) {
					LinearLayout linear = (LinearLayout) findViewById(R.id.linear_slidinguppanel);
					linear.removeView(findViewById(R.id.view_slidebar));
					linear.removeView(findViewById(R.id.linear_slidebar_hand));
					linear.addView(View.inflate(panel.getContext(),
							R.layout.view_slidebar_board, null), 0,
							new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									height));
					linear.findViewById(R.id.board_actions).setOnDragListener(
							new SlidebarDragListener());
					((PlayerView) (findViewById(R.id.playerview_slidebar_board)))
							.setPlayer(ActionController.user, true);

					Gallery g = (Gallery) findViewById(R.id.playerview_slider_board_cardgallery);
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) g
							.getLayoutParams();
					params.width = Gallery.LayoutParams.WRAP_CONTENT;
					params.height = Gallery.LayoutParams.MATCH_PARENT;
					params.weight = 3.0f;
					g.setLayoutParams(params);
					g.setAdapter(new SliderbarCardGallery(panel.getContext()));
					g.setSelection(ActionController.user.getNumberOfCards() / 2);
					g.setOnDragListener(new SlideBarCardGalleryDragListener());
					SlidingUpPanelLayout slide = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
					slide.setDragView(findViewById(R.id.board_actions));
					isExpanded = false;
				}
			}

			@Override
			public void onPanelAnchored(View panel) {
			
			}
		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		OmecaPopupMenu.show(this);
	    return false;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof ConnectionWifiDirectEvent) {
			if (!wifiDirectManager.isHost()) {
				setContentView(R.layout.view_game);
			}
		}
	}

	@Override
	public void onBackPressed() {
		OmecaPopupMenu.show(this);
	}

	public void showDealPopup() {
		if (ActionController.board.getDrawPile().getNumberOfCards() > 0) {
			final DealView dv = new DealView(ActionController.board.getDrawPile()
					.getNumberOfCards(), ActionController.board.getPlayers()
					.size(), this.getApplicationContext());
			AlertDialog.Builder builder = new AlertDialog.Builder(
					GameActivity.this);
			builder.setTitle("Distribuer les cartes");
			builder.setView(dv);
			final AlertDialog alert = builder.create();
			alert.show();
			dv.buttonSave.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					controller.dealCard(dv.getDealNumber());
					alert.dismiss();
				}
			});
			dv.buttonCancle.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					alert.cancel();
				}
			});
		} else
			Toast.makeText(getApplicationContext(), "La pile est vide",
					Toast.LENGTH_LONG).show();

	}

	/**
	 * Opens the popup menu after the corresponding menu button have been
	 * pressed
	 * 
	 * @param view
	 */
	public void options(View view) {
		System.out.println("Options !!!");
		OmecaPopupMenu.show(this);
	}

	/**
	 * Exits the current game after the corresponding menu button have been
	 * pressed
	 * 
	 * @param view
	 */
	public void disconnect(View view) {
		System.out.println("Disconnecting !!!");
		OmecaPopupMenu.dismiss();

		this.finish();
	}

}
