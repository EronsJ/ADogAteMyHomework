
/**
 * Created by Erons J on 7/10/2016.
 */
public class DeckBin extends Deck{
    public boolean isValidDeck(DeckStack cardStack) {
        return this.flipTop().getNumber() == cardStack.flipBottom().getNumber();
    }

    public Card getCardAtTop() {
        return this.flipTop();
    }
}
