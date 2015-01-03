package nkolkoikrzyzyk.events;


public class BoardClickedEvent extends ProgramEvent {

	public int position;
	public int gameId;
	
	public BoardClickedEvent(int position, int gameId) {
		this.position = position;
		this.gameId = gameId;
	}
}
