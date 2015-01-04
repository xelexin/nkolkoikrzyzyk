/**
 * 
 */
package nkolkoikrzyzyk.view.neuralnetworks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import nkolkoikrzyzyk.commons.LayerMockup;
import nkolkoikrzyzyk.commons.NeuralNetworkMockup;

/**
 * @author elohhim
 *
 */
public class NeuralNetworkDrawPanel extends JPanel 
{
	private NeuralNetworkMockup mockup = null;
	private static int size = 20;
	private static int spacingX = 150;
	private static int spacingY = 50;
	
	public NeuralNetworkDrawPanel() 
	{
		/*//test
		NeuralNetwork mlp = new NeuralNetwork();
		mlp.init(9, new int[]{9,9});
		this.mockup = mlp.getMockup();
		//test//*/
		
		initialize();
	}
	
	private void initialize() 
	{
		this.setBackground( Color.WHITE);
	}

	@Override
	public void paintComponent( Graphics g)
	{
		//initialization
		super.paintComponent(g);
		if(mockup == null)
			return;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor( Color.BLACK );
		g2d.setStroke( new BasicStroke(2));
		drawInputs(g2d);
		drawLayers(g2d);
		drawOutput(g2d);
	}

	private void drawInputs(Graphics2D g2d) {
		// TODO Auto-generated method stub
		
	}

	private void drawLayers(Graphics2D g2d) {
		int X = spacingX;
		int Y = size;
		
		Ellipse2D e;
		Line2D l;
		
		for( LayerMockup element : mockup.layers )
		{
			//drawing neurons
			Color fillColor = element.isSigmoid?Color.BLUE:Color.GREEN;
			for( int i = 0; i < element.inputSize; i++ )
			{	
				e = new Ellipse2D.Double(X, Y, size, size);
				g2d.setColor(fillColor);
				g2d.fill(e);
				g2d.setColor( Color.BLACK );
				g2d.draw(e);
				
				//drawing connections
				for( int j = 0; j < element.outputSize; j++)
				{
					l = new Line2D.Double(X+size, Y+size/2, X+spacingX, size/2+size+j*spacingY);
					g2d.draw(l);
				}//for j
				
				Y+=spacingY;
			}//for i
			Y = size;
			X+= spacingX;
		}//for element			
	}

	private void drawOutput(Graphics2D g2d) {
		// TODO Auto-generated method stub
		
	}
	
	public void setMockup( NeuralNetworkMockup mockup)
	{
		this.mockup = mockup;
	}
	
}
