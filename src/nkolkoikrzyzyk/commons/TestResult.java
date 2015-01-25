/**
 * 
 */
package nkolkoikrzyzyk.commons;

/**
 * @author elohhim
 *
 */
public class TestResult 
{
	public final long p1wins;
	public final long p2wins;
	public final long draws;
	public final long time;
	public final float p1percent;
	public final float p2percent;
	public final float dpercent;
	
	public TestResult( long p1wins, long p2wins, long draws, long time, long games)
	{
		this.p1wins = p1wins;
		this.p2wins = p2wins;
		this.draws = draws;
		this.time = time;
		this.p1percent = (float)p1wins/games*100;
		this.p2percent = (float)p2wins/games*100;
		this.dpercent = (float)draws/games*100;
	}
}
