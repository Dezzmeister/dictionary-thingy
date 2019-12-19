package com.dezzy.dictionary.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
	
	static {
		System.getProperty("line.separator");
	}
	
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
	 * Returns a text representation of this dictionary with all words (in alphabetical order) and their definitions.
	 * 
	 * @return text version of this dictionary (human readable)
	 */
	@Override
	public final String toString() {
		final Set<String> words = definitions.keySet();
		final List<String> sortedWords = new ArrayList<String>();
		for (String word : words) {
			sortedWords.add(word);
		}
		Collections.sort(sortedWords);
		
		final StringBuilder sb = new StringBuilder(name + System.lineSeparator());
		
		for (String word : sortedWords) {
			final String definition = definitions.get(word);
			
			sb.append(System.lineSeparator() + word + ":\t" + definition);
		}
		
		return sb.toString();
	}
	
	/**
	 * Serializes this Dictionary and saves it to a file.
	 * 
	 * @param path path of the file
	 * @throws IOException if there is a problem creating/writing to the file
	 */
	public final void save(final String path) throws IOException {
		final FileOutputStream fos = new FileOutputStream(new File(path));
		final ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(this);
		
		oos.close();
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
		
		return dictionary;
	}
}
