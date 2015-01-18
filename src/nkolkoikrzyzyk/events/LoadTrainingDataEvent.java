/**
 * 
 */
package nkolkoikrzyzyk.events;

import java.io.File;

/**
 * @author Johhny
 *
 */
public class LoadTrainingDataEvent extends ProgramEvent
{
	public final File file;

	public LoadTrainingDataEvent(File file)
	{
		this.file = file;
	}
}
