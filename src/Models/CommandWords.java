package Models;

import java.io.Serializable;

/**
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author Jaydon Haghighi
 * @version 2022.10.25
 */
public class CommandWords implements Serializable
{
    // a constant array that holds all valid command words
    private static final String[] validCommands = {
            "play", "exchange", "pass", "shuffle", "forfeit"
    };
    
    /**
     * Check whether a given String is a valid command word.
     * @return true if it is, false if it isn't.
     */
    public boolean isCommand(String aString)
    {
        for(int i = 0; i < validCommands.length; i++) {
            if(validCommands[i].equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }

    /**
     * Print all valid commands to System.out.
     */
    public String showAll()
    {
        StringBuilder sb = new StringBuilder();
        for(String command: validCommands) {
            sb.append(command + ".  ");
        }
        return sb.toString();
    }
}

