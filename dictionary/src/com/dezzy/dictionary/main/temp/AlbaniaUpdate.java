package com.dezzy.dictionary.main.temp;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.dezzy.dictionary.main.CommandHandler;
import com.dezzy.dictionary.main.Definition;
import com.dezzy.dictionary.main.Dictionary;

public class AlbaniaUpdate {
	
	public static void main(String[] args) throws ParseException, IOException {
		final SimpleDateFormat sdf = new SimpleDateFormat("MM:dd:yyyy:hh:mm");
		final Dictionary dict = new Dictionary("Ye Olde Royale Kingdom Dictionary");
		
		dict.temp_RAWDEFINE("adorability", 
				new Definition("a measure of how adorable someone/something is", sdf.parse("09:14:2019:22:09")));
		dict.temp_RAWDEFINE("adorbs", 
				new Definition("synonym for adorable", sdf.parse("12:22:2019:21:09")));
		dict.temp_RAWDEFINE("ayyy rigafigatoni", 
				new Definition("a greeting only to be used among Italians, or people who are making fun of Italians", sdf.parse("08:30:2019:17:46")));
		dict.temp_RAWDEFINE("bawss", 
				new Definition("really cool and awesome", sdf.parse("12:07:2019:17:09")));
		dict.temp_RAWDEFINE("Bingo Bango Presto", 
				new Definition("what you say if you're cool and you do something cool", sdf.parse("01:10:2020:20:31")));
		dict.temp_RAWDEFINE("cutemeter", 
				new Definition("a device for measuring cuteness", sdf.parse("09:25:2019:20:16")));
		dict.temp_RAWDEFINE("ewey", 
				new Definition("a synonym for \"ew\", but with more disgust", sdf.parse("12:04:2019:20:54")));
		dict.temp_RAWDEFINE("lulz", 
				new Definition("a better version of \"lol\", see \"The Law of Z Coolness\"", sdf.parse("09:04:2019:18:14")));
		dict.temp_RAWDEFINE("so much house", 
				new Definition("said only by sillies, indicates silliness", sdf.parse("09:02:2019:16:08")));
		dict.temp_RAWDEFINE("rigafigatoni", 
				new Definition("a fellow Italian", sdf.parse("08:30:2019:17:46")));
		dict.temp_RAWDEFINE("show ur boi", 
				new Definition("what you say when you are a boy and someone has something cool that you would like to see", sdf.parse("12:03:2019:15:27")));
		dict.temp_RAWDEFINE("spicey bread",
				new Definition("a synonym for \"pumpernickel\", see \"Pumpernickel Decree\"", sdf.parse("12:04:2019:12:46")));
		dict.temp_RAWDEFINE("Pumpernickel Decree", 
				new Definition("\"I hence forth write into law that no one may use the word pumpernickel on Wednesdays. They must use the term spicey bread in its place.\""
						+ " - Drafted and Signed by The Lawyer of the Land", sdf.parse("12:04:2019:12:46")));
		dict.temp_RAWDEFINE("Paper Plate Decree", 
				new Definition("\"I also here by dictate that every third thursday of each month everyone must throw away a clean paper plate.\""
						+ " - Drafted and Signed by The Lawyer of the Land", sdf.parse("12:04:2019:12:57")));
		dict.temp_RAWDEFINE("ski bunny", 
				new Definition("1. an adorable girl who skis, often says \"vroom vroom\" despite having no engine 2. Brianna (prime example of 1)", sdf.parse("01:08:2020:15:03")));
		dict.temp_RAWDEFINE("The Law of Z Coolness", 
				new Definition("a law, stated as follows: \"Adding more Z's to the end of a word increases its coolness exponentially.\" (not an official decree)", sdf.parse("12:15:2019:12:15")));
		dict.temp_RAWDEFINE("totes", 
				new Definition("interesting synonym for \"totally\"", sdf.parse("12:13:2019:16:44")));
		dict.temp_RAWDEFINE("ur boi", 
				new Definition("a synonym for \"me\"", sdf.parse("12:03:2019:15:27")));
		dict.temp_RAWDEFINE("vibeage", 
				new Definition("synonym for \"energy\", more specifically refers to different kinds of energies that can be radiated by memes", sdf.parse("12:09:2019:22:04")));
		dict.temp_RAWDEFINE("little noodle", 
				new Definition("1. a fun phrase to say 2. an adorable girl who is also short 3. Brianna (prime example of 2)", sdf.parse("01:10:2020:20:31")));
		dict.temp_RAWDEFINE("totes ma goats", 
				new Definition("a sillier synonym for \"totes\"", sdf.parse("01:10:2020:20:31")));
		dict.temp_RAWDEFINE("The Lawyer of the Land",
				new Definition("One of Brianna's official titles: the royal lawyer of the kingdom, can draft laws and sign (enact) them with or without input from the King."
						+ " - Recognized by the Kingdom", sdf.parse("08:28:2019:17:28")));
		dict.temp_RAWDEFINE("King Albanian Rock", 
				new Definition("One of Joe's official titles: the king of Albania, BC is his castle"
						+ " - Recognized by the Kingdom", sdf.parse("08:28:2019:17:22")));
		dict.temp_RAWDEFINE("Black Twizzler Decree", 
				new Definition("\"As long as I am king black twizzlers will be the national food and it will rain every other day. If it is not raining, it will be sunny. These words are now law.\""
						+ " - Drafted and Signed by The Lawyer of the Land and King Albanian Rock", sdf.parse("08:28:2019:17:24")));
		dict.temp_RAWDEFINE("Meerkat Decree 1", 
				new Definition("\"I now declare that all meerkats and mosquitoes must be killed on sight.\""
						+ " - Drafted and Signed by The Lawyer of the Land and King Albanian Rock", sdf.parse("08:28:2019:17:36")));
		dict.temp_RAWDEFINE("Meme Value Decree", 
				new Definition("\"From this moment on, the government will use a system in which goods and services are assigned a meme value instead of a monetary value. This value will be in units of \"yees\" (1 yee, 2 yees, 50.45 yees, etc.)\""
						+ " - Drafted and Signed by The Lawyer of the Land and King Albanian Rock", sdf.parse("12:26:2019:20:04")));
		dict.temp_RAWDEFINE("Catholic Republican Junkie", 
				new Definition("one of Brianna's official titles"
						+ " - Recognized by the Kingdom", sdf.parse("09:29:2019:12:59")));
		dict.temp_RAWDEFINE("Catholic Republican Junkie 2", 
				new Definition("one of Joe's official titles"
						+ " - Recognized by the Kingdom", sdf.parse("09:29:2019:13:01")));
		dict.temp_RAWDEFINE("Cuteness Decree", 
				new Definition("\"Let it be known that in the heart of King Albanian Rock, Brianna is the cutest, and this is an official decree.\""
						+ " - Drafted by King Albanian Rock, Signed by King Albanian Rock and The Lawyer of the Land", sdf.parse("09:29:2019:13:07")));
		dict.temp_RAWDEFINE("The Best Decree 1", 
				new Definition("\"From this day forward Ms. Lawyer, the Lawyer of the Land, will be known as The Best.\""
						+ " - Drafted and Signed by King Albanian Rock", sdf.parse("09:14:2019:19:03")));
		dict.temp_RAWDEFINE("The Best Decree 2", 
				new Definition("\"However from this day forward King Albanian Rock, the toughest yogi, will be known as the bestest most awsome person ever.\""
						+ " - Drafted and Signed by The Lawyer of the Land", sdf.parse("09:14:2019:19:05")));
		dict.temp_RAWDEFINE("The Best Decree 1, Amendment 1", 
				new Definition("\"From this day forward Ms. Lawyer, the Lawyer of the Land, will be known as the bestest, most awesome, coooooolest person ever.\""
						+ " - Drafted and Signed by King Albanian Rock", sdf.parse("09:14:2019:19:06")));
		dict.temp_RAWDEFINE("roasty toasty old sporty", 
				new Definition("a very silly phrase, expresses silliness while also calling someone \"old sporty\"", sdf.parse("08:30:2019:22:15")));
		dict.temp_RAWDEFINE("old sport", 
				new Definition("a silly and affectionate title from the Great Gatsby", sdf.parse("08:30:2019:21:47")));
		dict.temp_RAWDEFINE("old spiry", 
				new Definition("an originally unintentional misspelling of \"old sport\" that has come to have its own meaning as a synonym for \"old sport\", but with extra silliness and affection", sdf.parse("08:30:2019:22:05")));
		dict.temp_RAWDEFINE("Fuzzy Hat Lawyer", 
				new Definition("One of the Kingdom's official lawyers and a member of the Fuzzy Hat Lawyer Army; commanded by The Lawyer of the Land"
						+ " - Recognized by the Kingdom", sdf.parse("08:29:2019:22:17")));
		dict.temp_RAWDEFINE("Ms. Lawyer", 
				new Definition("One of the first of Brianna's official titles"
						+ " - Recognized by the Kingdom", sdf.parse("08:13:2019:23:38")));
		dict.temp_RAWDEFINE("Mr. Smarty", 
				new Definition("One of the first of Joe's official titles"
						+ " - Recognized by the Kingdom", sdf.parse("08:01:2019:22:38")));
		
		dict.save("albania/kingdom-dictionary.dict");
		final CommandHandler ch = new CommandHandler();
		System.out.println(ch.receive("open albania/kingdom-dictionary.dict"));
		System.out.println(ch.receive("print new"));
		System.out.println(ch.receive("printto albania/kingdom-dictionary.txt"));
		System.out.println(ch.receive("close"));
	}
	
}
