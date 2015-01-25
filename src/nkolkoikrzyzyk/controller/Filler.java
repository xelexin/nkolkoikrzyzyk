package nkolkoikrzyzyk.controller;

import java.awt.Toolkit;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.SwingWorker;

import nkolkoikrzyzyk.controller.players.LookupTablePlayer;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.GameModel;

public class Filler extends SwingWorker<Void, Void>
{
	private LookupTablePlayer player1;
	private LookupTablePlayer player2;
	private int games;
	private JButton button;

	public Filler( 
			BlockingQueue<ProgramEvent> blockingQueue,
			LookupTablePlayer player1,
			LookupTablePlayer player2,
			int games, JButton button)
	{
		this.player1 = player1;
		this.player2 = player2;
		this.games = games;
		this.button = button;
	}
	
	public void playGames()
	{
		player1.setTrainingInProgress(true);
		player2.setTrainingInProgress(true);
		long start = System.currentTimeMillis();
		for( int i = 1; i<=games; i++)
		{
			new GameModel().fastPlay( player1, player2);
			if (i % (games/100) == 0)
				setProgress((100*i)/games);
		}
		player1.setTrainingInProgress(false);
		player2.setTrainingInProgress(false);
	}
	
	@Override
	protected Void doInBackground() throws Exception 
	{
		playGames();
		return null;
	}
	
	@Override 
	public void done()
	{
		Toolkit.getDefaultToolkit().beep();
		this.button.setEnabled(true);
	}
}
