package com.dezzy.dictionary.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A named dictionary that maps String words and phrases to String definitions.
 *
 * @author Joe Desmond
 */
public final class Dictionary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7979506156638295020L;
	
	/**
	 * Name of the dictionary
	 */
	public final String name;
	
	/**
	 * Definitions in the dictionary
	 */
	private final Map<String, String> definitions = new HashMap<String, String>();
	
	/**
	 * Creates a dictionary with the given name and no definitions. <br>
	 * Definitions must be added with {@link #weakDefine} and {@link #strongDefine}.
	 * 
	 * @param _name name of the dictionary
	 */
	public Dictionary(final String _name) {
		name = _name;
	}
	
	/**
	 * Attempts to add a definition for a given word/phrase. If the definition already exists, does nothing.
	 * 
	 * @param word word/phrase to add a definition for (case insensitive)
	 * @param definition definition
	 * @return true if the definition was added successfully, false if not
	 */
	public final boolean weakDefine(final String word, final String definition) {
		if (!definitions.containsKey(word.toLowerCase())) {
			definitions.put(word.toLowerCase(), definition);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Adds a definition or updates an existing definition for a given word/phrase.
	 * 
	 * @param word word/phrase to add a definition for (case insensitive)
	 * @param definition definition
	 * @return true if the definition existed before, false if it's new
	 */
	public final boolean strongDefine(final String word, final String definition) {
		final boolean exists = definitions.containsKey(word.toLowerCase());
		
		definitions.put(word.toLowerCase(), definition);
		
		return !exists;
	}
	
	/**
	 * Gets the definition for a word/phrase.
	 * 
	 * @param word word/phrase (case insensitive)
	 * @return definition for the word ({@link Optional#empty} if the word is not defined)
	 */
	public final Optional<String> getDefinition(final String word) {
		final String definition = definitions.get(word.toLowerCase());
		
		return (definition == null) ? Optional.empty() : Optional.of(definition);
	}

	/**
	 * Loads a serialized Dictionary from a file.
	 * 
	 * @param path path to the dictionary
	 * @return the dictionary
	 * @throws IOException if there is a problem locating/reading the file
	 * @throws ClassNotFoundException if the file is not a serialized object
	 */
	public static final Dictionary load(final String path) throws IOException, ClassNotFoundException {
		final FileInputStream fis = new FileInputStream(new File(path));
		final ObjectInputStream ois = new ObjectInputStream(fis);
		final Dictionary dictionary = (Dictionary) ois.readObject();
		
		ois.close();
		fis.close();
		
		return dictionary;
	}
}
