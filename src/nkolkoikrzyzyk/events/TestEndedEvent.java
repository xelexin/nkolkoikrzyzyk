/**
 * 
 */
package nkolkoikrzyzyk.events;

import nkolkoikrzyzyk.controller.Tester;

/**
 * @author elohhim
 *
 */
public class TestEndedEvent extends ProgramEvent 
{
	public final Tester tester;

	public TestEndedEvent( Tester tester)
	{
		this.tester = tester;
	}
}
