/*StrategyFactory takes a player and a string representing the strategy you want to assign that player
and in it's constructor it calls the player's setStrategy mutator method to set it to that strategy.
If the strategy is invalid an exception is thrown to be handled in BasicPlayer class
 */

public class StrategyFactory {

    public StrategyFactory(Player player, String strategy) throws InvalidArgumentsException
    {
        if(strategy.equals("BasicStrategy") || strategy.equals("basicstrategy") || strategy.equals("basic"))
        {
            player.setStrategy(new BasicStrategy());
        }
        else if(strategy.equals("HumanStrategy") || strategy.equals("humanstrategy") || strategy.equals("human"))
        {
            player.setStrategy(new HumanStrategy());
        }
        else if(strategy.equals("ThinkerStrategy") || strategy.equals("thinkerstrategy") || strategy.equals("thinker"))
        {
            player.setStrategy(new ThinkerStrategy());
        }
        else if(strategy.equals("MyStrategy") || strategy.equals("mystrategy") || strategy.equals("my"))
        {
            player.setStrategy(new MyStrategy());
        }
        else
        {
            throw new InvalidArgumentsException("An error occurred: invalid strategy");
        }
    }
}
