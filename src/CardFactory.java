import com.sun.javaws.exceptions.InvalidArgumentException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ChikeUdenze on 7/10/16.
 */

public class CardFactory {







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

            while (numScan.hasNext()) {
                line = numScan.nextLine();
                Card CurrCard= new Card(Integer.parseInt(""+line.charAt(0)),convertStringToSuite(line.charAt(1)));
                deck.add(CurrCard);



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


