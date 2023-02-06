package Models; /**
 * Primary model of the game. Contains logic relating to the board and player hand management. This class also
 * initializes the Models.Bag that will be used to communicate with the player's hand. The players are also initialized
 * within this class. Since it's a model this class also manages updating the views.
 * @Author Akshay Vashisht
 * @Author Mohamed Kaddour
 * @Author Jaydon Haghighi
 * @Author Mahtab Ameli
 * @Date 2022-11-13
 * @Version 2.0
 */

import Views.HandPanel;
import Views.ScrabbleView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable{
    private static final long serialVersionUID = 3156533007620095055L;
    private Bag bag = new Bag();
    private Board board = new Board();
    private ArrayList<Player> playerList = new ArrayList<>();
    private Player currentPlayer;
    private boolean placementCheck;
    private List<ScrabbleView> views;
    private int activeCount;
    private ArrayList<Character> removeTilesFromHand;
    private ArrayList<Character> exchangeTilesFromHand;
    private boolean firstPlayInTurn;
    private String startingCoordinates;
    private boolean gameFinished;
    private boolean initialReadUndo;
    private boolean initialReadRedo;

    /**
     * Public constructor for class game.
     */
    public Game() {
        clearUndoRedoFileContents();
        this.initialReadUndo = true;
        this.initialReadRedo = true;

        this.views = new ArrayList<>();
        this.removeTilesFromHand= new ArrayList<>();
        this.exchangeTilesFromHand = new ArrayList<>();
        this.firstPlayInTurn = true;
        this.gameFinished = false;
    }

    private void clearUndoRedoFileContents()
    {
        try {
            PrintWriter writer = new PrintWriter(GameState.FILENAME_UNDO);
            writer.print("");
            writer.close();
            PrintWriter writer2 = new PrintWriter(GameState.FILENAME_REDO);
            writer.print("");
            writer2.close();
        } catch (FileNotFoundException e)
        {
            System.out.println("ignore");
        }
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<ScrabbleView> getViews() {
        return views;
    }

    public void setViews(List<ScrabbleView> views) {
        this.views = views;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    /**
     * Creates number of players based on user input
     * @param playerAmount User input for number of players
     */
    public void createPlayers(String playerAmount, String AIAmount) {
        int playerValue = Integer.parseInt(playerAmount);
        int AIValue = Integer.parseInt(AIAmount);
        playerList.clear();
        for (int i = 0; i < playerValue; i++) {
            playerList.add(new Player(i));
            playerList.get(i).initializePlayerHand((ArrayList<Tile>) bag.removeTiles(7));
        }
        for (int i = playerValue; i < AIValue + playerValue; i++) {
            playerList.add(new AIPlayer(i, this));
            playerList.get(i).initializePlayerHand((ArrayList<Tile>) bag.removeTiles(7));
        }

        this.activeCount = this.playerList.size();
        this.currentPlayer = this.playerList.get(0);
        saveCurrentGameState();
        for(ScrabbleView v : this.views){v.update(new ScrabbleEvent(this.currentPlayer, this.board, this.gameFinished));}
    }

    public void updateBoardPattern(Board.Pattern boardPattern) {
        board.updateBoardPattern(boardPattern);
    }

    /**
     * Adds Views.ScrabbleView object to list of views
     *
     * @param sv
     */
    public void addScrabbleView(ScrabbleView sv)
    {
        this.views.add(sv);
    }

    /**
     * Logic for changing the turn order from current player to next player
     */
    public void nextPlayer() throws IOException, ClassNotFoundException {
        this.removeTilesFromHand.clear();
        if (currentPlayer != null) {
            if (currentPlayer.getPoints() >= 120) {
                this.gameFinished = true;
            } else {
                if (this.currentPlayer.getPlayerNumber() == (this.playerList.size() - 1)) {
                    this.currentPlayer = this.playerList.get(0);
                    saveCurrentGameState();
                } else {
                    this.currentPlayer = this.playerList.get((this.currentPlayer.getPlayerNumber() + 1));

                    if (this.currentPlayer.isAI()) {
                        performAIPlay();
                    }
                    else
                    {
                        saveCurrentGameState();
                    }

                    this.initialReadUndo = false;
                }
            }
        }


        for(ScrabbleView v : this.views){v.update(new ScrabbleEvent(this.currentPlayer, this.board, this.gameFinished));}
        this.firstPlayInTurn = true;
    }

    /**
     * Getter method for bag
     * @return Models.Bag
     * */
    public Bag getBag()
    {
        return this.bag;
    }

    /**
     * Performs an AIPlay with Models.AIPlayer methods and then skips the turn. Also responsible for clearing out
     * blanks
     * */
    private void performAIPlay() throws IOException, ClassNotFoundException {
        AIPlayer aiPlayer = (AIPlayer) this.currentPlayer;
        System.out.println(this.currentPlayer.getHand().getHand().toString() + "INITIAL HAND");

        boolean flag = true;
        while (flag) {
            flag = false;

            ArrayList<Tile> mockHand = (ArrayList<Tile>) aiPlayer.getHand().getHand().clone();

            System.out.println(mockHand.toString());

            int i = 0;

            for (Tile t : mockHand)
            {
                i++;
                if (t.getLetter().equals("_"))
                {
                    System.out.println("BLANK DETECTED AT " + i);
                    flag = true;
                    this.addToExchangeTilesFromHand('_');
                    this.processCommand(new Command("exchange", "_", null));
                    this.exchangeTilesFromHand.clear();
                }
            }
        }

        aiPlayer.analyzeBoard(this.board);
        aiPlayer.playHighestMove(this);
        this.clearRemoveTilesFromHand();

        this.nextPlayer();
    }

    /**
     * Returns current player
     *
     * @return currentPlayer
     */
    public Player getCurrentPlayer()
    {
        return this.currentPlayer;
    }

    /**
     * Clears the array list RemoveTilesFromHand
     */
    public void clearRemoveTilesFromHand()
    {
        removeTilesFromHand.clear();
    }

    /**
     * Adds a character to RemoveTilesFromHand array list
     *
     * @param c
     */
    public void addToRemoveTilesFromHand(Character c, boolean blankTile)
    {
        this.removeTilesFromHand.add(c);
        this.firstPlayInTurn = false;

        if (blankTile)
        {
            ArrayList<Tile> mockHand = (ArrayList<Tile>) currentPlayer.getHand().getHand().clone();

            for (int i = 0; i < mockHand.size(); i++)
            {
                if (mockHand.get(i).getLetter().equals("_"))
                {
                    currentPlayer.getHand().getHand().get(i).setLetter(c.toString());
                    break;
                }
            }
        }
    }

    /**
     * Adds a character to ExchangeTilesFromHand array list
     *
     * @param c
     */
    public void addToExchangeTilesFromHand(Character c)
    {
        this.exchangeTilesFromHand.add(c);
    }

    /**
     * Removes a character from ExchangeTilesFromHand array list
     *
     * @param c
     */
    public void removeFromExchangeTilesFromHand(Character c)
    {
        this.exchangeTilesFromHand.remove(c);
    }

    /**
     * Clears the RemoveFromExchangeTilesFromHand array list
     */
    public void clearRemoveFromExchangeTilesFromHand()
    {
        this.exchangeTilesFromHand.clear();
    }

    /**
     * Returns ExchangeTilesFromHand array list
     *
     * @return exchangeTilesFromHand
     */
    public ArrayList<Character> getExchangeTilesFromHand()
    {
        return this.exchangeTilesFromHand;
    }

    /**
     * Returns FirstPlayInTurn
     *
     * @return firstPlayInTurn
     */
    public boolean getFirstPlayInTurn()
    {
        return this.firstPlayInTurn;
    }

    /**
     * Initializes the starting coordinates for the game
     *
     * @param startingCoordinates
     */
    public void setStartingCoordinates(String startingCoordinates)
    {
        this.startingCoordinates = startingCoordinates;
    }

    /**
     * Converts starting coordinate code from Horizontal direction to vertical direction
     */
    public void changeStartingCoordinatesToVertical()
    {
        String num = " ";
        char letter = ' ';
        if (this.startingCoordinates.length() == 3) {
            num = startingCoordinates.substring(0, 2);
            letter = this.startingCoordinates.charAt(2);
        }
        else
        {
            num =startingCoordinates.substring(0, 1);
            letter = this.startingCoordinates.charAt(1);

        }
        StringBuilder sb = new StringBuilder();

        sb.append(letter);
        sb.append(num);

        this.startingCoordinates = sb.toString();
    }

    /**
     * Returns the starting coordinate for the game
     *
     * @return startingCoordinate
     */
    public String getStartingCoordinates()
    {
        return this.startingCoordinates;
    }

    /**
     * Converts the characters in an array to a single string
     *
     * @param ar
     * @return arraylist String
     */
    public static String convertCharArrayListToString(ArrayList<Character> ar)
    {
        StringBuilder sb = new StringBuilder();

        for (Character c : ar)
        {
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * Takes in a command from a given player and performs an action based on the command
     * @param command Contains the different parts of the command the user entered
     * @return returns true of false (Should be void)
     */
    public boolean processCommand(Command command) throws FileNotFoundException {
        List<Tile> addTilesToHand;
        InHand inHand;
        placementCheck = true;
        boolean rc = true;

        String action = command.getAction();

        switch (action) {
            case "exchange":
                inHand = new InHand(convertCharArrayListToString(this.exchangeTilesFromHand), currentPlayer.getHand());
                if (inHand.wordInHand()) {
                    this.exchangeTilesFromHand = inHand.wordToList();
                    addTilesToHand = this.bag.removeTiles(this.exchangeTilesFromHand.size());
                    bag.placeTiles(currentPlayer.exchange((ArrayList<Tile>) addTilesToHand,
                            this.exchangeTilesFromHand));
                }
                else {
                    System.out.println("All tiles not in hand");
                    rc = false;
                }

                for(ScrabbleView v : this.views){
                    v.update(new ScrabbleEvent(this.currentPlayer, this.board, this.gameFinished));
                }
                break;

            case "play":
                inHand = new InHand(wordTypeToPassIn(command.getWordAttempt()), currentPlayer.getHand());
                if (inHand.wordInHand()) {
                    removeTilesFromHand = inHand.wordToList();
                    addTilesToHand = this.bag.removeTiles(removeTilesFromHand.size());
                    System.out.println(removeTilesFromHand.toString() + " " + command.getPlacementAttempt());
                    PlayMove playMove = new PlayMove(command.getPlacementAttempt(),
                            currentPlayer.exchange((ArrayList<Tile>) addTilesToHand, removeTilesFromHand),
                            this.board, command.getPlacementDirection());
                    if (playMove.placeTile()) {
                        if (playMove.checkWord()) {
                            this.board = playMove.getUpdatedBoard();
                            currentPlayer.addPoints(playMove.getPlayedWordScore());
                        }
                        else {
                            System.out.println("Word is not valid.");
                            this.bag.placeTiles(addTilesToHand);
                            currentPlayer.rollBack();
                            rc = false;
                        }

                    }
                    else {
                        this.bag.placeTiles(addTilesToHand);
                        currentPlayer.rollBack();
                        placementCheck = false;
                        rc = false;
                    }
                }
                else
                {
                    System.out.println("All tiles not in hand");
                    rc = false;
                }

                for(ScrabbleView v : this.views) {
                    v.update(new ScrabbleEvent(this.currentPlayer, this.board, this.gameFinished));
                }
                break;

            case "shuffle":
                currentPlayer.shuffle();
                for(ScrabbleView v : this.views) {
                    v.update(new ScrabbleEvent(this.currentPlayer, this.board, this.gameFinished));
                }
                break;

            case "pass":
                break;

            case "forfeit":
                System.out.println("Models.Player " + (currentPlayer.getPlayerNumber()+1) + " has forfeited");
                bag.placeTiles(currentPlayer.getHand().getHand());
                currentPlayer.setActive(false);
                break;

            default:
                break;
        }

        return rc;
    }

    /**
     * Models.Board getter method
     * @return returns instance of board in game
     */
    public Board getBoard() {
        return board;
    }

    public void refreshHandPanelView(Tile tile, boolean tileIsBlank) {
        for(ScrabbleView v : this.views) {
            if (v instanceof HandPanel) {
                ((HandPanel) v).removeTile(tile, tileIsBlank);
            }
        }
    }

    /**
     * Creates new Models.GameState type object using this game
     */
    public void saveCurrentGameState() {
        GameState gameState = new GameState(this, true);
    }

    /**
     * Serializes the game and saves all object data into the save.txt file
     */
    public void saveGame() {
        FileOutputStream outputStream;
        ObjectOutputStream oos;

        try {
            outputStream = new FileOutputStream(GameState.FILENAME_SAVE);
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(this);
            oos.close();
            outputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserializes the game and loads all object data from save.txt file and updates the view accordingly
     */
    public void loadGame() {
        FileInputStream inputStream;
        ObjectInputStream ois;
        Game gameToBeLoaded;

        try {
            inputStream = new FileInputStream(GameState.FILENAME_SAVE);
            ois = new ObjectInputStream(inputStream);

            gameToBeLoaded = (Game) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        changeCurrentGameState(gameToBeLoaded);
    }

    /**
     * Reverts the most recent player action.
     *
     * @return returns true or false based on whether an undo was possible based on state history
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean undoGame() throws IOException, ClassNotFoundException {
        ArrayList<GameState> stateHistory = new ArrayList<>();
        FileInputStream inputStream;
        ObjectInputStream ois;
        if (this.initialReadUndo == false) {
            try {
                inputStream = new FileInputStream(GameState.FILENAME_UNDO);
                ois = new ObjectInputStream(inputStream);
                try {
                    stateHistory = (ArrayList<GameState>) ois.readObject();
                } catch (ClassNotFoundException c)
                {
                    System.out.print("c");
                }
            } catch (IOException e)
            {
                System.out.println(e.getMessage());
            }

            GameState gameState = new GameState(stateHistory.remove(stateHistory.size() - 1).getGame(), false);
            this.initialReadRedo = false;
            popUndoStateStack(stateHistory);

            if (stateHistory.size() == 1)
            {
                this.initialReadUndo = true;
            }

            changeCurrentGameState(stateHistory.get(stateHistory.size() - 1).getGame());

            if (this.currentPlayer.isAI()) {
                performAIPlay();
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Redoes an undone player action. (Goes forward one action)
     *
     * @return returns true or false based on whether a redo was possible based on state history
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean redoGame() throws IOException, ClassNotFoundException {
        ArrayList<GameState> stateHistory = new ArrayList<>();
        FileInputStream inputStream;
        ObjectInputStream ois;
        if (this.initialReadRedo == false) {
            try {
                inputStream = new FileInputStream(GameState.FILENAME_REDO);
                ois = new ObjectInputStream(inputStream);
                try {
                    stateHistory = (ArrayList<GameState>) ois.readObject();
                } catch (ClassNotFoundException c)
                {
                    System.out.print("c");
                }
            } catch (IOException e)
            {
                System.out.println(e.getMessage());
            }

            changeCurrentGameState(stateHistory.get(stateHistory.size() - 1).getGame());
            GameState gameState = new GameState(stateHistory.remove(stateHistory.size() - 1).getGame(), true);
            popRedoStateStack(stateHistory);
            this.initialReadUndo = false;

            if (stateHistory.size() == 0)
            {
                this.initialReadRedo = true;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Sets the games attributes to the game instance and updates all views.
     *
     * @param game Instance of a game
     */
    private void changeCurrentGameState(Game game)
    {
        this.setBag(game.getBag());
        this.setCurrentPlayer(game.getCurrentPlayer());
        this.setBoard(game.getBoard());
        this.setPlayerList(game.getPlayerList());
        this.setGameFinished(game.isGameFinished());

        for(ScrabbleView v : this.views) {
            v.update(new ScrabbleEvent(this.currentPlayer, this.board, this.gameFinished));
        }
    }

    /**
     * Removes the most recent Models.GameState in the UndoState history and then updates its corresponding file.
     *
     * @param gameStates A list of game states
     */
    private void popUndoStateStack(ArrayList<GameState> gameStates)
    {
        FileOutputStream outputStream = null;
        try {
            PrintWriter writer = new PrintWriter(GameState.FILENAME_UNDO);
            writer.print("");
        } catch (FileNotFoundException f)
        {
            System.out.print("f");
        }
        ObjectOutputStream oos = null;
        try {
            outputStream = new FileOutputStream(GameState.FILENAME_UNDO);
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(gameStates);
        }
        catch (IOException e)
        {
            System.out.print("e2");
        }
    }

    /**
     * Removes the most recent Models.GameState in the RedoState history and then updates its corresponding file.
     *
     * @param gameStates A list of game states
     */
    private void popRedoStateStack(ArrayList<GameState> gameStates)
    {
        FileOutputStream outputStream = null;
        try {
            PrintWriter writer = new PrintWriter(GameState.FILENAME_REDO);
            writer.print("");
        } catch (FileNotFoundException f)
        {
            System.out.print("f");
        }
        ObjectOutputStream oos = null;
        try {
            outputStream = new FileOutputStream(GameState.FILENAME_REDO);
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(gameStates);
        }
        catch (IOException e)
        {
            System.out.print("e2");
        }
    }

    /**
     * Returns the word attempt by the AI
     *
     * @param wordAttempt
     * @return The word attempted by the AI
     */
    private String wordTypeToPassIn(String wordAttempt)
    {
        if (this.currentPlayer.isAI())
        {
            return wordAttempt;
        }
        else
        {
            return convertCharArrayListToString(this.removeTilesFromHand);
        }
    }
}
