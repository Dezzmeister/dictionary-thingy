package com.dezzy.dictionary.cmdstructure;

import com.dezzy.dictionary.main.Dictionary;
import com.dezzy.dictionary.stats.Statistics;

/**
 * A currently open session. Contains any state variables for the session; intended to be modified directly by {@link Command#execute(Prompt, Session, String)}.
 *
 * @author Joe Desmond
 */
public final class Session {
	
	/**
	 * The current dictionary, or null if there is no open dictionary
	 */
	public Dictionary openDictionary;
	
	/**
	 * Most recent path specified for a dictionary, or null if no path has been specified
	 */
	public String dictionaryPath;
	
	/**
	 * Latest dictionary printout, or null if the dictionary has not been printed out
	 */
	public String dictionaryText;
	
	/**
	 * Most recent statistics, or null if none have been generated
	 */
	public Statistics statistics;
	
	/**
	 * A flag that can be set by the user if they want to include custom date arguments in definitions. 
	 * If this is disabled, the current date is used.
	 */
	public boolean datesEnabled;
	
	/**
	 * Creates a new Session.
	 */
	public Session() {
		openDictionary = null;
		dictionaryPath = null;
		dictionaryText = null;
		statistics = null;
		datesEnabled = false;
	}
}
