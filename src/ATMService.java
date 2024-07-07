import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ATMService {
    private final List<Card> cards = CardStorage.loadCards();
    private Card currentCard;
    private int failedAttempts = 0;

    public ATMService() {
    }

    public Card findCard(String cardNumber) {
        Iterator var2 = this.cards.iterator();

        Card card;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            card = (Card)var2.next();
        } while(!card.getCardNumber().equals(cardNumber));

        return card;
    }

    public void checkCardUnblock(Card card) {
        if (card.isBlocked() && (new Date()).getTime() - card.getBlockTime().getTime() > 86400000L) {
            card.setBlocked(false);
            card.setBlockTime((Date)null);
            CardStorage.saveCards(this.cards);
        }

    }

    public boolean authenticate(Card card, String pinCode) {
        if (card == null) {
            return false;
        } else if (card.isBlocked()) {
            System.out.println("Карта заблокирована.");
            return false;
        } else if (card.getPinCode().equals(pinCode)) {
            this.currentCard = card;
            this.failedAttempts = 0;
            return true;
        } else {
            ++this.failedAttempts;
            if (this.failedAttempts >= 3) {
                card.setBlocked(true);
                card.setBlockTime(new Date());
                CardStorage.saveCards(this.cards);
                System.out.println("Карта заблокирована за три неправильных попытки ввода ПИН-кода.");
            }

            return false;
        }
    }

    public double checkBalance() {
        return this.currentCard != null ? this.currentCard.getBalance() : 0.0;
    }

    public boolean withdraw(double amount) {
        if (this.currentCard != null && amount <= this.currentCard.getBalance()) {
            this.currentCard.setBalance(this.currentCard.getBalance() - amount);
            CardStorage.saveCards(this.cards);
            return true;
        } else {
            return false;
        }
    }

    public boolean deposit(double amount) {
        if (this.currentCard != null && amount <= 1000000.0) {
            this.currentCard.setBalance(this.currentCard.getBalance() + amount);
            CardStorage.saveCards(this.cards);
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        this.currentCard = null;
    }
}
