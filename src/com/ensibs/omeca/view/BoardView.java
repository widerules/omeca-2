package com.ensibs.omeca.view;

import java.util.Hashtable;
import java.util.Map.Entry;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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
import com.ensibs.omeca.model.actions.MoveCardAction;
import com.ensibs.omeca.model.actions.ReturnCardAction;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

public class BoardView extends RelativeLayout {
	private Context context;
	private DrawPileView drawPileView;
	private DiscardPileView discardPileView;
	private Hashtable<Integer, PlayerView> playerViews;

	public BoardView(Context context) {
		super(context);
		init(context);
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
		this.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				//DealPopup.show(context);
				distribTo(0, 4*2);
				/*DistribTask dt = new DistribTask();
				dt.setParameter(1, 5, (BoardView)v);
				dt.execute();*/
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
						int height = 2 + metrics.heightPixels / CardView.SIZE;
						int width = (int) (2 + (height / CardView.RATIO));
						view.setLayoutParams(new RelativeLayout.LayoutParams(
								width, height));
						CardView card = (CardView) view;
						card.setOnTouchListener(card.new CardTouchListener());
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
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("Player", ActionController.user.getId(), view.getCard(), "BoardView")));						
					}
					else if(parent instanceof DrawPileView){
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("DrawPileView", "BoardView",view.getCard())));
					}
					else if(parent instanceof DiscardPileView){
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("DiscardPileView", "BoardView",view.getCard())));
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
	
	public void giveTo(final int idUser){
		final CardView vToMove = (CardView) drawPileView.getChildAt(0);	
		drawPileView.removeViewInLayout(vToMove);
		addView(vToMove);
		int x=0 , y =0;
		if( idUser == ActionController.user.getId()){
			x = this.getWidth()/2;
			y= this.getHeight();
		}else{
			x = (int) playerViews.get(idUser).getX();
			y= (int) playerViews.get(idUser).getY();
			Toast.makeText(context, playerViews.get(idUser).getPlayer().getName()  ,Toast.LENGTH_LONG).show();
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
				endAnim(idUser, vToMove);
			}
		});
		anim.setFillAfter(true);
		anim.setInterpolator(new DecelerateInterpolator(1.0f));
		anim.setDuration(400);
		vToMove.startAnimation(anim);
	}
	
	public void endAnim(int idUser, CardView vToMove ){
		if( idUser != ActionController.user.getId()){
			playerViews.get(idUser).getPlayer().addCard(vToMove.getCard());
			//TODO ajouter la fonction dans la player view pour lui ajouter une carte
			playerViews.get(idUser).setPlayer(playerViews.get(idUser).getPlayer(), false);
		}else
			ActionController.user.addCard(vToMove.getCard()); //TODO updater la slidBar et handView
		this.removeView(vToMove);
	}
	 
	
	private void distribTo( final int toId, final int nbCard) {
		if(nbCard> 0){ // on a pas fini
			//recup�ration de la cartes
			final CardView vToMove = (CardView) drawPileView.getChildAt(0);	
			drawPileView.removeViewInLayout(vToMove);
			addView(vToMove);
			
			// on choisi ca direction 
			int x=0 , y =0;
			if( toId == ActionController.user.getId()){
				x = this.getWidth()/2;
				y= this.getHeight();
			}else{
				x = (int) playerViews.get(toId).getX();
				y= (int) playerViews.get(toId).getY();
				Toast.makeText(context, playerViews.get(toId).getPlayer().getName()  ,Toast.LENGTH_LONG).show();
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
					newdistrib(toId, nbCard, vToMove);
				}
			});
			anim.setFillAfter(true);
			anim.setInterpolator(new DecelerateInterpolator(1.0f));
			anim.setDuration(400);
			vToMove.startAnimation(anim);
			
		}
	}
	
	private void newdistrib(int toId, int nb, CardView vToMove ){	
		// fin du traitement de la carte
		if( toId != ActionController.user.getId()){
			playerViews.get(toId).getPlayer().addCard(vToMove.getCard());
			//TODO ajouter la fonction dans la player view pour lui ajouter une carte
			playerViews.get(toId).setPlayer(playerViews.get(toId).getPlayer(), false);
		}else
			ActionController.user.addCard(vToMove.getCard()); //TODO updater la slidBar et handView
		this.removeView(vToMove);
		
		int pos=ActionController.board.getPlace(playerViews.get(toId).getPlayer());
		if(pos ==ActionController.board.getPlayers().size())
			distribTo(ActionController.board.getPlayers().get(0).getId(), nb--);
		else distribTo( ActionController.board.getPlayers().get(pos+1).getId()  , nb--);
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
					/* un tour de distribution 
					if(distributor == ActionController.user.getId()){ // distribution normal
						for(Entry<Integer, PlayerView> e : bv.getPlayerViews().entrySet()){
							if(e.getValue().getPlayer()!=null)
								bv.giveTo(e.getValue().getPlayer().getId());
						}
						bv.giveTo(distributor);
					}else{
						for(Entry<Integer, PlayerView> e : bv.getPlayerViews().entrySet()){
							if(e.getKey()> distributor && e.getValue().getPlayer()!=null)
								bv.giveTo(e.getValue().getPlayer().getId());
						}
						bv.giveTo(distributor);
						for(Entry<Integer, PlayerView> e : bv.getPlayerViews().entrySet()){
							if(e.getKey()<= distributor && e.getValue().getPlayer()!=null)
								bv.giveTo(e.getValue().getPlayer().getId());
						}
					}*/
					bv.giveTo(ActionController.user.getId());
				}
				return null;
			}
			
			public void giveTo(final int idUser){
				final CardView vToMove = (CardView) bv.getDrawPileView().getChildAt(0);	
				 bv.getDrawPileView().removeViewInLayout(vToMove);
				bv.addView(vToMove);
				int x=0 , y =0;
				if( idUser == ActionController.user.getId()){
					x = bv.getWidth()/2;
					y= bv.getHeight();
				}else{
					x = (int) bv.getPlayerViews().get(idUser).getX();
					y= (int) bv.getPlayerViews().get(idUser).getY();
					Toast.makeText(bv.context, bv.getPlayerViews().get(idUser).getPlayer().getName()  ,Toast.LENGTH_LONG).show();
				}	
				TranslateAnimation anim=new TranslateAnimation(  bv.getDrawPileView().getX(), x,  bv.getDrawPileView().getY(), y);
				anim.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						endAnim(idUser, vToMove);
					}
				});
				anim.setFillAfter(true);
				anim.setInterpolator(new DecelerateInterpolator(1.0f));
				anim.setDuration(400);
				vToMove.startAnimation(anim);
			}
			public void endAnim(int idUser, CardView vToMove ){
				if( idUser != ActionController.user.getId()){
					bv.getPlayerViews().get(idUser).getPlayer().addCard(vToMove.getCard());
					//TODO ajouter la fonction dans la player view pour lui ajouter une carte
					bv.getPlayerViews().get(idUser).setPlayer(bv.getPlayerViews().get(idUser).getPlayer(), false);
				}else
					ActionController.user.addCard(vToMove.getCard()); //TODO updater la slidBar et handView
				bv.removeView(vToMove);
			}
			
		}
	
}

