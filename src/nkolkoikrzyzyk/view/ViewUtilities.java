package nkolkoikrzyzyk.view;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class ViewUtilities
{
	public static Border titledBorder(String string)
	{
		return BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(string),
                BorderFactory.createEmptyBorder(5,5,5,5));
	} 
}
