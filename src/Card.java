/**
 * Created by ChikeUdenze on 7/10/16.
 */

/**
 * @author King David
 * @author Chike Udenze
 * CLASS TO REPRESENT EACH CARD. CONSISTS OF TWO FIELDS, THE NUMBER AND THE SHAPE
 */
public class Card{
    private int number;
    private CardSuite shape;
    private ActionCardType actionType;

    public Card(int number , CardSuite shape, ActionCardType actionType){
        this.number = number;
        this.shape = shape;
        this.actionType = actionType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (number != card.number) return false;
        if (shape != card.shape) return false;
        return actionType == card.actionType;

    }

}

