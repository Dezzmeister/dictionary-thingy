package com.dezzy.dictionary.main;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dezzy.dictionary.main.Dictionary.SearchResult;

/**
 * Handles commands for a dictionary.
 *
 * @author Joe Desmond
 */
public final class CommandHandler {
	
	/**
	 * Expected format for a date argument
	 */
	private static final SimpleDateFormat DATE_ARG_FORMAT = new SimpleDateFormat("MM:dd:yyyy:hh:mm");
	
	/**
	 * The current dictionary, or null if there is no open dictionary
	 */
	private Dictionary openDictionary;
	
	/**
	 * Most recent path specified for a dictionary, or null if no path has been specified
	 */
	private String dictionaryPath;
	
	/**
	 * Latest dictionary printout, or null if the dictionary has not been printed out
	 */
	private String dictionaryText;
	
	/**
	 * Creates a CommandHandler.
	 */
	public CommandHandler() {
		
	}
	
	/**
	 * Receives an unformatted input string, parses it into a command/argument pair, tries to execute the command, and returns a status string.
	 * 
	 * @param commandString unformatted input string
	 * @return status string
	 */
	public final String receive(final String commandString) {
		String command = commandString;
		String arg = "";
		
		if (commandString.contains(" ")) {
			command = commandString.substring(0, commandString.indexOf(" ")).toLowerCase();
			arg = commandString.substring(commandString.indexOf(" ") + 1);
		}
		
		return receive(command, arg);
	}
	
	/**
	 * Receives a command and its argument, executes the command, and returns a status string.
	 * 
	 * @param command lowercase command name
	 * @param arg command argument
	 * @return status string
	 */
	public final String receive(final String command, final String arg) {
		switch (command) {
			case "open":
				return openDictionary(arg);
			case "create":
				return createDictionary(arg);
			case "save":
				return save(arg);
			case "weakdefine":
				return newDefinition(false, arg, new Date());
			case "strongdefine":
				return newDefinition(true, arg, new Date());
			case "changedate":
				return changeDate(arg);
			case "remove":
				return removeDefinition(arg);
			case "find":
				return findDefinition(arg);
			case "print":
				return printDictionary(arg);
			case "printto":
				return saveDefinitionsTo(arg);
			case "search":
				return searchAll(arg);
			case "close":
				return close();
			default:
				return "ERROR: Invalid command!";
		}
	}
	
	/**
	 * Changes the entry date of an existing definition.
	 * 
	 * @param arg argument string, formatted as such: <code>MM:dd:yyyy:hh:mm word</code>, where "word" is the entry whose entry date to change
	 * @return status string
	 */
	private final String changeDate(final String arg) {
		if (openDictionary == null) {
			return "ERROR: No dictionary is open!"; 
		}
		
		if (!arg.contains(" ")) {
			return "ERROR: Invalid date or definition argument!";
		}
		final String dateString = arg.substring(0, arg.indexOf(" "));
		final String word = arg.substring(arg.indexOf(" ") + 1);
		Date date;
		
		try {
			date = DATE_ARG_FORMAT.parse(dateString);
		} catch (ParseException e) {
			return "ERROR: Date string is formatted incorrectly!";
		}
		
		final Optional<Definition> oldDefinition = openDictionary.getDefinition(word);
		if (oldDefinition.isEmpty()) {
			return "ERROR: No definition for \"" + word + "\" exists in the dictionary!";
		}
		
		final Date oldDate = oldDefinition.get().changeEntryDate(date);
		
		return "Changed entry date for \"" + word + "\" from " + DATE_ARG_FORMAT.format(oldDate) + " to " + dateString;
	}
	
	/**
	 * Searches the dictionary for a given regular expression string.
	 * 
	 * @param searchRegex search expression
	 * @return list of results (delimited by newlines), or status string
	 */
	private final String searchAll(final String searchRegex) {
		if (openDictionary == null) {
			return "ERROR: No dictionary is open!"; 
		}
		
		final List<SearchResult> results = openDictionary.searchAll(searchRegex);
		Collections.sort(results, new Dictionary.AlphabeticalRelevancyComparator());
		
		final List<SearchResult> relevantResults = new ArrayList<SearchResult>();
		
		for (int i = results.size() - 1; i >= 0; i--) {
			final SearchResult result = results.get(i);
			if (result.score == 0) {
				break;
			} else {
				relevantResults.add(result);
			}
		}
		
		if (relevantResults.isEmpty()) {
			return "No results";
		}
		
		final StringBuilder sb = new StringBuilder("Results (" + relevantResults.size() + "):" + System.lineSeparator());
		for (SearchResult result : relevantResults) {
			sb.append(System.lineSeparator() + result.definitionString);
		}
		
		return sb.toString();
	}
	
	/**
	 * Attempts to remove a definition from the dictionary.
	 * 
	 * @param word word/phrase to remove
	 * @return status string
	 */
	private final String removeDefinition(final String word) {
		if (openDictionary == null) {
			return "ERROR: No dictionary is open!"; 
		}
		
		try {
			openDictionary.remove(word);
			return "Removed \"" + word + "\" from the dictionary";
		} catch (NullPointerException e) {
			return "ERROR: No definition for \"" + word + "\" exists in the dictionary!";
		}
	}
	
	/**
	 * Saves the entries and definitions in the dictionary to a text file.
	 * 
	 * @param path path of the text file
	 * @return status string
	 */
	private final String saveDefinitionsTo(final String path) {
		final String definitions = printDictionary("new");
		
		if (definitions.startsWith("ERROR")) {
			return definitions;
		}
		
		try (PrintWriter pw = new PrintWriter(new FileWriter(new File(path)))) {
			pw.print(definitions);
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR: Problem occurred while trying to write to text file!";
		}
		
		return "Saved dictionary printout to \"" + path + "\"";
	}
	
	/**
	 * Prints all entries in the dictionary (and their definitions), in alphabetical order.
	 * 
	 * @param versionArg either "new" or "current" to specify new definition list or previously generated one <br>
	 * 					 using "current" with no generated definition list will generate a new one
	 * @return String containing the name of the dictionary and all entries in it
	 */
	private final String printDictionary(final String versionArg) {
		if (openDictionary == null) {
			return "ERROR: No dictionary is open!"; 
		}
		
		if (versionArg.equals("new")) {
			dictionaryText = openDictionary.toString();
			
			return dictionaryText;
		} else if (versionArg.equals("current")) {
			if (dictionaryText == null) {
				dictionaryText = openDictionary.toString();
			}
			
			return dictionaryText;
		} else {
			return "ERROR: Invalid version argument!";
		}
	}
	
	/**
	 * Closes the current dictionary, if one is open (sets <code>openDictionary</code> to <code>null</code>).
	 * 
	 * @return status string
	 */
	private final String close() {
		if (openDictionary == null) {
			return "No dictionary is open!";
		} else {
			final String name = openDictionary.name;
			openDictionary = null;
			return "Closed \"" + name + "\"";
		}
	}
	
	/**
	 * Finds the definition of a given word/phrase in the current dictionary.
	 * 
	 * @param word word/phrase to look up
	 * @return the definition if it exists, or a status string if it doesn't
	 */
	private final String findDefinition(final String word) {
		if (openDictionary == null) {
			return "ERROR: No dictionary is open!";
		}
		
		final Optional<Definition> definition = openDictionary.getDefinition(word);
		if (definition.isPresent()) {
			return word + ":\t" + definition.get().definition();
		} else {
			return "No definition exists for \"" + word + "\"";
		}
	}
	
	/**
	 * Adds a definition to the dictionary.
	 * 
	 * @param strong true if the definition should be added regardless of whether or not it already exists
	 * @param defString definition string of the form "<code>"word" definition</code>"
	 * @param date date of the definition
	 * @return status string
	 */
	private final String newDefinition(final boolean strong, final String defString, final Date date) {
		if (openDictionary == null) {
			return "ERROR: No open dictionary!";
		}
		
		//Regular expression that matches with the word being defined, which should be surrounded by quotes. Any matches after the first are ignored
		final String regex = "(\")[^\"]+(\"\\s)";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(defString);
		
		String rawWord;
		
		if (matcher.find()) {
			rawWord = matcher.group(0);
		} else {
			return "ERROR: Malformed definition argument!";
		}
		
		final String word = rawWord.replace('"', ' ').trim();
		final String definition = defString.substring(defString.indexOf(rawWord) + rawWord.length()).trim();
		
		if (!strong) {
			final boolean defSuccess = openDictionary.weakDefine(word, new Definition(definition, date));
			return defSuccess ? "\"" + word + "\" was defined successfully." : "A definition already exists for \"" + word + "\"!"; 
		} else {
			final boolean defExisted = openDictionary.strongDefine(word, new Definition(definition, date));
			return defExisted ? "Definition for \"" + word + "\" was updated." : "\"" + word + "\" was defined successfully.";
		}
	}
	
	/**
	 * Saves this dictionary to a file.
	 * 
	 * @param optionalPath the path to save to, or the empty string to use a previously set path
	 * @return status string
	 */
	private final String save(final String optionalPath) {		
		if (openDictionary != null) {
			if (optionalPath.equals("")) {
				if (dictionaryPath != null) {
					return trySaveDictionary();
				} else {
					return "ERROR: Nowhere to save to!";
				}
			} else {
				dictionaryPath = optionalPath;
				return trySaveDictionary();				
			}
		} else {
			return "ERROR: No dictionary is currently open!";
		}
	}
	
	/**
	 * Tries to save the dictionary to <code>dictionaryPath</code>.
	 * 
	 * @return status string
	 */
	private final String trySaveDictionary() {
		try {
			openDictionary.save(dictionaryPath);
			return "Saved current dictionary to \"" + dictionaryPath + "\"";
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR: Problem saving current dictionary!";
		}
	}
	
	/**
	 * Creates a new dictionary and sets it as the current dictionary. Does not save the dictionary!
	 * 
	 * @param dictionaryName name of the new dictionary
	 * @return status string
	 */
	private final String createDictionary(final String dictionaryName) {
		openDictionary = new Dictionary(dictionaryName);
		return "Created a new dictionary named \"" + dictionaryName + "\"";
	}
	
	/**
	 * Opens the dictionary at the given path.
	 * 
	 * @param _dictionaryPath path to a serialized dictionary object
	 * @return status string
	 */
	private String openDictionary(final String _dictionaryPath) {
		try {
			dictionaryPath = _dictionaryPath;
			openDictionary = Dictionary.load(_dictionaryPath);
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR: Problem opening dictionary at \"" + _dictionaryPath +"\"";
		}
		
		return "Opened \"" + openDictionary.name + "\"";
	}
}
