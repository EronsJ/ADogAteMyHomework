/**
 * Created by Erons J on 7/10/2016.
 */
public class DeckStack {
    private boolean validStack;
    private Card top;
    private boolean actionStack;

    public boolean isValidStack() {
        return validStack;
    }

    public boolean isActionStack() {
        return actionStack;
    }

    public ActionCardType getActionType() {
        return ActionCardType.GENERALMARKET;
    }

    public int getCountOfCards() {
        return 0;
    }


    public Card getTop() {
        return top;
    }
}
