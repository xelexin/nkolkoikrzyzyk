/**
 * 
 */
package nkolkoikrzyzyk.events;

import java.io.File;

/**
 * @author elohhim
 *
 */
public class LoadNetworkEvent extends ProgramEvent 
{
	public final File file;

	public LoadNetworkEvent(File file) 
	{
		this.file = file;
	}

}
