# MonteCarlo-Othello
Implementation of the Monte Carlo Tree Search for a computer player in a game of Othello

Implementation of the Monte Carlo Tree Search Algorithm to play Othello and the Upper Confidence Bound formula developed by Levente Kocsis and Csaba Szepevari.The algorithm manipulates objects of type Node which represent a hypothetical state of the board. The algorithm runs by creating branches of these nodes and selecting random nodes until the game results in a win or a loss. The algorithm them backpropogates the result up the tree and updates the statistics of all the nodes it went through and repeats this loop until time has run out to think. It then takes these results and runs them an upper confidence bound formula which determines which how confident it is that a hypothetical board state will lead to an acurate percent win rate and then makes a move.

To Compile- $ mvn compile

To Run- $ ./run.bat
