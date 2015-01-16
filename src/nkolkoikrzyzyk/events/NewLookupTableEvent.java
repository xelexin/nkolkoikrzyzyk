package nkolkoikrzyzyk.events;

public class NewLookupTableEvent extends ProgramEvent
{
	public String name;
	
	public NewLookupTableEvent(String name)
	{
		this.name = name;
	}

}
