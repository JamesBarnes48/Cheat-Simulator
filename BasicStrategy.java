/*BasicStrategy is a computer-controlled strategy which has the lowest level
of complexity to its decisions and for this reason is a superclass to all the other strategies.
it contains 2 protected methods which perform tasks that are required for any strategy
 */

import java.util.*;

public class BasicStrategy implements Strategy {

    //overridden to always return false as BasicStrategy never cheats
    @Override
    public boolean cheat(Bid b, Hand h)
    {
        Card.Rank bidRank = b.getRank();
        Iterator<Card> it = h.iterator();
        while(it.hasNext())
        {
            Card curCard = it.next();

            //if a card exists in the player's hand whose rank is required by the bid, no need to cheat
            if(curCard.getRank() == bidRank || curCard.getRank() == bidRank.next())
            {
                return false;
            }
        }

        //if the player has no cards that match the bid rank, it must cheat
        return true;
    }

    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat)
    {
        //card rank must match one of these ranks to be eligible to be played
        Card.Rank posRankLower = b.getRank();
        Card.Rank posRankHigher = b.getRank().next();

        //Hand and Rank used to create Bid instance to be returned
        Hand playHand = new Hand();
        Card.Rank playRank;

        if(cheat)
        {
            //adding a random card from the hand to the playing hand
            playHand.addSingleCard(h.getRandomCard());

            //decide on which potential rank to bid as
            playRank = getCheatRank(posRankLower, posRankHigher);

            return new Bid(playHand, playRank);
        }
        else
        {
            //holds the cards eligible to be played for either possible rank
            ArrayList<Card> toPlayLower = new ArrayList<>();
            ArrayList<Card> toPlayHigher = new ArrayList<>();

            //generate ArrayList's of cards at each potential rank
            toPlayLower = generateToPlay(h, b, 0);
            toPlayHigher = generateToPlay(h, b, 1);

            //if toPlayLower is empty play toPlayHigher, else play toPlayLower
            if(toPlayLower.isEmpty())
            {
                playHand.addCardCollection(toPlayHigher);
                playRank = posRankHigher;
            }
            else
            {
                playHand.addCardCollection(toPlayLower);
                playRank = posRankLower;
            }

            return new Bid(playHand, playRank);
        }
    }

    @Override
    public boolean callCheat(Hand h, Bid b)
    {
        int playedCount = b.getHand().size();
        int ownCount = countCurHand(h, b);

        //if you have over 2 cards of the bid rank the previous bid must be a cheat
        if(ownCount + playedCount > 4)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //randomly decides whether to bid with the lower or higher rank when cheating
    protected Card.Rank getCheatRank(Card.Rank posRankLower, Card.Rank posRankHigher)
    {
        //creating arraylist from possible ranks to randomly choose one
        ArrayList<Card.Rank> ranks = new ArrayList<Card.Rank>();
        ranks.add(posRankHigher);
        ranks.add(posRankLower);

        //randomly selecting whether to cheat as the lower or higher rank
        Random rand = new Random();
        int index = rand.nextInt(ranks.size());
        return ranks.get(index);
    }

    //returns an ArrayList of cards in the hand that can be played at the given bid rank
    //highOrLow != 1 finds cards at the bid rank
    //highOrLow == 1 finds cards at one above the bid rank
    protected ArrayList<Card> generateToPlay(Hand h, Bid b, int highOrLow)
    {
        //holds the cards eligible to be played at bid rank
        ArrayList<Card> toPlay = new ArrayList<>();

        //card rank must match one of the rank given by highOrLow to be eligible to be played
        Card.Rank posRank = b.getRank();
        if(highOrLow == 1)
        {
            posRank = b.getRank().next();
        }

        //iterate over hand and add cards that match posRank to toPlay
        Iterator<Card> it = h.iterator();
        while(it.hasNext())
        {
            Card curCard = it.next();
            if(curCard.getRank() == posRank)
            {
                toPlay.add(curCard);
            }
        }
        return toPlay;
    }

    /*counting matching cards to bid in current hand
    embedded in seperate method for code reuse*/
    protected int countCurHand(Hand h, Bid b)
    {
        //get details about the played hand
        Card.Rank playedRank = b.getRank();

        //iterate through own hand and count how many cards of the played rank there are
        Iterator<Card> it = h.iterator();
        int ownCount = 0;

        while(it.hasNext())
        {
            Card curCard = it.next();

            //if a card in your hand has the same rank add to running total
            if(curCard.getRank().equals(playedRank))
            {
                ownCount++;
            }
        }
        return ownCount;
    }

}
