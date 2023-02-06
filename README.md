# SYSC3110-Scrabble-G14

Overview:
This Implementation of Scrabble is a GUI-based, 4-player version of the game containing a 15x15 board and a bag of 100 seperate tiles. Each tile contains its own assigned score. Players are assigned 7 letters to start. Players take turns placing words on the board while attempting to form the largest word they possibly can with the previously placed tiles on the board. The ultimate goal is gaining as many points as possible. 

Build Status: 
As of 2022-11-13, the primary functionality of the game is up. Players can exchange, pass, shuffle, play when their turn is reached. A board and the player's hand is displayed each turn. For calculating points, the current implementation simply sums up the most recent played word and stores it with each individual player. The first tile can be placed where ever but afterwards, tiles must connect and placed tiles must all form words with all adjacent tiles. The game ends once one player reaches 50 points. Below is a list of improvements to be made in later milestones. 
- Focus of Models.Board implementation was functionality. Improvements to cohesion, process and efficiency will be made for the next milestone. 
- Focus of Models.Game implementation was functionality. Improvements to cohesion, process and efficiency will be made for the next milestone.
- Cohesion of classes Models.PlayMove, Models.Player, Models.InHand will be improved for next milestones. 
- Implementation for specific square points (multipliers) will be accounted for in later milestones. Faiing tests for these have been added with JUnit.
- Certain conditions for ending the game have not been set (board is unplayable with current state of all players' hands for example). These cases will be addressed in later milestones.
- Blank tiles can't be played now as it will initiate a command line user-input. This will be accounted for in milestone 3. 
- Unable to get a dictionary API. Storing all valid words in a local text file which is read during program runtime. 
- Models.Player does not need to start at middle of board, will be implemented in later milestone.
- Models.Command Panel to display more information later, for now it only displays current player turn and points. 
- Drag and Drop features such as returning tiles from board, removing tile form hand in real time and rejecting tile placements in real time will be added in later milestones. 
- GUI visual appeal to be improved in later milestones.  
- When placing tiles, the user must place the letters in the order in which they would appear in the word or else the word will return invalid. To be fixed in later milestones. 

Code Style: 
Primarily coded in Java v.17. Coded and Designed using fundamental Object-Oriented Programming concepts and Design Patterns and Forms discussed in SYSC3110. JUnit5 was utilized for unit testing and Swing was utilized for GUI. 

Tests:
JUnit4 tests were setup in order to test both board specific routes (added failing tests for expected failed multipliers). JUnit4 tests were added to run through different possible play scenarios for Models.Game in order to confirm different functions work as intended.  

How to Use: 

Please Refer to attached Manual.

Note a filenotfoundexception may be raised. In this case, go into Models.WordValidator class and modify the directory for the word dictionary accordingly. 

Credits: 
- Vashisht, Akshay
- Kaddour, Mohamed
- Haghighi Saed, Jaydon
- Ameli, Mahtab

Despite focusing on specific classes, each member contributed equally to the overall functionailty of the program.

License: 
Copyright 2022, G14-Scrabble for SYSC 3110 Project Carleton University, released with no intent of distribution and with heavy influence from the official scrabble board game. 
