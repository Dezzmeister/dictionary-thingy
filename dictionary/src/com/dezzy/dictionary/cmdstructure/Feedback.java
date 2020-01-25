package com.dezzy.dictionary.cmdstructure;

/**
 * Feedback to the user after executing a command. Contains a feedback string to show to the user
 * regarding the previously executed command, a prompt to show to the user for the next command.
 *
 * @author Joe Desmond
 */
public final class Feedback {
	
	/**
	 * Feedback string to be shown to the user
	 */
	public final String feedbackString;
	
	/**
	 * Next prompt to be shown to the user
	 */
	public final Prompt nextPrompt;
	
	/**
	 * Creates feedback with the given feedback string and next prompt.
	 * 
	 * @param _feedbackString feedback string
	 * @param _nextPrompt next prompt
	 */
	public Feedback(final String _feedbackString, final Prompt _nextPrompt) {
		feedbackString = _feedbackString;
		nextPrompt = _nextPrompt;
	}
}
