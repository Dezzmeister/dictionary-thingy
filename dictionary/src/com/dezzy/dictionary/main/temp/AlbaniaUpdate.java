package com.dezzy.dictionary.main.temp;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.dezzy.dictionary.main.Definition;
import com.dezzy.dictionary.main.Dictionary;

public class AlbaniaUpdate {
	
	public static void main(String[] args) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat("MM:dd:yyyy:hh:mm");
		final Dictionary dict = new Dictionary("Ye Olde Royale Kingdom Dictionary");
		dict.temp_RAWDEFINE("Bingo Bango Presto", 
				new Definition("what you say if you're cool and you do something cool", sdf.parse("01:11:2020:15:52")));
		
	}
	
}
