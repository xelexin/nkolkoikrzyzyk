/**
 * 
 */
package nkolkoikrzyzyk.view.game;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nkolkoikrzyzyk.controller.players.HumanPlayer;
import nkolkoikrzyzyk.controller.players.Player;
import nkolkoikrzyzyk.events.NewGameEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.view.LabeledForm;
import nkolkoikrzyzyk.view.SteppedComboBox;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author elohhim
 *
 */
public class GamePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;

	private static final int MIN_WIDTH = 550;
	private static final int MIN_HEIGHT= 260;
	private static final int MAX_WIDTH = 550;
	private static final int MAX_HEIGHT = 260;

	//outside
	private BlockingQueue<ProgramEvent> blockingQueue;
	//inside
	private SteppedComboBox<Player> playerX;
	private SteppedComboBox<Player> playerO;

	public GamePanel(BlockingQueue<ProgramEvent> blockingQueue)
	{
		this.blockingQueue = blockingQueue;
		initialize();
	}

	private void initialize()
	{
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		this.setBorder( ViewUtilities.titledBorder("Create game") );

		initializeComboBoxes();

		fill();
	}

	private void initializeComboBoxes() 
	{
		this.playerX = new SteppedComboBox<Player>();
		Dimension d = playerX.getPreferredSize();
		playerX.setPreferredSize(new Dimension(150, d.height));
		playerX.setPopupWidth(240);
		this.playerO = new SteppedComboBox<Player>();
		playerO.setPreferredSize(new Dimension(150, d.height));
		playerO.setPopupWidth(240);
	}

	private void fill()
	{
		this.add(new JLabel("Player X"));
		this.add(playerX);
		this.add(new JLabel("Player O"));
		this.add(playerO);
		this.add(newGameButton());
	}

	private JButton newGameButton() {
		JButton newGameButton = new JButton("New game");
		newGameButton.addActionListener( new ActionListener() 
		{	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if( playerX.getSelectedIndex() != -1 && playerO.getSelectedIndex() != -1)
				{
					blockingQueue.add( new NewGameEvent(
							GamePanel.this.playerX.getItemAt(playerX.getSelectedIndex()),
							GamePanel.this.playerO.getItemAt(playerO.getSelectedIndex())
							));	
				}
				else
				{
					JOptionPane.showMessageDialog(GamePanel.this, "You must select two players.");
				}
			}
		});
		return newGameButton;
	}

	public void populatePlayerCombos(Player[] playerListModel) 
	{
		List<Player> xPlayers = new LinkedList<Player>();
		List<Player> oPlayers = new LinkedList<Player>();
		for(Player p : playerListModel)
		{

			if(p.getMarkType() == Mark.CROSS)
				xPlayers.add(p);
			else
				oPlayers.add(p);
		}

		Player[] xArray = new Player[xPlayers.size()];
		xPlayers.toArray(xArray);
		playerX.setModel(new DefaultComboBoxModel<Player>(xArray));
		Player[] oArray = new Player[oPlayers.size()];
		oPlayers.toArray(oArray);
		playerO.setModel(new DefaultComboBoxModel<Player>(oArray));
	}

}
