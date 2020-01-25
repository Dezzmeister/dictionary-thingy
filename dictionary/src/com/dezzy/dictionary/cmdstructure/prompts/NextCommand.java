package com.dezzy.dictionary.cmdstructure.prompts;

import com.dezzy.dictionary.cmdstructure.Prompt;

/**
 * Standard prompt for accepting arbitrary commands.
 *
 * @author Joe Desmond
 */
public final class NextCommand extends Prompt {
	
	/**
	 * Creates an NextCommand with the prompt string <code>"Enter a command:"</code>
	 */
	public NextCommand() {
		super("Enter a command:");
	}
}
