/**
 * 
 */
package nkolkoikrzyzyk.controller;

import nkolkoikrzyzyk.events.ProgramEvent;

/**
 * @author Johhny
 *
 */
public class MoveMadeEvent extends ProgramEvent 
{
	public int[] newBoard = null;
	
	public MoveMadeEvent(int[] newBoard) 
	{
		this.newBoard = newBoard;
	}

}
