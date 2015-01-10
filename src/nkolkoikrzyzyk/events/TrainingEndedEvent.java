/**
 * 
 */
package nkolkoikrzyzyk.events;

import nkolkoikrzyzyk.model.Trainer;

/**
 * @author elohhim
 *
 */
public class TrainingEndedEvent extends ProgramEvent 
{
	public final Trainer trainer;

	public TrainingEndedEvent ()
	{
		this(null);
	}

	public TrainingEndedEvent (Trainer trainer)
	{
		this.trainer = trainer;
	}
}

