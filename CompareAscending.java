import java.util.Comparator;

public class CompareAscending implements Comparator<Card> {
    @Override
    public int compare(Card card1, Card card2)
    {
        if (card1.getRank() == card2.getRank())
        {
            return card1.getSuit().compareTo(card2.getSuit());
        }
        else
        {
            return card1.getRank().compareTo(card2.getRank());
        }
    }
}
