/**
 * 
 */
package nkolkoikrzyzyk.events;

import nkolkoikrzyzyk.controller.players.Player;

/**
 * @author elohhim
 *
 */
public class NewPlayerEvent extends ProgramEvent 
{

	public final Player player;
	
	public NewPlayerEvent(Player player)
	{
		this.player = player;
	}

}
