/*a BasicPlayer represents the player entity who will be playing the game. A player's hand and strategy
are all specific to BasicPlayer, meaning most of what BasicPlayer does concerns their hand or strategy,
the majority of what a player does is dictated by their strategy
 */

public class BasicPlayer implements Player {

    //player-specific attributes
    private Hand playerHand;
    private Strategy playerStrategy;
    private CardGame playerGame;

    /*----------CONSTRUCTOR METHODS-----------*/
    public BasicPlayer(String strategy, CardGame game)
    {
        playerHand = new Hand();
        //try to set strategy using strategy factory
        try
        {
            new StrategyFactory(this, strategy);
        }
        //if invalid arguments, set strategy to BasicPlayer by default
        catch(InvalidArgumentsException ex)
        {
            this.setStrategy(new BasicStrategy());
        }
        this.setGame(game);
    }

    /*----------CLASS METHODS-------------*/
    //adds a card to the player's hand
    @Override
    public void addCard(Card c)
    {
        playerHand.addSingleCard(c);
    }

    //adds a hand to the player's deck
    @Override
    public void addHand(Hand h)
    {
        playerHand.addHand(h);
    }

    //determines what the player will do on their turn
    @Override
    public Bid playHand(Bid b)
    {
        //evaluate whether to cheat or not
        boolean cheat = playerStrategy.cheat(b, playerHand);

        Bid chosenBid = playerStrategy.chooseBid(b, playerHand, cheat);

        //remove cards about to be played from the players hand
        playerHand.removeHand(chosenBid.getHand());

        //plays hand accordingly
        return chosenBid;
    }

    //uses strategy to decide whether to call cheat or not
    @Override
    public boolean callCheat(Bid b)
    {
        return playerStrategy.callCheat(playerHand, b);
    }

    /*----------ATTRIBUTE ACCESSOR & MUTATOR METHODS--------*/
    //returns the size of the players hand
    @Override
    public int cardsLeft()
    {
        return playerHand.size();
    }

    //sets the game this player belongs to
    @Override
    public void setGame(CardGame g)
    {
        this.playerGame = g;
    }

    //sets the strategy this player is following
    @Override
    public void setStrategy(Strategy s)
    {
        this.playerStrategy = s;
    }

    @Override
    public Hand getHand()
    {
        return playerHand;
    }
}
