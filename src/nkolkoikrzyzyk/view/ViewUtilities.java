package nkolkoikrzyzyk.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;

public class ViewUtilities
{
	public static final String LEFT = BorderLayout.LINE_START;
	public static final String RIGHT = BorderLayout.LINE_END;
	public static final String TOP = BorderLayout.PAGE_START;
	public static final String BOTTOM = BorderLayout.PAGE_END;
	
	public static Border titledBorder(String string)
	{
		return BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(string),
                BorderFactory.createEmptyBorder(5,5,5,5));
	} 
	
	public static JScrollPane scroll(Component component)
	{
		return new JScrollPane(component);
	}
}
