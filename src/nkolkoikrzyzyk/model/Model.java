/**
 * Model.java
 */
package nkolkoikrzyzyk.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import nkolkoikrzyzyk.nkolkoikrzyzyk;
import nkolkoikrzyzyk.commons.BoardMockup;
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

	/**
	 * @return
	 */
	public BoardMockup getMockup() {
		int[] board = null;
		return new BoardMockup( board );
	}
}
