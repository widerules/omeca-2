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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.model.actions.DisconnectionAction;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.utils.OmecaPopupMenu;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.utils.SlidingUpPanelLayout;
import com.ensibs.omeca.utils.SlidingUpPanelLayout.PanelSlideListener;
import com.ensibs.omeca.view.BoardView;
import com.ensibs.omeca.view.DealView;
import com.ensibs.omeca.view.HandView;
import com.ensibs.omeca.view.PlayerView;
import com.ensibs.omeca.view.SlideBarCardGalleryDragListener;
import com.ensibs.omeca.view.SlidebarDragListener;
import com.ensibs.omeca.wifidirect.WifiDirectManager;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

public class GameActivity extends Activity implements Observer {

	WifiDirectManager wifiDirectManager;
	ActionController controller;
	AlertDialog popupMenu;
	OmecaApplication app;
	private static GameActivity instance;

	public static GameActivity getActivity() {
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
		 wifiDirectManager = app.getWifiDirectManager();
		 wifiDirectManager.addObserver(this);
		 wifiDirectManager.setApplicationContext(this);

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
		
		findViewById(R.id.expand).setOnDragListener(
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
		
		// Seekbar
		HandView handView = (HandView)findViewById(R.id.view_hand_slidebar);
		((SeekBar)findViewById(R.id.cards_zoom_seekbar)).setOnSeekBarChangeListener(handView.new CardsZoomSeekbarChangeListener());
		
		// Setting up slider
		slide.setDragView(findViewById(R.id.expand));
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
					linear.findViewById(R.id.collapse).setOnDragListener(
							new SlidebarDragListener());
					((PlayerView) (findViewById(R.id.playerview_slidebar_hand)))
							.setPlayer(ActionController.user, true);
					SlidingUpPanelLayout slide = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
					slide.setDragView(findViewById(R.id.collapse));
					HandView handView = (HandView)findViewById(R.id.view_hand_slidebar);
					((ImageView)(linear.findViewById(R.id.button_value_order))).setOnTouchListener(handView.new OrderByValueTouchListener());
					((ImageView)(linear.findViewById(R.id.button_total_order))).setOnTouchListener(handView.new TotalOrderTouchListener());
					((ImageView)(linear.findViewById(R.id.button_color_order))).setOnTouchListener(handView.new OrderByColorTouchListener());
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
					linear.findViewById(R.id.expand).setOnDragListener(
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
					slide.setDragView(findViewById(R.id.expand));
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
		if (data instanceof WifiDirectEventImpl && ((WifiDirectEventImpl)data).getEvent() == WifiDirectEvent.EVENT) {
			WifiDirectEventImpl event = (WifiDirectEventImpl)data;
			if(event.getData() instanceof DisconnectionAction){
				Toast.makeText(this, "Deconnection", Toast.LENGTH_LONG);
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
	
	public void finishGameActivity(){
		this.wifiDirectManager.sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new DisconnectionAction(ActionController.user.getId())));
		this.wifiDirectManager.disconnect();
		this.finish();
	}

}
