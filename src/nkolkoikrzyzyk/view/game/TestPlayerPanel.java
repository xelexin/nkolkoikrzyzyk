/**
 * 
 */
package nkolkoikrzyzyk.view.game;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import nkolkoikrzyzyk.commons.TestResult;
import nkolkoikrzyzyk.controller.Tester;
import nkolkoikrzyzyk.controller.players.HumanPlayer;
import nkolkoikrzyzyk.controller.players.Player;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.view.LabeledForm;
import nkolkoikrzyzyk.view.SteppedComboBox;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author elohhim
 *
 */
public class TestPlayerPanel extends JPanel implements PropertyChangeListener
{
	private static final long serialVersionUID = 1L;

	private static final int MIN_WIDTH = 550;
	private static final int MIN_HEIGHT= 260;
	private static final int MAX_WIDTH = 550;
	private static final int MAX_HEIGHT = 260;
	//outside
	private final BlockingQueue<ProgramEvent> blockingQueue;
	//inside
	private SteppedComboBox<Player> playerX;
	private SteppedComboBox<Player> playerO;
	private JSpinner games;
	private JTextField player1wins;
	private JTextField player2wins;
	private JTextField draws;
	private JTextField time;
	private JProgressBar progressBar;

	private JButton testButton;

	public TestPlayerPanel(BlockingQueue<ProgramEvent> blockingQueue)
	{
		this.blockingQueue = blockingQueue;

		initialize();
	}

	public void initialize()
	{
		this.setLayout(new BorderLayout(5,5));
		this.setBorder( ViewUtilities.titledBorder("Test players") );

		initializeComboBoxes();
		initializeSpinner();
		initializeTextFields();
		initializeProgressBar();
		initializeTestButton();

		fill();
	}

	private void initializeProgressBar()
	{
		progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
	}

	private void initializeTextFields() 
	{
		this.player1wins = new JTextField();
		player1wins.setEditable(false);
		player1wins.setColumns(10);
		this.draws = new JTextField();
		draws.setEditable(false);
		draws.setColumns(10);
		this.player2wins= new JTextField();
		player2wins.setEditable(false);
		player2wins.setColumns(10);
		this.time = new JTextField();
		time.setEditable(false);
		time.setColumns(10);
	}

	private void initializeSpinner() 
	{
		this.games = 
				ViewUtilities.spinner(500000, 100000, 1000000, 100000, "0");
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

	public void fill()
	{
		this.add(leftPanel(), BorderLayout.LINE_START);
		this.add(rightPanel(), BorderLayout.LINE_END);
	}

	private JPanel leftPanel() 
	{
		JPanel leftPanel = new JPanel( new BorderLayout(5,5) );
		String[] labels = new String[]
				{"Player X", "Player O", "Games"};
		Component[] fields = new Component[]
				{playerX, playerO, games};
		LabeledForm form = new LabeledForm(fields, labels);
		leftPanel.add(form, BorderLayout.CENTER);
		leftPanel.add(bottomLeftPanel(), BorderLayout.PAGE_END);
		return leftPanel;
	}

	private JPanel bottomLeftPanel() 
	{
		JPanel bottomLeftPanel = new JPanel();
		bottomLeftPanel.add(testButton);
		bottomLeftPanel.add(progressBar);
		return bottomLeftPanel;
	}

	private void initializeTestButton() 
	{
		this.testButton = new JButton("Start test");
		testButton.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if( playerX.getSelectedIndex() == -1 || playerO.getSelectedIndex() == -1)
				{
					noPlayerSelected();
				}
				else
				{
					testButton.setEnabled(false);
					progressBar.setValue(0);
					Tester tester = getTester();
					tester.addPropertyChangeListener( TestPlayerPanel.this );
					tester.execute();
				}
			}
		});
	}

	private JPanel rightPanel() 
	{
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(ViewUtilities.titledBorder("Results"));
		String[] labels = new String[]{"Player X wins", "Player O wins", "Draws", "Elapsed time"};
		Component[] fields = new Component[]{player1wins, player2wins, draws, time};
		rightPanel.add(
				new LabeledForm(fields, labels)
				);
		return rightPanel;
	}

	private Tester getTester()
	{
		return new Tester(blockingQueue, 
				playerX.getItemAt(playerX.getSelectedIndex()),
				playerO.getItemAt(playerO.getSelectedIndex()),
				(Integer)games.getValue(), 
				testButton);
	}

	public void populatePlayerCombos(Player[] playerListModel) 
	{
		List<Player> xPlayers = new LinkedList<Player>();
		List<Player> oPlayers = new LinkedList<Player>();
		for(Player p : playerListModel)
		{
			if(p.getClass() != HumanPlayer.class) 
			{
				if(p.getMarkType() == Mark.CROSS)
					xPlayers.add(p);
				else
					oPlayers.add(p);
			}
		}
		Player[] xArray = new Player[xPlayers.size()];
		xPlayers.toArray(xArray);
		playerX.setModel(new DefaultComboBoxModel<Player>(xArray));
		Player[] oArray = new Player[oPlayers.size()];
		oPlayers.toArray(oArray);
		playerO.setModel(new DefaultComboBoxModel<Player>(oArray));
	}

	private void noPlayerSelected() 
	{
		JOptionPane.showMessageDialog(this, "You must select two players");
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		if( "progress" == evt.getPropertyName()) 
		{
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			this.repaint();
		}
	}

	public void setResults(TestResult testResult) 
	{
		player1wins.setText(Long.toString(testResult.p1wins) + " (" + 
				String.format("%.2f", testResult.p1percent) + "%)");
		player2wins.setText(Long.toString(testResult.p2wins) + " (" + 
				String.format("%.2f", testResult.p2percent) + "%)");
		draws.setText(Long.toString(testResult.draws) + " (" +
				String.format("%.2f", testResult.dpercent) + "%)");
		time.setText(Long.toString(testResult.time) + "ms.");
	}
}
