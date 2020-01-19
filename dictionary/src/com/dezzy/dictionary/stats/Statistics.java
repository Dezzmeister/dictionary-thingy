package com.dezzy.dictionary.stats;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import com.dezzy.dictionary.main.Dictionary;

/**
 * Creates and stores various statistics for a {@link Dictionary}.
 * 
 * @author Joe Desmond
 */
public final class Statistics {
	
	/**
	 * Date format used in {@link #toString()}
	 */
	public static final SimpleDateFormat DATE_OUTPUT_FORMAT = new SimpleDateFormat("MM/dd/YYYY hh:mm:ss a");
	
	/**
	 * The dictionary
	 */
	private final Dictionary dictionary;
	
	/**
	 * Distribution of time differences between consecutive definition entry dates (in minutes)
	 */
	public final Distribution timeDifferences;
	
	/**
	 * Number of entries in the dictionary
	 */
	public final int numEntries;
	
	/**
	 * Generates statistics for a dictionary. These statistics are meant to provide a snapshot of the current dictionary, and are immutable.
	 * 
	 * @param _dictionary the dictionary
	 */
	public Statistics(final Dictionary _dictionary) {
		dictionary = _dictionary;
		numEntries = dictionary.size();
		timeDifferences = timeDifferencesDistribution(dictionary);
	}
	
	/**
	 * Calculates the time differences between subsequent definitions (in minutes) and returns a distribution containing these differences.
	 * 
	 * @param dictionary dictionary
	 * @return distribution of time differences
	 */
	private final Distribution timeDifferencesDistribution(final Dictionary dictionary) {
		final List<String> dateSortedWords = dictionary.getEntryDateSortedWords();
		
		final float[] data = new float[dateSortedWords.size() - 1];
		
		for (int i = 0; i < data.length; i++) {
			final Date first = dictionary.getDefinition(dateSortedWords.get(i)).get().entryDate();
			final Date second = dictionary.getDefinition(dateSortedWords.get(i + 1)).get().entryDate();
			
			final LocalDateTime firstLDT = convertDate(first);
			final LocalDateTime secondLDT = convertDate(second);
			
			long diff = ChronoUnit.MINUTES.between(firstLDT, secondLDT);
			data[i] = diff;
		}
		
		return new Distribution("====== Time Differences Distribution ======", 
				"Distribution of entry times between consecutive definitions. Time is measured in minutes", data);
	}
	
	/**
	 * Converts a {@link Date} to a {@link LocalDateTime}, so that time differences can be calculated with 
	 * {@link ChronoUnit#between(java.time.temporal.Temporal, java.time.temporal.Temporal) ChronoUnit.between()}.
	 * 
	 * @param date date to be converted
	 * @return converted date
	 */
	private static final LocalDateTime convertDate(final Date date) {
		return date.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}
	
	/**
	 * Returns a multi-line string with raw statistics.
	 * 
	 * @return statistics
	 * @see Distribution#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("============ STATISTICS ============" + System.lineSeparator());
		
		final String dateString = DATE_OUTPUT_FORMAT.format(new Date());
		sb.append("As of " + dateString + ":" + System.lineSeparator());
		sb.append("There are " + numEntries + " definitions in " + dictionary.name);
		
		sb.append(System.lineSeparator() + System.lineSeparator());
		sb.append(timeDifferences.toString());
		
		return sb.toString();
	}
}
