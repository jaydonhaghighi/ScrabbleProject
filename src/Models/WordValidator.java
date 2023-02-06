package Models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class validates an arraylist of words formed by the players turn
 * based off the players word placement it will create either one or more words.
 * it will validate every word by checking if words.txt contains word.
 *
 * @author Jaydon Haghighi
 * @version 2022.10.25
 */
public class WordValidator implements Serializable {
    private final Map<Integer, String> wordsMap = new HashMap<>();
    private static final String FILE = "src/Resources/words.txt";

    /**
     * Initializes the scanner
     */
    public WordValidator() {
        try {
            scanner();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Scans the file containing all the valid words
     * @throws FileNotFoundException if file given is not found an exception will occur
     */
    public void scanner() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(FILE));
        int index = 0;
        while (scanner.hasNextLine()) {
            String word = scanner.nextLine();
            wordsMap.put(index, word);
            index++;
        }
    }

    /**
     * @param wordsToValidate Arraylist of words Models.Board has analyzed
     * @return returns true if all words given are valid and false if one or more words are not valid
     */
    public boolean isWordsValid(ArrayList<String> wordsToValidate) {
        //Reading the word to be found from the user
        int count = 0;
        for (String word : wordsToValidate) {
            if (wordsMap.containsValue(word)) {
                count++;
            }
        }
        if (count == wordsToValidate.size()) {
            return true;
        } else {
            return false;
        }
    }
}





