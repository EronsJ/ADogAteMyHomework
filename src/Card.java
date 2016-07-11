/**
 * Created by ChikeUdenze on 7/10/16.
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

