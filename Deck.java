/*A deck's main feature is the array of Card objects. Each card belongs
to a deck of 52 cards, which is represented using that array. It also
contains methods to manipulate that array*/


import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Deck<T extends Comparable<? super T>>
implements Serializable, Iterable<T>
{
    //used for version control when serializing
    static final long serialVersionUID = 100239761 - 10;
    public long id;

    //declaring arrays representing the deck
    public Card[] deckArray;

    /*-----------------CONSTRUCTOR METHOD-------------*/
    //default constructor creates a deck containing all possible cards
    public Deck()
    {
        //keeping track of position in deckArray
        int cardNum = 0;
        deckArray = new Card[52];

        //populating deckArray
        //iterating through all possible cards and adding to current position in deckArray
        for(Card.Suit suit : Card.Suit.values())
        {
            for(Card.Rank rank : Card.Rank.values())
            {
                deckArray[cardNum] = new Card(rank.name(), suit.name());
                cardNum++;
            }
        }

        //generates a random id for this deck, used when serializing
        id = 1L + (long) (Math.random() * (100L - 1L));
    }

    /*---------------CLASS METHODS-------------*/
    //returns number of cards in deck
    public int size()
    {
        return deckArray.length;
    }

    //shuffles the deck into random order
    public Card[] shuffle()
    {
        //converts deckArray to ArrayList so Collections.shuffle can be called on it
        List<Card> temp = Arrays.asList(this.deckArray);
        Collections.shuffle(temp);

        //setting deckArray to shuffled List and returning it
        Card[] newDeckArray = temp.toArray(new Card[0]);
        return newDeckArray;
    }

    //deals a card from the deck and updates all attributes affected
    public Card deal()
    {
        //retrieving the card at the top of the deck and storing it in dealtCard
        int topCardIndex = getTopCard();
        Card dealtCard = deckArray[topCardIndex];

        //removing the dealt card from deckArray
        deckArray[topCardIndex] = null;

        return dealtCard;
    }

    //Demonstrates all functionality of Deck class
    //Called from the projects main method
    public void main()
    {
        //initialising Deck object and iterator objects
        Deck deck = new Deck();
        Iterator<Card> it = deck.iterator();
        Iterator<Card> it2 = deck.oddEvenIterator();

        //prints deck in order
        deck.printDeck();

        //shuffles the deck
        deck.shuffle();

        //the default iterator is a DeckIterator
        //iterates through the deck in order they'll be dealt and prints to console
        System.out.println("DeckIterator now iterating through deck:");
        while(it.hasNext())
        {
            Card c = it.next();
            System.out.println(c.toString());
        }

        //an OddEvenIterator iterates through the deck and prints to console
        System.out.println("OddEvenIterator now iterating through deck:");
        while(it2.hasNext())
        {
            Card c = it2.next();
            System.out.println(c.toString());
        }

        //using getTopCard method to get number of cards left in deck
        //incrementing getTopCard by 1 to get true size because it returns an array index
        System.out.println("Number of cards in deck: " + deck.getTopCard());

        //dealing 2 cards and printing them to console
        System.out.println("dealt: " + deck.deal().toString());
        System.out.println("dealt: " + deck.deal().toString());
        System.out.println("Number of cards in deck: " + deck.getTopCard());

        //serializing then deserializing the deck object - should print size of deck to console (getTopCard() + 1)
        //in this example, loadFromByteCode should print 50 to the console
        String file = deck.saveThisToByteCode();
        Deck newDeck = new Deck();
        newDeck = deck.loadFromByteCode(file);
    }

    //prints every card remaining in the deck to the console
    public void printDeck()
    {
        for(int i = 0; i < getTopCard(); i++)
        {
            System.out.println(deckArray[i].toString());
        }
    }

    //returns a newly-initialised Deck object that can be used to reset the deck
    public final Deck newDeck()
    {
        return new Deck();
    }

    /*-------------ATTRIBUTE ACCESSOR METHODS---------*/
    //returns the deck array
    public Card[] getDeck()
    {
        return deckArray;
    }

    //returns the index of the top card from the deck
    public int getTopCard()
    {
        for(int i = 0; i < deckArray.length; i++)
        {
            if(deckArray[i] == null)
            {
                //the top card is the one just before a null pointer
                return i - 1;
            }
        }
        //if none are null, the deck is full and 51 is the top card
        return 51;
    }

    /*-------------ITERATOR METHODS----------*/
    //default iterator method returns the standard DeckIterator
    public Iterator<T> iterator()
    {
        return new DeckIterator();
    }

    //for special cases, oddEvenIterator method returns the OddEvenIterator class
    public Iterator<T> oddEvenIterator()
    {
        return new OddEvenIterator();
    }

    /*---------------SERIALIZATION METHODS--------------*/
    //saves the Deck object the method is called from to byte code using serialization and returns its unique filename
    private String saveThisToByteCode()
    {
        String filename = id + "deck.ser";

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

    //loads in the Deck object given by the method argument from byte code into a Card object and returns it
    private Deck loadFromByteCode(String filename)
    {
        try{
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            Deck deck = (Deck)in.readObject();
            in.close();
            fis.close();

            System.out.println(filename + " has been deserialized");

            //evidence that the correct deck has been loaded in
            System.out.println("Size of deserialized deck: " + deck.getTopCard());
            return deck;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        //if the deck is not found it is reinitialised as a new deck
        System.out.println("Error - deck not found. New deck created");
        return new Deck();
    }

    /*-----------ITERATOR INNER CLASSES--------------*/
    /*-----------DeckIterator inner class------------*/
    private class DeckIterator<T> implements Iterator<T>
    {
        //initialising attribute to keep track of position in array
        int pos = 0;

        //overriding hasNext to check if reached top card in deck
        @Override
        public boolean hasNext()
        {
            if(pos < getTopCard())
            {
                return true;
            }
            //reset pos variable so can be used more than once
            pos = 0;
            return false;
        }

        @Override
        public T next()
        {
            //return card at current position in array and increments position
            return (T)deckArray[pos++];
        }
    }

    /*-------------OddEvenIterator inner class-------------*/
    private class OddEvenIterator<T> implements Iterator<T>
    {
        //keeps track of position in array
        int pos = 1;

        //determines whether currently checking odd or even positions
        boolean even = false;

        //overriding hasNext to check if reached end of deck
        @Override
        public boolean hasNext()
        {
            //when not reached end of deck
            if(pos < 52)
            {
                return true;
            }

            //when at end of deck and still checking odd, move onto checking even
            if(!even)
            {
                //starting pos at 0 will check even numbers
                pos = 0;
                even = true;
                return true;
            }

            //when at end of deck and just checked even
            pos = 0;
            return false;
        }

        @Override
        public T next()
        {
            //increase pos for next call
            pos = pos + 2;
            //return card at previous pos
            return (T)deckArray[pos - 2];
        }
    }
}
