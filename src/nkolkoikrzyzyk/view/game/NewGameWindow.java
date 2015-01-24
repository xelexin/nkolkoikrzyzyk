/**
 * 
 */
package nkolkoikrzyzyk.view.game;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import nkolkoikrzyzyk.controller.players.AfterstatesANNPlayer;
import nkolkoikrzyzyk.controller.players.HumanPlayer;
import nkolkoikrzyzyk.controller.players.Player;
import nkolkoikrzyzyk.events.NewGameEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author Johhny
 *
 */
public class NewGameWindow extends JFrame implements ActionListener
{
	private class PlayerPanel extends JPanel implements ActionListener
	{
		private int id;
		private JTextField name;
		private JComboBox<NeuralNetwork> networkCombo;
		private boolean isHuman = true;
		private Mark mark;
		
		public PlayerPanel(int id )
		{
			this.id = id;
			this.name = new JTextField("Name");
			this.networkCombo = new JComboBox<NeuralNetwork>();
			this.mark=(id==1)?Mark.CROSS:Mark.NOUGHT;
			initialize();
		}
		
		private void initialize() 
		{
			this.setLayout(new GridLayout(4,1));
			this.setBorder( ViewUtilities.titledBorder("Player " + id + " - " + mark.str() ) );
			fill();
		}

		private void fill() 
		{
			JRadioButton humanButton = new JRadioButton("Human");
			humanButton.setActionCommand("human");
			humanButton.addActionListener(this);
			humanButton.setSelected(true);    	
			
		    JRadioButton neuralNetworkButton = new JRadioButton("Neural network");
		    neuralNetworkButton.setActionCommand("network");
		    neuralNetworkButton.addActionListener(this);
		    
		    //Group the radio buttons.
		    ButtonGroup group = new ButtonGroup();
		    group.add(humanButton);
		    group.add(neuralNetworkButton);
		    	    
		    this.add(humanButton);
		   	
		    this.add(name);
		    
		    this.add(neuralNetworkButton);
		    
		    networkCombo.setEnabled(false);
		    networkCombo.setEditable(false);
		    this.add(networkCombo);
		}
		
		public Player givePlayer()
		{
			if(isHuman)
			{
				return new HumanPlayer( name.getText(), mark);
			}
			else
			{
				//TODO zrobić wybór afterstates, beforestates
				NeuralNetwork network = networkCombo.getItemAt(networkCombo.getSelectedIndex());
				return new AfterstatesANNPlayer( "Network " + id, mark, network);
			}
		}

		public void populateNetworkList(NeuralNetwork[] networkList)
		{
			this.networkCombo.setModel( new DefaultComboBoxModel<>(networkList));	
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if( e.getActionCommand().equals("human") )
			{
				isHuman=true;
				name.setEnabled(true);
				networkCombo.setEnabled(false);
			} 
			else if (e.getActionCommand().equals("network"))
			{
				isHuman=false;
				name.setEnabled(false);
				networkCombo.setEnabled(true);
			}		
		}
	}

	private final BlockingQueue<ProgramEvent> blockingQueue;
	private PlayerPanel panel1;
	private PlayerPanel panel2;
		
	public NewGameWindow(BlockingQueue<ProgramEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
		initialize();
	}

	private void initialize() {
		this.setBounds(100, 100, 400, 300);
		this.setResizable( false );
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout( new BorderLayout() );
		panel1 = new PlayerPanel(1);
		panel2 = new PlayerPanel(2);
		fill();
		this.setVisible( true );
	}

	private void fill() {
		fillTopPanel();
		fillBottomPanel();		
	}

	private void fillTopPanel() 
	{
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,2));
		topPanel.add( panel1 );
		topPanel.add( panel2 );
		this.add(topPanel, BorderLayout.CENTER);
	}
		
	private void fillBottomPanel() 
	{
		JPanel bottomPanel = new JPanel();
		JButton newGameButton = new JButton("New game");
		newGameButton.addActionListener(this);
		bottomPanel.add(newGameButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NewGameWindow.this.dispose();
			}
		});
		bottomPanel.add(cancelButton);
		
		this.add(bottomPanel, BorderLayout.PAGE_END);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Player player1 = panel1.givePlayer();
		Player player2 = panel2.givePlayer();
		
		blockingQueue.add( new NewGameEvent( player1, player2 ) );
		this.dispose();
	}

	public void populateNetworkList(NeuralNetwork[] networkList)
	{
		panel1.populateNetworkList(networkList);
		panel2.populateNetworkList(networkList);
	}
}
