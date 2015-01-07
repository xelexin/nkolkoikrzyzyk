/**
 * 
 */
package nkolkoikrzyzyk.view.neuralnetworks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import nkolkoikrzyzyk.commons.LayerMockup;
import nkolkoikrzyzyk.commons.NeuralNetworkMockup;

/**
 * @author elohhim
 *
 */
public class NeuralNetworkDrawPanel extends JPanel 
{
	private static final int PREF_WIDTH = 600;
	private static final int PREF_HEIGHT = 600;
	private NeuralNetworkMockup mockup = null;
	private static int size = 15;
	private static int spacingX = 200;
	private static int spacingY = 100;
	private static int marginsX = spacingX/4;
	private static int marginsY = spacingY/2;
	
	public NeuralNetworkDrawPanel() 
	{		
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
		g2d.setStroke( new BasicStroke(1));
		drawHeaders(g2d);
		drawInputs(g2d);
		drawLayers(g2d);
		drawOutput(g2d);
	}

	private void drawHeaders(Graphics2D g2d)
	{
		g2d.drawString("Input Layer", marginsX, marginsY/2);
		g2d.drawString("Hidden Layers", marginsX+spacingX, marginsY/2);
		g2d.drawString("Output Layer", marginsX+mockup.layers.size()*spacingX, marginsY/2);
	}

	private void drawInputs(Graphics2D g2d) 
	{
		Ellipse2D e;
		Color fillColor = Color.ORANGE;
		for(int i = 0; i < mockup.layers.get(0).inputSize; i++ )
		{
			e = new Ellipse2D.Double(marginsX, marginsY+i*spacingY, size, size);
			g2d.setColor(fillColor);
			g2d.fill(e);
			g2d.setColor( Color.BLACK );
			g2d.draw(e);
			//drawing arrows
			drawArrow(g2d, marginsX-2*size, marginsY+size/2+i*spacingY, marginsX, marginsY+size/2+i*spacingY);
		}
		
	}

	private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2)
	{
		Line2D l;
		l = new Line2D.Double(x1, y1, x2, y2);
		g2d.draw(l);
		l = new Line2D.Double(x2,y2, x2-size/2, y2-size/2);
		g2d.draw(l);
		l = new Line2D.Double(x2,y2, x2-size/2, y2+size/2);
		g2d.draw(l);
	}

	private void drawLayers(Graphics2D g2d) 
	{
		
		int X = marginsX+spacingX;
		int Y = marginsY;
		
		Ellipse2D e;
		Line2D l;
		
		for( LayerMockup element : mockup.layers )
		{
			//drawing neurons
			Color fillColor = element.isSigmoid?Color.GREEN:Color.BLUE;
			for( int i = 0; i < element.outputSize; i++ )
			{	
				e = new Ellipse2D.Double(X, Y, size, size);
				g2d.setColor(fillColor);
				g2d.fill(e);
				g2d.setColor(Color.BLACK );
				g2d.draw(e);
				
				//drawing inputs
				for( int j = 0; j < element.inputSize; j++)
				{
					int x1 = X-spacingX+size;
					int y1 = size/2+marginsY+j*spacingY;
					int x2 = X;
					int y2 = Y+size/2;
					l = new Line2D.Double(x1, y1, x2, y2);
					g2d.draw(l);
					g2d.drawString( Float.toString(element.weights[j]), (x2+x1)/2, (y2+y1)/2);
				}//for j
				
				Y+=spacingY;
			}//for i
			Y = marginsY;
			X+= spacingX;
		}//for element			
	}

	private void drawOutput(Graphics2D g2d) 
	{
		
	}
	
	public void setMockup( NeuralNetworkMockup mockup)
	{
		this.mockup = mockup;
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		
		if(mockup != null)
		{	
			int max = mockup.layers.get(0).outputSize;
			for( LayerMockup element : mockup.layers)
			{
				max = element.outputSize>max?element.outputSize:max;
			}
			int x = (mockup.layers.size())*spacingX+size+2*marginsX;
			int y = (max-1)*spacingY+size+2*marginsY;
			return new Dimension( x, y);
		}
		else
		{
			return new Dimension( PREF_WIDTH, PREF_HEIGHT);
		}
	}
}
