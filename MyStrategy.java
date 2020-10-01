/*MyStrategy is my own custom strategy class that builds upon ThinkerStrategy, it relies heavily on random rolls
to make decisions, with the cheat and callCheat methods modified from ThinkerStrategy to have more random
elements to them
Since it is the most complex, this strategy heavily makes use of other classes' protected methods
as it inherits from ThinkerStrategy, giving it access to ThinkerStrategy's protected methods as well as it's own
superclass (BasicStrategy)
 */

import java.util.ArrayList;

public class MyStrategy extends ThinkerStrategy
implements Strategy {

    /*----INITIALIZING ATTRIBUTES----*/
    private int numTurns = 0;
    private ArrayList<Bid> playedBids = new ArrayList<>();
    private Hand previousPlays = new Hand();
    //cheatChance is recalculated every time it is referenced
    private double cheatChance;
    {
        if(cheatChance <= 0.3)
        {
            cheatChance = numTurns / 15.0;
        }
        else
        {
            cheatChance = 0.3;
        }
    }
    /*MyStrategy decides whether to cheat the same way as ThinkerStrategy but
    the chances of them cheating unnecessarily increase as the game progresses
    the chance to cheat caps at a certain point
     */
    @Override
    public boolean cheat(Bid b, Hand h)
    {
        //if not cheating, make random decision to cheat anyway
        if(!super.cheat(b, h))
        {
            return randomWithProbability(cheatChance);
        }
        return super.cheat(b, h);
    }

    /*MyStrategy chooses their bid depending on how many of that rank has been bid
    if that rank has been bid frequently, they have a higher chance
    of playing another rank when cheating
     */
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat)
    {
        //record current bid to use when deciding future bids
        playedBids.add(b);

        if(cheat)
        {
            Hand chosenHand = super.createCheatingPlayHand(b, h);
            int count = 0;

            //decide on rank to bid as
            Card.Rank posRankLower = b.getRank();
            Card.Rank posRankHigher = b.getRank().next();
            Card.Rank chosenRank = super.getCheatRank(posRankLower, posRankHigher);

            //count how many cards were bid for all the times this bid has been made before
            for(int i = 0; i < playedBids.size(); i++)
            {
                if(playedBids.get(i).getRank() == chosenRank)
                {
                    //add on how many cards were bid in a previous bid of that rank
                    int numPlayed = playedBids.get(i).getCount();
                    count += numPlayed;
                }
            }

            //decide whether to redraw the hand
            double p = count / 5.0;
            if(randomWithProbability(p))
            {
                //remove cards you chosen not to play from hand and redraw
                Hand newHand = new Hand(h);
                newHand.removeHand(chosenHand);

                //reinitialising chosenHand and putting redraw in it
                chosenHand = new Hand();
                chosenHand = super.createCheatingPlayHand(b, newHand);
                chosenRank = super.getCheatRank(posRankLower, posRankHigher);

                previousPlays.addHand(chosenHand);
                return new Bid(chosenHand, chosenRank);
            }
            //dont redraw and play the hand as it is
            else
            {
                chosenRank = chosenHand.getHand().get(0).getRank();
                previousPlays.addHand(chosenHand);
                return new Bid(chosenHand, chosenRank);
            }
        }
        /*when not cheating simply create hand and play it, no need to analyse
        number of that rank played as no risk of getting caught
         */
        else
        {
            Hand chosenHand = super.createPlayHand(b, h);
            Card.Rank chosenRank = chosenHand.getHand().get(0).getRank();
            previousPlays.addHand(chosenHand);
            return new Bid(chosenHand, chosenRank);
        }
    }

    /*MyStrategy will use check for certain cheats using their own hand and
    will use their previous plays, but as the game progresses MyStrategy
    will play more conservatively and call random cheats less*/
    @Override
    public boolean callCheat(Hand h, Bid b)
    {
        int playedCount = b.getHand().size();

        //combining count methods to count both
        int ownCount = countCurHand(h, b);

        ownCount += countPrevPlayed(h, b, previousPlays);

        //resets previousPlays and playedRanks hand on each cheat call after it has been used
        previousPlays = new Hand();
        playedBids = new ArrayList<>();

        //if you have over 2 cards of that rank the previous player must be cheating
        if(ownCount + playedCount > 4)
        {
            return true;
        }
        else
        {
            //determining small probability of calling cheat despite not being certain
            //p decreases as numTurns increases
            double p = 1.0 / (9 + numTurns);
            return randomWithProbability(p);
        }
    }
}
