import java.util.Comparator;

public class CompareDescending implements Comparator<Card> {
    @Override
    public int compare(Card card1, Card card2)
    {
        //if rank is equal, compare suit instead
        if (card1.getRank() == card2.getRank())
        {
            //complemented to sort in descending order instead
            return -(card1.getSuit().compareTo(card2.getSuit()));
        }
        else
        {
            int comp = card1.getRank().compareTo(card2.getRank());
            return -(comp);
        }
    }
}
