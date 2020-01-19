package com.dezzy.dictionary.stats;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dezzy.dictionary.main.Definition;
import com.dezzy.dictionary.main.Dictionary;

/**
 * Creates and stores various statistics for a {@link Dictionary}.
 * 
 * @author Joe Desmond
 */
public final class Statistics {
	
	/**
	 * The dictionary
	 */
	private final Dictionary dictionary;
	
	/**
	 * Definitions in the dictionary
	 */
	private final Map<String, Definition> definitions;
	
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
		definitions = dictionary.shallowCopyDefinitions();
		numEntries = definitions.size();
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
		
		return new Distribution(data);
	}
	
	private static final LocalDateTime convertDate(final Date date) {
		return date.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}
}
