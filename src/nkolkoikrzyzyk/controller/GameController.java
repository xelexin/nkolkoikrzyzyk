/**
 * 
 */
package nkolkoikrzyzyk.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import nkolkoikrzyzyk.controller.GameEndedEvent.EndState;
import nkolkoikrzyzyk.events.BoardClickedEvent;
import nkolkoikrzyzyk.events.GameTerminatedEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.GameModel;
import nkolkoikrzyzyk.model.NeuralNetworkPlayer;
import nkolkoikrzyzyk.model.Player;
import nkolkoikrzyzyk.view.GameWindow;
import nkolkoikrzyzyk.view.StartGameEvent;

/**
 * @author Johhny
 *
 */
public class GameController
{
	private Player player1;
	private Player player2;
	
	private Player activePlayer;
	
	/** reference to MVC View*/
	private final GameWindow view;
	/** reference to MVC Model*/
	private final GameModel model;
	/**application queue for ProgramEvent.*/
	private final BlockingQueue<ProgramEvent> appQueue;
	/**queue for GameEvent.*/
	private final BlockingQueue<ProgramEvent> gameQueue;
	/**mapping of objects ProgramEvent to objects ProgramAction*/
	private final Map<Class<? extends ProgramEvent>, ProgramAction> eventActionMap;
	
	private boolean waitingForHuman;

	/**
	 * creates object of Controller type
	 *
	 */
	public GameController(GameWindow view, GameModel model, BlockingQueue<ProgramEvent> gameQueue, BlockingQueue<ProgramEvent> appQueue, Player player1, Player player2) 
	{
		this.player1 = player1;
		this.player2 = player2;
		this.activePlayer = player1;
		
		this.model = model;
		this.appQueue = appQueue;
		this.gameQueue = gameQueue;
		this.view = view;
		this.eventActionMap = new HashMap<Class<? extends ProgramEvent>, ProgramAction>();
		this.fillEventActionMap();
	}

	/**
	 * infinite loop to intercept events from view
	 * <br> it takes events from blockingQueue and using eventActionMap
	 * starts action handling this event
	 * @throws InterruptedException
	 */
	public void work() throws InterruptedException
	{
		ProgramEvent event = null;
		while(true)
		{
			try
			{
				event = this.gameQueue.take();
				ProgramAction action = this.eventActionMap.get(event.getClass());
				action.go(event);
				if(event.getClass() == GameTerminatedEvent.class)
					break;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		System.out.println("Controlls returned.");
	}

	/**
	 * fills container eventActionMap
	 */
	private void fillEventActionMap()
	{
		this.eventActionMap.put( BoardClickedEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event )
			{
				BoardClickedEvent bCE = (BoardClickedEvent) event;
				System.out.println("Game #"+bCE.gameId+" Clicked: " + bCE.position);
				if( waitingForHuman )
				{
					GameController.this.gameQueue.add( new MoveMadeEvent(activePlayer.makeMove(model.getBoard(), bCE.position)));
					waitingForHuman = false;
				}
			}
		});
		
		this.eventActionMap.put( GameTerminatedEvent.class, new ProgramAction()
		{
			@Override
			public void go( ProgramEvent event )
			{
				GameTerminatedEvent gTE = (GameTerminatedEvent) event;
				System.out.println("Game #"+ model.getId() +" terminated. Returning controlls to AppController.");
			}
		});
		
		this.eventActionMap.put( StartGameEvent.class, new ProgramAction() 
		{
			@Override
			public void go(ProgramEvent event) {
				StartGameEvent sGE = (StartGameEvent) event;
				System.out.println("Game #" + model.getId() + " started.");
				GameController.this.playTurn();
			}
		});
		
		this.eventActionMap.put( GameEndedEvent.class, new ProgramAction() 
		{
			@Override
			public void go(ProgramEvent event) {
				GameEndedEvent gEE = (GameEndedEvent) event;
				System.out.println("Game #" + model.getId() + " ended.");
				view.setMessage( gEE.state.str());
			}
		});
		
		this.eventActionMap.put( MoveMadeEvent.class, new ProgramAction() 
		{
			@Override
			public void go(ProgramEvent event) {
				MoveMadeEvent mME = (MoveMadeEvent) event;
				System.out.println("Move made by " + activePlayer.getName());
				endTurn(mME.newBoard);
			}
		});
	}
	
	private void playTurn() 
	{
		if( activePlayer.getClass() == NeuralNetworkPlayer.class )
			gameQueue.add( new MoveMadeEvent( activePlayer.makeMove( model.getBoard(), -1 ) ) );
		else
			waitingForHuman = true;
	}

	private void endTurn( int[] newBoard) 
	{
		model.setBoard(newBoard);	

		this.view.refresh( newBoard );
		this.model.printBoard();
		
		if(model.ifWin()==true)
		{
			System.out.println(activePlayer.getName() + " won!");
			activePlayer.youWin(newBoard);
			gameQueue.add( new GameEndedEvent(activePlayer==player2?EndState.PLAYER1_WON:EndState.PLAYER2_WON) );
			return;
		}
			
		if(model.noMoreMove()==true)
		{
			player1.youLost();
			player2.youLost();
			System.out.println("Remis");
			gameQueue.add( new GameEndedEvent(EndState.DRAW) );
			return;
		}		
		//swapping players
		activePlayer = activePlayer==player2?player1:player2;
		playTurn();
	}
}
