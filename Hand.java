/*the key feature of the Hand class is its collection of Card objects representing the hand
I have chosen to use a Set to represent the hand because sets enforce a rule of no duplicates being allowed,
which is true also for a players hand. it contains methods that manipulate the hand and
allow it to interact with other entities too
 */

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Hand
implements Iterable, Serializable {

    //used for version control when serializing
    static final long serialVersionUID = 100239761 + 10;
    public long id;

    //initialising the hand as an ArrayList collection
    private ArrayList<Card> hand = new ArrayList<Card>();

    //initialising attribute holding total of all card values in the hand
    //totalValue will be recalculated each time it's referenced
    private int totalValue;
    {
        Iterator<Card> handIterator = hand.iterator();
        int count = 0;

        //iterates through set and adds each card's value to a running total
        while(handIterator.hasNext())
        {
            count += handIterator.next().getRankValue();
        }

        totalValue = count;
    }


    /*----------------CONSTRUCTOR METHODS---------------*/
    //default constructor creates an empty hand
    public Hand()
    {
         hand = new ArrayList<Card>();

         //generates a random id for this hand, used when serializing
         id = 1L + (long) (Math.random() * (100L - 1L));
    }

    //constructor that creates a hand from an input array
    public Hand(Card[] inputCards)
    {
        hand.addAll(Arrays.asList(inputCards));

        //generates a random id for this hand, used when serializing
        id = 1L + (long) (Math.random() * (100L - 1L));
    }

    //constructor that creates a hand by duplicating another
    public Hand(Hand inputHand)
    {
        hand = new ArrayList<Card>(inputHand.getHand());

        //generates a random id for this hand, used when serializing
        id = 1L + (long) (Math.random() * (100L - 1L));
    }

    /*-----------------CLASS METHODS--------------*/
    //adds a single card to the hand
    public void addSingleCard(Card inputCard)
    {
        hand.add(inputCard);
    }

    //adds a collection of cards to the hand
    public void addCardCollection(Collection<Card> inputCollection)
    {
        //add all cards to hand
        hand.addAll(inputCollection);
    }

    //adds a new hand object to an existing hand
    public void addHand(Hand inputHand)
    {
        //gets the hand set of input Hand object and stores in inputHandSet
        ArrayList<Card> inputHandList = new ArrayList<>(inputHand.getHand());

        //adds all cards in inputHandSet to this hand
        this.hand.addAll(inputHandList);
    }

    //removes a single card from the hand if present
    //if the card does not exist an exception is thrown instead
    public boolean removeSingleCard(Card inputCard) throws InvalidArgumentsException {
        if(hand.contains(inputCard))
        {
            hand.remove(inputCard);
            return true;
        }
        else
        {
            throw new InvalidArgumentsException("An error occurred: " + inputCard + " does not exist in hand");
        }
    }

    //removes all cards in a given hand from this hand
    public boolean removeHand(Hand inputHand)
    {
        //gets the hand set of input Hand object and stores in inputHandArr
        ArrayList<Card> inputHandArr = new ArrayList(inputHand.getHand());

        //flags used to check if any cards are successfully removed
        boolean flag = false;
        boolean found = false;

        //initialising iterator to iterate over input set once
        Iterator<Card> inputIterator = inputHandArr.iterator();

        while(inputIterator.hasNext())
        {
            //reset found for each card in input hand
            found = false;

            //saves each card iterated over in curInput and creates a new iterator to iterate over hand for each input card
            Card curInput = inputIterator.next();
            Iterator<Card> handIterator = hand.iterator();
            while(handIterator.hasNext())
            {
                //saves each card in the hand in curHand and compares them all to curInput
                //if curInput exists in hand then it is removed from the hand
                Card curHand = handIterator.next();
                if(curHand.toString().equals(curInput.toString()))
                {
                    handIterator.remove();
                    found = true;
                }
            }

            //if a card has not been found on this hand, enable the method to return false
            if(!found)
            {
                flag = true;
            }
        }

        //prints returns false if not all cards successfully removed, else returns true
        return !flag;
    }

    //removes a card from hand at a given index
    public Card removeCardAtIndex(int index) throws InvalidArgumentsException
    {
            Card target = hand.get(index);

            //if target card successfully removed return target, else throw exception
            if(hand.remove(target))
            {
                return target;
            }
            else
            {
                throw new InvalidArgumentsException("An error occurred: card does not exist at this position");
            }
    }

    //sorts this.hand into descending order
    public void sortDescending()
    {
        //creating array out of hand set so Card's sortDescending method can be called to sort it
        Card[] array = new Card[hand.size()];
        hand.toArray(array);

        //calling Card.sortDescending to sort array
        //calling from dummy Card object to prevent interference with sort
        Card temp = new Card();
        array = temp.sortDescending(array);

        //converting sorted array back to ArrayList
        hand = new ArrayList<>(Arrays.asList(array));
    }

    //sorts this.hand into ascending order
    public void sortAscending()
    {
        //creating array out of hand set so Card's sortDescending method can be called to sort it
        Card[] array = new Card[hand.size()];
        hand.toArray(array);

        //calling Card.sortDescending to sort array
        //calling from dummy Card object to prevent interference with sort
        Card temp = new Card();
        array = temp.sortAscending(array);

        //converting sorted array back to ArrayList
        hand = new ArrayList<>(Arrays.asList(array));
    }

    //counts the number of cards in this hand with the same rank as inpRank
    //NOTE - ARGUMENTS TO THIS METHOD MUST BE CAPITALISED
    public int countRank(String inpRank)
    {
        //iterator to iterate over hand and check rank
        Iterator<Card> it = hand.iterator();

        //int to keep running total of cards where rank == inpRank
        int total = 0;

        //Card object to hold current card being checked
        Card curCard;

        while(it.hasNext())
        {
            //loading next card in hand into card instance then comparing as Rank instead of String
            curCard = it.next();
            if(curCard.getRank().name() == inpRank)
            {
                total++;
            }
        }

        return total;
    }

    //checks the previous bid b against hand h to check if h can be played
    public boolean canPlayHand(Bid b, Hand h)
    {
        Iterator<Card> it = h.iterator();
        while(it.hasNext())
        {
            Card curCard = it.next();
            if(b.getRank() == curCard.getRank() || b.getRank().next() == curCard.getRank())
            {
                return true;
            }
        }
        return false;
    }

    //returns total value of all cards in hand
    public int handValue()
    {
        //iterator to iterate over hand and check value
        Iterator<Card> it = hand.iterator();

        //int to keep running total of value
        int total = 0;

        while(it.hasNext())
        {
            //adding each card's value to running total
            total = total + it.next().getRankValue();
        }

        return total;
    }

    //prints the hand in one line to the console
    public String toString()
    {
        //iterator to iterate over hand and convert each card to a string
        Iterator<Card> it = hand.iterator();

        //empty string used to hold hand as a string
        String handStr = "";

        //if the hand is empty, catches the exception and returns message
        try
        {
            while(it.hasNext())
            {
                //concatenating each card as string onto the end of handStr
                handStr = handStr + it.next().toString() + ", ";
            }
        }
        catch(NullPointerException ex)
        {
            return "Hand is empty";
        }

        return handStr;
    }

    //returns true if all cards are of the same suit
    public boolean isFlush()
    {
        //iterator used to iterate over hand to check each card's suit
        Iterator<Card> it = hand.iterator();

        //string to store what the suit should be to make the hand a flush
        //checking the first card independent of the loop to make note of the suit
        String targetSuit = it.next().getSuit().name();

        while(it.hasNext())
        {
            //if the suit of a card doesn't match the target suit, return false as it is not a flush
            if(!(it.next().getSuit().name().equals(targetSuit)))
            {
                return false;
            }
        }

        //return true if every card is checked to have the same suit
        return true;
    }

    //returns true if all cards are in consecutive order
    public boolean isStraight()
    {
        /*array of cards holding all cards in the hand
        curCard is checked against every element to see if it has a difference of
        1 to any other elements in the array
        array is used instead of set for simplicity as the array will be iterated over by a nested loop
         */
        Card[] handArray = new Card[hand.size()];
        hand.toArray(handArray);

        //boolean used to indicate if a consecutive card is found for each card
        boolean found = false;

        for(int i = 0; i < handArray.length; i++)
        {
            //reset found for each new card to compare to rest of array
            found = false;

            for(int j = 0; i < handArray.length; i++)
            {
                //compares each card to card at index i - if consecutive card found mark as found
                if(Card.difference(handArray[i], handArray[j]) == 1)
                {
                    found = true;
                }
            }

            //if no consecutive card found for a card return false
            if(!found)
            {
                return false;
            }
        }

        //return true if every card has been checked to have a consecutive card somewhere
        return true;
    }

    public void main()
    {
        Card card1 = new Card("TWO", "DIAMONDS");
        Card card2 = new Card("FOUR", "HEARTS");
        Card card3 = new Card("THREE", "HEARTS");
        Card card4 = new Card();
        Card card5 = new Card();
        Card[] cardArray = {card1, card2, card3, card4, card5};

        //creating a Hand instance from an array
        Hand mainHand = new Hand(cardArray);
        System.out.println(mainHand.toString());

        //demonstrating countRank() and handValue()
        System.out.println("Number of FOUR's in mainHand: " + mainHand.countRank("FOUR"));
        System.out.println("mainHand total value: " + mainHand.handValue());

        //sorting hand in descending order
        mainHand.sortDescending();
        System.out.println("mainHand in descending order: " + mainHand.toString());

        //demonstrating removeCardAtIndex()
        //if InvalidArgumentsException thrown trying to remove card, print details of exception instead
        try
        {
            System.out.println("Card removed from mainHand at index 2: " + mainHand.removeCardAtIndex(2).toString());
            System.out.println("mainHand after removal: " + mainHand.toString());
        }
        catch(InvalidArgumentsException ex)
        {
            ex.printStackTrace();
        }


        //creating empty hand using default constructor
        Hand flushHand = new Hand();

        //adding to flushHand one at a time
        flushHand.addSingleCard(new Card("FIVE", "DIAMONDS"));

        //demonstrating addCardCollection() and sortAscending()
        List<Card> cardInput = new ArrayList<>();
        cardInput.add(new Card("THREE", "DIAMONDS"));
        cardInput.add(new Card("JACK", "DIAMONDS"));
        flushHand.addCardCollection(cardInput);
        flushHand.sortAscending();
        System.out.println("flushHand in ascending order: " + flushHand.toString());

        //demonstrating isFlush() and isStraight()
        System.out.println("flushHand is a flush? " + flushHand.isFlush());
        System.out.println("flushHand is a straight? " + flushHand.isStraight());


        //creating new Hand instance by duplicating another
        Card[] cardArrayDuplicate = {card1, card2, card3};
        Hand temp = new Hand(cardArrayDuplicate);
        Hand straightHand = new Hand(temp);
        System.out.println(straightHand.toString());

        //demonstrating addHand()
        System.out.println("Adding a hand of 3 random cards to straightHand:");
        Card rand1 = new Card();
        Card rand2 = new Card();
        Card rand3 = new Card();
        Card[] cardArrayAdd = {rand1, rand2, rand3};
        Hand randomHand = new Hand(cardArrayAdd);
        straightHand.addHand(randomHand);
        System.out.println("Add complete: " + straightHand.toString());
        System.out.println("Removing the 3 random cards...");
        straightHand.removeHand(randomHand);
        System.out.println("Delete complete: " + straightHand.toString());

        //demonstrating isFlush() and isStraight()
        System.out.println("straightHand is a flush? " + straightHand.isFlush());
        System.out.println("straightHand is a straight? " + straightHand.isStraight());

        //demonstrating serialization
        String file = mainHand.saveThisToByteCode();
        Hand newHand = new Hand();
        newHand = newHand.loadFromByteCode(file);
    }

    /*-----------------ITERATOR METHODS--------------*/
    //default iterator method returns the standard HandIterator
    public Iterator iterator()
    {
        return new HandIterator();
    }

    /*------------ATTRIBUTE ACCESSOR METHODS--------*/
    //returns the hand set
    public ArrayList<Card> getHand()
    {
        return hand;
    }

    //returns total value of all cards in hand
    public int getTotalValue()
    {
        return totalValue;
    }

    //returns random card from the hand
    public Card getRandomCard()
    {
        int rand = new Random().nextInt(hand.size());
        return hand.get(rand);
    }

    //returns the size of the hand set as an int
    public int size()
    {
        return hand.size();
    }

    /*------------SERIALIZATION METHODS----------*/
    //saves the hand the method is called from to byte code and returns its unique filename
    private String saveThisToByteCode()
    {
        String filename = id + "hand.ser";
        try{
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fos);

            out.writeObject(this);
            out.close();
            fos.close();

            System.out.println(filename + " has been serialized");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return filename;
    }

    //loads in the hand given by the method argument from byte code into a Hand object and returns it
    private Hand loadFromByteCode(String filename)
    {
        try{
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            Hand hand = (Hand)in.readObject();
            in.close();
            fis.close();

            System.out.println(filename + " has been deserialized");

            //evidence that the correct hand has been loaded in
            System.out.println("Deserialized hand: " + hand.toString());
            return hand;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        //if the card is not found, a random card is returned instead
        System.out.println("Error - hand not found. Empty hand created");
        return new Hand();
    }

    /*--------------ITERATOR INNER CLASSES-------------*/
    /*--------------HandIterator inner class-----------*/
    private class HandIterator<T> implements Iterator<T>
    {
        //attribute to keep track of position in array
        int pos;

        public HandIterator()
        {
            pos = 0;
        }

        //overriding hasNext to check if reached end of deck
        @Override
        public boolean hasNext()
        {
            return pos < hand.size();
        }

        @Override
        public T next()
        {
            //return card at current position in array and increments position
            return (T)hand.get(pos++);
        }
    }
}
