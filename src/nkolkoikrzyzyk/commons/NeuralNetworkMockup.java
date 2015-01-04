/**
 * 
 */
package nkolkoikrzyzyk.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Johhny
 *
 */
public class NeuralNetworkMockup
{	
	public List<LayerMockup> layers;
	
	public NeuralNetworkMockup( int layersCount )
	{
		this.layers = new ArrayList<LayerMockup>();
	}
	
	public void addLayer(LayerMockup layer)
	{
		layers.add(layer);
	}
}
