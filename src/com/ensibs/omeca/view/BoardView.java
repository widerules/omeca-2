package com.ensibs.omeca.view;

import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class BoardView extends RelativeLayout{
	private Board board;
	private Context context;
	
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
	
	public void builtBoard(Board board){
		Log.w("context",context.toString());
		DrawPileView drawPileView = new DrawPileView(context, board.getDrawPile());
		DiscardPileView discardPileView = new DiscardPileView(context, board.getDiscardPile());
		addView(drawPileView);
		addView(discardPileView);
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
					View view = (View) event.getLocalState();
					ViewGroup parent = (ViewGroup)(view.getParent());
			        parent.removeView(view);
					RelativeLayout container = (RelativeLayout) v;
					container.addView(view);
					MarginLayoutParams marginParams = new MarginLayoutParams(view.getLayoutParams());
					int left = (int)(event.getX() - (view.getWidth()/2));
					int top = (int)(event.getY() - (view.getHeight()/2));
					int right = (int)(((View)view.getParent()).getWidth() - left + view.getWidth());
					int bottom = (int)(((View)view.getParent()).getHeight() - top + view.getHeight());
					marginParams.setMargins(left, top, right,bottom);
					view.setLayoutParams(new RelativeLayout.LayoutParams(marginParams));
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
	
}
