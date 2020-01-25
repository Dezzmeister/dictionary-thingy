package com.dezzy.dictionary.cmdstructure;

/**
 * A command that can be executed. 
 *
 * @author Joe Desmond
 */
public interface Command {
	
	/**
	 * Executes this command.
	 * 
	 * @param executor prompt that initiated this command
	 * @param session current open session
	 * @param args arguments to the command
	 * @return feedback
	 */
	public Feedback execute(final Prompt executor, Session session, final String args);
}
