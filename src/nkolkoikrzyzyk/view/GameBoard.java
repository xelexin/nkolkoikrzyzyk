package nkolkoikrzyzyk.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.concurrent.BlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nkolkoikrzyzyk.commons.BoardMockup;
import nkolkoikrzyzyk.events.BoardClickedEvent;
import nkolkoikrzyzyk.events.ProgramEvent;
import nkolkoikrzyzyk.model.Mark;

@SuppressWarnings("serial")
public class GameBoard extends JPanel
{
	private class BoardField extends JPanel
	{
		private int position;
		private Mark state;
						
		public Mark getState() {
			return state;
		}

		public void setState(Mark state) {
			this.state = state;
		}

		public BoardField(final int position) {
			this.position = position;
			this.state = Mark.EMPTY;
			this.setBackground(Color.WHITE);
			
			this.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					if (BoardField.this.state == Mark.EMPTY) {
						try 
						{
							blockingQueue.put(new BoardClickedEvent(position, gameId));
						}
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						}	
					}
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					if( state == Mark.EMPTY)
						BoardField.this.setBackground( new Color( 0xDDFFDD) );
					else
						BoardField.this.setBackground( new Color( 0xFFDDDD) );
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					BoardField.this.setBackground(Color.WHITE);
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}

		@Override
		public void paintComponent( Graphics g )
		{
			//initialization
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor( Color.BLACK );
			
			if( this.state == Mark.CROSS)
			{
				Line2D l = new Line2D.Double(0,0,this.getWidth(), this.getHeight());
				g2d.draw(l);
				l = new Line2D.Double(0,this.getWidth(), this.getHeight(), 0);
				g2d.draw(l);
			}
			else if(this.state == Mark.NOUGHT)
			{
				Ellipse2D e = new Ellipse2D.Double( 0, 0, this.getWidth(), this.getHeight());
				g2d.draw(e);
			}			
			

			
		}
	}
	
	private BlockingQueue<ProgramEvent> blockingQueue;
	
	private int gameId;
	private BoardField[] fields = null;
	
	public GameBoard(BlockingQueue<ProgramEvent> blockingQueue, int gameId) 
	{
		this.blockingQueue = blockingQueue;
		this.gameId = gameId;
		this.initialize();
	}
	
	private void initialize()
	{
		this.setBounds(0, 0, 300, 300);
		this.setBackground(Color.BLACK);
		this.setLayout( new GridLayout(3,3, 5, 5));
		this.fields = new BoardField[9];
		for( int i = 0; i < 9; i++)
		{
			BoardField button = new BoardField(i);
			this.fields[i] = button;
			this.add(button);
		}
		
		this.setVisible( true );
	}
	
	public void setFieldState(int position, Mark markType) {
		this.fields[position].setState(markType);
	}

	public void updateState(int[] newBoard) {
		for(int i = 0; i < 9; i++)
		{
			switch( newBoard[i] )
			{
			case 0:
				this.fields[i].setState(Mark.EMPTY);
				break;
				
			case -1:
				this.fields[i].setState(Mark.NOUGHT);
				break;
				
			case 1:
				this.fields[i].setState(Mark.CROSS);
				break;
				
			default:
				break;
			}
			
		}
	}
}
