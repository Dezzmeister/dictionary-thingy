package com.dezzy.dictionary.main;

import java.io.Serializable;
import java.util.Date;

/**
 * A dictionary definition, with metadata.
 *
 * @author Joe Desmond
 */
public final class Definition implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2041991497658680058L;

	/**
	 * The actual definition
	 */
	private final String definition;
	
	/**
	 * The date that the definition was added to the dictionary
	 */
	private Date entryDate;
	
	/**
	 * The number of times the definition was retrieved
	 */
	private int accesses = 0;
	
	/**
	 * Creates a new definition with zero accesses.
	 * 
	 * @param _definition the definition
	 * @param _entryDate date of creation
	 */
	public Definition(final String _definition, final Date _entryDate) {
		definition = _definition;
		entryDate = _entryDate;
	}
	
	/**
	 * The actual definition. Increases the access count before returning the definition.
	 * 
	 * @return the definition
	 */
	public final String definition() {
		accesses++;
		return definition;
	}
	
	/**
	 * The date that the definition was created.
	 * 
	 * @return entry date
	 */
	public final Date entryDate() {
		return entryDate;
	}
	
	/**
	 * Changes the entry date for this definition, and returns the old entry date. <br>
	 * This method is package-private; it should only be used through {@link CommandHandler}. <br>
	 * <b>This should only be used when transcribing old definitions!</b>
	 * 
	 * @param newDate new entry date
	 * @return old entry date
	 */
	final Date changeEntryDate(final Date newDate) {
		final Date oldDate = entryDate;
		entryDate = newDate;
		return oldDate;
	}
	
	/**
	 * The number of times the definition was accessed.
	 * 
	 * @return number of definition accesses
	 */
	public final int accesses() {
		return accesses;
	}
	
	/**
	 * Returns the stored definition and increases the access count; identical to calling {@link #definition()}.
	 * 
	 * @return the definition
	 */
	@Override
	public String toString() {
		return definition();
	}
}
