/**
 * Created by ChikeUdenze on 7/10/16.
 */

/**
 * cLASS TO REPRESENT EACH CARD. CONSISTS OF TWO FIELDS, THE NUMBER AND THE SHAPE
 */
    public class Card{
        int number;
        CardSuite shape;
        ActionCardType actionType;

        public Card(int number , CardSuite shape){
            this.number=number;
            this.shape=shape;
        }

        public void setActionType(ActionCardType actionType) {
            this.actionType = actionType;
        }

        public int getCountVal(){
            return shape == CardSuite.STAR ? 2 * number: number;
        }
        public int getNumber() {
            return number;
        }

        public CardSuite getShape() {
            return shape;
        }

        public ActionCardType getAction() {
            return actionType;
        }

        @Override
        public String toString() {
            return ("("+this.number+", "+this.shape+", "+this.actionType+")");
        }
    }

