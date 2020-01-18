package com.dezzy.dictionary.stats;

import java.util.Map;

import com.dezzy.dictionary.main.Definition;
import com.dezzy.dictionary.main.Dictionary;

public final class Statistics {
	private final Dictionary dictionary;
	private final Map<String, Definition> definitions;
	
	public final int numEntries;
	
	public Statistics(final Dictionary _dictionary) {
		dictionary = _dictionary;
		definitions = dictionary.shallowCopyDefinitions();
		numEntries = definitions.size();
	}
}
