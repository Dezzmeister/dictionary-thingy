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
import com.dezzy.dictionary.stats.Histogram;
import com.dezzy.dictionary.stats.Statistics;

/**
 * Handles commands for a dictionary. Accepts raw string commands, parses them into operation/argument pairs, then dispatches on the operation
 * to a function that parses the arguments and does the appropriate operation. Status strings are passed around; exceptions are caught and a status string
 * beginning with "ERROR" is returned instead.
 *
 * @author Joe Desmond
 */
public final class CommandHandler {
	
	/**
	 * Expected format for a date argument
	 */
	private static final SimpleDateFormat DATE_ARG_FORMAT = new SimpleDateFormat("MM:dd:yyyy:HH:mm");
	
	/**
	 * Date format used when printing definitions
	 */
	private static final SimpleDateFormat DATE_OUTPUT_FORMAT = new SimpleDateFormat("MM/dd/YYYY hh:mm:ss a");
	
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
	 * Most recent statistics, or null if none have been generated
	 */
	private Statistics statistics;
	
	/**
	 * A flag that can be set by the user if they want to include custom date arguments in definitions. 
	 * If this is disabled, the current date is used.
	 */
	private boolean datesEnabled;
	
	/**
	 * Creates a CommandHandler. {@link #receive(String)} or {@link #receive(String, String)} must be called
	 * in order for this CommandHandler to function.
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
				return newDefinition(false, arg);
			case "strongdefine":
				return newDefinition(true, arg);
			case "enabledates":
				return setDatesEnabled(true);
			case "disabledates":
				return setDatesEnabled(false);
			case "printstats":
				return printStatistics(arg);
			case "statsdump":
				return saveStatisticsTo(arg);
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
	 * Saves the current statistics to a directory. Creates several files in the specified directory 
	 * including histograms and a printout of dictionary statistics.
	 * 
	 * @param path path of a directory
	 * @return status string
	 */
	private final String saveStatisticsTo(final String directory) {
		final String stats = printStatistics("new");
		
		if (stats.startsWith("ERROR")) {
			return stats;
		}
		
		final Histogram rawTimeDifferencesHist = new Histogram(statistics.timeDifferences);
		final Histogram timeDifferencesNoOutliersHist = new Histogram(statistics.timeDifferences.copyNoOutliers());
		try {
			rawTimeDifferencesHist.saveTo(directory + File.separator + "raw-time-differences.png", "png");
			timeDifferencesNoOutliersHist.saveTo(directory + File.separator + "time-differences-no-outliers.png", "png");
		} catch (Exception e) {
			return "ERROR: Problem saving time differences histograms!";
		}
		
		try (PrintWriter pw = new PrintWriter(new FileWriter(new File(directory + File.separator + "stats.txt")))) {
			pw.print(stats);
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR: Problem occurred while trying to write to text file!";
		}
		
		return "Saved statistics to \"" + directory + "\" folder";
	}
	
	/**
	 * Prints statistics for the dictionary.
	 * 
	 * @param versionArg either "new" or "current" to specify new statistics or previously generated statistics <br>
	 * 					 using "current" with no generated statistics will generate a new statistics
	 * @return String containing the statistics of the dictionary, or a status string
	 */
	private final String printStatistics(final String versionArg) {
		if (openDictionary == null) {
			return "ERROR: No dictionary is open!"; 
		}
		
		if (versionArg.equalsIgnoreCase("new")) {
			statistics = new Statistics(openDictionary);
			
			return statistics.toString();
		} else if (versionArg.equalsIgnoreCase("current")) {
			if (statistics == null) {
				statistics = new Statistics(openDictionary);
			}
			
			return statistics.toString();
		} else {
			return "ERROR: Invalid version argument!";
		}
	}
	
	/**
	 * Sets the flag that determines if date arguments will be accepted in strongdefine/weakdefine invocations.
	 * If date arguments are disabled, the current date will be used when entering a new definition.
	 * 
	 * @param enabled true if date arguments should be enabled
	 * @return status string
	 */
	private final String setDatesEnabled(final boolean enabled) {
		datesEnabled = enabled;
		return enabled ? "Enabled date arguments" : "Disabled date arguments";
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
			final String dateString = DATE_OUTPUT_FORMAT.format(definition.get().entryDate());
			return word + ":\t" + definition.get().definition() + System.lineSeparator() + System.lineSeparator()
				   + dateString;
		} else {
			return "No definition exists for \"" + word + "\"";
		}
	}
	
	/**
	 * Adds a definition to the dictionary.
	 * 
	 * @param strong true if the definition should be added regardless of whether or not it already exists
	 * @param defString definition string of the form "<code>"word" "datestring" definition</code>"
	 * @return status string
	 */
	private final String newDefinition(final boolean strong, final String defString) {
		if (openDictionary == null) {
			return "ERROR: No open dictionary!";
		}
		
		//Regular expression that matches with the word being defined, which should be surrounded by quotes.
		//Any matches after the first are ignored, unless date arguments are enabled: in which case a date string should be provided, also in quotes and after the word.
		final String regex = "(\")[^\"]+(\"\\s)";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(defString);
		
		String rawWord;
		String rawDateString = "";
		String dateString;
		
		if (matcher.find()) {
			rawWord = matcher.group(0);
		} else {
			return "ERROR: Malformed definition argument!";
		}
		
		final Date date;
		
		if (!datesEnabled) {
			date = new Date();
		} else if (matcher.find()) {
			rawDateString = matcher.group(0);
			dateString = rawDateString.replace('"', ' ').trim();
			try {
				date = DATE_ARG_FORMAT.parse(dateString);
			} catch (Exception e) {
				return "ERROR: Malformed date argument!";
			}
		} else {
			return "ERROR: Missing date argument!";
		}
		
		final String word = rawWord.replace('"', ' ').trim();
		final String definition;
		
		if (!datesEnabled) {
			definition = defString.substring(defString.indexOf(rawWord) + rawWord.length()).trim();
		} else {
			definition = defString.substring(defString.indexOf(rawDateString) + rawDateString.length()).trim();
		}
		
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
