import java.util.Comparator;

public class CardComparator {
    public CardComparator() {
    }

    public int compare(Card c1, Card c2) {
        return c1.getCardNumber().compareTo(c2.getCardNumber());
    }
}