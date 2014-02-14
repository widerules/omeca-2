package com.ensibs.omeca.model.actions;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.controller.OmecaHandler;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Player player;

	/**
	 * Constructor
	 * 
	 * @param user
	 *            player
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

	@Override
	public void execute() {
		if (GameActivity.getActivity().getWifiDirectManager().getMod() == WifiDirectMod.HOST) {
			Player p = getPlayer();
			p.setId(ActionController.board.getPlayers().size());
			ActionController.board.addPlayerToTheFirstEmptyPlace(p);
			GameActivity
					.getActivity()
					.getWifiDirectManager()
					.sendEvent(
							new WifiDirectEventImpl(WifiDirectEvent.EVENT,
									new AknowlegmentConnectionAction(p,
											ActionController.board)));
			GameActivity
					.getActivity()
					.getOmecaHandler()
					.sendEmptyMessage(
							OmecaHandler.AKNOWLEGMENT_CONNECTION_ACTION);
		}

	}
}
