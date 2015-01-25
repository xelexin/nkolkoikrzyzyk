/**
 * 
 */
package nkolkoikrzyzyk.controller;

import java.awt.Toolkit;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.SwingWorker;

import nkolkoikrzyzyk.commons.TestResult;
import nkolkoikrzyzyk.controller.players.Player;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.TestEndedEvent;
import nkolkoikrzyzyk.events.TrainingEndedEvent;
import nkolkoikrzyzyk.model.GameModel;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.model.TrainingData;

/**
 * @author elohhim
 *
 */

public class Tester extends SwingWorker<TestResult, Void>
{
	//outside
	private final BlockingQueue<ProgramEvent> blockingQueue;
	private final JButton testButton;
	private final long games;
	private final Player p1;
	private final Player p2;
	
	//inside
	

	public Tester( 
			BlockingQueue<ProgramEvent> blockingQueue,
			Player p1,
			Player p2,
			long games,
			JButton testButton
			)
	{
		this.blockingQueue = blockingQueue;
		this.p1 = p1;
		this.p2 = p2;
		this.games = games;
		this.testButton = testButton;
		
	}
	
	private TestResult test()
	{
		long start = System.currentTimeMillis();
		long p1wins = p1.getWinCounter();
		long p2wins = p2.getWinCounter();
		
		for( int game = 1; game<=games; game++)
		{
			new GameModel().fastPlay(p1, p2);
			if( game%(games/100) == 0)
			{
				setProgress((int)(100*game/games));
			}
		}
			
		p1wins = p1.getWinCounter() - p1wins;
		p2wins = p2.getWinCounter() - p2wins;
		long draws = games - p1wins - p2wins;
		
		return new TestResult(
				p1wins, 
				p2wins, 
				draws, 
				(System.currentTimeMillis()-start),
				games);
	}
	
	@Override
	protected TestResult doInBackground() throws Exception 
	{
		return test();
	}
	
	@Override 
	public void done()
	{
		Toolkit.getDefaultToolkit().beep();
		blockingQueue.add( new TestEndedEvent(this));
		testButton.setEnabled(true);
	}
	
	
}
