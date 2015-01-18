package nkolkoikrzyzyk.view;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

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
	
	@SuppressWarnings("rawtypes")
	public static JSpinner spinner(Number val, Comparable min, Comparable max, Number step, String format )
	{
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(val, min, max, step));
		spinner.setEditor(new JSpinner.NumberEditor(spinner, format));
		spinner.setToolTipText( "from " + min + " to " + max);
		return spinner;
	}
}
