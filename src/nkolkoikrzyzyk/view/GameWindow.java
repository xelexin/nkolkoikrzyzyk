/**
 * 
 */
package nkolkoikrzyzyk.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nkolkoikrzyzyk.commons.BoardMockup;
import nkolkoikrzyzyk.commons.GameData;
import nkolkoikrzyzyk.events.GameTerminatedEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.Mark;

/**
 * @author Johhny
 *
 */
public class GameWindow extends JFrame implements WindowListener
{
	private BlockingQueue<ProgramEvent> blockingQueue;
	
	private int gameId;
	private String namePlayer1;
	private String namePlayer2;
	private JLabel message;
	private GameBoard gameBoard;
	
	public GameWindow(BlockingQueue<ProgramEvent> blockingQueue, GameData gameData) 
	{
		this.blockingQueue = blockingQueue;
		this.message = new JLabel("Welcome!");
		this.gameId = gameData.gameId;
		this.namePlayer1 = gameData.name1;
		this.namePlayer2 = gameData.name2;
		this.initialize();
	}
	
	private void initialize()
	{
		this.addWindowListener(this);
		this.setTitle("Tic-Tac-Toe - Game#" + gameId );
		this.setBounds(320, 100, 400, 500);
		this.setResizable( false );
		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout( new BorderLayout() );
		
		message.setForeground(Color.RED);
		this.gameBoard = new GameBoard(blockingQueue, gameId);
		this.add( gameBoard, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Game"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
		bottomPanel.setLayout( new GridLayout(3,1));
		bottomPanel.add(message);
		
		bottomPanel.add( new JLabel("Player 1: " + namePlayer1));
		bottomPanel.add( new JLabel("Player 2: " + namePlayer2));
		
		this.add(bottomPanel, BorderLayout.PAGE_END);
		this.setVisible( true );
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
        blockingQueue.add(new GameTerminatedEvent());
        this.dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		blockingQueue.add( new StartGameEvent());
	}

	public void refresh(int[] newBoard) 
	{
		this.gameBoard.updateState( newBoard );
		
		this.repaint();
	}

	public void setMessage(String str) 
	{
		message.setText(str);
	}

	public void setFieldState(int position, Mark markType) 
	{
		this.gameBoard.setFieldState(position, markType);
	}
}
