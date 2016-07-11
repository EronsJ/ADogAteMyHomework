import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Emmanuel Olaojo
 * @since 7/10/16
 */
public class Deck {
    private List<Card> cards;
    private Random random;

    public Deck(List<Card> cards){
        this.cards = cards;
        this.random = new Random();
    }

    public Card getCard(int index){
        return this.cards.remove(index);
    }

    public Deck getCards(int[] indices){
        List<Card> cards = new ArrayList<>();

        for(int index: indices){
            cards.add(this.cards.remove(index));
        }

        return new Deck(cards);
    }

    public Card getRandomCard(){
        int index =  getRandomIndex();

        return this.cards.remove(index);
    }

    public Deck getRandomCards(int count){
        int index;
        List<Card> cards = new ArrayList<>();

        for(int i = 0; i < count; i++){
            index = getRandomIndex();

            cards.add(this.cards.remove(index));
        }

        return new Deck(cards);
    }

    public int size(){
        return this.cards.size();
    }

    public void addCard(Card card){
        this.cards.add(card);
    }

    public void addCards(List<Card> cards){
        cards.forEach(card -> {
            this.cards.add(card);
        });
    }

    public void addCards(Deck cards){
        for(int i = 0; i < cards.size(); i++){
            this.cards.add(cards.getCard(i));
        }
    }

    private int getRandomIndex(){
        return this.random.nextInt(this.cards.size());
    }
}
