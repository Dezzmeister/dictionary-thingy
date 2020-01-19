package com.dezzy.dictionary.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Main class, serves only to hold the {@link Main#main(String[]) main()} function
 *
 * @author Joe Desmond
 */
public final class Main {
	
	/**
	 * This class should never be instantiated
	 */
	private Main() {
		
	}	
	
	/**
	 * Starts a {@link CommandHandler} and begins reading input from the standard input. 
	 * This function receives commands line-by-line and dispatches them to the CommandHandler.
	 * Once in the main loop, the function will not terminate unless {@link BufferedReader#readLine()} 
	 * throws an exception or the <code>quit</code> command is received. <p>
	 * 
	 * Note: <code>quit</code> is never dispatched to the CommandHandler; it is handled in this function.
	 * 
	 * @param args no arguments are expected
	 * @throws IOException if there is a problem with the {@link BufferedReader}
	 */
	public static final void main(final String[] args) throws IOException {		
		final CommandHandler commandHandler = new CommandHandler();		
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while (true) {
			System.out.println();
			System.out.println("Enter a command: ");
			final String input = br.readLine();
			
			if (input.equalsIgnoreCase("quit")) {
				System.out.println("Quitting...");
				br.close();
				System.exit(0);
			}
			
			final String output = commandHandler.receive(input);
			System.out.println(output);
		}
	}
}
