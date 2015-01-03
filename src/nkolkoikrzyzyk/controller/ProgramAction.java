package nkolkoikrzyzyk.controller;

import nkolkoikrzyzyk.events.ProgramEvent;

/**
 * interface for controllers response for model queries
 * needs definition of methods
 * @version 1.0
 */
public interface ProgramAction
{
	abstract public void go(ProgramEvent e);
}

