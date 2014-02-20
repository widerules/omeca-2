package com.ensibs.omeca.model.actions;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;

/**
 * Event when a Player exit the table
 * 
 * @author OMECA 2.0 Team (Raphael GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 * 
 */
public class DisconnectionAction implements Action {

	private static final long serialVersionUID = 1L;

	private Player player;

	/**
	 * Constructor
	 * 
	 * @param user
	 *            The Player who is disconnecting
	 */
	public DisconnectionAction(Player user) {
		this.player = user;
	}

	/**
	 * Getter on player
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Execute a Player disconnection action
	 */
	@Override
	public void execute() {
		Player p = getPlayer();
		for (Card c : p.getCards()) {
			GA.board.getDiscardPile().addCard(c);
		}
		GA.board.removePlayer(GA.board.getPlace(p));
		GameActivity.getActivity().getOmecaHandler()
				.sendEmptyMessage(OmecaHandler.DECONNEXION);
	}

}
