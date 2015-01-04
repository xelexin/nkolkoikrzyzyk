/**
 * 
 */
package nkolkoikrzyzyk.model;


/**
 * @author Johhny
 *
 */
public class GameModel {
	static private int count = 0;
	
	private int id;
	
	private int[] board;
	
	public int[] getBoard() {
		return board;
	}

	public void setBoard(int[] board) {
		this.board = board;
	}

	public GameModel() 
	{	
		id=++count;	
		board = new int[9];
		for(int i=0;i<9;i++)
			board[i]=0;
	}
	
	public void fastPlay( Player player1, Player player2 )
	{
		System.out.println("Gra nr " + id);
		
		Player activePlayer = player1;
		Player waitingPlayer = player2;
		
		for(int j=0;j<9;j++)
		{
			board = activePlayer.makeMove(board, -1);
			printBoard();
			if(ifWin()==true)
			{
				System.out.println(activePlayer.getName() + " won!");
				activePlayer.youWin(board);
				waitingPlayer.youLost();
				break;
			}
			if(noMoreMove()==true)
			{
				player1.youLost();
				player2.youLost();
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
	public int getId() {
		return id;
	}

	public void printBoard() {
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
		for(int i=0;i<9;i++)
			board[i]=0;
	}
}
