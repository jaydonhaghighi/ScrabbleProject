package Models;

import Models.Command;
import Models.CommandWords;

import java.io.Serializable;
import java.util.Scanner;

/**
 * This parser reads user input.
 * Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two or three word command depending on the command. It returns the command
 * as an object of class Models.Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 *
 * @author Jaydon Haghighi
 * @version 2022.10.25
 */
public class Parser implements Serializable {
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     * @return The next command from the user.
     */
    public Command getCommand() {
        String inputLine;   // will hold the full input line
        String word1 = null;
        String word2 = null;
        String word3 = null;

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();

        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        if (tokenizer.hasNext()) {
            word1 = tokenizer.next();           // get first word
            if (tokenizer.hasNext()) {
                word2 = tokenizer.next();       // get second word
                if (tokenizer.hasNext()) {
                    word3 = tokenizer.next();   // get third word
                    // note: all other inputs after word3 are disregarded
                }
            }
        }

        // Now check whether this word is known. If so, create a command.
        // if command does not exist null is appointed to word1 which is handled in Models.Command class.
        if (commands.isCommand(word1)) {
            if (word1.equals("play")) {
                return new Command(word1, word2, word3);
            } else if (word1.equals("exchange")) {
                return new Command(word1, word2, word3);
            } else if (word1.equals("pass") || word1.equals("shuffle") || word1.equals("forfeit")) {
                return new Command(word1, word2, word3);
            }
        } else {
            System.out.println("Models.Command does not exist please re-enter new command\nList of valid commands: "
                    + showCommands());
        }
        return new Command(null, word2, word3);
    }



    /**
     * Print out a list of valid command words.
     */
    public String showCommands() {
        return commands.showAll();
    }
}


