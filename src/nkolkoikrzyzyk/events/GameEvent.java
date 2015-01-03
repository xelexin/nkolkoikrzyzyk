/**
 * SimulationEvent.java
 */
package nkolkoikrzyzyk.events;

/**
 * @author elohhim
 *
 */
public class GameEvent {

	static private long eventCounter = 0;

	protected long id;

	public GameEvent() {
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
		return GameEvent.eventCounter;
	}

}
