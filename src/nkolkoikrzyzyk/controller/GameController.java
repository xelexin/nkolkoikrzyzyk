/**
 * 
 */
package nkolkoikrzyzyk.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import nkolkoikrzyzyk.controller.players.HumanPlayer;
import nkolkoikrzyzyk.controller.players.Player;
import nkolkoikrzyzyk.events.NewGameEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.game.BoardClickedEvent;
import nkolkoikrzyzyk.events.game.GameEndedEvent;
import nkolkoikrzyzyk.events.game.GameEndedEvent.EndState;
import nkolkoikrzyzyk.events.game.GameRestartEvent;
import nkolkoikrzyzyk.events.game.GameTerminatedEvent;
import nkolkoikrzyzyk.events.game.MoveMadeEvent;
import nkolkoikrzyzyk.events.game.RematchEvent;
import nkolkoikrzyzyk.events.game.StartGameEvent;
import nkolkoikrzyzyk.model.GameModel;
import nkolkoikrzyzyk.view.game.GameWindow;

/**
 * @author Johhny
 *
 */
public class GameController
{
	private static final int DELAY = 100;
	
	private enum ActivePlayer 
	{
		PLAYER1("Player 1"),
		PLAYER2("Player 2");
		
		private final String str;
		
		public String str()
		{
			return str;
		}
				
		private ActivePlayer( final String string)
		{
			this.str = string;
		}
	}
	
	private Player player1;
	private Player player2;
	
	private ActivePlayer activePlayer;
	
	private Player activePlayer()
	{
		return activePlayer==ActivePlayer.PLAYER1?player1:player2;
	}
	
	private Player waitingPlayer()
	{
		return activePlayer==ActivePlayer.PLAYER2?player1:player2;
	}
	
	private void swapPlayers()
	{
		activePlayer=activePlayer==ActivePlayer.PLAYER2?ActivePlayer.PLAYER1:ActivePlayer.PLAYER2;
	}
	
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
		this.activePlayer = ActivePlayer.PLAYER1;
		
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
					GameController.this.gameQueue.add( 
							new MoveMadeEvent(activePlayer().makeMove(model.getBoard(), bCE.position)));
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
				GameController.this.view.dispose();
			}
		});
		
		this.eventActionMap.put( StartGameEvent.class, new ProgramAction() 
		{
			@Override
			public void go(ProgramEvent event) {
				StartGameEvent sGE = (StartGameEvent) event;
				System.out.println("Game #" + model.getId() + " started.");
				view.setBoardEnabled(true);
				GameController.this.startTurn();
			}
		});
		
		this.eventActionMap.put( GameEndedEvent.class, new ProgramAction() 
		{
			@Override
			public void go(ProgramEvent event) {
				GameEndedEvent gEE = (GameEndedEvent) event;
				System.out.println("Game #" + model.getId() + " ended.");
				
				view.setMessage( gEE.state.str());
				view.refresh(model.getBoard());
				
				view.setBoardEnabled(false);
			}
		});
		
		this.eventActionMap.put( MoveMadeEvent.class, new ProgramAction() 
		{
			@Override
			public void go(ProgramEvent event) {
				MoveMadeEvent mME = (MoveMadeEvent) event;
				System.out.println("Move made by " + activePlayer().getName());
				endTurn(mME.newBoard);
			}
		});
		
		this.eventActionMap.put( GameRestartEvent.class, new ProgramAction() 
		{
			@Override
			public void go(ProgramEvent event) {
				GameRestartEvent gRE = (GameRestartEvent) event;
				System.out.println("Game #" + model.getId() + " restarted.");
				GameController.this.model.reset();
				GameController.this.view.refresh(model.getBoard());
			}
		});
		
		this.eventActionMap.put( RematchEvent.class, new ProgramAction() 
		{
			@Override
			public void go(ProgramEvent event) {
				RematchEvent rE = (RematchEvent) event;
				GameController.this.appQueue.add( 
							new NewGameEvent(GameController.this.player1, GameController.this.player2));
				
				GameController.this.gameQueue.add(new GameTerminatedEvent());
			}
		});
	}
	
	private void startTurn() 
	{
		this.view.setMessage(activePlayer.str() + " move..." );
		if( activePlayer().getClass() == HumanPlayer.class )
		{
			waitingForHuman = true;
		}
		else
		{
			try {
				Thread.sleep(DELAY);
				gameQueue.add( new MoveMadeEvent( activePlayer().makeMove( model.getBoard(), -1 ) ) );
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}			
	}

	private void endTurn( int[] newBoard) 
	{
		model.setBoard(newBoard);	
		this.view.refresh( newBoard );
		
		if(model.ifWin()==true)
		{
			System.out.println(activePlayer().getName() + " won!");
			activePlayer().youWin(model.getEnderHistoryStack());
			waitingPlayer().youLost(model.getNotEnderHistoryStack());
			model.printHistory();
			gameQueue.add( new GameEndedEvent(activePlayer==ActivePlayer.PLAYER1?EndState.PLAYER1_WON:EndState.PLAYER2_WON) );
			return;
		}
			
		if(model.noMoreMove()==true)
		{
			activePlayer().youDraw( model.getEnderHistoryStack() );
			waitingPlayer().youDraw( model.getNotEnderHistoryStack() );
			gameQueue.add( new GameEndedEvent(EndState.DRAW) );
			return;
		}		
		swapPlayers();
		startTurn();
	}
}
