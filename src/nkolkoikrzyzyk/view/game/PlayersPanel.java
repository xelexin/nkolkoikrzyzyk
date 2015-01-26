/**
 * 
 */
package nkolkoikrzyzyk.view.game;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import nkolkoikrzyzyk.controller.players.AfterstatesANNPlayer;
import nkolkoikrzyzyk.controller.players.BeforestatesANNPlayer;
import nkolkoikrzyzyk.controller.players.ChaoticNeutralPlayer;
import nkolkoikrzyzyk.controller.players.HumanPlayer;
import nkolkoikrzyzyk.controller.players.LookupTablePlayer;
import nkolkoikrzyzyk.controller.players.Player;
import nkolkoikrzyzyk.events.DeletePlayerEvent;
import nkolkoikrzyzyk.events.NewPlayerEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.LookupTable;
import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.model.NeuralNetwork;
import nkolkoikrzyzyk.view.LabeledForm;
import nkolkoikrzyzyk.view.SteppedComboBox;
import nkolkoikrzyzyk.view.ViewUtilities;

/**
 * @author elohhim
 *
 */
public class PlayersPanel extends JPanel implements ActionListener 
{	
	private static final long serialVersionUID = 1L;

	private static final int MIN_WIDTH = 550;
	private static final int MIN_HEIGHT= 260;
	private static final int MAX_WIDTH = 550;
	private static final int MAX_HEIGHT = 260;

	private final BlockingQueue<ProgramEvent> blockingQueue;
	private JTextField name;
	private SteppedComboBox<LookupTable> lookupTableCombo;
	private SteppedComboBox<NeuralNetwork> afterstatesANNCombo;
	private SteppedComboBox<NeuralNetwork> beforestatesANNCombo;
	private boolean isHuman = true;
	private Mark mark;
	private JList<Player> playerList;

	private enum PlayerType {
		HUMAN,
		LOOKUP_TABLE,
		AFTERSTATES_ANN,
		BEFORESTATES_ANN,
		CHAOTIC_NEUTRAL
	};

	private PlayerType type = PlayerType.HUMAN;

	public PlayersPanel( BlockingQueue<ProgramEvent> blockingQueue)
	{
		this.blockingQueue = blockingQueue;

		initialize();
	}

	private void initialize() 
	{
		this.setLayout(new BorderLayout(5,5));
		this.setBorder( ViewUtilities.titledBorder("Manage players") );

		this.name = new JTextField();
		name.setColumns(15);

		initializeList();
		initializeComboBoxes();
		fill();
	}

	private void initializeList()
	{
		this.playerList = new JList<Player>();

	}

	private void initializeComboBoxes() 
	{
		initializeLookupComboBox();
		initializeAfterstatesComboBox();
		initializeBeforestatesComboBox();
	}

	private void initializeAfterstatesComboBox() {
		this.afterstatesANNCombo = new SteppedComboBox<NeuralNetwork>();
		afterstatesANNCombo.setEnabled(false);
		afterstatesANNCombo.setEditable(false);
		Dimension d = afterstatesANNCombo.getPreferredSize();
		afterstatesANNCombo.setPreferredSize(new Dimension(150, d.height));
		afterstatesANNCombo.setPopupWidth(240);
	}

	private void initializeBeforestatesComboBox() {
		this.beforestatesANNCombo = new SteppedComboBox<NeuralNetwork>();
		beforestatesANNCombo.setEnabled(false);
		beforestatesANNCombo.setEditable(false);
		Dimension d = beforestatesANNCombo.getPreferredSize();
		beforestatesANNCombo.setPreferredSize(new Dimension(150, d.height));
		beforestatesANNCombo.setPopupWidth(240);
	}

	private void initializeLookupComboBox() {
		this.lookupTableCombo = new SteppedComboBox<LookupTable>();
		lookupTableCombo.setEnabled(false);
		lookupTableCombo.setEditable(false);
		Dimension d = lookupTableCombo.getPreferredSize();
		lookupTableCombo.setPreferredSize(new Dimension(150, d.height));
		lookupTableCombo.setPopupWidth(240);
	}

	private void fill() 
	{
		this.add(firstPanel(), BorderLayout.CENTER);
		this.add(secondPanel(), BorderLayout.LINE_END);
	}

	private JPanel firstPanel() 
	{
		JPanel firstPanel = new JPanel(new BorderLayout(5,5));

		firstPanel.add( new JLabel("Players list"), BorderLayout.PAGE_START);
		firstPanel.add( ViewUtilities.scroll(playerList), BorderLayout.CENTER );
		firstPanel.add( firstPanelButtons(), BorderLayout.PAGE_END);
		return firstPanel;
	}

	private Component firstPanelButtons() {
		JPanel buttons = new JPanel( new GridLayout(2,1));
		buttons.add(createDeleteButton());
		buttons.add(createDetailButton());
		return buttons;
	}

	private JButton createDetailButton() {
		JButton detailsButton = new JButton("Show details");
		detailsButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//TADA
			}
		});
		//TODO
		detailsButton.setEnabled(false);
		return detailsButton;
	}

	private JButton createDeleteButton() 
	{
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Player p = PlayersPanel.this.playerList.getSelectedValue();
				PlayersPanel.this.blockingQueue.add(
						new DeletePlayerEvent( p )						
						);	
			}
		});
		return deleteButton;
	}

	private JPanel secondPanel() 
	{
		JPanel secondPanel = new JPanel();
		secondPanel.setLayout( 
				new BorderLayout(5, 5));

		secondPanel.add(secondCenterPanel(), BorderLayout.CENTER);
		secondPanel.add(secondBottomPanel(), BorderLayout.PAGE_END);
		return secondPanel;
	}

	private LabeledForm secondCenterPanel() 
	{
		String[] labels = new String[]{"Name:","I.", "II.", "III.", "IV.", "V."};
		Component[] fields = createRadioForm();

		return new LabeledForm(fields, labels);
	}

	private Component[] createRadioForm() 
	{
		ButtonGroup group = new ButtonGroup();

		Box humanBox = new Box(BoxLayout.LINE_AXIS);
		humanBox.add(humanButton(group));

		Box lookupBox = new Box(BoxLayout.LINE_AXIS);
		lookupBox.add(lookupButton(group));
		lookupBox.add(lookupTableCombo);

		Box afterstatesBox = new Box(BoxLayout.LINE_AXIS);
		afterstatesBox.add(afterstatesNetworkButton(group));
		afterstatesBox.add(afterstatesANNCombo);

		Box beforestatesBox = new Box(BoxLayout.LINE_AXIS);
		beforestatesBox.add(beforestatesNetworkButton(group));
		beforestatesBox.add(beforestatesANNCombo);

		Box chaoticBox = new Box(BoxLayout.LINE_AXIS);
		chaoticBox.add(chaoticButton(group));

		return new Component[]{
				name,
				humanBox, 
				lookupBox,
				afterstatesBox,
				beforestatesBox,
				chaoticBox
		};
	}

	private JRadioButton lookupButton(ButtonGroup group) 
	{
		JRadioButton lookupTableButton = new JRadioButton("Lookup table");
		lookupTableButton.setActionCommand("lookup");
		lookupTableButton.addActionListener(this);
		group.add(lookupTableButton);
		return lookupTableButton;
	}

	private JRadioButton humanButton(ButtonGroup group) 
	{
		JRadioButton humanButton = new JRadioButton("Human");
		humanButton.setActionCommand("human");
		humanButton.addActionListener(this);
		group.add(humanButton);
		humanButton.setSelected(true);
		return humanButton;
	}

	private JRadioButton chaoticButton(ButtonGroup group) {
		JRadioButton chaoticButton = new JRadioButton("Chaotic player");
		chaoticButton.setActionCommand("chaotic");
		chaoticButton.addActionListener(this);
		group.add(chaoticButton);
		return chaoticButton;
	}

	private JRadioButton afterstatesNetworkButton(ButtonGroup group) {
		JRadioButton afterstatesNetworkButton = new JRadioButton("Afterstates ANN");
		afterstatesNetworkButton.setActionCommand("afterstates");
		afterstatesNetworkButton.addActionListener(this);
		group.add(afterstatesNetworkButton);
		return afterstatesNetworkButton;
	}

	private JRadioButton beforestatesNetworkButton(ButtonGroup group) {
		JRadioButton beforestatesNetworkButton = new JRadioButton("Beforestates ANN");
		beforestatesNetworkButton.setActionCommand("beforestates");
		beforestatesNetworkButton.addActionListener(this);
		group.add(beforestatesNetworkButton);
		return beforestatesNetworkButton;
	}

	private JPanel secondBottomPanel() 
	{
		JPanel secondBottomPanel = new JPanel();
		secondBottomPanel.add(newXButton());
		secondBottomPanel.add(newOButton());
		return secondBottomPanel;
	}

	private JButton newXButton() 
	{
		JButton newXButton = new JButton("Create X");
		newXButton.addActionListener(new ActionListener() 
		{	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Player p = givePlayer(Mark.CROSS);
				if(p!=null)
					PlayersPanel.this.blockingQueue.add(
							new NewPlayerEvent( p )
							);
				else
					failPrompt();
			}
		});
		return newXButton;
	}

	private JButton newOButton() 
	{
		JButton newOButton = new JButton("Create O");
		newOButton.addActionListener(new ActionListener() 
		{	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Player p = givePlayer(Mark.NOUGHT);
				if(p!=null)
					PlayersPanel.this.blockingQueue.add(
							new NewPlayerEvent( p ));
				else
					failPrompt();
			}
		});
		return newOButton;
	}

	public Player givePlayer(Mark mark)
	{
		Player p=null;
		String name = this.name.getText();
		if (name.trim().length() == 0)
			name = null;
		switch(type)
		{
		case AFTERSTATES_ANN:
			NeuralNetwork aNetwork = 
			afterstatesANNCombo.getItemAt(afterstatesANNCombo.getSelectedIndex());
			if(aNetwork!=null)
				p = new AfterstatesANNPlayer(name, mark, aNetwork);
			break;

		case BEFORESTATES_ANN:
			NeuralNetwork bNetwork = 
			beforestatesANNCombo.getItemAt(beforestatesANNCombo.getSelectedIndex());
			if(bNetwork!=null)
				p = new BeforestatesANNPlayer(name, mark, bNetwork);
			break;

		case CHAOTIC_NEUTRAL:
			p = new ChaoticNeutralPlayer(name, mark);
			break;

		case HUMAN:
			p = new HumanPlayer(name, mark);
			break;

		case LOOKUP_TABLE:
			LookupTable table = 
			lookupTableCombo.getItemAt(lookupTableCombo.getSelectedIndex()); 
			if(table!=null)
				p = new LookupTablePlayer(name, mark, table);
			break;

		default:
			break;
		}
		return p;
	}

	public void populateNetworkList(NeuralNetwork[] networkList)
	{
		this.afterstatesANNCombo.setModel( new DefaultComboBoxModel<>(networkList));	
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		switch(e.getActionCommand())
		{
		case "afterstates":
			type = PlayerType.AFTERSTATES_ANN;
			afterstatesANNCombo.setEnabled(true);
			beforestatesANNCombo.setEnabled(false);
			lookupTableCombo.setEnabled(false);

			break;

		case "beforestates":
			type = PlayerType.BEFORESTATES_ANN;
			afterstatesANNCombo.setEnabled(false);
			beforestatesANNCombo.setEnabled(true);
			lookupTableCombo.setEnabled(false);
			break;

		case "chaotic":
			type = PlayerType.CHAOTIC_NEUTRAL;
			afterstatesANNCombo.setEnabled(false);
			beforestatesANNCombo.setEnabled(false);
			lookupTableCombo.setEnabled(false);
			break;

		case "human":
			type = PlayerType.HUMAN;
			afterstatesANNCombo.setEnabled(false);
			beforestatesANNCombo.setEnabled(false);
			lookupTableCombo.setEnabled(false);
			break;

		case "lookup":
			type = PlayerType.LOOKUP_TABLE;
			afterstatesANNCombo.setEnabled(false);
			beforestatesANNCombo.setEnabled(false);
			lookupTableCombo.setEnabled(true);
			break;

		default:
			break;
		}		
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(MIN_WIDTH, MIN_HEIGHT);
	}

	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(MAX_WIDTH, MAX_HEIGHT);
	}

	public void populateList(Player[] playerListModel) 
	{
		this.playerList.setModel(
				new DefaultComboBoxModel<Player>(playerListModel)
				);
	}

	private void failPrompt() 
	{
		JOptionPane.showMessageDialog(null,"Failed to create player.");
	}

	public void populateNetworkCombos(
			NeuralNetwork[] afterstatesNetworkListModel,
			NeuralNetwork[] beforestatesNetworkListModel) 
	{
		this.afterstatesANNCombo.setModel( 
				new DefaultComboBoxModel<>(afterstatesNetworkListModel));
		this.beforestatesANNCombo.setModel( 
				new DefaultComboBoxModel<>(beforestatesNetworkListModel));
	}

	public void populateLookupTableCombo(
			LookupTable[] lookupTableListModel
			)
	{
		this.lookupTableCombo.setModel(
				new DefaultComboBoxModel<>(lookupTableListModel));
	}
}
