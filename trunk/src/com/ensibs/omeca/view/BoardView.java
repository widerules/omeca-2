package com.ensibs.omeca.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.RelativeLayout;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.view.HandView.HandCardsAdapter;

public class BoardView extends RelativeLayout{
	private Context context;
	private DrawPileView drawPileView;
	private DiscardPileView discardPileView;
	
	public BoardView(Context context) {
	    super(context);
	    init(context);
	}

	public BoardView(Context context, AttributeSet attrs) {
	    this(context, attrs,0);
	    init(context);
	}

	public BoardView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    init(context);
	}
	
	private void init(Context context) {
		this.context = context;
		RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
		setOnDragListener(new BoardDragListener());
		
	}
	
	public void buildBoard(Board board){
		this.drawPileView = new DrawPileView(context, board.getDrawPile());
		this.discardPileView = new DiscardPileView(context, board.getDiscardPile());
		addView(drawPileView);
		addView(discardPileView);
		RelativeLayout players_left = (RelativeLayout)findViewById(R.id.players_left);
		RelativeLayout players_right = (RelativeLayout)findViewById(R.id.players_right);
		
		PlayerView player = new PlayerView(context);
		RelativeLayout.LayoutParams params;
		
		Player p = null;
		
		//Player 1
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		players_left.addView(player, params);		
		p = new Player("Jean", 5, 1);
		player.setPlayer(p, false);
		
		
		//Player 2
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		players_left.addView(player, params);		
		p = new Player("Emile", 7, 2);
		player.setPlayer(p, false);
		
		//Player 3
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		players_left.addView(player, params);	
		p = new Player("Etienne", 2, 3);
		player.setPlayer(p, false);
		
		//Player 4
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		addView(player, params);	
		p = new Player("Lucienne", 14, 4);
		player.setPlayer(p, false);
		
		//Player 5
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		players_right.addView(player, params);	
		p = new Player("Jacquie", 10, 5);
		player.setPlayer(p, false);
		
		//Player 6
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		players_right.addView(player, params);	
				
		//Player 7
		player = new PlayerView(context);
		params = (RelativeLayout.LayoutParams) player.getLayoutParams();
		params.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		params.addRule(ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		players_right.addView(player, params);
	}
	
	public DrawPileView getDrawPileView() {
		return drawPileView;
	}

	public DiscardPileView getDiscardPileView() {
		return discardPileView;
	}

	private class BoardDragListener implements OnDragListener{

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
					CardView view = (CardView) event.getLocalState();
					ViewGroup parent = (ViewGroup)(view.getParent());
			        parent.removeViewInLayout(view);
					addView(view);
					if(parent instanceof HandView){
						ActionController.user.removeCard(view.getCard());
						Gallery g = (Gallery)parent;
						HandCardsAdapter a = (HandCardsAdapter)g.getAdapter();
						a.notifyDataSetChanged();
						g.setSelection(ActionController.user.getNumberOfCards()/2);
						DisplayMetrics metrics = GameActivity.getActivity().getApplicationContext().getResources().getDisplayMetrics();
						int height = 2+metrics.heightPixels/CardView.SIZE;
						int width = (int) (2+(height/CardView.RATIO));
						view.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
						CardView card = (CardView) view;
						card.setOnTouchListener(card.new CardTouchListener());
						card.setOnDragListener(null);
						Gallery g2 = ((Gallery)((View)v.getParent()).findViewById(R.id.playerview_slider_board_cardgallery));
						if(g2 != null){
							SliderbarCardGallery a2 = (SliderbarCardGallery) g2.getAdapter();
							a2.notifyDataSetChanged();
						}
					}
					MarginLayoutParams marginParams = new MarginLayoutParams(view.getLayoutParams());
					int left = (int)(event.getX() - (view.getWidth()/2));
					int top = (int)(event.getY() - (view.getHeight()/2));
					int right = (int)(((View)view.getParent()).getWidth() - left + view.getWidth());
					int bottom = (int)(((View)view.getParent()).getHeight() - top + view.getHeight());
					marginParams.setMargins(left, top, right,bottom);
					view.setLayoutParams(new RelativeLayout.LayoutParams(marginParams));
					for(int i=0;i<getChildCount();i++){
						getChildAt(i).setVisibility(View.VISIBLE);
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
	
}
