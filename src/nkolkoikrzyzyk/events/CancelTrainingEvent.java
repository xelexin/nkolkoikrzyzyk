package nkolkoikrzyzyk.events;

import nkolkoikrzyzyk.controller.Trainer;

public class CancelTrainingEvent extends ProgramEvent
{
	public final Trainer trainer; 
	
	public CancelTrainingEvent(Trainer currentTrainer)
	{
		this.trainer = currentTrainer;
	}
}
