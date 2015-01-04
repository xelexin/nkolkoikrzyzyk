/**
 * 
 */
package nkolkoikrzyzyk.events;


/**
 * @author Johhny
 *
 */
public class GameEndedEvent extends ProgramEvent 
{
	public enum EndState
	{ 
		PLAYER1_WON( "Player 1 won!" ),
		PLAYER2_WON( "Player 2 won!" ),
		DRAW( "Game ended with draw." );
		
		private final String str;
		private EndState( final String string )
		{
			this.str = string;
		}
		
		/**
		 * @return the str
		 */
		public String str() {
			return str;
		}
		
	}
	
	public EndState state;
	
	public GameEndedEvent( EndState state)
	{
		this.state = state;
	}
}
