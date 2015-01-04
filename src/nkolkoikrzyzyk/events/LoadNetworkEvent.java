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
	public File file;

	public LoadNetworkEvent(File file) 
	{
		this.file = file;
	}

}
