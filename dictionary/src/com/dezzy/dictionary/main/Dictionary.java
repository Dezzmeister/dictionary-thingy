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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
	private final Map<String, Definition> definitions = new HashMap<String, Definition>();
	
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
	 * Returns all definitions stored in the dictionary.
	 * 
	 * @return a reference to the HashMap backing this dictionary
	 */
	final Map<String, Definition> getDefinitions() {
		return definitions;
	}
	
	/**
	 * Attempts to add a definition for a given word/phrase. If the definition already exists, does nothing.
	 * 
	 * @param word word/phrase to add a definition for (case sensitive)
	 * @param definition definition
	 * @return true if the definition was added successfully, false if not
	 */
	public final boolean weakDefine(final String word, final Definition definition) {
		if (!definitions.containsKey(word)) {
			definitions.put(word, definition);
			return true;
		}
		
		return false;
	}
	
	public final void temp_RAWDEFINE(final String word, final Definition definition) {
		definitions.put(word, definition);
	}
	
	/**
	 * Tries to remove an entry from this dictionary, and returns true if the operation was successful.
	 * 
	 * @param word word/phrase to remove
	 * @return true if the word/phrase was removed, false if it didn't exist
	 */
	public final boolean remove(final String word) {
		return definitions.remove(word) != null;
	}
	
	/**
	 * A single result of a dictionary search.
	 *
	 * @author Joe Desmond
	 */
	public final class SearchResult {
		/**
		 * Word/phrase pointing to the search result
		 */
		public final String definitionString;
		
		/**
		 * A metric of the relevancy of this search result
		 */
		public final int score;
		
		/**
		 * Creates a search result with the given definition string (search candidate) and score.
		 * 
		 * @param _definitonString search candidate containing the search term
		 * @param _score relevancy of this result
		 */
		public SearchResult(final String _definitionString, final int _score) {
			definitionString = _definitionString;
			score = _score;
		}
	}
	
	/**
	 * {@link SearchResult} sorting metric, compares by relevancy first and alphabetical order second.
	 *
	 * @author Joe Desmond
	 */
	public static final class AlphabeticalRelevancyComparator implements Comparator<SearchResult> {
		
		@Override
		public int compare(final SearchResult s1, final SearchResult s2) {
			if (s1.score == s2.score) {
				return s2.definitionString.compareTo(s1.definitionString);
			} else if (s1.score > s2.score) {
				return 1;
			} else {
				return -1;
			}
		}
	}
	
	/**
	 * Searches the dictionary for a given search term and returns EVERY result, even completely irrelevant results.
	 * 
	 * @param regex regular expression search term
	 * @return search results
	 */
	public final List<SearchResult> searchAll(final String regex) {
		final Pattern searchPattern = Pattern.compile(regex);
		
		return definitions.keySet()
				.stream()
				.map(word -> searchDefinitionString(searchPattern, getDefinitionString(word)))
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns the search result for a search term and one candidate string.
	 * 
	 * @param pattern search pattern (compiled regex)
	 * @param defString single search candidate (word and definition string)
	 * @return search result data
	 */
	private final SearchResult searchDefinitionString(final Pattern pattern, final String defString) {
		final Matcher matcher = pattern.matcher(defString);
		
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		
		return new SearchResult(defString, count);
	}
	
	/**
	 * Adds a definition or updates an existing definition for a given word/phrase.
	 * 
	 * @param word word/phrase to add a definition for (case sensitive)
	 * @param definition definition
	 * @return true if the definition existed before, false if it's new
	 */
	public final boolean strongDefine(final String word, final Definition definition) {
		final boolean exists = definitions.containsKey(word);
		
		definitions.put(word, definition);
		
		return !exists;
	}
	
	/**
	 * Gets the definition for a word/phrase.
	 * 
	 * @param word word/phrase (case sensitive)
	 * @return definition for the word ({@link Optional#empty} if the word is not defined)
	 */
	public final Optional<Definition> getDefinition(final String word) {
		final Definition definition = definitions.get(word);
		
		return (definition == null) ? Optional.empty() : Optional.of(definition);
	}
	
	/**
	 * Returns a text representation of this dictionary with all words (in alphabetical order) and their definitions.
	 * 
	 * @return text version of this dictionary (human readable)
	 */
	@Override
	public final String toString() {
		final List<String> sortedWords = getSortedWords();
		
		final StringBuilder sb = new StringBuilder(name + System.lineSeparator());
		sortedWords.forEach(word -> sb.append(System.lineSeparator() + getDefinitionString(word)));
		
		return sb.toString();
	}
	
	/**
	 * Returns a list of all defined words in the dictionary, sorted alphabetically (case insensitive).
	 * 
	 * @return a list of sorted words
	 */
	final List<String> getSortedWords() {
		final Set<String> words = definitions.keySet();
		final List<String> sortedWords = new ArrayList<String>();
		
		sortedWords.addAll(words);
		Collections.sort(sortedWords, String.CASE_INSENSITIVE_ORDER);
		
		return sortedWords;
	}
	
	/**
	 * Returns a readable definition string containing a word and its definition.
	 * 
	 * @param word word/phrase
	 * @return definition string
	 * @throws NullPointerException if the definition does not exist
	 */
	private final String getDefinitionString(final String word) {
		final Definition definition = definitions.get(word);
		
		if (definition == null) {
			throw new NullPointerException("No definition exists for \"" + word + "\"!");
		}
		
		return word + ":\t" + definition.definition();
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
