import java.util.Observable;


/**
 * Used to Monitor the actions in the Whot game
 * and maintains the DeckBin where players get rid
 * of their cards.
 */
public class PlayArea extends Observable {

    /**
     * Action Events Play Area Signals
     **/
    public static int NOACTION = 0x80;
    public static int PICK2 = 0x1;
    public static int PICK3 = 0x2;
    public static int HOLDON = 0x4;
    public static int SUSPENSION = 0x8;
    public static int GENERALMARKET = 0x10;
    public static int WHOT = 0x20;
    public static int WRONGMOVE = 0x40;

    //Used as a mask to get the active Action Event
    //if a user plays a 2, this value becomes 00000001
    //if a user plays general market and pick 3
    //this value become 00010010
    private int currentActionPending;

    //Counts the numofActionEvents that have not be attended to
    //if a user plays a 2 this value get incremented 1
    //if another user play a 2 this value gets incremented to 2
    private int numOfActionEvents;

    //CurrentPlayer sets the variable when
    //Last Card is played
    private boolean playerCheckUp;

    private CardSuite playerWHOTRequest;

    //Top of the Bin
    private DeckBin binTop;

    public PlayArea(DeckBin binTop){
        this.binTop = binTop;
    }

    //Checks if a card can be placed on binTop
    private boolean isStackValid(DeckStack cardStack){
        //Checks all the numbers in the stack are the same
        boolean isValidStack = cardStack.isValidStack() && binTop.isValidDeck(cardStack);

        //Check if card Matches user request if a whot card action is pending
        if(numOfActionEvents > 1) {
            int action = currentActionPending & WHOT;
            if (action == 1) {
                CardSuite currentCardSuite = cardStack.getBottomCard().getShape();
                isValidStack = isValidStack && currentCardSuite == playerWHOTRequest;
            }
            if(isValidStack){
                actionEventSolved(WHOT, 1);
            }
        }

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
        updateBinTop(cardStack);
        this.notifyObservers(currentActionPending);
    }

    //Update Top of Card
    private void updateBinTop(DeckStack cardStackTop){
        binTop.addCards(cardStackTop);
    }

    //Not an action Card so reset environment
    private void playCardSetUpAndNotifyObservers(DeckStack cardStack) {
        numOfActionEvents++;
        currentActionPending = NOACTION;
        updateBinTop(cardStack);
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

            //player can't check up with action card;
            if(playerCheckUp){
                playerCheckUp = false;
                return false;
            }
            setupActionAndNotifyObservers(cardStack);
            return true;
        }
        //Else its a regular card and no actions needed
        playCardSetUpAndNotifyObservers(cardStack);
        return true;
    }

    //Player plays Last Card
    //Does necessary Check and updates
    //setups up Necessary condition for the right action to take place
    public boolean playLastCard(DeckStack cardStack){
        this.playerCheckUp = true;
        return playCard(cardStack);
    }

    public int getNumOfActionEvents() {
        return numOfActionEvents;
    }

    /**
     * Called when action Event has been resolved
     * @param actionEvent - That has been resolved
     */
    public void actionEventSolved(int actionEvent, int numOfActions){
        currentActionPending &= ~(actionEvent);
        numOfActionEvents -= numOfActions;
    }

    /**
     *
     * @return bit Mask of action
     */
    public int getCurrentActionPending(){
        return currentActionPending;
    }

    /**
     * Used to set a request after a WHOT card is played
     * @param playerWHOTRequest - card suite player desires
     */
    public void setWHOTRequest(CardSuite playerWHOTRequest){
        if(binTop.getCardAtTop().getAction() != ActionCardType.WHOT)
            return;
        this.playerWHOTRequest = playerWHOTRequest;
    }

    /**
     *
     * @return the Card at the top of the Bin
     */
    public Card getBinTop(){
        return  binTop.getCardAtTop();
    }

    /**
     *
     * @return true if user has checked
     */
    public boolean getPlayerChecked(){
        return playerCheckUp;
    }

}
