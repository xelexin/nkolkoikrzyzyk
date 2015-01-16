package nkolkoikrzyzyk.events;

import nkolkoikrzyzyk.controller.Trainer;


public class StartTrainingEvent extends ProgramEvent 
{
	public final Trainer trainer;
	
	public StartTrainingEvent()
	{
		this(null);
	}
	
	public StartTrainingEvent( Trainer trainer)
	{
		this.trainer = trainer;
	}
}
