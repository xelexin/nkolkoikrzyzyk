/**
 * 
 */
package nkolkoikrzyzyk.events;

import nkolkoikrzyzyk.model.LookupTable;

/**
 * @author Johhny
 *
 */
public class DeleteLookupTableEvent extends ProgramEvent
{
	public final LookupTable table;

	public DeleteLookupTableEvent(LookupTable selectedValue)
	{
		this.table = selectedValue;
	}

}
