import java.util.Observable;


/**
 * Used to Monitor the actions in the Whot game
 * and maintains the DeckBin where players get rid
 * of their cards.
 */
public class PlayArea extends Observable {
    /*Action Events Play Area Signals*/
    public static int NOACTION = 0x0;
    public static int PICK2 = 0x1;
    public static int PICK3 = 0x2;
    public static int HOLDON = 0x4;
    public static int SUSPENSION = 0x8;
    public static int GENERALMARKET = 0x10;
    public static int WHOT = 0x20;
    public static int WRONGMOVE = 0x40;

    //Used as a mask to get the active Action Event
    //if a user play a 2, this value becomes 00000001
    //if a user plays general market and pick 3
    //this value become 00010010
    private int currentActionPending;
    //Counts the numofActionEvents that have not be attended to
    //if a user plays a 2 this value get incremented 1
    //if another user play a 2 this value gets incremented to 2
    private int numOfActionEvents;

    //Top of the Bin
    private DeckBin binTop;

    //Checks if a card can be placed on binTop
    private boolean isStackValid(DeckStack cardStack){
        //Checks all the numbers in the stack are the same
        boolean isValidStack = cardStack.isValidStack() && binTop.isValidDeck(cardStack);
        //if not the same
        if(!isValidStack){
            setupWrongMoveAndNotifyObservers();
        }
        return isValidStack;
    }

    //Sets up conditions for wrong moves
    private void setupWrongMoveAndNotifyObservers() {
        numOfActionEvents++;
        currentActionPending |= WRONGMOVE;
        this.notifyObservers(currentActionPending);
    }

    //Sets up conditions for all actions
    private void setupActionAndNotifyObservers(DeckStack cardStack) {
        //Update the num of Action events to
        numOfActionEvents += cardStack.getCountOfCards();
        if(cardStack.getActionType() == ActionCardType.PICK2){
            currentActionPending |= PICK2;
        }
        else if(cardStack.getActionType() == ActionCardType.PICK3){
            currentActionPending |= PICK3;
        }
        else if(cardStack.getActionType() == ActionCardType.HOLDON){
            currentActionPending |= HOLDON;
        }
        else if(cardStack.getActionType() == ActionCardType.SUSPENSION){
            currentActionPending |= SUSPENSION;
        }
        else if(cardStack.getActionType() == ActionCardType.GENERALMARKET){
            currentActionPending |= GENERALMARKET;
        }
        else if(cardStack.getActionType() == ActionCardType.WHOT){
            currentActionPending |= WHOT;
        }
        updateBinTop(cardStack.getTop());
        this.notifyObservers(currentActionPending);
    }

    //Update Top of Card
    private void updateBinTop(Card cardStackTop){
        binTop.setTop(cardStackTop);
    }

    //Not an action Card so reset environment
    private void playCardSetUpAndNotifyObservers(DeckStack cardStack) {
        numOfActionEvents = NOACTION;
        currentActionPending = NOACTION;
        updateBinTop(cardStack.getTop());
        this.notifyObservers(currentActionPending);
    }



    //Play Card
    //Does necessary Checks and updates
    //Necessary conditions for the right action to take place
    public boolean playCard(DeckStack cardStack){
        //Check if the stack is valid
        if( !isStackValid(cardStack)){
            return false;
        }
        //Check if the stack is made up of action cards
        boolean isActionStack = cardStack.isActionStack();
        if(isActionStack){
            setupActionAndNotifyObservers(cardStack);
            return true;
        }
        //Else its a regular card and no actions needed
        playCardSetUpAndNotifyObservers(cardStack);
        return true;
    }

    public int getNumOfActionEvents() {
        return numOfActionEvents;
    }

    public void setBinTop(DeckBin binTop) {
        this.binTop = binTop;
    }
}
