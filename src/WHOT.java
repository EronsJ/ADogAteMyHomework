import java.util.*;

/**
 * Created by Erons J on 7/10/2016.
 * Sets up and corrdinates the whole game
 */
public class WHOT implements Observer{

    //When a player checks up the game is restarted
    //numOfRounds times
    private int numOfRounds;

    //Sets when the current player has correctly checked up
    public boolean moveToNextRound;

    //The initial deck of card that distributes cards and creates the binTop
    private DeckMarket market;

    //The card in the middle
    private DeckBin BinTop;

    //Number of Players in the game
    private PlayerManager playerManager;

    //Mointors the DeckBin and Updates this class
    //On what action the current player has taken
    //and what the next player needs to take
    private PlayArea playArea;

    //The player that is about to play
    private Player currentPlayer;

    //if game has been won
    private boolean gameWon;

    /**
     * Creates the DeckMarket, DeckBin, Players and PlayArea
     *
     * @param numOfPlayers - Number of players in the game
     * @param includeComputer - Whether or not the HumanPlayers want the computer to play
     */
    public WHOT(int numOfPlayers, boolean includeComputer){
        //-1 is used as a sentiel value to tell the program
        // to use the default num of rounds
        this(numOfPlayers, includeComputer, -1);
    }

    /**
     * Creates the DeckMarket, DeckBin, Players and PlayArea
     *
     * @param numOfPlayers - Number of players in the game
     * @param includeComputer - Whether or not the HumanPlayers want the computer to play
     * @param numOfRounds - if not 1, the game keeps going by eliminate the player with the highest count
     */
    public WHOT(int numOfPlayers, boolean includeComputer, int numOfRounds){
        if(numOfPlayers < 1){
            throw new IllegalArgumentException("You can't play without players :(! You need at least 1 player");
        }
        initializeMembers(false, numOfPlayers, includeComputer, numOfRounds);
    }

    /***
     * Initializes all the member variables of this class
     * This function is mainly needed when a round finishes
     */
    private void initializeMembers(boolean isNextRound,int numOfPlayers, boolean includeComputer, int numOfRounds){

        //Create Market
        market = new DeckMarket();

        //Create BinTop - Card in the middle
        BinTop = market.createDeckBin();

        //Create Player or Computer player if it's initial play
        if(!isNextRound) {
            List<Player> players = createPlayers(numOfPlayers, includeComputer);
            playerManager = new PlayerManager(players, market);
        }

        //Update the number of rounds to be played
        //if there are 2 player 1 round because once one person checks up the person wins
        //if there are 3 palyers 2 rounds because when one checks up person with highest count goes out
        //and game restarts
        this.numOfRounds = numOfRounds < 0 ? numOfPlayers - 1 : numOfRounds;

        //Creates the logic of the binTop
        playArea = new PlayArea(BinTop);

        //Used to instruct the main game about the current and next action to take
        //on a player
        playArea.addObserver(this);

        //Randomly Choose which Method to distribute Cards
        Random rand = new Random(50);
        int randomMethod = rand.nextInt();
        for(int i = 0; i < new Random(100).nextInt(); i++){
            randomMethod = rand.nextInt();
        }

        //Give Players DeckHands
        if( randomMethod < 50) {
            playerManager.distributeDeckHandRotate(market);
        }else {
            playerManager.distributeDeckHandOnce(market);
        }

        //Choose first player by picking a random
        //player from the list
        currentPlayer = playerManager.getFirstPlayer();
    }

    /**
     * Creates the players of the game
     * NOTE: Num of players must be greater than one
     * @param numOfPlayers
     * @param includeComputer
     */
    private List<Player> createPlayers(int numOfPlayers, boolean includeComputer) {
        LinkedList<Player> players = new LinkedList<>();

        includeComputer = numOfPlayers > 1 ? true : includeComputer;
        if (numOfPlayers == 1 || includeComputer) {
            numOfPlayers++;
            players.add(new ComputerPlayer(this.playArea) );
        }
        for (int i = 0; i < numOfPlayers; i++) {
            players.add(new HumanPlayer(this.playArea) );
        }
        return players;
    }

    public void startGame(){
        while(!gameWon){

            while(numOfRounds >= 1 && !moveToNextRound){
                currentPlayer.playCard();
            }

            //Announce winner of Round
            System.out.println(currentPlayer + "Won");

            //Eliminate player with highest count
            playerManager.removePlayerWithHighestCount();

            //Update Game conditions
            moveToNextRound = false;
            numOfRounds--;
            if(numOfRounds <= 0){
                gameWon = true;
            }
            //Restart game by
            //Reinitialize class by updating all variables
            //and doing initialize actions.
            //The include computer parameter here doesn't matter
            initializeMembers(true, playerManager.getNumberOfPlayers(), false , numOfRounds );
        }

        //Game Winner message
        System.out.println(currentPlayer + "Won the Entire Game");
        System.out.println(currentPlayer + "Is King of WHOT and should be revered by all!!!");

    }

    //this is where the game logic is
    @Override
    public void update(Observable o, Object arg) {
        int actionEvent = playArea.getCurrentActionPending();

        //if player successfully checks up
        //Break out of ain loop
        //Declare winner of round
        //Move to next round/declare winner of game
        if(playArea.getPlayerChecked()){
            moveToNextRound = true;
            return;
        }

        //If wrong move playerManger punishes current player
        //Resolves the action in the playArea and
        //Goes to next player
        int action = actionEvent & playArea.WRONGMOVE;
        if(action == 1){
            playerManager.punish(currentPlayer);
            playArea.actionEventSolved(playArea.WRONGMOVE, 1);
            currentPlayer = playerManager.getNextPlayer();
            return;
        }

        //if not action card
        //resolve action in PlayArea
        //Go to next player
        action = actionEvent & playArea.NOACTION;
        if(action == 1){
            playArea.actionEventSolved(playArea.NOACTION, 1);
            currentPlayer = playerManager.getNextPlayer();
            return;
        }

        //if general market
        //player manager gives each player
        //a number of Cards that is equal to the number of general market in the played
        //DeckStack
        action = actionEvent & playArea.GENERALMARKET;
        if(action == 1){
            playerManager.giveAllPlayer(playArea.getNumOfActionEvents());
            playArea.actionEventSolved(playArea.GENERALMARKET,playArea.getNumOfActionEvents() );
            return;
        }

        //if WHOT card
        //PlayManager tells all players that a WHOT Request is available
        //PlayArea resolves this condition
        //and players should look at the play area to know whether
        //the event has been resolved
        action = actionEvent & playArea.WHOT;
        if(action == 1){
            playerManager.setWHOTRequestAvailable();

            //PlayArea solves this event in isValidStack Method
            //by making all cards invalid until the request has
            //been statisfied
            currentPlayer = playerManager.getNextPlayer();
            return;
        }

        //if suspension
        //player manger skips the number of player equivalent to the number
        //of suspension cards played
        action = actionEvent & playArea.SUSPENSION;
        if(action == 1){
            int numberOfSuspensionsPlayed = playArea.getNumOfActionEvents();
            currentPlayer = playerManager.getNextPlayer(numberOfSuspensionsPlayed);
            playArea.actionEventSolved(playArea.SUSPENSION, numberOfSuspensionsPlayed);
            return;
        }

        //if HOLDON
        //the current player is unchanged and get to play again
        action = actionEvent & playArea.HOLDON;
        if(action == 1){
            playArea.actionEventSolved(playArea.HOLDON,playArea.getNumOfActionEvents() );
            return;
        }

        //if pick2
        //playManager tells next player to pick
        //if player disagrees, main game tell player to play,
        //PlayArea block all cards from being played except it is a 2
        //until a player chooses to  pick a card
        //PlayArea increases the number of pick2 as we go to the next player
        //when a player chooses to pick
        //playManager gives them the appropriate number of cards
        //and goes to the next player
        action = actionEvent & playArea.PICK2;
        if(action == 1){
            currentPlayer = playerManager.getNextPlayer();
            boolean isResolved = currentPlayer.pickCard(playArea.getNumOfActionEvents());
            if(isResolved){
                playArea.actionEventSolved(playArea.PICK2, playArea.getNumOfActionEvents());
                playerManager.giveCard(currentPlayer, playArea.getNumOfActionEvents());
                currentPlayer = playerManager.getNextPlayer();
            }
            return;
        }

        //if pick3
        //playManager tells next player to pick
        //if player disagrees, main game tell player to play,
        //PlayArea block all cards from being played except it is a 5
        //until a player chooses to  pick a card
        //PlayArea increases the number of pick 3 as we go to the next player
        //when a player chooses to pick
        //playManager gives them the appropriate number of cards
        //and goes to the next player
        action = actionEvent & playArea.PICK3;
        if(action == 1){
            currentPlayer = playerManager.getNextPlayer();
            boolean isResolved = currentPlayer.pickCard(playArea.getNumOfActionEvents());
            if(isResolved){
                playArea.actionEventSolved(playArea.PICK3, playArea.getNumOfActionEvents());
                playerManager.giveCard(currentPlayer, playArea.getNumOfActionEvents());
                currentPlayer = playerManager.getNextPlayer();
            }
            return;
        }
    }

}
