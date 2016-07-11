import com.sun.javaws.exceptions.InvalidArgumentException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



public class CardFactory {

    public ActionCardType convertNumToActionType(int cardNum){
        switch(cardNum){
            case 1:
                return ActionCardType.HOLDON;
            case 2:
                return ActionCardType.PICK2;
            case 5:
                return ActionCardType.PICK3;
            case 8:
                return ActionCardType.SUSPENSION;
            case 14:
                return ActionCardType.GENERALMARKET;
            case 20:
                return ActionCardType.WHOT;
            default:
                return ActionCardType.NOACTION;
        }


    }
    public CardSuite convertStringToSuite(char suiteString){
        switch(suiteString){
            case '+':
                return CardSuite.CROSS;
            case '*':
                return CardSuite.STAR;
            case '^':
                return CardSuite.TRIANGLE;
            case '[':
                return CardSuite.SQUARE;
            case '0':
                return CardSuite.CIRCLE;
            case 'W':
                return CardSuite.WHOT;
            default: throw new IllegalArgumentException("Wrong character in file");
        }
    }
    public ArrayList<Card> createDeck(String filename) throws IOException {
        ArrayList<Card> deck = new ArrayList<>();


        File f = new File(filename);
        Scanner numScan = new Scanner(f);

        String line;

        while (numScan.hasNextLine()) {
            line = numScan.nextLine();
            int cardNumber=Integer.parseInt(""+line.charAt(0));
            CardSuite cardSuite= convertStringToSuite(line.charAt(1));

            Card CurrCard= new Card(cardNumber,cardSuite);
            deck.add(CurrCard);
            CurrCard.setActionType(convertNumToActionType(cardNumber));
        }
        return deck;
    }
    public String toString(){
        return("");
    }


    public static void main(String [] args) throws IOException{
        CardFactory D= new CardFactory();
        System.out.print(D.createDeck(args[0]));
    }
}


