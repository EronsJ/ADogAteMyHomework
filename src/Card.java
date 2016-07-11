/**
 * Created by ChikeUdenze on 7/10/16.
 */

/**
 * cLASS TO REPRESENT EACH CARD. CONSISTS OF TWO FIELDS, THE NUMBER AND THE SHAPE
 */
    public class Card{
        int number;
        CardSuite shape;

        public Card(int number , CardSuite shape){
            this.number=number;
            this.shape=shape;
        }

        @Override
        public String toString() {
            return ("("+this.number+this.shape+")");
        }
    }

