/**
 * 
 */
package nkolkoikrzyzyk.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Johhny
 *
 */
public class LabeledForm extends JPanel
{
	public LabeledForm(Component[] fields, String[] labels, char[] mnemonics)
	{
		super(new BorderLayout(5,5));
	    JPanel labelPanel = new JPanel(new GridLayout(labels.length, 1));
	    JPanel fieldPanel = new JPanel(new GridLayout(fields.length, 1));
	    add(labelPanel, BorderLayout.LINE_START);
	    add(fieldPanel, BorderLayout.CENTER);
	    
	    for (int i = 0; i < labels.length; i += 1) 
	    {
	    	JLabel label = new JLabel(labels[i]);
	    	label.setLabelFor(fields[i]);
	    	if (i < mnemonics.length)
	    	{
	    		label.setDisplayedMnemonic(mnemonics[i]);
	    	}
	    	
	    	JPanel l = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    	l.add(label);
	    	labelPanel.add(l);
	    	
	    	JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    	f.add(fields[i]);
	    	fieldPanel.add(f);
	    }
	}

	public LabeledForm(Component[] fields, String[] labels)
	{
		this(fields, labels, new char[0]);
	}
}
