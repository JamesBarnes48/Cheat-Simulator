/*A Card object represents a single card in the full deck, it contains all
information about each card such as Suit & Rank. The class can be used to
model cards across any card game, making it universally applicable*/

import java.io.*;
import java.util.*;

public class Card implements Serializable, Comparable<Card>
{
    //used for version control when serializing
    static final long serialVersionUID = 100239761;

    //enum type for Rank
    public enum Rank
    {
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(10),
        QUEEN(10),
        KING(10),
        ACE(11);

        //initialising local variables value, prevValue & a temporary variable
        final int value;
        final int prevValue;
        int temp;

        Rank(int x) {
            value = x;

            //finding prevValue
            //if the current rank is TWO circle back to an ACE for previous card
            if ((x - 1) == 1) {
                temp = 11;
            } else {
                temp -= 1;
            }
            //value of prevValue is found using temp before being finalized
            prevValue = temp;
        }

        //returns a randomly selected rank - used when InvalidArgumentsException is thrown in constructor
        private static Rank randomRank()
        {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }

        //method for getting next rank from the rank it's called from
        private static Rank[] vals = values();
        public Rank next()
        {
            return vals[(this.ordinal() + 1) % vals.length];
        }

        //accessor method for value
        private int getValue()
        {
            return value;
        }
    }

    //enum type for Suit
    public enum Suit
    {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES;

        //returns a randomly selected suit - used when InvalidArgumentsException is thrown in constructor
        private static Suit randomSuit()
        {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

    //attributes
    private Rank rank;
    private Suit suit;

    /*----------CONSTRUCTOR METHODS------------*/
    //NOTE - ARGUMENTS TO CONSTRUCTOR METHODS MUST BE CAPITALISED
    public Card(String rankArg, String suitArg)
    {
        //assigning the rank attribute based on constructor arguments
        try {
            switch(rankArg)
            {
                case "TWO":
                    this.rank = Rank.TWO;
                    break;
                case "THREE":
                    this.rank = Rank.THREE;
                    break;
                case "FOUR":
                    this.rank = Rank.FOUR;
                    break;
                case "FIVE":
                    this.rank = Rank.FIVE;
                    break;
                case "SIX":
                    this.rank = Rank.SIX;
                    break;
                case "SEVEN":
                    this.rank = Rank.SEVEN;
                    break;
                case "EIGHT":
                    this.rank = Rank.EIGHT;
                    break;
                case "NINE":
                    this.rank = Rank.NINE;
                    break;
                case "TEN":
                    this.rank = Rank.TEN;
                    break;
                case "JACK":
                    this.rank = Rank.JACK;
                    break;
                case "QUEEN":
                    this.rank = Rank.QUEEN;
                    break;
                case "KING":
                    this.rank = Rank.KING;
                    break;
                case "ACE":
                    this.rank = Rank.ACE;
                    break;
                //in the case of invalid arguments, a random rank and suit is selected and an exception is thrown to inform the user
                default:
                    this.rank = Rank.randomRank();
                    this.suit = Suit.randomSuit();
                    throw new InvalidArgumentsException("An error occurred: " + rankArg + " is not a valid argument for Card. Random rank & suit selected: ", rank.name(), suit.name());
            }

            switch(suitArg)
            {
                case "SPADES":
                    this.suit = Suit.SPADES;
                    break;
                case "DIAMONDS":
                    this.suit = Suit.DIAMONDS;
                    break;
                case "HEARTS":
                    this.suit = Suit.HEARTS;
                    break;
                case "CLUBS":
                    this.suit = Suit.CLUBS;
                    break;
                //in the case of an invalid suit argument, one is selected at random and an exception is thrown to inform the user
                default:
                    this.suit = Suit.randomSuit();
                    throw new InvalidArgumentsException("An error occurred: " + suitArg + " is not a valid argument for Suit. Random suit selected: ", suit.name());
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    //alternative constructor which selects a suit at random
    public Card(String rankArg)
    {
        try {
            //assigning the rank attribute based on constructor arguments
            switch (rankArg) {
                case "TWO":
                    this.rank = Rank.TWO;
                    break;
                case "THREE":
                    this.rank = Rank.THREE;
                    break;
                case "FOUR":
                    this.rank = Rank.FOUR;
                    break;
                case "FIVE":
                    this.rank = Rank.FIVE;
                    break;
                case "SIX":
                    this.rank = Rank.SIX;
                    break;
                case "SEVEN":
                    this.rank = Rank.SEVEN;
                    break;
                case "EIGHT":
                    this.rank = Rank.EIGHT;
                    break;
                case "NINE":
                    this.rank = Rank.NINE;
                    break;
                case "TEN":
                    this.rank = Rank.TEN;
                    break;
                case "JACK":
                    this.rank = Rank.JACK;
                    break;
                case "QUEEN":
                    this.rank = Rank.QUEEN;
                    break;
                case "KING":
                    this.rank = Rank.KING;
                    break;
                case "ACE":
                    this.rank = Rank.ACE;
                    break;
                default:
                    this.rank = Rank.randomRank();
                    this.suit = Suit.randomSuit();
                    throw new InvalidArgumentsException("An error occurred: " + rankArg + " is not a valid argument for Card. Random rank selected: ", rank.name());
            }
            //selects a random suit even in the case of no exception
            this.suit = Suit.randomSuit();
            System.out.println("Random suit selected: " + this.suit);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    //alternative constructor that selects rank and suit at random
    public Card()
    {
        this.rank = Rank.randomRank();
        this.suit = Suit.randomSuit();
    }

    /*------------CLASS METHODS------------*/
    //returns the difference in value between two cards
    public static int differenceValue(Card card1, Card card2)
    {
        int card1Value = card1.rank.getValue();
        int card2Value = card2.rank.getValue();
        return Math.abs(card1Value - card2Value);
    }

    //returns the difference in rank between two cards
    public static int difference(Card card1, Card card2)
    {
        int card1Rank = card1.rank.ordinal();
        int card2Rank = card2.rank.ordinal();
        return Math.abs(card1Rank - card2Rank);
    }

    //uses the CompareAscending comparator class to sort in ascending order
    public Card[] sortAscending(Card[] inputArray)
    {
        Comparator comp = new CompareAscending();
        Arrays.sort(inputArray, comp);
        return inputArray;
    }

    //uses the CompareDescending comparator class to sort in descending order
    public Card[] sortDescending(Card[] inputArray)
    {
        Comparator comp = new CompareDescending();
        Arrays.sort(inputArray, comp);
        return inputArray;
    }

    //uses the CompareSuit comparator class to sort by suit in ascending order
    public Card[] sortSuit(Card[] inputArray)
    {
        Comparator comp = new CompareSuit();
        Arrays.sort(inputArray, comp);
        return inputArray;
    }

    private static void selectTest(Card inputCard)
    {
        //initialising comparators
        Comparator<Card> compSuit = new CompareSuit();
        Comparator<Card> compAscending = new CompareAscending();

        //initialising and populating list of random cards
        ArrayList<Card> cardList = new ArrayList<Card>();
        for(int i = 0; i < 4; i++)
        {
            cardList.add(new Card());
            System.out.println("Randomly generated card #" + i+1 + ": " + cardList.get(i));
        }

        //iterating through cardList ArrayList
        for(int i = 0; i < cardList.size(); i++)
        {
            //initialising lambda expressions
            Comparator<Card> compRankLambda = (Card card1, Card card2)->card1.getRank().compareTo(card2.getRank());
            Comparator<Card> compSuitLambda = (Card card1, Card card2)->-(card1.getSuit().compareTo(card2.getSuit()));

            //applying the compare method of each Comparator & the first lambda
            int suitResult = compSuit.compare(inputCard, cardList.get(i));
            int ascResult = compAscending.compare(inputCard, cardList.get(i));
            int lambdaResult = compRankLambda.compare(inputCard, cardList.get(i));

            //if the ranks are the same, apply the lambda that compares by suit instead
            if(lambdaResult == 0)
            {
                lambdaResult = compSuitLambda.compare(inputCard, cardList.get(i));
            }

            System.out.println("Comparing " + inputCard.toString() + " and " + cardList.get(i).toString());
            System.out.println("CompareSuit comparison result: " + suitResult);
            System.out.println("CompareAscending comparison result: " + ascResult);
            System.out.println("Lambda comparison result: " + lambdaResult);
        }
    }

    //Demonstrates all functionality of Card class
    //Called from the projects main method
    public void main()
    {
        //demonstrating different types of constructors in use
        Card card1 = new Card("ACE", "SPADES");
        Card card2 = new Card("TWO");
        //this should throw an exception and create a random card instead
        Card card3 = new Card("foo");
        //cards of identical rank test if the sorting methods can sort by suit
        Card card4 = new Card("TEN", "CLUBS");
        Card card5 = new Card("TEN", "SPADES");
        Card card6 = new Card("TEN", "HEARTS");
        //default constructor should create a random card
        Card card7 = new Card();

        //demonstrating selectTest method
        selectTest(card1);

        //demonstrating difference methods
        System.out.println(difference(card1, card2));
        System.out.println(differenceValue(card1, card2));

        //creating cardArray to be sorted by Comparator methods
        Card[] cardArray = {card1, card2, card3, card4, card5, card6, card7};

        //sorting in descending order
        card1.sortDescending(cardArray);
        System.out.println("descending order");
        for(int i=0; i < cardArray.length; i++)
        {
            System.out.println(cardArray[i].toString());
        }

        //sorting in ascending order
        card1.sortAscending(cardArray);
        System.out.println("ascending order");
        for(int i=0; i < cardArray.length; i++)
        {
            System.out.println(cardArray[i].toString());
        }

        //sorting in suit order
        card1.sortSuit(cardArray);
        System.out.println("suit order");
        for(int i=0; i < cardArray.length; i++)
        {
            System.out.println(cardArray[i].toString());
        }

        //serializing then deserializing card1 - should print "ACE of SPADES" to console
        card1.saveThisToByteCode();
        card1.loadFromByteCode();
    }

    /*--------------ATTRIBUTE ACCESSOR METHODS---------*/
    public Rank getRank()
    {
        return rank;
    }

    public Suit getSuit()
    {
        return suit;
    }

    public int getRankValue()
    {
        return rank.getValue();
    }

    //toString method returns the full name of the card as a string
    public String toString()
    {
        return rank.name() + " of " + suit.name();
    }

    /*---------------SERIALIZATION METHODS------------*/
    //saves the Card object the method is called from to byte code using serialization
    private void saveThisToByteCode()
    {
        String filename = toString() + ".ser";
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
    }

    //loads in the Card object given by the method argument from byte code into a Card object and returns it
    private Card loadFromByteCode()
    {
        try{
            String filename = toString() + ".ser";
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            Card card = (Card)in.readObject();
            in.close();
            fis.close();

            System.out.println(filename + " has been deserialized");

            //evidence that the correct card has been loaded in
            System.out.println("Deserialized card: " + card.toString());
            return card;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        //if the card is not found, a random card is returned instead
        return new Card();
    }

    /*----------OVERRIDDEN METHODS----------*/
    //overriding compareTo method to compare to other Card objects
    @Override
    public int compareTo(Card otherCard)
    {
        return this.rank.compareTo(otherCard.rank);
    }


}

