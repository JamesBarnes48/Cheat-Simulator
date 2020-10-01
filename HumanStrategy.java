/*HumanStrategy is the strategy class that the user can directly control. It is different to the other strategies
in that it has no decision making of its own, that is all done by the user's own input. Due to the fact
there is no computerised decision-making the majority of this code is input validation
 */

import java.util.*;

public class HumanStrategy extends BasicStrategy
        implements Strategy {

    //initialising keyboard input scanner to be used throughout the class
    Scanner input = new Scanner(System.in);

    @Override
    public boolean cheat(Bid b, Hand h)
    {
        //checks if hand is unplayable when not cheating before prompting
        //if unplayable automatically evaluates to true
        if(!h.canPlayHand(b, h))
        {
            System.out.println("Hand unplayable, you must cheat");
            return true;
        }

        System.out.println("Will you cheat this turn? (Y/N) ");
        return promptYN();
    }

    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat)
    {
        boolean valid = false;
        boolean finished;
        ArrayList<Card> playArr = new ArrayList<>();
        Card.Rank playRank;

        //local cheat attribute in case the user changes their mind
        boolean localCheat = cheat;

        while(!valid)
        {
            //setting validity to true by default. Invalid entries will falsify it
            valid = true;
            //resetting finished in case of invalid entry so flow can break out of while loop
            finished = false;

            System.out.println("The previous bid was " + b.toString());
            System.out.println("Cheating? " + localCheat);
            System.out.println("Your hand: ");
            for(int i = 0; i < h.size(); i++)
            {
                System.out.println("Enter " + i + " to play " + h.getHand().get(i).toString());
            }
            System.out.println("Enter Q to stop adding to hand");

            //reset the playHand if an invalid input is detected
            playArr = new ArrayList<>();


            //adding cards to play hand and validating them
            while(!finished)
            {
                char entry = input.next().charAt(0);

                //set play rank - default setting is for not cheating
                playRank = h.getHand().get(0).getRank();

                //checks if user wanted to stop adding to hand
                //checks as char type
                if(entry == 'Q' || entry == 'q')
                {
                    if(playArr.isEmpty())
                    {
                        System.out.println("Cannot make an empty bid. Please add cards to your bid");
                    }
                    else
                    {
                        break;
                    }
                }

                //validating input to see if it is a valid card
                //tries converting to int that can be used as index
                try
                {
                    int entryIndex = Character.getNumericValue(entry);
                    h.getHand().get(entryIndex);
                }
                catch(Exception ex)
                {
                    System.out.println("Invalid input: please enter a given number");
                    valid = false;
                    break;
                }

                //if deemed valid, initialise entryIndex so the card can be accessed in hand
                int entryIndex = Character.getNumericValue(entry);

                //validating input to see if it violates the rules of the game
                //checking to see if all cards in play hand are the same rank
                for(int i = 0; i < playArr.size(); i++)
                {
                    Card curCard = playArr.get(i);
                    Card enteredCard = h.getHand().get(entryIndex);
                    if(curCard.getRank() != enteredCard.getRank())
                    {
                        System.out.println("Invalid input: all cards to be played must be the same rank");
                        valid = false;
                        finished = true;
                    }
                }

                //validating input to see if it's permitted by if they're cheating or not
                if(localCheat)
                {
                    Card.Rank curBid = b.getRank();
                    Card.Rank enteredBid = h.getHand().get(entryIndex).getRank();
                    if(curBid == enteredBid || curBid.next() == enteredBid)
                    {
                        //in case of invalid entry, gives the user the option to change bid
                        System.out.println("Invalid entry while cheating: Card rank matches current bid");
                        localCheat = cheat(b, h);
                        System.out.println("Cheating status updated. Please re-enter bid");
                        valid = false;
                        finished = true;
                    }
                }

                else
                {
                    Card.Rank curBid = b.getRank();
                    Card.Rank enteredBid = h.getHand().get(entryIndex).getRank();

                    if(curBid != enteredBid && curBid.next() != enteredBid)
                    {
                        //in case of invalid entry, gives the user the option to change bid
                        System.out.println("Invalid entry when not cheating: Card rank doesnt match current bid");
                        localCheat = cheat(b, h);
                        System.out.println("Cheating status updated. Please re-enter bid");
                        valid = false;
                        finished = true;
                    }
                }

                //if still deemed valid add to playHand
                if(valid)
                {
                    System.out.println("added to hand");
                    playArr.add(h.getHand().get(entryIndex));
                }
                System.out.println(playArr.toString());
            }
        }
        //setting playRank based on cheat
        if(localCheat)
        {
            Card.Rank posRankLower = b.getRank();
            Card.Rank posRankHigher = b.getRank().next();
            playRank = getCheatRank(posRankLower, posRankHigher);
        }
        else
        {
            playRank = playArr.get(0).getRank();
        }
        //if deemed valid return new Bid
        Hand playHand = new Hand();
        playHand.addCardCollection(playArr);
        return new Bid(playHand, playRank);
    }

    @Override
    public boolean callCheat(Hand h, Bid b)
    {
        System.out.println("Will you call cheat? (Y/N) ");
        return promptYN();
    }

    //new method reduces redundant code
    private boolean promptYN()
    {
        boolean valid = false;

        //boolean used to contain program in a loop until valid input entered
        while(!valid)
        {
            //first character entered to console evaluated
            char entry = input.next().charAt(0);
            if(entry == 'Y' || entry == 'y')
            {
                valid = true;
            }
            else if(entry == 'N' || entry == 'n')
            {
                return false;
            }
            else
            {
                System.out.println("Invalid input. Please enter using Y or N");
            }
        }

        return true;
    }
}
