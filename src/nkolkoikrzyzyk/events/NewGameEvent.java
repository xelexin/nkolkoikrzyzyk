/**
 * 
 */
package nkolkoikrzyzyk.events;

import nkolkoikrzyzyk.controller.players.Player;

/**
 * @author Johhny
 *
 */
public class NewGameEvent extends ProgramEvent {

	public final Player player1;
	public final Player player2;
	
	public NewGameEvent(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

}
