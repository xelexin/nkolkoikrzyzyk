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
	public class LayerMockup {
		public boolean isSigmoid = false;
		public float[] weights = null;
		
		public LayerMockup( float[] weights, boolean isSigmoid)
		{
			this.weights=weights;
			this.isSigmoid=isSigmoid;
		}
	};
	
	public List<LayerMockup> layers;
	
	public NeuralNetworkMockup( int layersCount )
	{
		this.layers = new ArrayList<LayerMockup>();
	}
	
	public void addLayer(float[] weights, boolean isSigmoid)
	{
		layers.add(new LayerMockup(weights, isSigmoid));
	}
}
