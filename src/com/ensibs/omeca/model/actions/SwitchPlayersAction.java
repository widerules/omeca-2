package com.ensibs.omeca.model.actions;

import java.util.Map.Entry;

import android.util.Log;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Player;

/**
 * Event when two Players are switched
 * 
 * @author OMECA 2.0 Team (Raphaël GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 * 
 */
public class SwitchPlayersAction implements Action {

	private static final long serialVersionUID = 1L;

	private Player player1;
	private Player player2;
	private int position;

	/**
	 * Constructor
	 * 
	 * @param player1
	 *            First Player
	 * @param player2
	 *            Second Player
	 * @param pos
	 *            Position of the current Player
	 */
	public SwitchPlayersAction(Player player1, Player player2, int pos) {
		this.player1 = player1;
		this.player2 = player2;
		this.position = pos;
	}

	/**
	 * Getter on player1
	 * 
	 * @return player1
	 */
	public Player getP1() {
		return player1;
	}

	/**
	 * Getter on player2
	 * 
	 * @return player2
	 */
	public Player getP2() {
		return player2;
	}

	/**
	 * Getter on position
	 * 
	 * @return position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Execute the Player changing place action depending on the position of the
	 * current Player
	 */
	@Override
	public void execute() {
		Player p1 = getP1();
		Player p2 = getP2();
		if (p1 != null) {
			GA.board.switchPlayers(p1, p2);
		} else {
			GA.board.movePlayerTo(p2, getPosition());
		}

		for(Entry<Integer, Player> e : GA.board.getPlayers().entrySet())
			Log.w("Player", e.getValue()+ " "+e.getKey());
		GameActivity.getActivity().getOmecaHandler()
				.sendEmptyMessage(OmecaHandler.SWITCH_PLAYERS_ACTION);
	}

}
