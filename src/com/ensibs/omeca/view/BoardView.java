package com.ensibs.omeca.view;

import java.util.Hashtable;
import java.util.Map.Entry;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Gallery;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.actions.MoveCardAction;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.DealPopup;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;


public class BoardView extends RelativeLayout {
	private Context context;
	private DrawPileView drawPileView;
	private DiscardPileView discardPileView;
	private Hashtable<Integer, PlayerView> playerViews;


	private DistribTask distribTask;


	public void setDistribTask(int from, int nbCard) {
		this.distribTask.setParameter(from, nbCard, this);
		distribTask.execute();
	}


	public BoardView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init(context);
	}

	public BoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		RelativeLayout.LayoutParams params = new LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
		setOnDragListener(new BoardDragListener());
		playerViews = new Hashtable<Integer, PlayerView>();
	}

	public void buildBoard(Board board) {
		this.drawPileView = new DrawPileView(context, board.getDrawPile());
		this.discardPileView = new DiscardPileView(context,
				board.getDiscardPile());
		addView(drawPileView);
		addView(discardPileView);
		distribTask = new DistribTask();
		this.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DealPopup.show(context);
				return true;
			}
		});
		displayPlayers();
	}

	public void displayPlayers() {

		RelativeLayout players_left = (RelativeLayout) findViewById(R.id.players_left);
		RelativeLayout players_right = (RelativeLayout) findViewById(R.id.players_right);

		PlayerView player = new PlayerView(context);
		RelativeLayout.LayoutParams params;

		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		players_left.addView(player, params);
		playerViews.put(1, player);

		// Player 2
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		players_left.addView(player, params);
		playerViews.put(2, player);

		// Player 3
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		players_left.addView(player, params);
		playerViews.put(3, player);


		// Player 4
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		addView(player, params);
		playerViews.put(4, player);

		// Player 5
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		players_right.addView(player, params);
		playerViews.put(5, player);

		// Player 6
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		players_right.addView(player, params);
		playerViews.put(6, player);

		// Player 7
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		players_right.addView(player, params);
		playerViews.put(7, player);


	}

	public void updatePlayers(){
		int myPlace = ActionController.board.getPlace(ActionController.user);
		Player p;
		for(int i=1; i<Board.NB_PLAYER_MAX;i++){
			myPlace = (myPlace >=Board.NB_PLAYER_MAX-1) ? 0 : myPlace+1;
			p = ActionController.board.getPlayers().get(myPlace);
			playerViews.get(i).setPlayer(p, false);

		}
	}

	public DrawPileView getDrawPileView() {
		return drawPileView;
	}

	public DiscardPileView getDiscardPileView() {
		return discardPileView;
	}

	public Hashtable<Integer, PlayerView> getPlayerViews() {
		return playerViews;
	}
	
	public int getPlayerViewPosition(PlayerView pv){
		for(Entry<Integer, PlayerView> e : playerViews.entrySet()){
			if(pv == e.getValue())
				return e.getKey();
		}
		return -1;
	}

	public void setPlayerViews(Hashtable<Integer, PlayerView> playerViews) {
		this.playerViews = playerViews;
	}

	private class BoardDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				View vTmp = (View) event.getLocalState();
				if (vTmp instanceof CardView) {
					CardView view = (CardView) vTmp;
					ViewGroup parent = (ViewGroup) (view.getParent());
					parent.removeViewInLayout(view);
					addView(view);
					if (parent instanceof HandView) {
						DisplayMetrics metrics = GameActivity.getActivity()
								.getApplicationContext().getResources()
								.getDisplayMetrics();
						CardView card = (CardView) view;
						card.setOnDragListener(null);
						Gallery g2 = (Gallery) GameActivity
								.getActivity()
								.findViewById(
										R.id.playerview_slider_board_cardgallery);
						if (g2 != null) {
							SliderbarCardGallery a2 = (SliderbarCardGallery) g2
									.getAdapter();
							a2.notifyDataSetChanged();
						}
					}
					
					MarginLayoutParams marginParams = new MarginLayoutParams(
							view.getLayoutParams());
					int left = (int) (event.getX() - (view.getWidth() / 2));
					int top = (int) (event.getY() - (view.getHeight() / 2));
					int right = (int) (((View) view.getParent()).getWidth()
							- left + view.getWidth());
					int bottom = (int) (((View) view.getParent()).getHeight()
							- top + view.getHeight());
					marginParams.setMargins(left, top, right, bottom);
					view.setLayoutParams(new RelativeLayout.LayoutParams(
							marginParams));
					
					if (parent instanceof HandView) {
						MoveCardAction movecard = new MoveCardAction("Player", ActionController.user.getId(), view.getCard(), "BoardView");
						movecard.setPourcentageX(left*100/((View) view.getParent()).getWidth());
						movecard.setPourcentageY(top*100/((View) view.getParent()).getHeight());
						Log.i(WifiDirectProperty.TAG, movecard.getPourcentageX()+" "+movecard.getPourcentageY());
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, movecard));						
					}
					else if(parent instanceof DrawPileView){
						MoveCardAction movecard = new MoveCardAction("DrawPileView", "BoardView",view.getCard());
						movecard.setPourcentageX(left*100/((View) view.getParent()).getWidth());
						movecard.setPourcentageY(top*100/((View) view.getParent()).getHeight());
						Log.i(WifiDirectProperty.TAG, movecard.getPourcentageX()+" "+movecard.getPourcentageY());
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, movecard));
					}
					else if(parent instanceof DiscardPileView){
						MoveCardAction movecard = new MoveCardAction("DiscardPileView", "BoardView",view.getCard());
						movecard.setPourcentageX(left*100/((View) view.getParent()).getWidth());
						movecard.setPourcentageY(top*100/((View) view.getParent()).getHeight());
						Log.i(WifiDirectProperty.TAG, movecard.getPourcentageX()+" "+movecard.getPourcentageY());
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, movecard));
					}
					
					for (int i = 0; i < getChildCount(); i++) {
						getChildAt(i).setVisibility(View.VISIBLE);
					}
				} else {
					vTmp.setVisibility(View.VISIBLE);
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				break;
			default:
				break;
			}
			return true;
		}
	}

	public void giveTo(final int playerPlace){
		final CardView vToMove = (CardView) drawPileView.getChildAt(0);	
		drawPileView.removeViewInLayout(vToMove);
		addView(vToMove);
		int x=0 , y =0;
		final int placeView = (playerPlace - ActionController.board.getPlace(ActionController.user)<0) ? 
				playerPlace - ActionController.board.getPlace(ActionController.user)+Board.NB_PLAYER_MAX 
				: playerPlace - ActionController.board.getPlace(ActionController.user);
		if( playerPlace == ActionController.board.getPlace(ActionController.user)){
			x = this.getWidth()/2;
			y= this.getHeight();
		}else{
			
			x = (int) playerViews.get(placeView).getX();
			y= (int) playerViews.get(placeView).getY();
		}	
		TranslateAnimation anim=new TranslateAnimation( drawPileView.getX(), x, drawPileView.getY(), y);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Log.i(WifiDirectProperty.TAG, "lancer "+ placeView);
				endAnim(placeView, vToMove);
			}
		});
		anim.setFillAfter(true);
		anim.setInterpolator(new DecelerateInterpolator(1.0f));
		anim.setDuration(400);
		vToMove.startAnimation(anim);
	}

	public void endAnim(int placePlayer, CardView vToMove ){
		Log.i(WifiDirectProperty.TAG, "endAnim "+ placePlayer);
		if( placePlayer != 0){
			playerViews.get(placePlayer).getPlayer().addCard(vToMove.getCard());
			//TODO ajouter la fonction dans la player view pour lui ajouter une carte
			playerViews.get(placePlayer).setPlayer(playerViews.get(placePlayer).getPlayer(), false);
		}else{
			ActionController.user.addCard(vToMove.getCard()); //TODO updater la slidBar et handView
			Gallery gallery = (Gallery) GameActivity.getActivity().findViewById(R.id.playerview_slider_board_cardgallery);
			SliderbarCardGallery adapter = (SliderbarCardGallery)gallery.getAdapter();
			adapter.notifyDataSetChanged();
			gallery.setSelection(ActionController.user.getNumberOfCards()-1);
			HandView handView = (HandView) GameActivity.getActivity().findViewById(R.id.handview);
			handView.updateView(true);
		}
		this.removeView(vToMove);
	}


	//L'AsyncTask de distribution
	static class DistribTask extends AsyncTask<Void, Integer, Boolean> {
		private int distributor;
		private int numberCard; // numbre de carte par personne
		private BoardView bv;

		public void setParameter( int from, int card, BoardView bv ) {
			this.distributor = from;
			numberCard = card;
			this.bv = bv;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			for (int i = numberCard; i>0; i--){
				int pos;
				// récupération de la position de l'utilisateur 
				if (distributor== ActionController.user.getId())
					pos = ActionController.board.getPlace(ActionController.user);
				else 
					pos=ActionController.board.getPlace(bv.playerViews.get(distributor).getPlayer());
				Log.i(WifiDirectProperty.TAG, "pos "+pos);
				Player p;
				for( int j = pos+1; j<8; j++){
					Message msgObj = GameActivity.getActivity().getOmecaHandler().obtainMessage();
					Bundle b = new Bundle();
					p =ActionController.board.getPlayers().get(j);
					if(p !=null){
						Log.i(WifiDirectProperty.TAG, "parcouru "+p.getName());
						b.putInt("playerPlace", j);
						msgObj.what = OmecaHandler.GIVETO;
						msgObj.setData(b);
						GameActivity.getActivity().getOmecaHandler().sendMessage(msgObj);
					}
					
				}
				Message msgObj1 = GameActivity.getActivity().getOmecaHandler().obtainMessage();
				Bundle b1 = new Bundle();
				b1.putInt("playerPlace", distributor);
				msgObj1.what = OmecaHandler.GIVETO;
				msgObj1.setData(b1);
				GameActivity.getActivity().getOmecaHandler().sendMessage(msgObj1);
				
				
				for( int j = 0; j<distributor; j++){
					Message msgObj = GameActivity.getActivity().getOmecaHandler().obtainMessage();
					Bundle b = new Bundle();
					p =ActionController.board.getPlayers().get(j);
					if(p !=null){
						Log.i(WifiDirectProperty.TAG, "parcouru "+p.getName());
						b.putInt("playerPlace", j);
						msgObj.what = OmecaHandler.GIVETO;
						msgObj.setData(b);
						GameActivity.getActivity().getOmecaHandler().sendMessage(msgObj);
					}
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
		return null;
	}
	}
}



			
		