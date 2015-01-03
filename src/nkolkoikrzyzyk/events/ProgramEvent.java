/**
 * SimulationEvent.java
 */
package nkolkoikrzyzyk.events;

/**
 * @author elohhim
 *
 */
public class ProgramEvent {

	static private long eventCounter = 0;

	protected long id;

	public ProgramEvent() {
		this.id = eventCounter++;
	}

	/**
	 * @return
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @return
	 */
	public static long getCounter() {
		return ProgramEvent.eventCounter;
	}

}
