package com.dezzy.dictionary.main;


public final class CommandHandler {
	private Dictionary openDictionary;
	private String dictionaryPath;
	
	public final String receive(final String commandString) {
		String command = commandString;
		String arg = "";
		
		if (commandString.contains(" ")) {
			command = commandString.substring(0, commandString.indexOf(" "));
			arg = commandString.substring(commandString.indexOf(" ") + 1);
		}
		
		receive(command, arg);
	}
	
	public final String receive(final String command, final String arg) {
		switch (command) {
			case "open":
				return openDictionary(arg);
			case "create":
				return createDictionary(arg);
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
