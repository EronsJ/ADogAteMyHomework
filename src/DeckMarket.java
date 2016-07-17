import java.util.List;

/**
 * Created by Erons J on 7/16/2016.
 */
public class DeckMarket {
    public DeckBin createDeckBin() {
        return new DeckBin();
    }

    public void distributeDeckHand(List<Player> players) {
    }

    public DeckHand getTop() {
        return new DeckHand();
    }

    public DeckHand giveDeckHand(int initialNumCardsPerPlayer) {
        return new DeckHand();
    }
}
