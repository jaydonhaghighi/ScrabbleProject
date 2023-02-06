package Views; /**
 * This class is part of the "Scrabble" application.
 *
 * This panel initializes the game by creating a Selector Panel with the choice for the number of players needed to
 * start the game.
 *
 * @author Jaydon Haghighi
 * @date 2022.11.13
 */

import Models.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

public class SelectionPanel extends JPanel implements ItemListener {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 25;
    private Board board;
    private JComboBox playerComboBox = new JComboBox();
    private JComboBox AIComboBox = new JComboBox();
    private JComboBox boardComboBox = new JComboBox();
    private JComboBox oldPlayerComboBox = new JComboBox();
    private JComboBox oldAIComboBox = new JComboBox();
    private int playerAmount;
    private int AILimit;
    private int AIAmount;

    private Image backgroundImage;

    private StartMenuFrame startMenuFrame;
    private JLabel playerSelectorLabel = new JLabel("Please Select Number of players", SwingConstants.CENTER);
    private JLabel AISelectorLabel = new JLabel("Please Select Number of AIs", SwingConstants.CENTER);
    private JLabel boardSelectorLabel = new JLabel("Please Select Style of Models.Board", SwingConstants.CENTER);

    /**
     * Constructor for PlayerSelectorPanel
     *
     * @param startMenuFrame
     */
    public SelectionPanel(StartMenuFrame startMenuFrame) {
        super(new GridLayout(3, 0));
        this.startMenuFrame = startMenuFrame;
        initializePlayerSelector();
    }

    /**
     * Initializes the player selector combo box and panel. For display.
     */
    private void initializePlayerSelector() {
        try {
            backgroundImage = ImageIO.read(new File("src/Resources/start_menu_background.jpg"));
        } catch (IOException e) {
            playerSelectorLabel.setForeground(Color.black);
            AISelectorLabel.setForeground(Color.black);
            boardSelectorLabel.setForeground(Color.black);
        }

        playerComboBox.setName("playerComboBox");
        JPanel playerComboBoxPanel = new JPanel(new GridLayout(2, 0));
        playerComboBoxPanel.setBackground(new Color(0, 0, 0, 0));
        playerSelectorLabel.setForeground(Color.white);
        playerComboBox.addItem("");
        for (int i = 2; i < 5; i++) {
            playerComboBox.addItem(i + " players");
        }
        playerComboBox.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        playerComboBoxPanel.add(playerSelectorLabel);
        playerComboBoxPanel.add(playerComboBox);
        playerComboBox.addItemListener(this);
        this.add(playerComboBoxPanel);
    }

    /**
     * Initializes the specific AI selector combo box and panel
     *
     * @param AILimit
     */
    private void initializeAISelector(int AILimit) {
        AIComboBox.setName("AIComboBox");
        JPanel AIComboBoxPanel = new JPanel(new GridLayout(2, 0));
        AIComboBoxPanel.setBackground(new Color(0, 0, 0, 0));
        AISelectorLabel.setForeground(Color.white);
        AIComboBox.addItem("");
        for (int i = 0; i < AILimit; i++) {
            AIComboBox.addItem(i + " AI players");
        }
        AIComboBox.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        AIComboBoxPanel.add(AISelectorLabel);
        AIComboBoxPanel.add(AIComboBox);
        AIComboBox.addItemListener(this);
        this.add(AIComboBoxPanel);
    }

    private void initializeBoardLayoutSelector() {
        boardComboBox.setName("boardSelectComboBox");
        JPanel boardComboBoxPanel = new JPanel(new GridLayout(2, 0));
        boardComboBoxPanel.setBackground(new Color(0, 0, 0, 0));
        boardSelectorLabel.setForeground(Color.white);
        boardComboBox.addItem("");
        boardComboBox.addItem("Standard");
        boardComboBox.addItem("Tetris");
        boardComboBox.addItem("Diamond");
        boardComboBox.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        boardComboBoxPanel.add(boardSelectorLabel);
        boardComboBoxPanel.add(boardComboBox);
        boardComboBox.addItemListener(this);
        this.add(boardComboBoxPanel);
    }

    /**
     * Helper method to pain component g
     *
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image.
        g.drawImage(backgroundImage, 0, 0, this);
    }


    /**
     * Changes the state of the AI combo box if the state of the player select has changed
     *
     * @param e ItemEvent
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            playerComboBox.setName(String.valueOf(playerComboBox.getSelectedItem()));
            if (e.getSource().equals(playerComboBox)) {
                if (playerComboBox.getSelectedItem().equals(2 + " players")) {
                    playerAmount = 2;
                    AILimit = 2;
                    oldPlayerComboBox.setName(String.valueOf(playerComboBox.getSelectedItem()));
                } else if (playerComboBox.getSelectedItem().equals(3 + " players")) {
                    playerAmount = 3;
                    AILimit = 3;
                    oldPlayerComboBox.setName(String.valueOf(playerComboBox.getSelectedItem()));
                } else if (playerComboBox.getSelectedItem().equals(4 + " players")) {
                    playerAmount = 4;
                    AILimit = 4;
                    oldPlayerComboBox.setName(String.valueOf(playerComboBox.getSelectedItem()));
                }
                playerComboBox.setEnabled(false);
                initializeAISelector(AILimit);
                initializeBoardLayoutSelector();
            } else if (e.getSource().equals(AIComboBox)) {
                if (AIComboBox.getSelectedItem().equals(0 + " AI players")) {
                    AIAmount = 0;
                    oldAIComboBox.setName(String.valueOf(AIComboBox.getSelectedItem()));
                } else if (AIComboBox.getSelectedItem().equals(1 + " AI players")) {
                    AIAmount = 1;
                    oldAIComboBox.setName(String.valueOf(AIComboBox.getSelectedItem()));
                } else if (AIComboBox.getSelectedItem().equals(2 + " AI players")) {
                    AIAmount = 2;
                    oldAIComboBox.setName(String.valueOf(AIComboBox.getSelectedItem()));
                } else if (AIComboBox.getSelectedItem().equals(3 + " AI players")) {
                    AIAmount = 3;
                    oldAIComboBox.setName(String.valueOf(AIComboBox.getSelectedItem()));
                }
                playerAmount = playerAmount - AIAmount;
            } else if (e.getSource().equals(boardComboBox)) {
                if (boardComboBox.getSelectedItem().equals("Standard")) {
                    startMenuFrame.updateBoardPattern(Board.Pattern.STANDARD);
                } else if (boardComboBox.getSelectedItem().equals("Tetris")) {
                    startMenuFrame.updateBoardPattern(Board.Pattern.TETRIS);
                } else if (boardComboBox.getSelectedItem().equals("Diamond")) {
                    startMenuFrame.updateBoardPattern(Board.Pattern.DIAMOND);
                }
                startMenuFrame.createPlayers(Integer.toString(playerAmount), Integer.toString(AIAmount));

            }
            this.revalidate();
            this.repaint();
        }
    }
}