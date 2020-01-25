package com.dezzy.dictionary.cmdstructure.prompts;

import com.dezzy.dictionary.cmdstructure.Command;
import com.dezzy.dictionary.cmdstructure.Prompt;

/**
 * A prompt asking for confirmation from the user. Runs one command if the user says yes, and another if the user says no.
 *
 * @author Joe Desmond
 */
public final class AreYouSure extends Prompt {
	
	/**
	 * Key associated with the "yes" command in {@link Prompt#metadata}
	 */
	public static final String YES_CMD_KEY = "yes";
	
	/**
	 * Key associated with the "no" command in {@link Prompt#metadata}
	 */
	public static final String NO_CMD_KEY = "no";
	
	/**
	 * Creates an AreYouSure prompt with the given yes and no actions. These commands will be found in {@link Prompt#metadata}
	 * with the keys at {@link #YES_CMD_KEY} and {@link #NO_CMD_KEY}, respectively.
	 * 
	 * @param yesCommand command to be run if the user confirms
	 * @param noCommand command to be run if the user does not confirm
	 */
	public AreYouSure(final Command yesCommand, final Command noCommand) {
		super("Are you sure? (y/n):");
		metadata.put(YES_CMD_KEY, yesCommand);
		metadata.put(NO_CMD_KEY, noCommand);
	}
}
