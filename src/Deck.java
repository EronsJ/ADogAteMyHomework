import java.util.ArrayList;
import java.util.Arrays;
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

    public Card drawCard(int index){
        return this.cards.remove(index);
    }

    public Deck drawCards(int[] indices){
        List<Card> cards = new ArrayList<>();
        int numRemoved = 0;

        for(int index: indices){
            cards.add(this.cards.get(index));
        }

        Arrays.sort(indices);

        for(int index: indices){
            this.cards.remove(index - numRemoved);
            numRemoved++;
        }

        return new Deck(cards);
    }

    public Card drawRandomCard(){
        int index =  randomIndex();

        return this.cards.remove(index);
    }

    public Deck drawRandomCards(int count){
        int index;
        List<Card> cards = new ArrayList<>();

        for(int i = 0; i < count; i++){
            index = randomIndex();

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

    public void addCards(Deck cards){
        int size = cards.size();

        for(int i = 0; i < size; i++){
            this.cards.add(cards.drawCard(0));
        }
    }

    public String toString(){
        return cards.toString();
    }

    private int randomIndex(){
        return this.random.nextInt(this.cards.size());
    }

    private static void printLine(){
        System.out.println("_______________________________________________" +
                "_______________________________________________________" +
                "___________________________________________");
    }

    private static void print(Object o){
        System.out.println(o);
    }

    public static void main(String[] args) {
        try {
            print("Creating Deck...");
            Deck testDeck = new Deck(CardFactory.createDeck("cards.txt"));
            print("Deck: " + testDeck);
            printLine();

            print("Adding '2 Circle' to deck...");
            testDeck.addCard(new Card(2, CardSuite.CIRCLE, ActionCardType.PICK2));
            print("Deck: " + testDeck);
            printLine();

            print("Drawing the Card at index 1: '5 Star'...");
            print("Card: " + testDeck.drawCard(1));
            print("Deck: " + testDeck);
            printLine();


            print("Drawing cards at index 2, 0, 4: '4 Square, 1 Cross, 2 Circle'...");
            int[] cardIndices = {2, 0, 4};
            Deck drawnCards = testDeck.drawCards(cardIndices);
            print("Drawn Cards: " + drawnCards);
            print("Deck: " + testDeck);
            printLine();

            print("Adding the drawn cards back to the deck...");
            testDeck.addCards(drawnCards);
            print("Deck: " + testDeck);
            printLine();

            print("Drawing random card...");
            print("Card: " + testDeck.drawRandomCard());
            print("Deck: " + testDeck);
            printLine();

            print("Drawing 2 random cards...");
            print("Cards: " + testDeck.drawRandomCards(2));
            print("Deck: " + testDeck);
            printLine();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
