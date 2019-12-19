package com.dezzy.dictionary.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Main {
	
	
	public static final void main(final String[] args) throws IOException {
		final CommandHandler commandHandler = new CommandHandler();
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while (true) {
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
