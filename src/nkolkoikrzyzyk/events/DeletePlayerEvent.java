/**
 * 
 */
package nkolkoikrzyzyk.events;

import nkolkoikrzyzyk.controller.players.Player;

/**
 * @author elohhim
 *
 */
public class DeletePlayerEvent extends ProgramEvent 
{
	public final Player player;
	
	public DeletePlayerEvent(Player p) 
	{
		this.player = p;
	}
}
