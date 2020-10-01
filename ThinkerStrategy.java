/*ThinkerStrategy is the second most complex strategy as it introduces more complex and informed
decision making. Decision making in the three overridden methods has more random elements and all the
hands the ThinkerStrategy plays are recorded and held in previousPlays to inform its future decisions
 */

import java.util.*;

public class ThinkerStrategy extends BasicStrategy
        implements Strategy {

    //hand that stores all previously played cards for use in callCheat
    private Hand previousPlays = new Hand();

    //cheats if necessary, but will also randomly cheat when unnecessary
    @Override
    public boolean cheat(Bid b, Hand h)
    {
        Card.Rank bidRank = b.getRank();
        Iterator<Card> it = h.iterator();
        while(it.hasNext())
        {
            Card curCard = it.next();

            //checks if a card exists in the player's hand whose rank is required by the bid
            if(curCard.getRank() == bidRank || curCard.getRank() == bidRank.next())
            {
                return randomDecision();
            }
        }

        //if the player has no cards that match the bid rank, it must cheat
        return true;
    }

    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat)
    {

        //Hand and Rank used to create Bid instance to be returned
        Hand playHand = new Hand();
        Card.Rank playRank;

        if(cheat)
        {
            /*------determine playHand-----*/
            playHand = createCheatingPlayHand(b, h);

            /*-----determine playRank------*/
            Card.Rank posRankLower = b.getRank();
            Card.Rank posRankHigher = b.getRank().next();
            playRank = getCheatRank(posRankLower, posRankHigher);

            return new Bid(playHand, playRank);
        }
        else
        {
            /*-------determining playHand----------*/
            //delegated to createPlayHand method so code can be reused in MyStrategy
            playHand = createPlayHand(b, h);

            /*------determining playRank--------*/
            //set playRank to whatever rank the cards to be played are
            playRank = playHand.getHand().get(0).getRank();

            //adds cards to be played to previousPlays
            previousPlays.addHand(playHand);

            return new Bid(playHand, playRank);
        }
    }

    //decides whether to call cheat using current hand with a random probability p to call cheat anyway
    @Override
    public boolean callCheat(Hand h, Bid b)
    {
        //get details about the played hand
        int playedCount = b.getHand().size();

        //combining count methods to count both
        int ownCount = super.countCurHand(h, b);
        ownCount += countPrevPlayed(h, b, this.previousPlays);

        //resets previousPlays hand on each cheat call after it has been used
        previousPlays = new Hand();

        //if you have over 2 cards of that rank the previous player must be cheating
        if(ownCount + playedCount > 4)
        {
            return true;
        }
        else
        {
            //determining small probability of calling cheat despite not being certain
            double p = (ownCount + playedCount) / 20.0;
            return randomWithProbability(p);
        }
    }

    /*counting matching cards in previously played hands
    embedded in seperate method for code reuse*/
    protected int countPrevPlayed(Hand h, Bid b, Hand previousPlays)
    {
        Card.Rank playedRank = b.getRank();
        int ownCount = 0;

        //iterate through previously played cards and count how many cards of the played rank there are
        Iterator<Card> it2 = previousPlays.iterator();
        while(it2.hasNext())
        {
            Card curCard = it2.next();
            //if a previously played card has the same rank add to running total
            if(curCard.getRank().equals(playedRank))
            {
                ownCount++;
            }
        }
        return ownCount;
    }

    //creates a hand to play when making a non-cheating bid
    //expanded upon in MyStrategy to factor in number of that rank played
    protected Hand createPlayHand(Bid b, Hand h)
    {
        Hand playHand = new Hand();

        //holds the cards eligible to be played for either possible rank
        ArrayList<Card> toPlayLower = super.generateToPlay(h, b, 0);
        ArrayList<Card> toPlayHigher = super.generateToPlay(h, b, 1);

        //random decision to play all cards or a random number of them
        if(randomDecision())
        {
            /*---------playing all cards---------*/
            //if toPlayLower is empty play toPlayHigher, else play toPlayLower
            if(toPlayLower.isEmpty())
            {
                playHand.addCardCollection(toPlayHigher);
            }
            else
            {
                playHand.addCardCollection(toPlayLower);
            }
        }
        else
        {
            /*----playing random amount of cards with preference to lower rank----*/
            if(toPlayLower.isEmpty())
            {
                //creating random number no larger than toPlayHigher length and no less than 1
                Random rand = new Random();
                int numPlayed = rand.nextInt((toPlayHigher.size() - 1) + 1) + 1;
                for(int i = 0; i < numPlayed; i++)
                {
                    playHand.addSingleCard(toPlayHigher.get(i));
                }
            }
            else
            {
                //creating random number no larger than toPlayLower length and no less than 1
                Random rand = new Random();
                int numPlayed = rand.nextInt((toPlayLower.size() - 1) + 1) + 1;
                for(int i = 0; i < numPlayed; i++)
                {
                    playHand.addSingleCard(toPlayLower.get(i));
                }
            }
        }
        return playHand;
    }

    //creates a play hand when making a cheating bid
    protected Hand createCheatingPlayHand(Bid b, Hand h)
    {
        //Hand and Rank used to create Bid instance to be returned
        Hand playHand = new Hand();

        //creating new arraylist of cards in hand that will be cheating to play
        ArrayList<Card> cheatCards = new ArrayList<>();

        for(int i = 0; i < h.size(); i++)
        {
            Card.Rank curRank = h.getHand().get(i).getRank();
            if(curRank != b.getRank() || curRank != b.getRank().next())
            {
                cheatCards.add(h.getHand().get(i));
            }
        }

        //selecting a card to play from cheatCards with a bias towards higher ranks
        int randIndex = randomLargeBiasInt(0, h.size() - 1);
        Card selectedCard = h.getHand().get(randIndex);
        playHand.addSingleCard(selectedCard);

        //checking for other cards of the same rank to play
        for(int i = 0; i < cheatCards.size(); i++)
        {
            Card.Rank curRank = cheatCards.get(i).getRank();
            if(curRank == selectedCard.getRank() && i != randIndex)
            {
                playHand.addSingleCard(cheatCards.get(i));
            }
        }

        return playHand;
    }

    //randomly makes a yes or no decision when called
    protected boolean randomDecision()
    {
        Random rand = new Random();
        return rand.nextBoolean();
    }

    //randomly generates a boolean value with probability p of being true
    protected boolean randomWithProbability(double p)
    {
        return Math.random() < p;
    }

    //generates a random number within a given range with a bias towards the larger values
    protected int randomLargeBiasInt(int min, int max)
    {
        Random rand = new Random();

        //generate 2 different random ints within the given range
        int rand1 = rand.nextInt((max - min) + 1) + min;
        int rand2 = rand.nextInt((max - min) + 1) + min;

        //selects the larger random number, giving a bias to larger numbers
        return Math.max(rand1, rand2);
    }
}
