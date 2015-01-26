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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import nkolkoikrzyzyk.commons.LayerMockup;
import nkolkoikrzyzyk.commons.NeuralNetworkMockup;

/**
 * @author elohhim
 *
 */
public class NeuralNetworkDrawPanel extends JPanel implements MouseListener
{
	private static final int PREF_WIDTH = 600;
	private static final int PREF_HEIGHT = 600;
	
	private NeuralNetworkMockup mockup = null;
	
	private int selectedColumn = -1;
	private int selectedRow = -1;
	
	private static int size = 15;
	private static int spacingX = 80;
	private static int spacingY = 50;
	private static int marginsX = spacingX/2;
	private static int marginsY = spacingY;
	
	public NeuralNetworkDrawPanel() 
	{		
		initialize();
	}
	
	private void initialize() 
	{
		this.addMouseListener(this);
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
		drawRotate(g2d, marginsX/2, marginsY/2, 90, "Input");
		drawRotate(g2d, marginsX/2, marginsY/2+spacingY, 90, "Hidden");
		drawRotate(g2d, marginsX/2, marginsY/2+mockup.layers.size()*spacingY, 90, "Output");
		
	}

	private static void drawRotate(Graphics2D g2d, double x, double y, int angle, String text) 
	{    
	    g2d.translate((float)x,(float)y);
	    g2d.rotate(Math.toRadians(angle));
	    g2d.drawString(text,0,0);
	    g2d.rotate(-Math.toRadians(angle));
	    g2d.translate(-(float)x,-(float)y);
	}  
	
	private void drawInputs(Graphics2D g2d) 
	{
		Ellipse2D e;
		Color fillColor = Color.ORANGE;
		for(int i = 0; i < mockup.layers.get(0).inputSize; i++ )
		{
			e = new Ellipse2D.Double(marginsX+i*spacingX, marginsY, size, size);
			g2d.setColor(fillColor);
			g2d.fill(e);
			g2d.setColor( Color.BLACK );
			g2d.draw(e);
			//drawing arrows
			drawArrow(g2d, marginsX+size/2+i*spacingX, marginsY-2*size, marginsX+size/2+i*spacingX, marginsY);
		}
		
	}

	private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2)
	{
		Line2D l;
		l = new Line2D.Double(x1, y1, x2, y2);
		g2d.draw(l);
		l = new Line2D.Double(x2,y2, x2-size/2, y2-size/2);
		g2d.draw(l);
		l = new Line2D.Double(x2,y2, x2+size/2, y2-size/2);
		g2d.draw(l);
	}

	private void drawLayers(Graphics2D g2d) 
	{
		
		int X = marginsX;
		int Y = marginsY+spacingY;
		
		Ellipse2D e;
		Line2D l;
		int row = 0;
		for( LayerMockup element : mockup.layers )
		{
			//drawing neurons
			Color fillColor = element.isSigmoid?Color.GREEN:Color.BLUE;
			for( int i = 0; i < element.outputSize; i++ )
			{	
				e = new Ellipse2D.Double(X, Y, size, size);
				g2d.setColor((i==selectedColumn && row==selectedRow)?Color.MAGENTA:fillColor);
				g2d.fill(e);
				g2d.setColor((i==selectedColumn && row==selectedRow)?Color.MAGENTA:Color.BLACK );
				g2d.draw(e);
				
				//drawing inputs
				for( int j = 0; j < element.inputSize+1; j++)
				{
					int x1 = size/2+marginsX+j*spacingX;
					int y1 = Y-spacingY+size;
					int x2 = X+size/2;
					int y2 = Y;
					l = new Line2D.Double(x1, y1, x2, y2);
					g2d.draw(l);
					if(j==element.inputSize) 
					{
						e = new Ellipse2D.Double(x1-size/2, y1-size, size, size);
						g2d.setColor(Color.LIGHT_GRAY);
						g2d.fill(e);
						g2d.setColor(Color.BLACK );
						g2d.draw(e);
						g2d.drawString("1", x1-size/4, y1-size/4);
					}
				}//for j
				
				X+=spacingX;
			}//for i
			X = marginsX;
			Y+= spacingY;
			row++;
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
			int max = mockup.layers.get(0).inputSize;
			for( LayerMockup element : mockup.layers)
			{
				max = element.outputSize>max?element.outputSize:max;
			}
			int x = (max)*spacingX+size+2*marginsX;
			int y = (mockup.layers.size())*spacingY+size+2*marginsY;
			return new Dimension( x, y);
		}
		else
		{
			return new Dimension( PREF_WIDTH, PREF_HEIGHT);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		int x = e.getX()-marginsX;
		int y = e.getY() - marginsY - spacingY;
		int hCell = x/spacingX;
		int vCell = y/spacingY;
		if(x > hCell*spacingX && x < size+hCell*spacingX)
		{
			if(y > vCell*spacingY && y < size+vCell*spacingY)
			{
				selectedColumn = hCell;
				selectedRow = vCell;
			}
		}
		else
		{
			selectedColumn = -1;
			selectedRow = -1;
		}
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
