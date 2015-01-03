package nkolkoikrzyzyk;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import nkolkoikrzyzyk.controller.AppController;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.Model;
import nkolkoikrzyzyk.view.View;

/**
 * Application.java
 */

/**
 * @author elohhim
 *
 */
public final class Application 
{
	
	public static void main(String[] args) 
	{
		try{
			final BlockingQueue<ProgramEvent> blockingQueue  = new LinkedBlockingQueue<ProgramEvent>();
			final Model model = new Model( blockingQueue );
			final View view = new View( blockingQueue );
			final AppController controller = new AppController( view, model, blockingQueue );
			controller.work();
		}
		catch( Exception e ){
			e.printStackTrace();
			System.exit( 1 );
		}
	}
}
