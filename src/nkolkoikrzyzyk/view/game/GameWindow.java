/**
 * 
 */
package nkolkoikrzyzyk.view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nkolkoikrzyzyk.commons.GameData;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.events.game.GameRestartEvent;
import nkolkoikrzyzyk.events.game.GameTerminatedEvent;
import nkolkoikrzyzyk.events.game.RematchEvent;
import nkolkoikrzyzyk.events.game.StartGameEvent;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author Johhny
 *
 */
public class GameWindow extends JInternalFrame
{
	private BlockingQueue<ProgramEvent> blockingQueue;
	
	private int gameId;
	private String namePlayer1;
	private String namePlayer2;
	private JLabel message;
	private GameBoard gameBoard;
	private JPanel glass;
	
	public GameWindow(BlockingQueue<ProgramEvent> blockingQueue, GameData gameData) 
	{
		super("Game #" + gameData.gameId,
		          false, //resizable
		          true, //closable
		          false, //maximizable
		          true);//iconifiable
		this.blockingQueue = blockingQueue;
		this.message = new JLabel("Welcome!");
		this.gameId = gameData.gameId;
		this.namePlayer1 = gameData.name1;
		this.namePlayer2 = gameData.name2;
		this.initialize();
	}
	
	private void initialize()
	{
		this.setLayout( new BorderLayout(5,5) );
		this.gameBoard = new GameBoard(blockingQueue, gameId);
		
		this.add(gameBoard, BorderLayout.CENTER);
		this.add(bottomPanel(), BorderLayout.PAGE_END);
		fillGlassPanel();
		this.pack();
		blockingQueue.add(new StartGameEvent());
	}

	private void fillGlassPanel() 
	{
		glass = new JPanel() 
		{
			@Override
			protected void paintComponent(Graphics g)
		    {
		        g.setColor( getBackground() );
		        g.fillRect(0, 0, getWidth(), getHeight());
		        super.paintComponent(g);
		    }
		};
		glass.setOpaque(false);
		glass.setBackground( new Color(0xcccccccc, true));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		glass.setLayout(new BorderLayout());
		JButton rematchButton = new JButton("Rematch?");
		rematchButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add(new RematchEvent());
			}
		});
		buttonPanel.add(rematchButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add(new GameTerminatedEvent());
			}
		});
		buttonPanel.add(exitButton);
		glass.add(buttonPanel, BorderLayout.PAGE_END);
		glass.addMouseListener( new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		this.setGlassPane(glass);
	}

	private JPanel bottomPanel() 
	{
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(ViewUtilities.titledBorder("Game"));
		bottomPanel.setLayout( new GridLayout(4,1));
		
		message.setForeground(Color.RED);
		bottomPanel.add(message);
		
		bottomPanel.add( new JLabel("Player 1: " + namePlayer1));
		bottomPanel.add( new JLabel("Player 2: " + namePlayer2));
		
		JButton restartButton = new JButton("Restart");
		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				blockingQueue.add( new GameRestartEvent());
			}
		});
		bottomPanel.add(restartButton);
		return bottomPanel;
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
	
	public void setBoardEnabled( boolean b )
	{
		glass.setVisible(!b);
	}

}
