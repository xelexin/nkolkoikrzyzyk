/**
 * 
 */
package nkolkoikrzyzyk.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import nkolkoikrzyzyk.controller.Player;


/**
 * @author Johhny
 *
 */
public class GameModel 
{
	static private int count = 0;

	private int id;

	private int[] board;

	private List<int[]> history;

	public int[] getBoard() 
	{
		return board;
	}

	public void setBoard(int[] board) 
	{
		this.board = board;
		history.add( Arrays.copyOf(board, board.length) );
	}

	public GameModel() 
	{	
		id=++count;
		history = new LinkedList<int[]>();
		board = new int[9];
		for(int i=0;i<9;i++)
			board[i]=0;
	}

	public void fastPlay( Player player1, Player player2 )
	{
		System.out.println("Game #" + id);

		Player activePlayer = player1;
		Player waitingPlayer = player2;

		for(int j=0;j<9;j++)
		{
			board = activePlayer.makeMove(board, -1);
			history.add(Arrays.copyOf(board, board.length) );
			printBoard();
			if(ifWin()==true)
			{
				System.out.println(activePlayer.getName() + " won!");
				activePlayer.youWin( getEnderHistoryStack() );
				waitingPlayer.youLost( getNotEnderHistoryStack() );
				break;
			}
			if(noMoreMove()==true)
			{
				activePlayer.youDraw(getEnderHistoryStack());
				waitingPlayer.youDraw(getNotEnderHistoryStack());
				System.out.println("Remis");
				break;
			}			
			//swapping players
			Player temp = waitingPlayer; 
			waitingPlayer = activePlayer;
			activePlayer = temp;
		}
	}

	/**
	 * @return the id
	 */
	public int getId() 
	{
		return id;
	}

	public void printBoard() 
	{
		for(int i=0;i<9;i++)
		{
			if(board[i]<0)
				System.out.print("O");
			else if(board[i]>0)
				System.out.print("X");
			else
				System.out.print(".");

			if (i%3==2)
				System.out.println();
		}
	}

	public void printHistory()
	{
		System.out.println("Game history: ");
		for( int[] element : history )
		{
			GameModel.printBoard(element);
		}
	}

	public static void printBoard(int[]  board) 
	{
		for(int i=0;i<9;i++)
		{
			if(board[i]<0)
				System.out.print("O");
			else if(board[i]>0)
				System.out.print("X");
			else
				System.out.print(".");

			if (i%3==2)
				System.out.println();
		}
		System.out.println("");
	}

	public boolean noMoreMove() {
		for(int i=0;i<9;i++)
		{
			if(board[i]==0)
				return false;
		}
		return true;
	}

	public boolean ifWin() {
		boolean win=false;
		//test poziomy
		for(int i=0;i<3;i++)
		{
			if(board[i*3]==board[i*3+1] && board[i*3+1]==board[i*3+2] && (board[i*3]==1 || board[i*3]==-1))
			{
				win=true;
			}
		}
		//test pionowy
		for(int i=0;i<3;i++)
		{
			if(board[i]==board[(i+6)] && board[(i+6)]==board[(i+3)] && (board[i]==1 || board[i]==-1))
			{
				win=true;
			}
		}
		//test z lewej na prawa
		if(board[0]==board[4] && board[8]==board[4] && (board[0]==1 || board[0]==-1))
		{
			win=true;
		}
		//test z prawej na lewa
		if(board[2]==board[4] && board[6]==board[4] && (board[4]==1 || board[4]==-1))
		{
			win=true;
		}
		return win;
	}

	public void reset() 
	{
		history.clear();
		for(int i=0;i<9;i++)
			board[i]=0;
	}

	/*public static double[] hashXO( int[] board )
	{
		double hashX = 0.0f;
		double hashO = 0.0f;
		for( int i = 0; i<9; i++)
		{
			if(board[i] == 1)
				hashX += Math.pow(0.5, i+1);
			if(board[i] == -1)
				hashO += Math.pow(0.5, i+1);
		}
		return new double[]{hashX, hashO};
	}*/

	public static double hash( int[] board )
	{
		double hash = 0.0f;
		for( int i = 0; i<9; i++)
		{
			if(board[i] != 0)
				hash += Math.pow(0.5, board[i]*(i+1));
		}
		return hash;
	}

	public static int[] unhash( double hash )
	{
		System.out.println("Unhashing " + hash);
		int[] unhashed = new int[]{0,0,0,0,0,0,0,0,0};
		for(int i=-9; i<9; i++)
		{
			if( i == 0 ) continue;
			//System.out.println(i + " " + hash + " " + Math.pow(0.5, i));
			if( hash >= Math.pow(0.5, i))
			{
				unhashed[Math.abs(i)-1] = Math.abs(i)/i;
				hash -= Math.pow(0.5, i);
				GameModel.printBoard(unhashed);
			}
		}
		return unhashed;
	}

	public static int[] map(int[] board)
	{
		int[] temp = Arrays.copyOf(board, board.length);
		//		double[] tempHashXO = hashXO(temp);
		double tempHash = hash(temp);
		int[] mapped = temp;
		//		double[] mappedHashXO = tempHashXO;
		double mappedHash = hash(temp);
		int [] flip = verticalFlip(temp);
		//		double[] flipHashXO = hashXO(flip);
		double flipHash = hash(temp);
		if(flipHash/*XO[0]*/ < mappedHash/*XO[0]*/)
		{
			mapped = temp;
			mappedHash/*XO*/ = tempHash/*XO*/;
		} 
		/*else if ( flipHashXO[0] == mappedHashXO[0]) 
		{
			if( flipHashXO[1] < mappedHashXO[1])
			{
				mapped = temp;
				mappedHashXO = tempHashXO;
			}
		}*/

		for( int i = 0; i<3; i++)
		{
			temp = rotateCW(temp);
			if(tempHash/*XO[0] */< mappedHash/*XO[0]*/)
			{
				mapped = temp;
				mappedHash/*XO*/ = tempHash/*XO*/;
			} 
			/*else if ( tempHashXO[0] == mappedHashXO[0]) 
			{
				if( tempHashXO[1] < mappedHashXO[1])
				{
					mapped = temp;
					mappedHashXO = tempHashXO;
				}		
			}*/

			flip = verticalFlip(temp);
			flipHash/*XO */= hash/*XO*/(flip);
			if(flipHash/*XO[0] */< mappedHash/*XO[0]*/)
			{
				mapped = flip;
				mappedHash/*XO*/ = flipHash/*XO*/;
			} 
			/*else if ( flipHashXO[0] == mappedHashXO[0]) 
			{
				if( flipHashXO[1] < mappedHashXO[1])
				{
					mapped = temp;
					mappedHashXO = flipHashXO;
				}
			}*/
		}
		return mapped;
	}

	public static int[] verticalFlip( int[] board )
	{
		int[] flipped = new int[9];
		for(int i = 0; i<9 ; i+=3)
		{
			flipped[i] = board[i+2];
			flipped[i+1] = board[i+1];
			flipped[i+2] = board[i];
		}
		return flipped;
	}

	public static int[] rotateCW( int[] board )
	{
		int[] rotated = new int[9];
		for( int i = 0; i<3; i++)
		{
			for(int j = 0; j<3; j++)
			{
				rotated[i*3+j] = board[(2-j)*3 + i];
			}
		}
		return rotated;
	}

	public static int[] rotateCCW( int[] board )
	{
		int[] rotated = new int[9];
		for( int i = 0; i<3; i++)
		{
			for(int j = 0; j<3; j++)
			{
				rotated[(2-j)*3 + i] = board[i*3+j];
			}
		}
		return rotated;
	}

	public Stack<int[]> getEnderHistoryStack() 
	{
		Stack<int[]> stack = new Stack<int[]>();
//		System.out.println("Ender history:" );
		for( int i  = history.size()%2==1?0:1; i<history.size(); i+=2)
		{
			stack.push(history.get(i));
//			GameModel.printBoard(history.get(i));
		}
		return stack;
	}

	public Stack<int[]> getNotEnderHistoryStack() 
	{
		Stack<int[]> stack = new Stack<int[]>();
//		System.out.println("Not ender history:" );
		for( int i  = history.size()%2==1?1:0; i<history.size(); i+=2)
		{
			stack.push(history.get(i));
//			GameModel.printBoard(history.get(i));
		}
		return stack;
	}

}
