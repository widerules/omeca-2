package com.ensibs.omeca.model.actions;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;

/**
 * Event when a Player exit the table
 * @author Nicolas
 *
 */
public class DisconnectionAction implements Action{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Player player;
	
	/**
	 * Constructor
	 * @param user player
	 */
	public DisconnectionAction(Player user){
		this.player = user;
	}

	/**
	 * Getter on player
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	@Override
	public void execute() {
		Player p = getPlayer();
		for (Card c : p.getCards()) {
			ActionController.board.getDiscardPile().addCard(c);
		}
		ActionController.board.removePlayer(ActionController.board
				.getPlace(p));
		GameActivity.getActivity().getOmecaHandler().sendEmptyMessage(OmecaHandler.DECONNEXION);
	}

}
