package com.dezzy.dictionary.main;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles commands for a dictionary.
 *
 * @author Joe Desmond
 */
public final class CommandHandler {
	
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
			command = commandString.substring(0, commandString.indexOf(" "));
			arg = commandString.substring(commandString.indexOf(" ") + 1);
		}
		
		return receive(command, arg);
	}
	
	/**
	 * Receives a command and its argument, executes the command, and returns a status string.
	 * 
	 * @param command command name
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
				return newDefinition(false, arg);
			case "strongdefine":
				return newDefinition(true, arg);
			case "find":
				return findDefinition(arg);
			case "print":
				return printDictionary(arg);
			case "printto":
				return saveDefinitionsTo(arg);
			case "close":
				return close();
			default:
				return "ERROR: Invalid command!";
		}
	}
	
	/**
	 * Saves the entries and definitions in this dictionary to a text file.
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
		
		final Optional<String> definition = openDictionary.getDefinition(word);
		if (definition.isPresent()) {
			return word + ":\t" + definition.get();
		} else {
			return "No definition exists for \"" + word + "\"";
		}
	}
	
	/**
	 * Adds a definition to the dictionary.
	 * 
	 * @param strong true if the definition should be added regardless of whether or not it already exists
	 * @param defString definition string of the form "<code>"word" definition</code>"
	 * @return status string
	 */
	private final String newDefinition(final boolean strong, final String defString) {
		if (openDictionary == null) {
			return "ERROR: No open dictionary!";
		}
		
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
			final boolean defSuccess = openDictionary.weakDefine(word, definition);
			return defSuccess ? "\"" + word + "\" was defined successfully." : "A definition already exists for \"" + word + "\"!"; 
		} else {
			final boolean defExisted = openDictionary.strongDefine(word, definition);
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
