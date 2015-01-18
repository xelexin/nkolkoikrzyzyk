/**
 * 
 */
package nkolkoikrzyzyk.events;

import java.io.File;

import nkolkoikrzyzyk.model.TrainingData;

/**
 * @author Johhny
 *
 */
public class SaveTrainingDataEvent extends ProgramEvent
{
	public final File file;
	public final TrainingData data;
	
	public SaveTrainingDataEvent(File file, TrainingData data)
	{
		this.file = file;
		this.data = data;
	}

}
