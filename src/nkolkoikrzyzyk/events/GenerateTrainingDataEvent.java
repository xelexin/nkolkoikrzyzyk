/**
 * 
 */
package nkolkoikrzyzyk.events;

import nkolkoikrzyzyk.model.LookupTable;
import nkolkoikrzyzyk.model.TrainingData.TrainingDataType;

/**
 * @author Johhny
 *
 */
public class GenerateTrainingDataEvent extends ProgramEvent
{
	public final LookupTable table;
	public final TrainingDataType type;
	
	public GenerateTrainingDataEvent(LookupTable lookupTable, TrainingDataType type)
	{
		this.table = lookupTable;
		this.type = type;
	}
}
