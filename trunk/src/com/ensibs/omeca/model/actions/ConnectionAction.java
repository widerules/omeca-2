package com.ensibs.omeca.model.actions;

import java.util.Map.Entry;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.mod.WifiDirectMod;

/**
 * Event when a Player join the table
 * 
 * @author Nicolas
 * 
 */
public class ConnectionAction implements Action {

	private static final long serialVersionUID = 1L;

	private Player player;

	/**
	 * Constructor
	 * 
	 * @param user
	 *            The Player who is connecting
	 */
	public ConnectionAction(Player user) {
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
	 * Execute a new Player connection action
	 */
	@Override
	public void execute() {
		if (GameActivity.getActivity().getWifiDirectManager().getMod() == WifiDirectMod.HOST) {
			Player p = getPlayer();
			p.setId(GA.board.getPlayers().size());
			GA.board.addPlayerToTheFirstEmptyPlace(p);
			Card[] discardPileCards = new Card[GA.board
					.getDiscardPile().getCards().size()];
			Card[] drawPileCards = new Card[GA.board
					.getDrawPile().getCards().size()];
			Player[] players = new Player[Board.NB_PLAYER_MAX];
			int i = 0;
			for (Card c : GA.board.getDiscardPile().getCards()) {
				discardPileCards[i] = c;
				i++;
			}
			i = 0;
			for (Card c : GA.board.getDrawPile().getCards()) {
				drawPileCards[i] = c;
				i++;
			}

			for (Entry<Integer, Player> e : GA.board.getPlayers()
					.entrySet()) {
				players[e.getKey()] = e.getValue();
			}
			GameActivity
					.getActivity()
					.getWifiDirectManager()
					.sendEvent(
							new WifiDirectEventImpl(WifiDirectEvent.EVENT,
									new AknowlegmentConnectionAction(p,
											players, drawPileCards,
											discardPileCards)));
			GameActivity
					.getActivity()
					.getOmecaHandler()
					.sendEmptyMessage(
							OmecaHandler.AKNOWLEGMENT_CONNECTION_ACTION);
		}

	}
}
