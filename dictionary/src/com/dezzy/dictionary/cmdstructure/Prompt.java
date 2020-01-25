package com.dezzy.dictionary.cmdstructure;

import java.util.HashMap;
import java.util.Map;

/**
 * A user prompt; when a command is executed it will return a prompt for the user to enter the next command.
 *
 * @author Joe Desmond
 */
public abstract class Prompt {
	
	/**
	 * The string that the user sees; ex> <code>"Enter a command:</code>
	 */
	public final String promptString;
	
	/**
	 * Any data that the next command may need
	 */
	protected final Map<String, Object> metadata;
	
	/**
	 * Creates a prompt with the given prompt string and no metadata.
	 * 
	 * @param _promptString prompt string
	 */
	protected Prompt(final String _promptString) {
		promptString = _promptString;
		metadata = new HashMap<String, Object>();
	}
	
	/**
	 * Gets the metadata associated with the given key. 
	 * 
	 * @param key String key
	 * @return metadata
	 * @see #metadata
	 */
	public final Object getMetadata(final String key) {
		return metadata.get(key);
	}
}
