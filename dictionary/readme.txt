This program lets you create, edit, and mess around with text dictionaries.

How to use it:
1. You need Java
2. Run 'run.bat' 
3. Enter commands



Commands can have one or zero arguments. When typing these commands in, replace the argument name and square brackets with a value. These are the commands:

open [dictionary filename] - opens the dictionary at the specified path
create [dictionary name] - creates a dictionary with the given name and sets it as the open dictionary
save [file location] - saves the currently open dictionary to the specified file. If the file does not exist, it creates a new file, and if no file is specified, it saves it to the previously specified file (for example, if open was used before this, it will save the dictionary to the same path)
weakdefine ["word/phrase" definition] - adds a definition for the given word/phrase only if it does not exist already. The word/phrase must be in quotes, with a space between the last quote and the definition.
strongdefine ["word/phrase" definition] - same as weakdefine, except if the word is already defined, it will be updated
remove [word/phrase] - removes an entry from the dictionary. For this and subsequent commands, the word does not need to be in quotes.
find [word/phrase] - gives the definition for a specified word, if that word is defined.
print new - prints every entry in the dictionary to the screen
print current - prints the results of the last 'print new', or just simulated 'print new' if there was no previous 'print new'
printto [file location] - gets the results of 'print new' and saves them to a file
search [search expression] - see "How to use search"
close - closes the currently open dictionary
quit - closes the program, and closes the currently open dictionary if there is one



How to use search

The search command takes a search expression and searches every word + definition pair for matches.
The number of matches for a dictionary entry is determined by the number of matches, and results are sorted by relevancy first and alphabetical order second.
The search expression is a Java regular expression, which means that it can be a basic string of characters or a more complicated pattern.
However, because it is a regular expression, some sequences of characters may not be interpreted literally. You only need to worry about this if your search term contains something other than letters and digits.
Here is a reference explaining how to use regular expressions: https://www.tutorialspoint.com/java/java_regular_expressions.htm



Example command sequence:

open test/test2.dict
find hot dogs
strongdefine "brick" usually red and used for building
save
print new
printto test/test2.txt
search cold
close
quit