/**
 * 
 */
package nkolkoikrzyzyk.view;

import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.JFrame;

import nkolkoikrzyzyk.events.CloseNeuralNetworksModuleEvent;
import nkolkoikrzyzyk.events.ProgramEvent;

/**
 * @author Johhny
 *
 */
public class NeuralNetworksWindow extends JFrame implements WindowListener 
{
	private NeuralNetworksWindow instance;
	
	private BlockingQueue<ProgramEvent> blockingQueue;
	
	public NeuralNetworksWindow(BlockingQueue<ProgramEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
		this.initialize();
	}

	private void initialize()
	{
		this.addWindowListener(this);
		this.setBounds(100, 100, 600, 600);
		this.setResizable( false );
		this.setLayout( new GridLayout(0,2) );
		this.setVisible( true );
		
		fill();
	}

	private void fill() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {
		blockingQueue.add( new CloseNeuralNetworksModuleEvent());
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
