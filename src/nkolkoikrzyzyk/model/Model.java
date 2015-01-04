/**
 * Model.java
 */
package nkolkoikrzyzyk.model;

import java.util.concurrent.BlockingQueue;

import nkolkoikrzyzyk.events.ProgramEvent;

/**
 * @author elohhim
 *
 */
public class Model {

	private final BlockingQueue<ProgramEvent> blockingQueue;
	
	public Model( BlockingQueue<ProgramEvent> blockingQueue ) {
		this.blockingQueue = blockingQueue;
	}
}
