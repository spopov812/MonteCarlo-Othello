package my.project.othello;

import java.lang.Math;
import java.util.ArrayList;

/**
 * Class IntelligentComputerPlayer, implements Monte Carlo Tree Search Algorithm
 * and uses the Upper Confidence Bound formula developed by Levente Kocsis and Csaba Szepevari.
 * Player of a reversi game that is controlled by a computer.
 *
 * @author Aleksandr Popov
 */
public class MonteCarloPlayer extends Player{

	//Implementation of Monte Carlo Tree Search
	
	private int time;
	private char[][] currentBoard;
	private Node currentGameState = null;
	private boolean debug;

	/**
	*
	* Constructor for a player type IntelligentComputerPlayer
	*
	* @param piece_local sets piece that this player is playing as, also deduces piece that opponent is playing
	* @param time time (in seconds) that IntelligentComputerPlayer has to make a move
	* @param debug if set to true in constructor, will display information regarding computer's logic
	*/
	public MonteCarloPlayer(char piece_local, int time, boolean debug){

		piece = piece_local;
		
		if (piece_local == 'X'){

			otherPiece = 'O';
		}

		else{

			otherPiece = 'X';
		}

		this.time = time;
		this.debug = debug;
	}

	/**
	*
	* Main logic of algorithm, builds tree of nodes (hypothetical board situations) until a win or loss,
	* then backpropogates that result up the tree
	*
	* @return String returns board position that this player thinks will lead to a win
	*/
	public String getInput(){

		char winner;
		boolean result;

		if (!debug){
			System.out.print("Player " + (piece == 'X'? "1" : "2") + " please make a move with the " + piece + " piece: ");
		}

		//player object created for simulation purposes
		PseudoPlayer testPlayer = new PseudoPlayer(piece);
		Node testNode = null;
		boolean foundState = false;

		//computer has already moved
		if (currentGameState != null){

			ArrayList<Node> prevRoots = currentGameState.getChildren();
			ArrayList<Node> currRoots = null;

			//play has passed as normal from one player to the other
			for (int i = 0; i < prevRoots.size(); i++){
				for (int j = 0; j < prevRoots.get(i).getChildren().size(); j++){

					currRoots = prevRoots.get(i).getChildren();

					if (currRoots.get(j).equalsGameState(currentBoard)){
						
						currentGameState = currRoots.get(j);
						currentGameState.setParent();
						currentGameState.buildChain();
												
						foundState = true;
						break;
					}
				}
			}
		}

		/*some kind of abnormality has occurred in play
		*(a player has repeated their move)
		*or first move of the board for monte carlo player
		*/
		if (!foundState || currentGameState == null){

			testPlayer.analyzeMoves(currentBoard);
			currentGameState = new Node(currentBoard, null, piece, null);
			currentGameState.buildChain();
		}

		//sets up timer to measure how long computer can calculate
		long currentTime = System.currentTimeMillis();
		long targetTime = currentTime +  (1000 * time);

		int numSims = 0;

		//while computer has time to think
		while (targetTime > currentTime){

			testNode = currentGameState.getRandomNode();

			//loops until a win or a loss
			while(true){

				//a simulation has not gone through this node, so children of this node are created
				if (!testNode.wasUsed()){
		
					testNode.buildChain();
				}
		
				//if it is possible for the correct player to make a move from this board state
				if (testNode.hasNodes()){

					testNode = testNode.getRandomNode();
				}

				//no possible children
				else{
	
					//flips which player is to move and build tree of children
					testNode.regenerateChain();

					if (testNode.hasNodes()){

						testNode = testNode.getRandomNode();
					}

					//win, loss, or tie has been achieved
					else{

						break;
					}
				}
			}

			//simulation has run to the end
			winner = Board.getWinner(testNode.boardStateCopy());

			//doesn't consider a tie as a success
			if (winner == piece){

				result = true;
			}

			else{

				result = false;
			}
	
			//backpropogation to a root of the current game state
			while(testNode.hasParent()){

				testNode.updateStats(result);
				testNode = testNode.getParent();
			}

			testNode.updateStats(result);
			currentTime = System.currentTimeMillis();

			numSims++;
		}

		//prints number of simulations computer has run through
		if (debug){
			System.out.println("Number of Simluations Completed- " + numSims);
			System.out.println("");
		}

		//time has run out to think and computer must make a decision
		ArrayList<Node> listOfRoots = currentGameState.getChildren();
		ArrayList<Double> formulaVals = new ArrayList<Double>();
		Node computingNode = null;
		double winLossRatio;

		double lnTotalSims = 0;

		for (int i = 0; i < listOfRoots.size(); i++){

			lnTotalSims += listOfRoots.get(i).getSimulations();
		}

		lnTotalSims = Math.log(lnTotalSims);

		/*operation of formula created by Levente Kocsis and Csaba Szepevari
		 *creates an upper confidence bound which helps decide which node to go down.
		 *Solution for the multi armed bandit problem
		*/
		for (int i = 0; i < listOfRoots.size(); i++){

			computingNode = listOfRoots.get(i);

			winLossRatio =(double) computingNode.getWins() / computingNode.getSimulations();

			//formula computation
			formulaVals.add(winLossRatio + (Math.sqrt(2) * (Math.sqrt(lnTotalSims / computingNode.getSimulations()))));

			if (debug){
				System.out.println("Formula value is " + formulaVals.get(i));
			}

			if (debug){	
				System.out.print("The move at (" + (CoordinatePair.decodeRow(computingNode.getPrevMove()) + 1));
				System.out.print(", " + (CoordinatePair.decodeCol(computingNode.getPrevMove()) + 1) + ") ");
				System.out.print("would have won " + computingNode.getWins() + "/");
				System.out.print(computingNode.getSimulations() + " (" + winLossRatio * 100 + "%) times.\n\n");
			}
		}

		Double highestVal = formulaVals.get(0);
		int highestValLocation = 0;

		//finds highest value after formula computation
		for (int i = 1; i < formulaVals.size(); i ++){
	
			if (formulaVals.get(i) > highestVal){

				highestVal = formulaVals.get(i);
				highestValLocation = i;
			}
		}

		String returnString = listOfRoots.get(highestValLocation).getPrevMove();


		if (debug){

			System.out.print(piece + " Player made a move to ");
		}

		System.out.print((CoordinatePair.decodeRow(returnString) + 1) + " " + (CoordinatePair.decodeCol(returnString) + 1));

		if (debug){

			System.out.println(" (winrate of " + (((double)listOfRoots.get(highestValLocation).getWins() / listOfRoots.get(highestValLocation).getSimulations()) * 100) +  "%)");
		}

		System.out.println("");

		return returnString;
	}

	@Override
	
	/**
	*
	* Updates hashmap for key-value pairs of where this player can move.
	* <p>
	* Overrides method writen in Player class by updating current board state in IntelligentComputerPlayer
	* class. Still performs analyze moves implemented in Player class however.
	*
	* @param board current state of the board as a 2D char array
	*/
	public void analyzeMoves(char[][] board){

		currentBoard = board;

		super.analyzeMoves(currentBoard);
	}
}
