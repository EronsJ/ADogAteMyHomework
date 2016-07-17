import java.util.List;
import java.util.Random;

/**
 * Created by Erons J on 7/16/2016.
 */
public class PlayerManager {

    //Number of cards each player gets
    public static int INITIAL_NUM_CARDS_PER_PLAYER = 4;
    private final DeckMarket market;

    //List of Players
    private List<Player> players;

    //List of
    private int currentPlayerIndex;

    public PlayerManager(List<Player> players, DeckMarket market) {
        this.players = players;
        this.market = market;
    }

    /**
     * Gives a player a deckHand by giving a player INITIAL_NUM_CARDS_PER_PLAYER
     * at once
     */
    public void distributeDeckHandOnce(DeckMarket market) {
        int numOfPlayers = players.size();
        for (int i = 0; i < numOfPlayers; i++ ) {
            DeckHand playerDeckHand = market.giveDeckHand(INITIAL_NUM_CARDS_PER_PLAYER);
            players.get(i).takeCard(playerDeckHand);
        }
    }

    /**
     * Gives a player a deckHand by giving on player a deck at a time
     * @param market - market that gives deckHand
     */
    public void distributeDeckHandRotate(DeckMarket market) {
        int numOfPlayers = players.size();
        int numOfCardsPerPlayer = INITIAL_NUM_CARDS_PER_PLAYER;
        for (int i = 0; i < numOfPlayers * numOfCardsPerPlayer; i++ ) {
            int index =  i % numOfPlayers;
            DeckHand playerDeckHand = market.getTop();
            players.get(index).takeCard(playerDeckHand);
        }
    }

    /**
     *
     * @return Next Player
     */
    public Player getNextPlayer(){
        int numOfPlayers = players.size();
        int nextIndex = currentPlayerIndex % numOfPlayers;
        currentPlayerIndex = nextIndex;
        return players.get(currentPlayerIndex);
    }

    /**
     * Get first Player at the beginning of the game
     * @return Randomly Chosen First Player
     */
    public Player getFirstPlayer() {
        Random randomIndexGenerator = new Random();
        int numOfPlayers = players.size();
        int firstPlayer = randomIndexGenerator.nextInt(numOfPlayers);
        currentPlayerIndex  = firstPlayer;
        return players.get(firstPlayer);
    }

    /**
     * Removes all the player that have the biggest
     * Counts
     */
    public void removePlayerWithHighestCount() {
        int max = getPlayerIndexWithMaxCount();
        players.removeIf(player -> player.getCount() == max );
    }

    /**
     *
     * @return Maximum Count a player has
     */
    private int getPlayerIndexWithMaxCount() {
        int max = 0;
        for ( Player player: players ) {
            int playerMax = player.getCount();
            if( playerMax > max){
                max = playerMax;
            }
        }
        return max;
    }

    /**
     *
     * @return Num of players in list
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * Gives a player one card from the market
     * @param currentPlayer
     */
    public void punish(Player currentPlayer){
        //Verify that player is correct
        if(!players.get(currentPlayerIndex).equals(currentPlayer)){
            throw new IllegalArgumentException("Something is wrong with you indexing! Check logic");
        }
        DeckHand cardToGivePlayer = market.getTop();
        currentPlayer.takeCard(cardToGivePlayer);
    }

    /**
     * Give each player except from current player one card
     * @param numOfActionEvents
     */
    public void giveAllPlayer(int numOfActionEvents) {
        for(Player player : players){
            //Skip currentPlayer
            if(players.get(currentPlayerIndex).equals(player)){
                continue;
            }
            DeckHand cardsToGivePlayer = market.giveDeckHand(numOfActionEvents);
            player.takeCard(cardsToGivePlayer);
        }
    }

    /**
     * Set the field of players to true so before playing they can check
     * if WHOT request is pending
     */
    public void setWHOTRequestAvailable() {
        for(Player player : players){
            player.setWHOTRequest();
        }
    }

    /**
     * Returns the next num of action events player
     * @param numOfActionEvents
     * @return
     */
    public Player getNextPlayer(int numOfActionEvents) {
        currentPlayerIndex = (currentPlayerIndex + numOfActionEvents) % players.size();
        return players.get(currentPlayerIndex);
    }

    /**
     * Gives current player card
     * @param currentPlayer
     * @param numOfActionEvents
     */
    public void giveCard(Player currentPlayer, int numOfActionEvents) {
        //Verify that player is correct
        if(!players.get(currentPlayerIndex).equals(currentPlayer)){
            throw new IllegalArgumentException("Something is wrong with you indexing! Check logic");
        }
        DeckHand cardsToGivePlayer = market.giveDeckHand(numOfActionEvents);
        currentPlayer.takeCard(cardsToGivePlayer);
    }

}


