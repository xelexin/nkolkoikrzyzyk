package nkolkoikrzyzyk.events;

public class NewLookupTableEvent extends ProgramEvent
{
	public final String name;
	
	public NewLookupTableEvent(String name)
	{
		this.name = name;
	}

}
